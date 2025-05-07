package com.kunlun.firmwaresystem;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kunlun.firmwaresystem.device.Gateway_devices;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.location_util.backup.Gateway_device;
import com.kunlun.firmwaresystem.location_util.backup.Location;
import com.kunlun.firmwaresystem.location_util.backup.LocationUtil;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;
import com.kunlun.firmwaresystem.sql.FWordcard_Sql;
import com.kunlun.firmwaresystem.sql.History_Sql;

import com.kunlun.firmwaresystem.sql.Project_Sql;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.util.StringUtil.*;
import static java.lang.Math.abs;

@Component
public class getLocationTask {
    final boolean open_b=false;
    //取两点之间的坐标值。二分之一
    final double open_b_p=0.33;
    public static String beacon_address="";
    public static int interval=3;
    Map<String, List<Integer>> map = new HashMap<>();
    LocationUtil util = new LocationUtil();
    static int count = 0;
    int s=0;
    History_Sql history_sql=new History_Sql();
    @Scheduled(cron = "*/1 * * * * ?")
    public void execute() throws Exception {

        //  println("定时一秒一次="+count);
        count++;
    if(count % interval == 0){

        Project_Sql project_sql = new Project_Sql();
        List<Project> projects = project_sql.getAllProject(projectMapper);
        for (Project project : projects) {
            //Check_sheet check_sheet =check_sheetMap.get(key);
            //  println("项目="+project.getProject_name()+" Count="+count);

            if (count % interval == 0) {
                //  println("符合="+interval+" 项目="+project.getProject_name());
                s++;
                Gateway_devices gateways = null;
                ArrayList<Gateway_device> beaconTags = null;
                //  println("debug-="+beacon_address);
                if (beacon_address != null && !beacon_address.isEmpty()) {
                    Beacon beacon = beaconsMap.get(beacon_address);
                    if (beacon != null) {
                        if (beacon.getProject_key().equals(project.getProject_key())) {
                            String json = null;
                            try {
                                json = (String) redisUtil.get(redis_key_device_gateways + beacon_address);
                                if (json != null) {
                                    gateways = new Gson().fromJson(json, Gateway_devices.class);
                                    Location location = util.calculate(gateways.getGatewayDevices(), beacon_address,beacon.getL_type());
                                    // println("X="+location.getX()+"   Y="+location.getY());
                                    beacon.setMap_key(location.getMap_key());
                                    ArrayList<Object> list = new ArrayList<>();
                                    if (open_b) {
                                        Location location1 = null;
                                        try {
                                            location1 = (Location) redisUtil.get(redis_key_location + beacon_address);

                                        } catch (Exception e) {
                                            println("莫名错误11");
                                        }
                                        if (location1 != null) {
                                            //<=3米，33%        0.33x              1米对应0.33米， 3米对应1米
                                            //3-5米，   1+(x-3)*0.75                5米对应2.5米
                                            //5-8米，   2.5+（x-5)*0.1            8米对应2.8米
                                            //>8米    2.8+(x-8)* 0.05            12米对应3米      20米对应3.4米
                                            double d = Math.sqrt(Math.pow((location.getX() - location1.getX()), 2) + Math.pow((location.getY() - location1.getY()), 2));
                                            double y = (location.getY() - location1.getY());
                                            double x = (location.getX() - location1.getX());
                                            int a = x >= 0 ? 1 : -1;
                                            int b = y >= 0 ? 1 : -1;
                                            if (d > 8) {
                                                location.setX((2.8 + (d - 8) * 0.05) * a + location1.getX());
                                                location.setY((2.8 + (d - 8) * 0.05) * b + location1.getY());

                                            } else if (d > 5) {
                                                location.setX((2.5 + (d - 5) * 0.1) * a + location1.getX());
                                                location.setY((2.5 + (d - 5) * 0.1) * b + location1.getY());
                                            } else if (d > 3) {
                                                location.setX((1 + (d - 3) * 0.75) * a + location1.getX());
                                                location.setY((1 + (d - 3) * 0.75) * b + location1.getY());
                                            } else {
                                                location.setX(d * 0.33 * a + location1.getX());
                                                location.setY(d * 0.33 * b + location1.getY());
                                                // println("这里666="+d*0.33*a);
                                            }
                                        }
                                        beacon.setX(location.getX());
                                        beacon.setY(location.getY());
                                        redisUtil.set(redis_key_location + beacon_address, location);

                                    }
                                    else{
                                        beacon.setX(location.getX());
                                        beacon.setY(location.getY());
                                    }
                                    list.add(beacon);
                                    beacon.setUseDatalist((ArrayList) location.getDataMap());
                                    beacon.setGatewayDevices(gateways.getGatewayDevices());
                                    JSONObject jsonObject1 = new JSONObject();
                                    jsonObject1.put("tag", list);
                                    RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(), beacon.getMap_key());
                                    directExchangeProducer.send(rabbitMessage1.toString(), "sendtoMap_debug");
                                }
                            } catch (Exception e) {
                                println("json=" + json);
                                println("22法国红酒封口的" + json);
                            }
                        }
                    }

                }

                for (String address : beaconsMap.keySet()) {
                    Beacon beacon = beaconsMap.get(address);
                    // System.out.println("信标="+beacon.getMac()+"在线离线="+beacon.getOnline()+"   项目循环="+beacon.getProject_key()+"   当前项目="+key);

                    if (beacon.getOnline() == 1 && beacon.getProject_key().equals(project.getProject_key())) {
                        String json = null;
                        try {
                            json = (String) redisUtil.get(redis_key_device_gateways + address);
                            if (json != null) {
                                gateways = new Gson().fromJson(json, Gateway_devices.class);
                                if(gateways.getGatewayDevices().isEmpty()){
                                    continue;
                                }
                                ///   println("数据="+json);
                                Location location = util.calculate(gateways.getGatewayDevices(), address,beacon.getL_type());
                                //  println("计算位置="+location);
                                beacon.setMap_key(location.getMap_key());
                                ArrayList<Object> list = new ArrayList<>();
                                if (open_b) {
                                    //记得这里可能涉及json的无穷大只
                                    //   aa
                                    Location location1 = null;
                                    try {
                                        location1 = (Location) redisUtil.get(redis_key_location + address);
                                    } catch (Exception e) {
                                        println("莫名错误11");
                                    }

                                    if (location1 != null && !Double.isNaN(location1.getY()) && !Double.isNaN(location1.getX())) {

                                        //  println("location1="+location1);
                                        //<=3米，33%        0.33x              1米对应0.33米， 3米对应1米
                                        //3-5米，   1+(x-3)*0.75                5米对应2.5米
                                        //5-8米，   2.5+（x-5)*0.1            8米对应2.8米
                                        //>8米    2.8+(x-8)* 0.05            12米对应3米      20米对应3.4米
                                        double d = Math.sqrt(Math.pow((location.getX() - location1.getX()), 2) + Math.pow((location.getY() - location1.getY()), 2));
                                        // println("两点距离="+d);
                                        double y = (location.getY() - location1.getY());
                                        double x = (location.getX() - location1.getX());
                                        int a = x >= 0 ? 1 : -1;
                                        int b = y >= 0 ? 1 : -1;
                                         /* if(location.getMac().equals("f0c890021002")){
                                              println("原始=="+location.getX());
                                              println("原始=="+location.getY());
                                              println("旧的原始=="+location1.getX());
                                              println("旧的原始=="+location1.getY());

                                              println("A="+a);
                                              println("B="+b);
                                              println("D="+d);
                                          }*/

                                        if (d > 8) {
                                            location.setX((2.8 + (d - 8) * 0.05) * a + location1.getX());
                                            location.setY((2.8 + (d - 8) * 0.05) * b + location1.getY());
                                        } else if (d > 5) {
                                            location.setX((2.5 + (d - 5) * 0.1) * a + location1.getX());
                                            location.setY((2.5 + (d - 5) * 0.1) * b + location1.getY());
                                        } else if (d > 3) {
                                            location.setX((1 + (d - 3) * 0.75) * a + location1.getX());
                                            location.setY((1 + (d - 3) * 0.75) * b + location1.getY());
                                        } else {
                                            location.setX(d * 0.33 * a + location1.getX());
                                            location.setY(d * 0.33 * b + location1.getY());
                                            // println("这里666="+d*0.33*a);
                                        }
                                    } else {
                                        println("不存在+位置");
                                    }
                                  /*    if(location.getMac().equals("f0c890021002")) {
                                          println("原始==" + location.getX());
                                          println("原始==" + location.getY());
                                      }*/

                                    //    println("定位信标="+address+"   地图key="+beacon.getMap_key());

                                }
                                beacon.setX(location.getX());
                                beacon.setY(location.getY());
                                list.add(beacon);
                                beaconsMap.put(address, beacon);
                                try {
                                    redisUtil.set(redis_key_location + address, location);
                                } catch (Exception e) {
                                    println("莫名错误11");
                                }
                                //  println("location.getY="+location.getY());

                                beacon.setGateway_address(location.getgAddress());
                                sendLocationPush(beacon);
                                //  if(s%6==0){
                                try {
                                    JSONObject jsonObject1 = new JSONObject();
                                    //   println("list="+list);
                                    jsonObject1.put("tag", list);

                                    RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(), beacon.getMap_key());
                                    directExchangeProducer.send(rabbitMessage1.toString(), "sendtoMap");
                                } catch (Exception e) {
                                    println("莫名错误996" + e.getMessage());
                                }
                                //  }

                                gateways.setGatewayDevices(new ArrayList<>());
                                redisUtil.set(redis_key_device_gateways + address, gateways.toString());
                            }
                        } catch (Exception e) {
                            println("json=" + json);
                            println("11法国红酒封口的" + e.getMessage());
                        }

                    }


                }


                FWordcard_Sql fWordcardSql = new FWordcard_Sql();
                HashMap<String, FWordcard> fWordcardMap1 = fWordcardSql.getAll(fWordcardMapper);

                for (String imei : fWordcardMap1.keySet()) {

                    FWordcard fWordcard = fWordcardMap1.get(imei);
                    // println("11数据="+fWordcard);
                    if (fWordcard != null && fWordcard.getOnline() == 1 && fWordcard.getProject_key().equals(project.getProject_key())) {
                        //   println("331数据="+fWordcard);

                        try {

                            Object o = redisUtil.get(redis_key_device_gateways + imei);


                            if (o != null) {
                                if (o.getClass().getName().contains("String")) {
                                    System.out.println("O=异常=" + ((String) o));
                                }

                                beaconTags = (ArrayList<Gateway_device>) o;
                            } else {
                                continue;
                            }

                            println("1数据2=" + beaconTags.size());
                            if (beaconTags.isEmpty()) {
                                continue;
                            }
                            Location location = util.calculate(beaconTags, imei,2);
                            //  println("计算位置="+location);


                            fWordcard.setMap_key(location.getMap_key());

                            ArrayList<Object> list = new ArrayList<>();
                            if (open_b) {
                                //记得这里可能涉及json的无穷大只
                                //   aa
                                Location location1 = null;
                                try {
                                    location1 = (Location) redisUtil.get(redis_key_location + imei);
                                } catch (Exception e) {
                                    println("莫名错误11");
                                }

                                if (location1 != null && !Double.isNaN(location1.getY()) && !Double.isNaN(location1.getX())) {

                                    //  println("location1="+location1);
                                    //<=3米，33%        0.33x              1米对应0.33米， 3米对应1米
                                    //3-5米，   1+(x-3)*0.75                5米对应2.5米
                                    //5-8米，   2.5+（x-5)*0.1            8米对应2.8米
                                    //>8米    2.8+(x-8)* 0.05            12米对应3米      20米对应3.4米
                                    double d = Math.sqrt(Math.pow((location.getX() - location1.getX()), 2) + Math.pow((location.getY() - location1.getY()), 2));
                                    // println("两点距离="+d);
                                    double y = (location.getY() - location1.getY());
                                    double x = (location.getX() - location1.getX());
                                    int a = x >= 0 ? 1 : -1;
                                    int b = y >= 0 ? 1 : -1;
                                     /* if(location.getMac().equals("f0c890021002")){
                                          println("原始=="+location.getX());
                                          println("原始=="+location.getY());
                                          println("旧的原始=="+location1.getX());
                                          println("旧的原始=="+location1.getY());

                                          println("A="+a);
                                          println("B="+b);
                                          println("D="+d);
                                      }*/

                                    if (d > 8) {
                                        location.setX((2.8 + (d - 8) * 0.05) * a + location1.getX());
                                        location.setY((2.8 + (d - 8) * 0.05) * b + location1.getY());


                                    } else if (d > 5) {
                                        location.setX((2.5 + (d - 5) * 0.1) * a + location1.getX());
                                        location.setY((2.5 + (d - 5) * 0.1) * b + location1.getY());
                                    } else if (d > 3) {
                                        location.setX((1 + (d - 3) * 0.75) * a + location1.getX());
                                        location.setY((1 + (d - 3) * 0.75) * b + location1.getY());
                                    } else {
                                        location.setX(d * 0.33 * a + location1.getX());
                                        location.setY(d * 0.33 * b + location1.getY());
                                        // println("这里666="+d*0.33*a);
                                    }
                                } else {
                                    println("不存在+位置");
                                }
                              /*    if(location.getMac().equals("f0c890021002")) {
                                      println("原始==" + location.getX());
                                      println("原始==" + location.getY());
                                  }*/

                                //    println("定位信标="+address+"   地图key="+beacon.getMap_key());

                            }

                            //    fWordcardMap.put(imei,fWordcard);
                            try {
                                redisUtil.set(redis_key_location + imei, location);
                            } catch (Exception e) {
                                println("莫名错误11");
                            }
                            //  println("location.getY="+location.getY());
                            fWordcard.setX(location.getX());
                            fWordcard.setY(location.getY());
                            list.add(fWordcard);
                            sendLocationPush_OFcat1(fWordcard);
                            //  if(s%6==0){
                            try {
                                JSONObject jsonObject1 = new JSONObject();
                                //   println("list="+list);
                                jsonObject1.put("tag", list);

                                RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(), fWordcard.getMap_key());
                                println("CAT1推送-----" + rabbitMessage1.toString());
                                directExchangeProducer.send(rabbitMessage1.toString(), "sendtoMap");
                            } catch (Exception e) {
                                println("莫名错误996" + e.getMessage());
                            }
                            //
                            redisUtil.set(redis_key_device_gateways + imei, null);
                        } catch (Exception e) {
                            println("22json=" + beaconTags);
                            println("11法国红酒封口的" + e.getMessage());
                        }

                    }


                }


                //   B fWordcardSql=new FWordcard_Sql();
             //   println("循环" + project.getProject_key());
                for (String address : braceletsMap.keySet()) {

                    Bracelet bracelet = braceletsMap.get(address);
                   // println(project.getProject_key() + "手环=" + bracelet);
                    if (bracelet != null && bracelet.getProject_key().equals(project.getProject_key())) {
                        //   println("331数据="+fWordcard);

                        try {

                            Object o = redisUtil.get(redis_key_device_gateways + address);
                            if (o != null) {
                                if (o.getClass().getName().contains("String")) {
                                    System.out.println("O=异常=" + ((String) o));
                                }

                                beaconTags = (ArrayList<Gateway_device>) o;
                            } else {
                              //  println("手环为何会退出=" + address);
                                continue;
                            }
                            //println("1数据=" + beaconTags);
                          //  println("1数据2=" + beaconTags.size());
                            if (beaconTags.isEmpty()) {
                                continue;
                            }
                            Location location = util.calculate(beaconTags, address,2);
                            //  println("计算位置="+location);


                            bracelet.setMap_key(location.getMap_key());

                            ArrayList<Object> list = new ArrayList<>();
                            if (open_b) {
                                //记得这里可能涉及json的无穷大只
                                //   aa
                                Location location1 = null;
                                try {
                                    location1 = (Location) redisUtil.get(redis_key_location + address);
                                } catch (Exception e) {
                                    println("莫名错误11");
                                }

                                if (location1 != null && !Double.isNaN(location1.getY()) && !Double.isNaN(location1.getX())) {

                                    //  println("location1="+location1);
                                    //<=3米，33%        0.33x              1米对应0.33米， 3米对应1米
                                    //3-5米，   1+(x-3)*0.75                5米对应2.5米
                                    //5-8米，   2.5+（x-5)*0.1            8米对应2.8米
                                    //>8米    2.8+(x-8)* 0.05            12米对应3米      20米对应3.4米
                                    double d = Math.sqrt(Math.pow((location.getX() - location1.getX()), 2) + Math.pow((location.getY() - location1.getY()), 2));
                                    // println("两点距离="+d);
                                    double y = (location.getY() - location1.getY());
                                    double x = (location.getX() - location1.getX());
                                    int a = x >= 0 ? 1 : -1;
                                    int b = y >= 0 ? 1 : -1;
                                     /* if(location.getMac().equals("f0c890021002")){
                                          println("原始=="+location.getX());
                                          println("原始=="+location.getY());
                                          println("旧的原始=="+location1.getX());
                                          println("旧的原始=="+location1.getY());

                                          println("A="+a);
                                          println("B="+b);
                                          println("D="+d);
                                      }*/

                                    if (d > 8) {
                                        location.setX((2.8 + (d - 8) * 0.05) * a + location1.getX());
                                        location.setY((2.8 + (d - 8) * 0.05) * b + location1.getY());


                                    } else if (d > 5) {
                                        location.setX((2.5 + (d - 5) * 0.1) * a + location1.getX());
                                        location.setY((2.5 + (d - 5) * 0.1) * b + location1.getY());
                                    } else if (d > 3) {
                                        location.setX((1 + (d - 3) * 0.75) * a + location1.getX());
                                        location.setY((1 + (d - 3) * 0.75) * b + location1.getY());
                                    } else {
                                        location.setX(d * 0.33 * a + location1.getX());
                                        location.setY(d * 0.33 * b + location1.getY());
                                        // println("这里666="+d*0.33*a);
                                    }
                                } else {
                                    println("不存在+位置");
                                }
                              /*    if(location.getMac().equals("f0c890021002")) {
                                      println("原始==" + location.getX());
                                      println("原始==" + location.getY());
                                  }*/

                                //    println("定位信标="+address+"   地图key="+beacon.getMap_key());

                            }

                            //    fWordcardMap.put(imei,fWordcard);
                            try {
                                redisUtil.set(redis_key_location + address, location);
                            } catch (Exception e) {
                                println("莫名错误11");
                            }
                            //  println("location.getY="+location.getY());
                            bracelet.setX(location.getX());
                            bracelet.setY(location.getY());
                            list.add(bracelet);
                            sendLocationPush_Bracelet(bracelet);
                            //  if(s%6==0){
                            try {
                                JSONObject jsonObject1 = new JSONObject();
                                //   println("list="+list);
                                jsonObject1.put("tag", list);

                                RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(), bracelet.getMap_key());
                                println("手环推送-----" + rabbitMessage1.toString());
                                directExchangeProducer.send(rabbitMessage1.toString(), "sendtoMap");
                            } catch (Exception e) {
                                println("莫名错误996" + e.getMessage());
                            }
                            //
                            redisUtil.set(redis_key_device_gateways + address, null);
                        } catch (Exception e) {
                            println("22json=" + beaconTags);
                            println("11法国红酒封口的" + e.getMessage());
                        }

                    }


                }

//蓝牙工卡定位
                for (String mac : wordcard_aMap.keySet()) {

                    Wordcard_a wordcardA = wordcard_aMap.get(mac);
                    // println("11数据="+fWordcard);
                    if (wordcardA != null && wordcardA.getOnline() == 1 && wordcardA.getProject_key().equals(project.getProject_key())) {
                        //   println("331数据="+fWordcard);

                        try {

                            Object o = redisUtil.get(redis_key_device_gateways + mac);


                            if (o != null) {
                                if (o.getClass().getName().contains("String")) {
                                    System.out.println("O=异常=" + ((String) o));
                                }

                                beaconTags = (ArrayList<Gateway_device>) o;
                            } else {
                                continue;
                            }

                            println("1数据2=" + beaconTags.size());
                            if (beaconTags.isEmpty()) {
                                continue;
                            }
                            Location location = util.calculate(beaconTags, mac,2);
                            println("计算位置="+location);
                            println("原始=="+beaconTags);


                            wordcardA.setMap_key(location.getMap_key());

                            ArrayList<Object> list = new ArrayList<>();
                            if (open_b) {
                                //记得这里可能涉及json的无穷大只
                                //   aa
                                Location location1 = null;
                                try {
                                    location1 = (Location) redisUtil.get(redis_key_location + mac);
                                    println("位置="+location1);
                                } catch (Exception e) {
                                    println("莫名错误11");
                                }

                                if (location1 != null && !Double.isNaN(location1.getY()) && !Double.isNaN(location1.getX())) {

                                    double d = Math.sqrt(Math.pow((location.getX() - location1.getX()), 2) + Math.pow((location.getY() - location1.getY()), 2));
                                    // println("两点距离="+d);
                                    double y = (location.getY() - location1.getY());
                                    double x = (location.getX() - location1.getX());
                                    int a = x >= 0 ? 1 : -1;
                                    int b = y >= 0 ? 1 : -1;


                                    if (d > 8) {
                                        location.setX((2.8 + (d - 8) * 0.05) * a + location1.getX());
                                        location.setY((2.8 + (d - 8) * 0.05) * b + location1.getY());


                                    } else if (d > 5) {
                                        location.setX((2.5 + (d - 5) * 0.1) * a + location1.getX());
                                        location.setY((2.5 + (d - 5) * 0.1) * b + location1.getY());
                                    } else if (d > 3) {
                                        location.setX((1 + (d - 3) * 0.75) * a + location1.getX());
                                        location.setY((1 + (d - 3) * 0.75) * b + location1.getY());
                                    } else {
                                        location.setX(d * 0.33 * a + location1.getX());
                                        location.setY(d * 0.33 * b + location1.getY());
                                        // println("这里666="+d*0.33*a);
                                    }
                                } else {
                                    println("不存在+位置");
                                }
                              /*    if(location.getMac().equals("f0c890021002")) {
                                      println("原始==" + location.getX());
                                      println("原始==" + location.getY());
                                  }*/

                                //    println("定位信标="+address+"   地图key="+beacon.getMap_key());

                            }

                            //    fWordcardMap.put(imei,fWordcard);
                            try {
                                redisUtil.set(redis_key_location + mac, location);
                            } catch (Exception e) {
                                println("莫名错误11");
                            }
                            //  println("location.getY="+location.getY());
                            wordcardA.setX(location.getX());
                            wordcardA.setY(location.getY());
                            list.add(wordcardA);
                            sendLocationPush_workcard(wordcardA);
                            //  if(s%6==0){
                            try {
                                JSONObject jsonObject1 = new JSONObject();
                                //   println("list="+list);
                                jsonObject1.put("tag", list);

                                RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(), wordcardA.getMap_key());
                                println("蓝牙工卡位置推送-----" + rabbitMessage1.toString());
                                directExchangeProducer.send(rabbitMessage1.toString(), "sendtoMap");
                            } catch (Exception e) {
                                println("莫名错误996" + e.getMessage());
                            }
                            //
                            redisUtil.set(redis_key_device_gateways + mac, null);
                        } catch (Exception e) {
                            println("22json=" + beaconTags);
                            println("11法国红酒封口的" + e.getMessage());
                            redisUtil.set(redis_key_device_gateways + mac, null);
                        }

                    }


                }



            }

        }

    }
    }


}

