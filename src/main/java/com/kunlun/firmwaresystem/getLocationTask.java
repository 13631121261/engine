package com.kunlun.firmwaresystem;

import com.google.gson.Gson;
import com.kunlun.firmwaresystem.device.Gateway_devices;
import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.entity.Beacon_tag;
import com.kunlun.firmwaresystem.entity.Check_sheet;
import com.kunlun.firmwaresystem.entity.FWordcard;
import com.kunlun.firmwaresystem.location_util.backup.Gateway_device;
import com.kunlun.firmwaresystem.location_util.backup.Location;
import com.kunlun.firmwaresystem.location_util.backup.LocationUtil;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;
import com.kunlun.firmwaresystem.sql.FWordcard_Sql;
import com.kunlun.firmwaresystem.sql.History_Sql;
import net.sf.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.util.StringUtil.sendLocationPush;
import static com.kunlun.firmwaresystem.util.StringUtil.sendLocationPush_OFcat1;
import static java.lang.Math.abs;

@Component
public class getLocationTask {
    final boolean open_b=true;
    //取两点之间的坐标值。二分之一
    final double open_b_p=0.33;
    public static String beacon_address="";
    public static int interval=3;
    Map<String, List<Integer>> map = new HashMap<>();
    LocationUtil util = new LocationUtil();
    int count = 0;
    int s=0;
    History_Sql history_sql=new History_Sql();
    @Scheduled(cron = "*/1 * * * * ?")
    public void execute() throws Exception {

   //  println("定时一秒一次="+count);
        count++;


        /*
        count++;
        for (String address : wordcard_aMap.keySet()) {
            //只对转发精准定位工卡做三点定位
            if (wordcard_aMap.get(address).getType() != 3) {
                continue;
            }
            if(!address.equals("e5a2674d14b5")&&!address.equals("cabb00640003")&&!address.equals("cabb00640002")&&!address.equals("cabb00640001")){
                continue;
            }
            String json = null;
            try {
                json = (String) redisUtil.get(redis_key_card_map + address);
                if (json == null) {
                    json = (String) redisUtil.get(redis_key_device_gateways + address);
                    //println("信标"+json);/
                }

            } catch (Exception e) {
                println("异常" + e.getMessage());
            }
            // println("数据="+json);
            if (json == null) {
                //  println("此设备没有收集到网关记录");
            } else {
                try {
                    Gateway_devices gateways = new Gson().fromJson(json, Gateway_devices.class);
                    List<Gateway_device> list = gateways.getGatewayDevices();
                    println("*长度//" + list.size());
                    for(int i=0;i<list.size();i++){
                        println("*长度//"+list.get(i).getgAddress()+"  信号="+list.get(i).getRssi());

                    }
                    if (list == null) {
                        println("定位列表为空");
                        return;
                    }
                    for (Gateway_device gateway_device : list) {
                        List<Integer> list1 = map.get(gateway_device.getgAddress());
                        if (list1 == null) {
                            list1 = new ArrayList<>();
                        }
                        list1.add(gateway_device.getRssi());
                        map.put(gateway_device.getgAddress(), list1);
                    }
                    if(count==20){
                        println("开始写数据");
                        StringUtil.createExcelTwo("工卡与信标数据.xls",map);
                    }
                    else{
                        println("累计次数="+count);
                    }
                    Location location = util.calculate(list, address);
                    if (location != null) {
                        println(address + " X=" + location.getX() + "   Y=" + location.getY());
                        //   location.setName("");
                        for(int i=0;i<area_list.size();i++){
                            Area area=area_list.get(i);
                            if(check(area,location)){
                                location.setName(area.getName());
                                println("已定位"+area.getName());
                                 break;
                            }
                        }
                        redisUtil.set(redis_key_card_map + address, null);
                        redisUtil.set(redis_key_device_gateways + address, null);
                        println("已保存");
                        Location location1 = (Location) redisUtil.get(redis_key_location_tag + address);
                        if (location1 != null) {
                            double x = (location.getX() + location1.getX()) / 2;
                            double y = (location.getY() + location1.getY()) / 2;
                            location.setX(x);
                            location.setY(y);
                        }
                        if (LocationTask.recordMap.get(address) != null) {

                            LocationTask.recordMap.get(address).setX(location.getX());
                            LocationTask.recordMap.get(address).setY(location.getY());
                            if (address.equals("c906fae22cad")) {
                                println("工卡不为空" + LocationTask.recordMap.get(address));
                            }

                        }
                        redisUtil.set(redis_key_location_tag + address, location);
                    }
                } catch (Exception e) {
                }
            }
        }*/
        if(check_sheetMap==null){
            return;

        }
        for(String key:check_sheetMap.keySet()){
          Check_sheet check_sheet =check_sheetMap.get(key);

          if(check_sheet!=null){
              int interval=check_sheet.getIntervals();
              if(count%interval==0){
                  //println("符合="+interval+" 项目="+key);
                  s++;
                  Gateway_devices gateways = null;
                  ArrayList<Gateway_device> beaconTags = null;
                //  println("debug-="+beacon_address);
                  if(beacon_address!=null&& !beacon_address.isEmpty()){

                      Beacon beacon=beaconsMap.get(beacon_address);

                      if(beacon!=null){
                          if(beacon.getProject_key().equals(key)){
                              String json = null;
                              try {
                                  json = (String) redisUtil.get(redis_key_device_gateways + beacon_address);
                                  if(json!=null){
                                      gateways = new Gson().fromJson(json, Gateway_devices.class);
                                      Location location = util.calculate(    gateways.getGatewayDevices(), beacon_address);

                                     // println("X="+location.getX()+"   Y="+location.getY());
                                      beacon.setMap_key(location.getMap_key());
                                      ArrayList<Object> list=new ArrayList<>();
                                      if(open_b){
                                        Location location1=null;
                                          try {
                                              location1=(Location) redisUtil.get(redis_key_location +beacon_address);

                                          }catch (Exception e){
                                              println("莫名错误11");
                                          }
                                          if(location1!=null){
                                              //<=3米，33%        0.33x              1米对应0.33米， 3米对应1米
                                              //3-5米，   1+(x-3)*0.75                5米对应2.5米
                                              //5-8米，   2.5+（x-5)*0.1            8米对应2.8米
                                              //>8米    2.8+(x-8)* 0.05            12米对应3米      20米对应3.4米
                                              double d=Math.sqrt(Math.pow((location.getX()-location1.getX()),2)+Math.pow((location.getY()-location1.getY()),2));
                                              double y=(location.getY()-location1.getY());
                                              double x=(location.getX()-location1.getX());
                                              int a=x>=0?1:-1;
                                              int b=y>=0?1:-1;
                                              if(d>8){
                                                  location.setX(( 2.8+(d-8)* 0.05)*a+location1.getX());
                                                  location.setY(( 2.8+(d-8)* 0.05)*b+location1.getY());

                                              }else if(d>5){
                                                  location.setX((2.5+(d-5)*0.1)*a+location1.getX());
                                                  location.setY((2.5+(d-5)*0.1)*b+location1.getY());
                                              }
                                              else if(d>3){
                                                  location.setX((1+(d-3)*0.75)*a +location1.getX());
                                                  location.setY((1+(d-3)*0.75)*b +location1.getY());
                                              }else {
                                                  location.setX(d*0.33*a+location1.getX());
                                                  location.setY(d*0.33*b+location1.getY());
                                                  // println("这里666="+d*0.33*a);
                                              }
                                          }
                                          beacon.setX(location.getX());
                                          beacon.setY(location.getY());
                                          redisUtil.set(redis_key_location + beacon_address,location);
                                          list.add(beacon);
                                      }
                                      beacon.setUseDatalist((ArrayList)location.getDataMap());
                                      beacon.setGatewayDevices(gateways.getGatewayDevices());


                              //        println("debug推送"+beacon_address);
                                      JSONObject jsonObject1 = new JSONObject();
                                      jsonObject1.put("tag", list);
                                      RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(),beacon.getMap_key());
                                      directExchangeProducer.send(rabbitMessage1.toString(), "sendtoMap_debug");
                                  }
                              } catch (Exception e) {
                                  println("json="+json);
                                  println("22法国红酒封口的"+json);
                              }
                          }
                      }




                  }























                  for(String address:beaconsMap.keySet()){

                      Beacon beacon=beaconsMap.get(address);
                      if(beacon!=null&&beacon.getOnline()==1&&beacon.getProject_key().equals(key)){

                          String json = null;
                          try {
                              json = (String) redisUtil.get(redis_key_device_gateways + address);
                              if(json!=null){

                                  gateways = new Gson().fromJson(json, Gateway_devices.class);
                               ///   println("数据="+json);
                                  if(gateways.getGatewayDevices().isEmpty()){
                                      if(address.equals("f0c890022077")){
                                          println("参数为空+位置");
                                      }
                                      continue;
                                  }
                                  Location location = util.calculate(    gateways.getGatewayDevices(), address);
                                //  println("计算位置="+location);



                                  beacon.setMap_key(location.getMap_key());

                                  ArrayList<Object> list=new ArrayList<>();
                                  if(open_b){
                                   //记得这里可能涉及json的无穷大只
                                   //   aa
                                      Location location1=null;
                                      try {
                                          location1=(Location) redisUtil.get(redis_key_location +address);
                                      }catch (Exception e){
                                          println("莫名错误11");
                                      }

                                      if(location1!=null&&!Double.isNaN(location1.getY())&&!Double.isNaN(location1.getX())){

                                        //  println("location1="+location1);
                                          //<=3米，33%        0.33x              1米对应0.33米， 3米对应1米
                                          //3-5米，   1+(x-3)*0.75                5米对应2.5米
                                          //5-8米，   2.5+（x-5)*0.1            8米对应2.8米
                                          //>8米    2.8+(x-8)* 0.05            12米对应3米      20米对应3.4米
                                          double d=Math.sqrt(Math.pow((location.getX()-location1.getX()),2)+Math.pow((location.getY()-location1.getY()),2));
                                         // println("两点距离="+d);
                                          double y=(location.getY()-location1.getY());
                                          double x=(location.getX()-location1.getX());
                                          int a=x>=0?1:-1;
                                          int b=y>=0?1:-1;
                                         /* if(location.getMac().equals("f0c890021002")){
                                              println("原始=="+location.getX());
                                              println("原始=="+location.getY());
                                              println("旧的原始=="+location1.getX());
                                              println("旧的原始=="+location1.getY());

                                              println("A="+a);
                                              println("B="+b);
                                              println("D="+d);
                                          }*/

                                          if(d>8){
                                              location.setX(( 2.8+(d-8)* 0.05)*a+location1.getX());
                                              location.setY(( 2.8+(d-8)* 0.05)*b+location1.getY());


                                          }else if(d>5){
                                              location.setX((2.5+(d-5)*0.1)*a+location1.getX());
                                              location.setY((2.5+(d-5)*0.1)*b+location1.getY());
                                          }
                                          else if(d>3){
                                              location.setX((1+(d-3)*0.75)*a +location1.getX());
                                              location.setY((1+(d-3)*0.75)*b +location1.getY());
                                          }else {
                                              location.setX(d*0.33*a+location1.getX());
                                              location.setY(d*0.33*b+location1.getY());
                                             // println("这里666="+d*0.33*a);
                                          }
                                      }else{
                                              println("不存在+位置");
                                      }
                                  /*    if(location.getMac().equals("f0c890021002")) {
                                          println("原始==" + location.getX());
                                          println("原始==" + location.getY());
                                      }*/
                                      list.add(beacon);
                                  //    println("定位信标="+address+"   地图key="+beacon.getMap_key());

                                  }
                                  beaconsMap.put(address,beacon);
                                  try {
                                      redisUtil.set(redis_key_location + address, location);
                                  }catch (Exception e){
                                      println("莫名错误11");
                                  }
                                //  println("location.getY="+location.getY());
                                  beacon.setX(location.getX());
                                  beacon.setY(location.getY());
                                  sendLocationPush(beacon);
                                //  if(s%6==0){
                                  try {
                                      JSONObject jsonObject1 = new JSONObject();
                                   //   println("list="+list);
                                      jsonObject1.put("tag", list);

                                      RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(), beacon.getMap_key());
                                      directExchangeProducer.send(rabbitMessage1.toString(), "sendtoMap");
                                  }
                                  catch (Exception e){
                                      println("莫名错误996"+e.getMessage());
                                  }
                                //  }

                                  gateways.setGatewayDevices(new ArrayList<>());
                                  redisUtil.set(redis_key_device_gateways + address,gateways.toString());
                              }
                          } catch (Exception e) {
                              println("json="+json);
                              println("11法国红酒封口的"+e.getMessage());
                          }

                      }


                  }






                  FWordcard_Sql fWordcardSql=new FWordcard_Sql();
                 HashMap <String,FWordcard> fWordcardMap1=fWordcardSql.getAll(fWordcardMapper);

                  for(String imei:fWordcardMap1.keySet()){

                      FWordcard  fWordcard=fWordcardMap1.get(imei);
                     // println("11数据="+fWordcard);
                      if(fWordcard!=null&&fWordcard.getOnline()==1&&fWordcard.getProject_key().equals(key)){
                       //   println("331数据="+fWordcard);

                          try {
                              Object o=redisUtil.get(redis_key_device_gateways + imei);
                              if(o==null){
                                  return;
                              }
                              beaconTags = (ArrayList<Gateway_device>) redisUtil.get(redis_key_device_gateways + imei);
                              println("1数据="+beaconTags);
                              if(beaconTags!=null){
                                  println("1数据2="+beaconTags.size());
                                  if(beaconTags.isEmpty()){
                                      continue;
                                  }
                                  Location location = util.calculate( beaconTags, imei);
                                  //  println("计算位置="+location);



                                  fWordcard.setMap_key(location.getMap_key());

                                  ArrayList<Object> list=new ArrayList<>();
                                  if(open_b){
                                      //记得这里可能涉及json的无穷大只
                                      //   aa
                                      Location location1=null;
                                      try {
                                          location1=(Location) redisUtil.get(redis_key_location +imei);
                                      }catch (Exception e){
                                          println("莫名错误11");
                                      }

                                      if(location1!=null&&!Double.isNaN(location1.getY())&&!Double.isNaN(location1.getX())){

                                          //  println("location1="+location1);
                                          //<=3米，33%        0.33x              1米对应0.33米， 3米对应1米
                                          //3-5米，   1+(x-3)*0.75                5米对应2.5米
                                          //5-8米，   2.5+（x-5)*0.1            8米对应2.8米
                                          //>8米    2.8+(x-8)* 0.05            12米对应3米      20米对应3.4米
                                          double d=Math.sqrt(Math.pow((location.getX()-location1.getX()),2)+Math.pow((location.getY()-location1.getY()),2));
                                          // println("两点距离="+d);
                                          double y=(location.getY()-location1.getY());
                                          double x=(location.getX()-location1.getX());
                                          int a=x>=0?1:-1;
                                          int b=y>=0?1:-1;
                                         /* if(location.getMac().equals("f0c890021002")){
                                              println("原始=="+location.getX());
                                              println("原始=="+location.getY());
                                              println("旧的原始=="+location1.getX());
                                              println("旧的原始=="+location1.getY());

                                              println("A="+a);
                                              println("B="+b);
                                              println("D="+d);
                                          }*/

                                          if(d>8){
                                              location.setX(( 2.8+(d-8)* 0.05)*a+location1.getX());
                                              location.setY(( 2.8+(d-8)* 0.05)*b+location1.getY());


                                          }else if(d>5){
                                              location.setX((2.5+(d-5)*0.1)*a+location1.getX());
                                              location.setY((2.5+(d-5)*0.1)*b+location1.getY());
                                          }
                                          else if(d>3){
                                              location.setX((1+(d-3)*0.75)*a +location1.getX());
                                              location.setY((1+(d-3)*0.75)*b +location1.getY());
                                          }else {
                                              location.setX(d*0.33*a+location1.getX());
                                              location.setY(d*0.33*b+location1.getY());
                                              // println("这里666="+d*0.33*a);
                                          }
                                      }else{
                                          println("不存在+位置");
                                      }
                                  /*    if(location.getMac().equals("f0c890021002")) {
                                          println("原始==" + location.getX());
                                          println("原始==" + location.getY());
                                      }*/
                                      list.add(fWordcard);
                                      //    println("定位信标="+address+"   地图key="+beacon.getMap_key());

                                  }
                              //    fWordcardMap.put(imei,fWordcard);
                                  try {
                                      redisUtil.set(redis_key_location + imei, location);
                                  }catch (Exception e){
                                      println("莫名错误11");
                                  }
                                  //  println("location.getY="+location.getY());
                                  fWordcard.setX(location.getX());
                                  fWordcard.setY(location.getY());
                                  sendLocationPush_OFcat1(fWordcard);
                                  //  if(s%6==0){
                                  try {
                                      JSONObject jsonObject1 = new JSONObject();
                                      //   println("list="+list);
                                      jsonObject1.put("tag", list);

                                      RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(), fWordcard.getMap_key());
                                      println("CAT1推送-----"+rabbitMessage1.toString());
                                      directExchangeProducer.send(rabbitMessage1.toString(), "sendtoMap");
                                  }
                                  catch (Exception e){
                                      println("莫名错误996"+e.getMessage());
                                  }
                                  //
                                  redisUtil.set(redis_key_device_gateways + imei,"");
                              }
                          } catch (Exception e) {
                              println("json="+beaconTags);
                              println("11法国红酒封口的"+e.getMessage());
                          }

                      }


                  }

              }
          }
        }


    }


}

