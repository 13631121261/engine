package com.kunlun.firmwaresystem;

import com.google.gson.Gson;
import com.kunlun.firmwaresystem.device.Gateway_device;
import com.kunlun.firmwaresystem.device.Gateway_devices;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.entity.device.Device_offline;
import com.kunlun.firmwaresystem.entity.device.Devicep;
import com.kunlun.firmwaresystem.mappers.DeviceOfflineMapper;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;
import com.kunlun.firmwaresystem.sql.Alarm_Sql;
import com.kunlun.firmwaresystem.sql.Beacon_Sql;
import com.kunlun.firmwaresystem.sql.DeviceOffline_Sql;
import com.kunlun.firmwaresystem.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import static com.kunlun.firmwaresystem.DeviceTask.writeLog;
import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static java.lang.StrictMath.abs;

@Component
public class BeaconTask {
    @Autowired
    private DeviceOfflineMapper deviceOfflineMapper;
    //信标判断在线离线
    public static List<Record> recordList = new ArrayList<>();
    public static Map<String, Record> recordMap = new HashMap<>();
    int runcount = 0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");//设置日期格式1

    @Scheduled(cron = "*/60 * * * * ?")
    public void execute() throws Exception {
        try {
            System.out.println("信标定时检测运行  60秒一次-------------------------------------------" + runcount);
            Beacon_Sql beacon_sql = new Beacon_Sql();
            if(beaconsMap==null){
                writeLog("beaconsMap是空的，重新获取一次");

                beaconsMap = beacon_sql.getAllBeacon(beaconMapper);
            }
            long thisTime = System.currentTimeMillis()/1000;
            runcount++;
            if (runcount > 10000) {
                runcount = 0;
            }
          //  System.out.println("111");
            Beacon beacon;
            DeviceOffline_Sql deviceOffline_sql = new DeviceOffline_Sql();
            for (String mac : beaconsMap.keySet()) {
                beacon = beaconsMap.get(mac);
               System.out.println(mac + "信标mac=" + beacon.getLastTime());
               // System.out.println("222");
                if (beacon.getLastTime() == 0) {
                    //beacon.setOnline(0);
                    StringUtil.sendBeaconPush(beacon, 0);
                    if (beacon.getDevice_sn() != null) {
                        Device_offline device_offline1 = deviceOffline_sql.gettime(deviceOfflineMapper, beacon.getDevice_sn(), beacon.getUser_key());
                        if (device_offline1 != null) {
                            beacon.setLastTime(device_offline1.getLasttime());
                        } else {
                            beacon.setLastTime(-28800);
                        }
                    } else {
                        beacon.setLastTime(-28800);
                    }
                    continue;
                }
                //System.out.println("333");
                System.out.println(beacon.getMac() + "信标相差时间" + (thisTime - beacon.getLastTime()));
                int linetime = check_sheetMap.get(beacon.getUser_key()).getLinetime();
                System.out.println(beacon.getUser_key() + "信标检测在线周期=" + linetime + "分钟");
                System.out.println(beacon.getMac() + "信标最后在线时间=" + beacon.getLastTime());
              if (thisTime - beacon.getLastTime() < linetime * 60) {
                //   if (thisTime - beacon.getLastTime() < 20) {
                    //beacon.setOnline(1);
                    System.out.println("信标在线");
                    StringUtil.sendBeaconPush(beacon, 1);
                    Devicep deviceP = null;
                    if (beacon.getIsbind() == 1) {
                        if(beacon.getBind_type()==1){
                            redisUtil.set300(device_check_online_status_res+beacon.getDevice_sn(),"1");
                            deviceP = devicePMap.get(beacon.getDevice_sn());
                            if(deviceP==null){
                                continue;
                            }
                            deviceP.setRssi(beacon.getRssi());
                            deviceP.setBt(beacon.getBt());
                        }else if(beacon.getBind_type()==2){
                            redisUtil.set300(person_check_online_status_res+beacon.getDevice_sn(),"1");
                           /*Person person = personMap.get(beacon.getDevice_sn());
                            if(person==null){
                                continue;
                            }
                            person.setRssi(beacon.getRssi());
                            person.setBt(beacon.getBt());
                            */
                        }
                    }
                    //未绑定设备，就不对设备进行做离线记录了
                    else if (beacon.getIsbind() == 0) {
                        continue;
                    }
                    //不需要额外操作，因为正常来说，同步绑定设备已在信标上传的同时就进行同步了
                  //  System.out.println("设备在线");
                    String gs = (String) redisUtil.get(redis_key_device_gateways + deviceP.getBind_mac());
                    Gateway_devices gatewayDevices = new Gson().fromJson(gs, Gateway_devices.class);
                    Gateway_device gateway_device1 = null;
                    if (gatewayDevices != null) {
                        List<Gateway_device> list = gatewayDevices.getGatewayDevices();
                        int i = list.size() - 1;
                        int rssi = -100;
                        for (; i >= 0; i--) {
                            Gateway_device gateway_device = list.get(i);
                            //System.out.println("网关循环=" + gateway_device.getAddress() + "   时间=" + gateway_device.getTime());
                            if (thisTime - gateway_device.getTime() < 60 * 3) {
                               // System.out.println("符合循环=" + gateway_device.getAddress() + gateway_device.getRssi());
                                if (gateway_device.getRssi() > rssi) {
                                    rssi = gateway_device.getRssi();
                                    gateway_device1 = gateway_device;
                                    System.out.println("最后循环=" + gateway_device.getAddress() + gateway_device.getRssi());
                                }
                            }
                        }
                    }
                    if (gateway_device1 != null){
                        System.out.println(GatewayMap.get(gateway_device1.getAddress()).getArea_id());
                        Area area = area_Map.get(GatewayMap.get(gateway_device1.getAddress()).getArea_id());
                        if(deviceP.getSn().equals("10013100001628")){
                            if(area!=null){
                                if(deviceP.getIs_area()==1&&deviceP.getArea_id()!=area.getId()&&deviceP.getB_area_id()!=area.getId()){
                                    deviceP.setArea_sos(1);
                                    System.out.println("区域报警了111");
                                    JSONObject jsonObject1=new JSONObject();
                                    jsonObject1.put("device", deviceP);
                                    jsonObject1.put("type", "device_area_sos");
                                    jsonObject1.put("id",886);
                                    jsonObject1.put("time",df.format(new Date()));

                                    System.out.println("原始"+df.format(new Date())+"发给网页了"+"id="+886);

                                    RabbitMessage rabbitMessage1 = new RabbitMessage(deviceP.getProject_key(),jsonObject1.toString());
                                    directExchangeProducer.send(rabbitMessage1.toString(), "sendtoHtml");

                                }else {
                                    System.out.println("区域报警了000");
                                    deviceP.setArea_sos(0);

                                }
                            }else {
                                if(deviceP.getIs_area()==1&&deviceP.getArea_sos()==0){
                                    System.out.println("2区域报警了111");
                                    deviceP.setArea_sos(1);
                                }else {
                                    System.out.println("2区域报警了000");
                                }
                            }

                        }

                      //  System.out.println(deviceP);
                        if (area != null) {
                            System.out.println("设置资产区域="+area.getName());
                            deviceP.setPoint_name(area.getMap_name());
                            deviceP.setB_area_name(area.getName());
                            deviceP.setB_area_id(area.getId());
                            deviceP.setGateway_mac(gateway_device1.getAddress());
                        } else {
                            deviceP.setPoint_name(GatewayMap.get(gateway_device1.getAddress()).getName());
                            deviceP.setPoint_name("");
                            deviceP.setB_area_name("");
                            deviceP.setB_area_id(-1);
                            deviceP.setGateway_mac(gateway_device1.getAddress());
                        }
                    } else {
                        //这种情况不应该存在，如果存在，那就是系统异常，存到log
                        writeLog("这种情况不应该存在，如果存在，那就是系统异常，存到log");
                    }
                } else {
                  System.out.println("信标离线"+beacon.getMac());

                    //  beacon.setOnline(0);
                   /* if (beacon.getOnline() != 0) {
                        StringUtil.saveRecord(beacon.getMac(), beacon.getLastTime(), beacon.getUser_key(), 2, 0,beacon.getProject_key());
                    }*/
                    StringUtil.sendBeaconPush(beacon, 0);

                    if (beacon.getIsbind() == 1) {
                        if(beacon.getBind_type()==1){
                            Devicep deviceP = devicePMap.get(beacon.getDevice_sn());
                          String res=(String)redisUtil.get(device_check_online_status_res+beacon.getDevice_sn());
                          if(deviceP==null){
                                System.out.println("不符合，继续");
                                continue;
                            }else{
                                System.out.println("继续运行");
                            }
                          if(res!=null&&res.equals("0")){
                              System.out.println("已经保存为离线，不需要再次记录");
                              redisUtil.set300(device_check_online_status_res+beacon.getDevice_sn(),"0");
                              continue;
                          }

                            redisUtil.set300(device_check_online_status_res+beacon.getDevice_sn(),"0");
                            StringUtil.sendDevicePush(deviceP,0);
                            // deviceP.setOnline(0);
                            Alarm_Sql alarm_sql = new Alarm_Sql();
                            alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_offline,Alarm_object.device,beacon.getMap_key(),deviceP.getFence_id(),fenceMap.get(deviceP.getFence_id()).getName(),beacon.getBt(),0,"",deviceP.getName(),deviceP.getSn(),deviceP.getProject_key()));

                        }else if(beacon.getBind_type() == 2){
                            Person person = personMap.get(beacon.getDevice_sn());
                            if(person==null){
                                System.out.println("不符合，继续");
                                continue;
                            }else{
                                System.out.println("继续运行");
                            }
                            String res=(String)redisUtil.get(person_check_online_status_res+beacon.getDevice_sn());
                            if(res!=null&&res.equals("0")){
                                redisUtil.set300(person_check_online_status_res+beacon.getDevice_sn(),"0");
                                System.out.println("已经保存为离线，不需要再次记录");
                                continue;
                            }
                            else{
                                System.out.println("未保存为离线，"+res);
                            }
                            System.out.println("开始保存为离线");
                            redisUtil.set300(person_check_online_status_res+beacon.getDevice_sn(),"0");

                            //StringUtil.sendDevicePush(deviceP,0);
                            // deviceP.setOnline(0);
                            Alarm_Sql alarm_sql = new Alarm_Sql();
                            alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_offline,Alarm_object.person,beacon.getMap_key(),person.getFence_id(),fenceMap.get(person.getFence_id()).getName(),beacon.getBt(),0,"",person.getName(),person.getIdcard(),person.getProject_key()));
                        }
                    //    deviceOffline_sql.addDeviceOffline(deviceOfflineMapper, new Device_offline(deviceP.getSn(), deviceP.getName(), deviceP.getRssi(), deviceP.getBt(), deviceP.getBind_mac(), deviceP.getPhoto(), deviceP.getType_id(), deviceP.getPoint_name(), deviceP.getGateway_mac(), beacon.getLastTime(), beacon.getUser_key(), beacon.getCustomer_key()));
                    }else{
                        System.out.println("未绑定");
                    }
                }
                beacon_sql.update(beaconMapper, beacon);
            }
        }catch (Exception e){
            System.out.println("信标定时运行异常报错="+e.getMessage());
            writeLog("信标定时运行异常报错="+e.getMessage());
        }
    }
}