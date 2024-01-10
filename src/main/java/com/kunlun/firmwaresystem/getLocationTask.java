package com.kunlun.firmwaresystem;

import com.google.gson.Gson;
import com.kunlun.firmwaresystem.device.Gateway_device;
import com.kunlun.firmwaresystem.device.Gateway_devices;
import com.kunlun.firmwaresystem.entity.Area;
import com.kunlun.firmwaresystem.location.Location;
import com.kunlun.firmwaresystem.util.LocationUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.redisUtil;
import static com.kunlun.firmwaresystem.NewSystemApplication.wordcard_aMap;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;

@Component
public class getLocationTask {
    Map<String, List<Integer>> map = new HashMap<>();
    LocationUtil util = new LocationUtil();
    int count = 0;

    @Scheduled(cron = "*/10 * * * * ?")
    public void execute() throws Exception {/*
        count++;
        for (String address : wordcard_aMap.keySet()) {
            //只对转发精准定位工卡做三点定位
            if (wordcard_aMap.get(address).getType() != 3) {
                continue;
            }
            *//*if(!address.equals("e5a2674d14b5")&&!address.equals("cabb00640003")&&!address.equals("cabb00640002")&&!address.equals("cabb00640001")){
                continue;
            }*//*
            String json = null;
            try {
                json = (String) redisUtil.get(redis_key_card_map + address);
                if (json == null) {
                    json = (String) redisUtil.get(redis_key_device_gateways + address);
                    //System.out.println("信标"+json);/
                }

            } catch (Exception e) {
                System.out.println("异常" + e.getMessage());
            }
            // System.out.println("数据="+json);
            if (json == null) {
                //  System.out.println("此设备没有收集到网关记录");
            } else {
                try {
                    Gateway_devices gateways = new Gson().fromJson(json, Gateway_devices.class);
                    List<Gateway_device> list = gateways.getGatewayDevices();
                    System.out.println("*长度//" + list.size());
                 *//*   for(int i=0;i<list.size();i++){
                        System.out.println("*长度//"+list.get(i).getgAddress()+"  信号="+list.get(i).getRssi());

                    }*//*
                    if (list == null) {
                        System.out.println("定位列表为空");
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
                  *//*  if(count==20){
                        System.out.println("开始写数据");
                        StringUtil.createExcelTwo("工卡与信标数据.xls",map);
                    }
                    else{
                        System.out.println("累计次数="+count);
                    }*//*
                    Location location = util.calculate(list, address);
                    if (location != null) {
                        System.out.println(address + " X=" + location.getX() + "   Y=" + location.getY());
                        //   location.setName("");
                  *//*      for(int i=0;i<area_list.size();i++){
                            Area area=area_list.get(i);
                            if(check(area,location)){
                                location.setName(area.getName());
                                System.out.println("已定位"+area.getName());
                                 break;
                            }
                        }*//*
                        redisUtil.set(redis_key_card_map + address, null);
                        redisUtil.set(redis_key_device_gateways + address, null);
                        System.out.println("已保存");
                        Location location1 = (Location) redisUtil.get(redis_key_location_tag + address);
                        if (location1 != null) {
                            double x = (location.getX() + location1.getX()) / 2;
                            double y = (location.getY() + location1.getY()) / 2;
                            location.setX(x);
                            location.setY(y);
                        }
                    *//*    if (LocationTask.recordMap.get(address) != null) {

                            LocationTask.recordMap.get(address).setX(location.getX());
                            LocationTask.recordMap.get(address).setY(location.getY());
                            if (address.equals("c906fae22cad")) {
                                System.out.println("工卡不为空" + LocationTask.recordMap.get(address));
                            }

                        }*//*
                        redisUtil.set(redis_key_location_tag + address, location);
                    }
                } catch (Exception e) {
                }
            }
        }*/
    }


}

