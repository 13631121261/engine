package com.kunlun.firmwaresystem.mqtt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.device.Gateway_device;
import com.kunlun.firmwaresystem.device.Gateway_devices;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.entity.device.Devicep;
import com.kunlun.firmwaresystem.gatewayJson.Constant;
import com.kunlun.firmwaresystem.gatewayJson.state.ConnectDetail;
import com.kunlun.firmwaresystem.gatewayJson.state.ConnectState;
import com.kunlun.firmwaresystem.gatewayJson.state.HeartDetail;
import com.kunlun.firmwaresystem.gatewayJson.state.HeartState;
import com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params;
import com.kunlun.firmwaresystem.gatewayJson.type_response.App_Server;
import com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter;
import com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_params;
import com.kunlun.firmwaresystem.gatewayJson.type_response.*;
import com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Ibcn_infos;
import com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report;
import com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report_data;
import com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report_data_info;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.StringUtil;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.gatewayStatusTask.writeLog;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.transpond;
import static com.kunlun.firmwaresystem.util.StringUtil.sendDeviceSoSPush;
import static com.kunlun.firmwaresystem.util.StringUtil.sendTagPush;

public class CallBackHandlers implements Runnable {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    long times=System.currentTimeMillis()/1000;
    long time;
    String topic;
    MqttMessage message;

    Gson gson = new Gson();

    public CallBackHandlers(String topic, MqttMessage message) {
        this.message = message;
        this.topic = topic;
    }
    @Override
    public void run() {
        // subscribe后得到的消息会执行到这里面
        //  System.out.println("接收消息主题:" + topic);

        if(topic.equals("connected")){

        }
        if(topic.equals("/cle/mqtt")){

           try {

               String map_key="";
              String jsonstr= StringUtil.unzip(message.getPayload());
              JSONObject jsonObject=JSONObject.parseObject(jsonstr);
             //  System.out.println(jsonstr);
               if(jsonObject.getString("type").equals("sensors")) {
                   //标签定位
                   JSONObject beacons = jsonObject.getJSONObject("data");
                   Set<String> macs = beacons.keySet();
                   ArrayList<Object> deviceps = new ArrayList<>();
                  // System.out.println("步骤1");
                   for (String key : macs) {
                      // System.out.println(key);
                       if (beaconsMap.get(key) != null) {
                           Locator locator=null;
                           Beacon beacon = beaconsMap.get(key);
                           if(beacon==null){
                               return;
                           }
                        //   System.out.println("信标" + beacon);
                           if(beacon!=null){
                               JSONObject a = beacons.getJSONObject(key);
                               beacon.setX(a.getDouble("x"));
                               beacon.setY(a.getDouble("y"));
                               beacon.setLastTime(a.getLong("updatedAt") / 1000);
                               beacon.setRssi(a.getIntValue("rssi"));
                               beacon.setGateway_address(a.getString("nearestGateway"));
                               locator=(Locator)  redisUtil.get(redis_key_locator+  beacon.getGateway_address());
                               //System.out.println("基站="+locator+beacon.getGateway_address());
                               beacon.setOnline(1);
                               beacon.setBt(a.getIntValue("rssi"));
                               if (a.getJSONObject("userData") != null) {
                                   JSONObject userData = a.getJSONObject("userData");
                                   if (userData.getJSONArray("9") != null) {
                                       JSONArray s9 = userData.getJSONArray("9");
                                       String str = Integer.toBinaryString((((int) s9.get(1)) & 0xFF) + 0x100).substring(1);
                                       char[] bit = str.toCharArray();
                                       if (bit[2] == '1') {
                                           beacon.setSos(1);
                                        //   System.out.println("信标SOS1");
                                       } else {
                                         //  System.out.println("信标SOS/"+str);
                                           beacon.setSos(0);
                                       }
                                       if (bit[4] == '1') {
                                           beacon.setRun(1);
                                       } else {
                                          // System.out.println("信标SOS/"+str);
                                           beacon.setRun(0);
                                       }

                                       double bt = ((double)((int) s9.get(3))*1.0) / 255 * 6.6;
                                       DecimalFormat decimalFormat=new DecimalFormat("#.00");
                                       String bts= decimalFormat.format(bt);
                                       beacon.setBt(Double.valueOf(bts));

                                       // System.out.println(beacon.toString());
                                   }
                               }
                           }
                           if (beacon.getIsbind() == 1 && beacon.getDevice_sn() != null) {
                               JSONObject a = beacons.getJSONObject(key);

                                //这里会有一个过期的缓存
                                   Map map = (Map) redisUtil.get(redis_id_map + a.getString("mapId"));


                                   if (map != null) {
                                       beacon.setY(map.getHeight() - beacon.getY());
                                     //  redisUtil.set(redis_id_map + a.getString("mapId"), map,0);
                                       beacon.setMap_key(map.getMap_key());
                                       map_key = map.getMap_key();
                                      // System.out.println("地图=" + map_key);
                                   }else{
                                       System.out.println("地图是空的");
                                   }
                                //   System.out.println("步骤2");
                                   if (beacon.getBind_type() == 1) {
                                       Devicep devicep = devicePMap.get(beacon.getDevice_sn());
                                       devicep.setX(beacon.getX());
                                       devicep.setY(beacon.getY());
                                       devicep.setLasttime(beacon.getLastTime());
                                       devicep.setGateway_mac(beacon.getGateway_address());

                                       devicep.setSos(beacon.getSos());
                                       devicep.setOnline(1);
                                       devicep.setRun(beacon.getRun());
                                       devicep.setBt(beacon.getBt());
                                    //   System.out.println("资产="+devicep);
                                       if(locator!=null){
                                        //   System.out.println("有信息"+devicep);
                                           devicep.setB_area_name(locator.getArea_name());
                                           devicep.setB_area_id(locator.getArea_id());
                                           devicep.setMap_name(locator.getMap_name());
                                           devicep.setGateway_name(locator.getName());
                                       }
                                       deviceps.add(devicep);

                                   //    System.out.println("步骤2" + key);
                                       String res = (String) redisUtil.get(device_check_online_status_res + beacon.getDevice_sn());
                                       if (res == null || res.equals("0")) {
                                           Alarm_Sql alarm_sql = new Alarm_Sql();
                                           alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_online, Alarm_object.device, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", devicep.getName(), devicep.getSn(), devicep.getProject_key()));
                                       }
                                       redisUtil.setnoTimeOut(device_check_online_status_res + beacon.getDevice_sn(), "1");
                                       handleSos_AOA(beacon);
                                       handleFence(beacon, map.getProportion());
                                       handleBt_AOA(beacon);
                                       if(devicep.getOpen_run()==1){
                                           handleRun_AOA(beacon);
                                       }

                                   }
                                   //System.out.println(beacons.getJSONObject(key));
                                   else if (beacon.getBind_type() == 2) {
                                       Person person = personMap.get(beacon.getDevice_sn());
                                      // System.out.println("缩放" + map.getProportion());
                                       person.setX(beacon.getX());
                                       person.setY(beacon.getY());
                                       person.setOnline(1);
                                       if(locator!=null){
                                           System.out.println(locator);
                                           person.setB_area_name(locator.getArea_name());
                                           person.setB_area_id(locator.getArea_id());
                                           person.setMap_name(locator.getMap_name());
                                           person.setGateway_mac(locator.getAddress());
                                           person.setGateway_name(locator.getName());
                                          // System.out.println(person);
                                       }
                                       deviceps.add(person);
                                       // System.out.println("数量"+deviceps.size()+deviceps);
                                       String res = (String) redisUtil.get(person_check_online_status_res + beacon.getDevice_sn());
                                      // System.out.println("步骤2" + key);
                                       if (res == null || res.equals("0")) {
                                           Alarm_Sql alarm_sql = new Alarm_Sql();
                                           alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_online, Alarm_object.person, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", person.getName(), person.getIdcard(), person.getProject_key()));
                                       }
                                       redisUtil.setnoTimeOut(person_check_online_status_res + beacon.getDevice_sn(), "1");
                                       handleFence(beacon, map.getProportion());
                                       handleSos_AOA(beacon);
                                       handleBt_AOA(beacon);

                                       //   System.out.println("步骤2");
                                   }
                           }
                       }
                   }
                   if (deviceps.size() > 0) {
                       //System.out.println("需要推送的1");
                       sendTagPush(deviceps, map_key);
                   } else {
                       //    System.out.println("没有需要推送的"+deviceps);
                   }
               }else if(jsonObject.getString("type").equals("locators")){
                   JSONObject locators = jsonObject.getJSONObject("data");
                   Set<String> ips = locators.keySet();

                 //  System.out.println("步骤1");
                   for (String ip : ips) {
                      // System.out.println("IP ="+ip);
                       JSONObject a = locators.getJSONObject(ip);
                       if(a!=null){
                           int tag=0;
                         String address=  a.getString("mac").replaceAll(":","");
                         Locator locator=(Locator)  redisUtil.get(redis_key_locator+address);
                         if(locator==null){
                             System.out.println("新的AOA");
                             locator=new Locator();
                             locator.setAddress(address);
                             tag=1;
                         }else{
                             //System.out.println("旧的AOA");
                         }
                          JSONObject info = a.getJSONObject("info");
                           locator.setX(info.getDouble("x"));
                           locator.setY(info.getDouble("y"));

                           locator.setZ(info.getDouble("z"));
                           locator.setMap_id(info.getString("mapId"));
                           Map map = (Map) redisUtil.get(redis_id_map + info.getString("mapId"));
                           if(map!=null){
                               locator.setMap_key(map.getMap_key());
                               locator.setUser_key(map.getUser_key());
                               locator.setProject_key(map.getProject_key());
                               locator.setMap_name(map.getName());
                               locator.setY(map.getHeight() - locator.getY());
                               locator.setProportion(map.getProportion());
                           }else{
                               //此基站未有绑定地图,不自动增加
                               continue;
                           }

                           locator.setName(info.getString("name"));

                           locator.setModel_name(info.getString("modelName"));
                           locator.setVersion(info.getString("version"));
                           locator.setIp(ip);
                           locator.setOnline(a.getBoolean("online")?1:0);
                           locator.setLast_time(a.getLong("updatedAt")/1000);
                        if(tag==1){
                            Locators_Sql locators_sql=new Locators_Sql();
                            locators_sql.addLocator(locatorMapper,locator);
                        }
                        redisUtil.setnoTimeOut(redis_key_locator+address,locator);
                       }
                   }
               }
           }catch (DataFormatException e){
               System.out.println("AOA解析异常1"+e);
            }catch (IOException e) {
               System.out.println("AOA解析异常2"+e);
           }
            return;
        }
        String data = new String(message.getPayload());
     //   System.out.println("接收消息Qos:" + data);
        time = System.currentTimeMillis()/1000;// new Date()为获取当前系统时间
        if (data.isEmpty() || !data.contains("pkt_type")) {
            return;
        }
        Gateway gateway = null;
        JSONObject jsonObject = null;
        String pkt_type = null;
        String gatewayAddress = null;
        try {
            jsonObject = JSONObject.parseObject(data);
            pkt_type = jsonObject.getString("pkt_type");
        /*    if(pkt_type!=null&&pkt_type.equals("scan_report")){
                //不处理与扫描数据有关的消息
               // System.out.println("是扫描数据");
                return;
            }else{
             //   System.out.println("非扫描数据"+topic);
            }*/
            gatewayAddress = jsonObject.getString("gw_addr");
            if(gatewayAddress!=null&&gatewayAddress.equals("ffffffffffff")){
                return;
            }
            //System.out.println("吃吃吃"+redis_key_gateway + gatewayAddress);
            if(redisUtil==null){
                System.out.println("redis 空引用");
                return;
            }

            if(gatewayMapper==null){
                System.out.println("gatewayMapper 空引用");
                return;
            }
            if(gatewayMap==null){
                System.out.println("gatewayMap 空引用");
                return;
            }
            if(gatewayMap.get(gatewayAddress)!=null){
                gateway = (Gateway) redisUtil.get(redis_key_gateway + gatewayAddress);
            }


            //标准版本不做自动添加网关功能
           /* if (gateway == null&&!topic.equals("AlphaRsp")) {

               // System.out.println("项目长度=="+gatewayConfigMap.size());
                //System.out.println("主题==="+topic);
                   writeLog("新增加一个网关="+gatewayAddress);

                   for(String key:gatewayConfigMap.keySet()){
                       String a=gatewayConfigMap.get(key).getPub_topic();
                       String b=topic.toLowerCase().replace(gatewayAddress,"");
                     //  System.out.println("项目主题="+a);
                      // System.out.println("网关主题="+b);
                       if(a.contains(b)){
                         //  System.out.println("网关主题一致========");
                           gateway = new Gateway("", gatewayAddress, "", key, "admin", gatewayConfigMap.get(key).getName(), 0,0,0);
                          // System.out.println("跳出循环"+gateway.getConfig_key());
                           break;
                       }else{
                        //   System.out.println("网关主题不一致1111111111");
                         //  System.out.println("继续循环");
                       }

                   }
                   if(gateway==null){
                     //  System.out.println("最后循环"+topic);
                       gateway = new Gateway("", gatewayAddress, "", "projectdefault_admin", "admin", "默认目", 0,0,0);
                   }
                gateway.setIsyn(0);
               if(!topic.equals("AlphaRsp")){
                   gateway.setPub_topic(topic);
               }

                if(check_sheet!=null&&check_sheet.getDefaultsub()!=null){
                    gateway.setSub_topic(check_sheet.getDefaultsub());
                }else{
                    gateway.setSub_topic("/kunlun-cmd/ftxydcyy");
                }

              //  gateway.setPub_topic();
                Gateway_sql gateway_sql = new Gateway_sql();

                Lock lock = new ReentrantLock();
                lock.lock();//上锁
                try {
                    if (gateway_sql.addGateway(gatewayMapper, gateway)) {
                        redisUtil.set(redis_key_gateway + gateway.getAddress(), gateway);
                        System.out.println("增加一个网关"+gateway.getAddress());
                        gatewayMap.put(gateway.getAddress(), gateway.getAddress());
                        System.out.println("最新长度"+gateway.getAddress());
                    }else{
                        System.out.println("插入问题");
                    }
                    // System.out.println(2);
                } finally {
                    lock.unlock();//解锁
                }

                return;
            }*/
            if(gateway==null){
                Gateway_sql gateway_sql=new Gateway_sql();
                gatewayMap= gateway_sql.getAllGateway(redisUtil,gatewayMapper);
                gateway = (Gateway) redisUtil.get(redis_key_gateway + gatewayAddress);
                if(gateway==null){
                    return;
                }

                //   return;
            }
            //上一次离线时间
            if (gateway.getOnline() == 0) {
                redisUtil.set(redis_key_gateway_onLine_time + gatewayAddress, time);
                redisUtil.set(redis_key_gateway_revice_count + gatewayAddress, 0);
                writeLog("网关：" + gateway.getAddress() + "重新上线  在线时间为" + time);
            } else if (redisUtil.get(redis_key_gateway_onLine_time + gatewayAddress) == null) {
                redisUtil.set(redis_key_gateway_onLine_time + gatewayAddress, time);
                redisUtil.set(redis_key_gateway_revice_count + gatewayAddress, 0);
            }
          //  System.out.println("Reboo="+gateway.getReboot());
            if(gateway.getReboot()>0){
                int reboot=gateway.getReboot()-1;
                gateway.setReboot(reboot);
                 redisUtil.set(redis_key_gateway + gatewayAddress,gateway);
                return;
            }
            gateway.setLasttime(System.currentTimeMillis()/1000);
          //  gateway.setOnline(1);
            if(gateway.getOnline()!=1){
               // StringUtil.saveRecord(gateway.getAddress(),gateway.getLasttime(),gateway.getUser_key(),1,1,gateway.getProject_key());
                Alarm_Sql alarm_sql = new Alarm_Sql();
                alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_online,Alarm_object.gateway,gateway.getMap_key(),0,"", 0,0,"",gateway.getName(),gateway.getAddress(),gateway.getProject_key()));
            }
            StringUtil.sendGatewayPush(gateway,1);

           // System.out.println("吃吃吃"+gatewayAddress);
            redisUtil.set(redis_key_gateway + gatewayAddress, gateway);

        } catch (Exception e) {
            System.out.println("json格式不对=" +"ssd"+e);
            return;
        }

        //gateway.setPub_topic(topic);
        /*if (topic.contains("_")) {
            gateway.setSub_topic(topic.split("_")[0] + "_sub");
        } else {
            gateway.setSub_topic("GwData");
        }*/
        Object object = null;
        if (pkt_type.equals(Constant.pkt_type_scan_report)) {
            Integer count = (Integer) redisUtil.get(redis_key_gateway_revice_count + gatewayAddress);
            if (count != null)
                count++;
            else count = 1;
            redisUtil.set(redis_key_gateway_revice_count + gatewayAddress, count);
        }
        if (!pkt_type.equals(Constant.pkt_type_scan_report)) {
        }
        switch (pkt_type) {
            case Constant.pkt_type_response:
                object = analysisResponse(jsonObject);
                break;
            case Constant.pkt_type_scan_report:
                object = analysisScanReport(data);
                break;
            case Constant.pkt_type_command:
                break;
            case Constant.pkt_type_state:
                object = analysisState(jsonObject);
                break;
        }

        if (object == null) {
         //   System.out.println("此消息未有解析，请更新服务器"+jsonObject.toString());
            return;
        }
        String className = object.getClass().getSimpleName();
        Gateway_sql gateway_sql;
        // System.out.println("ClassnMame="+className);
        switch (className) {
            case Constant.Scan_report:
                String Gaddress = ((com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report<Scan_report_data>) object).getGw_addr();
                try {
                    gateway = (Gateway) redisUtil.get(redis_key_gateway + Gaddress);
                   /* //   System.out.println("尝试执行转发");
                    RabbitMessage rabbitMessage = new RabbitMessage(gateway.getConfig_key(), data);
                    directExchangeProducer.send(rabbitMessage.toString(), transpond);
                   */ com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report scan_report = ((com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report<Scan_report_data>) object);
                    //  System.out.println("设备数量="+((Scan_report<Scan_report_data>) object).getData().getDev_infos().length);
                    //把每个设备保存它响应的10个网关记录，方便后续调用
                    saveRecord(gateway, scan_report);
                    Scan_report_data scan_report_data = (Scan_report_data) scan_report.getData();
                    String type = scan_report_data.getReport_type();
                    if (type.equals("stuff_card")) {
                        //把工卡进行解析，SOS即时保存并通知到网页，并且把工卡扫描到的每个信标进行保存。
                        WordCardHandle(gateway, scan_report_data);
                    } else if (type.equals("adv_only") || type.equals("adv_srp")) {
                        //处理beacon解析，SOS即时保存并通知网页
                        BeaconHandle(gateway, scan_report_data);
                    }
                } catch (Exception e) {
                    System.out.println("异常" + e.toString() + data);
                }
                break;
            case Constant.ConnectExecute:
                redisUtil.set("sendToGateway_id=" + ((com.kunlun.firmwaresystem.gatewayJson.type_response.ConnectExecute<ConnectExecuteDetail>) object).getData().getMsgId(), ((com.kunlun.firmwaresystem.gatewayJson.type_response.ConnectExecute<ConnectExecuteDetail>) object).getData().isResult());
                //  Util.add_user_device_one();
                break;
            case Constant.ConnectState:
                // ((ConnectState<ConnectDetail>)object).getData().getDevice_state();
                redisUtil.set(Constant.ConnectState + ((com.kunlun.firmwaresystem.gatewayJson.state.ConnectState<ConnectDetail>) object).getData().getDevice_addr(), ((ConnectState<ConnectDetail>) object).getData().getDevice_state());
                System.out.println("连接状态=" + ((ConnectState<ConnectDetail>) object).getData().getDevice_state());
                //准备就绪，推送
                if (((ConnectState<ConnectDetail>) object).getData().getDevice_state().equals(ConnectState_redy)) {
                    System.out.println("连接状态已就绪  开始推送至rabittmq=");
                }
                break;
            case Constant.Scan_filter:
                // System.out.println("进入两次");
                //取出网关
                gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getGw_addr());
               //  System.out.println("缓存");
                gateway.Filter_name1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_name());
               // System.out.println("缓存66");
                gateway.setFilter_dev_mac1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_dev_mac());
                // System.out.println("缓存1");
                gateway.Filter_ibeacon1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().isFilter_beacon_b());
               // System.out.println("缓存2");
                try {
                    gateway.Filter_companyId1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_comp_ids());
                }catch (Exception e){
                    System.out.println("异常====="+e.getMessage());
                }
               // System.out.println("缓存3");
                gateway.setFilter_rssi(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_rssi() + "");
             //   System.out.println("缓存4");
                gateway.setFilter_uuid1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_uuid());
                gateway.setScan_filter_serv_data_uuid1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getScan_filter_serv_data_uuid());
               //  System.out.println("过滤的UUID==="+((Scan_filter )object).getData().getFilter_uuid());
                //更新后再次缓存
                redisUtil.set(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getGw_addr(), gateway);
                //存到数据库 同步
                gateway_sql = new Gateway_sql();

                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Network_Status:
                gateway.setWifi_address(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWlan_mac());
                gateway.setWan_gw(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWan_gw());
                gateway.setWan_ip(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWlan_ip());
                gateway.setWan_mask(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWan_mask());
                gateway.setWan_mode(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWan_mode());
                gateway.setWlan_mask(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWlan_mask());
                gateway.setWlan_ssid(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWlan_ssid());
                gateway.setOp_mode(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getOp_mode());
                gateway.setWlan_ip(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWlan_ip());
                gateway.setWifi_onoff(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWifi_onoff());
                gateway.setUp_time(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getUp_time());
                gateway.setCRC_FLAG(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getCRC_FLAG());
                //更新后       再次缓存
                redisUtil.set(redis_key_gateway + ((Network_Status) object).getGw_addr(), gateway);
                //存到数据库 同步
                gateway_sql = new Gateway_sql();
                System.out.println("22");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.Scan_params:
                gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_params) object).getGw_addr());
                gateway.Scan_out1(((Scan_params) object).getData().isReport_onoff());
                gateway.Report_type1(((Scan_params) object).getData().isRequest_onoff(), ((Scan_params) object).getData().isStuff_card_onoff());
                gateway.Scan_interval(((Scan_params) object).getData().getReport_interval());
                //更新后再次缓存
                redisUtil.set(redis_key_gateway + ((Scan_params) object).getGw_addr(), gateway);
                //存到数据库 同步
                gateway_sql = new Gateway_sql();
               // System.out.println("33");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.Adv_params:
                gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params) object).getGw_addr());
                gateway.Broadcast1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params) object).getData().isAdv_onoff());
                redisUtil.set(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params) object).getGw_addr(), gateway);
                gateway_sql = new Gateway_sql();
                System.out.println("44");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.WifiVersion:
                gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.WifiVersion) object).getGw_addr());
                gateway.setWifi_version(((com.kunlun.firmwaresystem.gatewayJson.type_response.WifiVersion<WifiVersionDetail>) object).getData().getVersion());
                redisUtil.set(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.WifiVersion) object).getGw_addr(), gateway);
                gateway_sql = new Gateway_sql();
                System.out.println("55");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.BleVersion:
                gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion) object).getGw_addr());
                gateway.setBle_version(((com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion<BleVersionDetail>) object).getData().getVersion());
                redisUtil.set(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion) object).getGw_addr(), gateway);
                gateway_sql = new Gateway_sql();
                System.out.println("66");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.App_Server:
                gateway = (Gateway) redisUtil.get(redis_key_gateway + ((App_Server) object).getGw_addr());
                if (gateway == null) {
                    return;
                }
                System.out.println("状态="+((App_Server) object).getData().toString());
                gateway.setSub_topic(((App_Server) object).getData().getSub());
                gateway.setIp((((App_Server) object).getData().getHost()));
                gateway.setPub_topic(((App_Server) object).getData().getPub());

                redisUtil.set(redis_key_gateway + ((App_Server) object).getGw_addr(), gateway);
                gateway_sql = new Gateway_sql();

                gateway_sql.updateGateway(gatewayMapper, gateway);
                System.out.println(""+gateway);

                RabbitMessage rabbitMessage1 = new RabbitMessage(gateway.getUser_key(), gateway.getPub_topic());
                directExchangeProducer.send(rabbitMessage1.toString(), "mqtt_topic");
                break;
        }
    }

    private void BeaconHandle(Gateway gateway, Scan_report_data scan_report_data) {
        //System.out.println("收到原始=");
        try {
            Scan_report_data_info[] devices = scan_report_data.getDev_infos();
            for (Scan_report_data_info device : devices) {
                Beacon beacon=beaconsMap.get(device.getAddr());
                //信标不在系统内，跳过
                if(beacon == null){
                    continue;
                }
                //信标不为空，处理信标
                else if (beacon != null) {
                //    System.out.println("信标不为空="+beacon.getMac());
                 /*   if(beacon.getMac().equals("f0c8140c0ca1")){
                        System.out.println("信标数据="+device.getAdv_raw());
                    }*/
                    beacon.setGateway_address(gateway.getAddress());
                    byte[] btdata = StringUtil.hexToByteArr(device.getAdv_raw());
                    if (btdata.length == 30) {
                        String str = Integer.toBinaryString((btdata[29] & 0xFF) + 0x100).substring(1);
                        char[] bit = str.toCharArray();
                      //  System.out.println("二进制"+str);
                        beacon.setLastTime(time);
                        if(beacon.getType()==2){
                            str = str.substring(0, 6);
                            beacon.setBt(((double) (Byte.parseByte(str, 2) & 0xff)) / 10.0);
                            if (bit[6] == '1') {
                                beacon.setSos(0);
                            }else{
                                beacon.setSos(1);
                            }
                        }else if(beacon.getType()==1){
                            str = str.substring(2, 6);
                            beacon.setBt((((double) (Byte.parseByte(str, 2) & 0xff)) / 10.0)+2.0);
                            if (bit[7] == '1') {
                                beacon.setSos(1);
                            }else{
                                beacon.setSos(0);
                            }
                        }
                    }
                    beacon.setRssi(device.getRssi());
                }
                if(beacon.getIsbind()==0){
                    writeLog("---信标未被设备绑定，跳过，继续循环"+beacon.getMac());
                    continue;
                }
                //如果绑定了但是没有设备SN号码，那就是异常，记录日志
               else  if(beacon.getDevice_sn()==null||beacon.getDevice_sn().equals("")){

                    writeLog("###信标绑定设备异常，状态绑定了没有SN");

                    continue;
                }
                Devicep deviceP= devicePMap.get(beacon.getDevice_sn());
               if(deviceP==null){
                   writeLog("###信标绑定设备异常,数据库中没有对真实对应的资产");
                   continue;
               }
                deviceP.setLasttime(beacon.getLastTime());
                if(deviceP==null){
                    writeLog("###信标绑定设备异常，信标获取设备SN却找不到对应的设备");
                    continue;
                }
               else {
                        if(beacon.getSos()==1&&deviceP.getSos()==0){
                            Alarm_Sql alarm_sql = new Alarm_Sql();
                            alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_key,Alarm_object.device,beacon.getMap_key(),deviceP.getFence_id(),fenceMap.get(deviceP.getFence_id()).getName(),beacon.getBt(),0,"",deviceP.getName(),deviceP.getSn(),deviceP.getProject_key()));
                               // RecordSos_Sql recordSos_sql = new RecordSos_Sql();
                             //   recordSos_sql.addRecordSos(recordSosMapper, new Record_sos(1, time, device.getAddr(), gateway.getAddress(), gateway.getName(),deviceP.getName(),deviceP.getSn(), "beacon",gateway.getCustomerkey(),gateway.getUser_key()));
                        }
                    if(beacon.getOnline()!=1&&beacon.getIsbind()==1){
                        if(beacon.getBind_type()==1){
                            Devicep  devicep=devicePMap.get(beacon.getDevice_sn());
                            Alarm_Sql alarm_sql = new Alarm_Sql();
                            alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_online,Alarm_object.device,beacon.getMap_key(),devicep.getFence_id(),fenceMap.get(devicep.getFence_id()).getName(),beacon.getBt(),0,"",devicep.getName(),devicep.getSn(),devicep.getProject_key()));
                        }
                        else{
                            Person  person=personMap.get(beacon.getDevice_sn());
                            Alarm_Sql alarm_sql = new Alarm_Sql();
                            alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_online,Alarm_object.person,beacon.getMap_key(),person.getFence_id(),"",beacon.getBt(),0,"",person.getName(),person.getIdcard(),person.getProject_key()));

                        }
                    }
                     StringUtil.sendBeaconPush(beacon,1);
                       deviceP.setBt(beacon.getBt());
                       sendDeviceSoSPush(deviceP,beacon.getSos());
                       StringUtil.sendDevicePush(deviceP,1);
                       // deviceP.setSos(beacon.getSos());
                    }
                }
        } catch (Exception e) {
            System.out.println("异常检测666"+e.getMessage());
        }
    }

    private void WordCardHandle(Gateway gateway, Scan_report_data scan_report_data) {
        Scan_report_data_info[] scanReportDataInfos = scan_report_data.getDev_infos();
        for (Scan_report_data_info device : scanReportDataInfos) {
            //工卡如果是空，说明不在系统登记，跳过，继续循环

            if (wordcard_aMap.get(device.getAddr()) == null) {
                continue;
            }
            if(wordcard_aMap.get(device.getAddr()).getIsbind()==0){

                writeLog("---工卡不是空，但是未被人员绑定，跳过，继续循环");
                continue;
            }
            //如果绑定了但是没有身份证号，那就是异常，记录日志
            if(wordcard_aMap.get(device.getAddr()).getIdcard()==null){
                writeLog("###工卡绑定人员异常，状态绑定了没有身份证号");
                continue;
            }
            Person person= personMap.get(wordcard_aMap.get(device.getAddr()).getIdcard());
            if(person==null){
                writeLog("###工卡绑定人员异常，工卡获取身份证号却找不到对应的人员");
                continue;
            }
            Wordcard_a wordCard_a = wordcard_aMap.get(device.getAddr());
            //  System.out.println("输出设备="+device.toString()+device);
            int bt = device.getBatt();
            if (bt != -1) {
                wordCard_a.setBt(bt);
            }
            wordCard_a.setRun(device.getMotion());
            wordCard_a.setMac(device.getAddr());
            wordCard_a.setOnline(1);
            redisUtil.set(redis_key_device_sos+device.getAddr(),device.getKeys());
            //当前工卡SOS状态和之前的状态SOS不一致并且是报警，就更新推送，否则不推送
            if ((device != null && device.getKeys() == 1 && wordCard_a.getSos() == 0)) {
                //更新工卡当前状态SOS
                wordCard_a.setSos(device.getKeys());
                //更新推送到网页
                net.sf.json.JSONObject jsonObject1 = new net.sf.json.JSONObject();
                jsonObject1.put("time", time);
                jsonObject1.put("device", wordCard_a);
                jsonObject1.put("gatewayName", gateway.getName());
                jsonObject1.put("person", person);
                jsonObject1.put("type", "person_line");
                RabbitMessage rabbitMessage1 = new RabbitMessage(gateway.getProject_key(), jsonObject1.toString());
                directExchangeProducer.send(rabbitMessage1.toString(), "sendtoHtml");
                // 实时存储SOS记录
                /*RecordSos_Sql recordSos_sql = new RecordSos_Sql();
                recordSos_sql.addRecordSos(recordSosMapper, new Record_sos(1, time, device.getAddr(), gateway.getAddress(), gateway.getName(),person.getName(),person.getIdcard(), "wordCard",gateway.getCustomerkey(), gateway.getUser_key()));
          */

                Alarm_Sql alarm_sql = new Alarm_Sql();
                alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_key,Alarm_object.person,gateway.getMap_key(),0,"",device.getBatt(),0,"",person.getName(),person.getIdcard(),person.getProject_key()));
            }
            Ibcn_infos[] ibcn_infos = device.getIbcn_infos();
            Gateway_devices gateways = null;
            String json = null;
            try {
                json = (String) redisUtil.get(redis_key_card_map + device.getAddr());
            } catch (Exception e) {
                System.out.println("法国红酒封口的" + e.getMessage());
            }
            if (json == null) {
                gateways = new Gateway_devices();
                ArrayList<Gateway_device> gatewayDevices = new ArrayList<>();
                gateways.setGatewayDevices(gatewayDevices);
                System.out.println("没有数据" + device.getAddr());
                //   System.out.println(data);
            } else {
                try {
                    gateways = new Gson().fromJson(json, Gateway_devices.class);
                    if (gateways.getGatewayDevices().size() >= 10) {
                        gateways.getGatewayDevices().remove(0);
                    }
                } catch (Exception e) {
                }
            }
         /*   for (int j = 0; j < ibcn_infos.length; j++) {
                //   System.out.println("循环输出"+wordcard_aMap.get(device.getAddr()).getProject_key()+ibcn_infos[j].getMajor()+"/"+ibcn_infos[j].getMinor());
                Beacon_tag beacon_tag = beacon_tagMap.get( ibcn_infos[j].getMajor() + "/" + ibcn_infos[j].getMinor());
                if (beacon_tag != null) {
                    beacon_tag.settRssi(ibcn_infos[j].getRssi());
                    try {

                        gateways.getGatewayDevices().add(new Gateway_device(beacon_tag.getN(),  ""+ibcn_infos[j].getMajor() +"/"+ ibcn_infos[j].getMinor(), device.getAddr(), ibcn_infos[j].getRssi(), gateway.getSub_topic(), gateway.getPub_topic(), beacon_tag.getX(), beacon_tag.getY(), ibcn_infos[j].getMajor() + "/" + ibcn_infos[j].getMinor() + ""));
                      //  System.out.println("标签mac:" + device.getAddr());
                    } catch (Exception e) {
                        //System.out.println("wewe" + e.getMessage());
                    }
                }
            }
            redisUtil.set(redis_key_card_map + device.getAddr(), gateways.toString());
*/

        }
    }

    private void saveRecord(Gateway gateway, Scan_report<Scan_report_data> object) {
        for (Scan_report_data_info scanReportDataInfo : object.getData().getDev_infos()) {

            int index = -1;
           for (String beaconMac : beaconsMap.keySet()) {
                if (beaconMac.equals(scanReportDataInfo.getAddr()) && beaconsMap.get(beaconMac) != null) {
                    index = 100;
                    break;
                }
             }
            for (String wordcardMac : wordcard_aMap.keySet()) {
                if (wordcardMac.equals(scanReportDataInfo.getAddr()) && wordcard_aMap.get(wordcardMac) != null) {
                    index = 100;
                    break;
                }
            }
            if (index == -1) {
                continue;
            }
            Gateway_devices gateways = null;
            String json = null;
            try {
                json = (String) redisUtil.get(redis_key_device_gateways + scanReportDataInfo.getAddr());
            } catch (Exception e) {
                 System.out.println("法国红酒封口的"+e.getMessage());
            }
            if (json == null) {
                gateways = new Gateway_devices();
                ArrayList<Gateway_device> gatewayDevices = new ArrayList<>();
                gateways.setGatewayDevices(gatewayDevices);
                System.out.println("没有数据11" + scanReportDataInfo.getAddr());
                //   System.out.println(data);
            } else {
                try {
                    gateways = new Gson().fromJson(json, Gateway_devices.class);
                    if (gateways.getGatewayDevices().size() >= 10) {
                        gateways.getGatewayDevices().remove(0);
                    }
                } catch (Exception e) {
                }
            }
            try {
                if (gateway != null) {
                    gateways.getGatewayDevices().add(new Gateway_device(gateway.getAddress(),scanReportDataInfo.getRssi(),times));

                 //   gateways.getGatewayDevices().add(new Gateway_device(gateway.getN(), gateway.getAddress(), scanReportDataInfo.getAddr(), scanReportDataInfo.getRssi(), gateway.getSub_topic(), gateway.getPub_topic(), gateway.getX(), gateway.getY(), gateway.getName()));
                    //  System.out.println("标签mac     "+scanReportDataInfo.getAddr());
                    redisUtil.set(redis_key_device_gateways + scanReportDataInfo.getAddr(), gateways.toString());
                }
            } catch (Exception e) {
                System.out.println("wewe" + e.getMessage());
            }
        }
    }

    private Object analysisResponse(JSONObject jsonRaw) {

        Type type = null;
        try {
            JSONObject data = jsonRaw.getJSONObject("data");
            String resp = data.getString("resp");
            //  System.out.println("具体的头="+jsonRaw.toString());
            switch (resp) {
                case response_sys_get_ver:
                    type = new TypeToken<com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion<BleVersionDetail>>() {
                    }.getType();
                    com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion<BleVersionDetail> Ble_version = gson.fromJson(jsonRaw.toString(), type);
                    //   System.out.println("蓝牙版本号=" + Ble_version.getData().getVersion());
                    return Ble_version;
                case response_network_status:
                    Network_Status_Detail network_status_detail = new Network_Status_Detail(jsonRaw.getJSONObject("data").toString());
                    Network_Status network_status = new Network_Status();
                    network_status.setData(network_status_detail);
                    network_status.setGw_addr(jsonRaw.getString("gw_addr"));
                    network_status.setPkt_type(jsonRaw.getString("pkt_type"));
                    network_status.setTime(jsonRaw.getString("time"));
                    return network_status;
                //下发连接的状态
                case response_sys_get_wifi_ver:
                    type = new TypeToken<com.kunlun.firmwaresystem.gatewayJson.type_response.WifiVersion<WifiVersionDetail>>() {
                    }.getType();
                    com.kunlun.firmwaresystem.gatewayJson.type_response.WifiVersion<WifiVersionDetail> Wifi_version = gson.fromJson(jsonRaw.toString(), type);
                    // System.out.println("WIfi版本号=" +   Wifi_version.getData().getVersion());
                    return Wifi_version;


                case response_conn_addr_request:
                    type = new TypeToken<com.kunlun.firmwaresystem.gatewayJson.type_response.ConnectExecute<ConnectExecuteDetail>>() {
                    }.getType();
                    com.kunlun.firmwaresystem.gatewayJson.type_response.ConnectExecute<ConnectExecuteDetail> connectExecute = gson.fromJson(jsonRaw.toString(), type);
                    System.out.println("连接执行状态=" + connectExecute.getData().isResult());
                    return connectExecute;
                case response_scan_filter_get:
                    Scan_filterDetail scanFilterDetail = new Scan_filterDetail(jsonRaw.getJSONObject("data").toString());
                    com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter scan_filter = new Scan_filter();
                    scan_filter.setData(scanFilterDetail);
                    scan_filter.setGw_addr(jsonRaw.getString("gw_addr"));
                    scan_filter.setPkt_type(jsonRaw.getString("pkt_type"));
                    scan_filter.setTime(jsonRaw.getString("time"));
                    if(scanFilterDetail.getFilter_comp_ids()!=null&&scanFilterDetail.getFilter_comp_ids().length>10){
                        System.out.println("原始数据="+jsonRaw.toString());
                }
                    return scan_filter;

                case response_scan_params_get:
                    try {
                        // System.out.println("log");
                        Scan_paramsDetail scan_paramsDetail = new Scan_paramsDetail(jsonRaw.getJSONObject("data").toString());
                        //  System.out.println("log1222");
                        Scan_params scanParams = new Scan_params();
                        //  System.out.println("333");
                        scanParams.setData(scan_paramsDetail);
                        //  System.out.println("444");
                        scanParams.setGw_addr(jsonRaw.getString("gw_addr"));
                        //  System.out.println("555");
                        scanParams.setPkt_type(jsonRaw.getString("pkt_type"));
                        // System.out.println("666");
                        scanParams.setTime(jsonRaw.getString("time"));
                        // System.out.println("777");
                        return scanParams;
                    } catch (Exception e) {
                        System.out.println("response_scan_params_get异常输出=" + e.toString());
                        return null;
                    }
                case response_adv_params_get:
                    Adv_paramsDetail adv_paramsDetail = new Adv_paramsDetail(jsonRaw.getJSONObject("data").toString());
                    com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params adv_params = new Adv_params();
                    adv_params.setData(adv_paramsDetail);
                    adv_params.setGw_addr(jsonRaw.getString("gw_addr"));
                    //  System.out.println("555");
                    adv_params.setPkt_type(jsonRaw.getString("pkt_type"));
                    //  System.out.println("666");
                    adv_params.setTime(jsonRaw.getString("time"));
                    return adv_params;
                case sys_app_server:
                    App_ServerDetail app_serverDetail = new App_ServerDetail(jsonRaw.getJSONObject("data").toString());
                    App_Server app_server = new App_Server();
                    app_server.setData(app_serverDetail);
                    app_server.setGw_addr(jsonRaw.getString("gw_addr"));
                    app_server.setPkt_type(jsonRaw.getString("pkt_type"));
                    app_server.setTime(jsonRaw.getString("time"));
                    return app_server;


            }
        } catch (Exception e) {
            System.out.println("解析异常=" + jsonRaw.toString());
        }
        return null;
    }

    private Object analysisState(JSONObject jsonRaw) {
        Type type = null;
        JSONObject data = null;
        String state = null;
        try {
            data = jsonRaw.getJSONObject("data");
            state = data.getString("state");
            //  System.out.println("具体的头111="+state);

            switch (state) {
                case state_sta_gw_hb:
                    type = new TypeToken<HeartState<HeartDetail>>() {
                    }.getType();
                    HeartState<HeartDetail> heartState = gson.fromJson(jsonRaw.toString(), type);
                  //  System.out.println(heartState.getGw_addr() + "心跳状态=" + heartState.getData().getTicks_cnt());

                    Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + heartState.getGw_addr());
                    String synStr = (String) redisUtil.get(redis_key_project_sys + gateway.getAddress());
                    System.out.println("当前心跳=" + heartState.getData().getTicks_cnt());
                    Integer heart = (Integer) redisUtil.get(redis_key_project_heart + gateway.getAddress());
                    if (heart != null) {
                        System.out.println("记录心跳心跳=" + heart.intValue());
                    }
                    if (synStr != null && synStr.contains("upgrade")) {
                        if (heart != null && Math.abs(heartState.getData().getTicks_cnt() - heart) < 10) {
                            System.out.println("刚更新过固件，不再执行");
                            return heartState;
                        }
                    }

                    Gateway_config gatewayConfig = gatewayConfigMap.get(gateway.getConfig_key());
                    if (gateway != null) {
                        redisUtil.set(redis_key_gateway + heartState.getGw_addr(), gateway);
                        // System.out.println("网关配置="+gateway.toString());
                        // System.out.println("项目配置="+gatewayConfig.toString());
                        try {
                            if (gatewayConfig != null) {

                                List<String> cmds = new ArrayList<>();
                                System.out.println("项目不为空");
                                String cmd = "";
                                //      System.out.println("scan_filter_comp_ids");
                                if(gateway.getIsyn()==1&&gatewayConfig.getIsyn()==1){
                                    System.out.println("有网关需要同步");


                                if (isChange(gatewayConfig.getFilter_companyids(), gateway.getFilter_companyId())&&gatewayConfig.isIs_filter_companyid()) {
                                    cmd = getParamsJson("scan_filter_comp_ids", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }
                          /*  if (isChange(gatewayConfig.getFilter_rssi(), gateway.getFilter_rssi())) {
                                cmd = getParamsJson("scan_filter_comp_ids", gatewayConfig, gateway.getAddress(), null, null);
                                cmds.add(cmd);
                            }*/
                                if (isChange(gatewayConfig.getFilter_rssi()+"", gateway.getFilter_rssi())&&gatewayConfig.isIs_filter_rssi()) {
                                    cmd = getParamsJson("scan_filter_rssi", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }
                          /*  if(!gatewayConfig.getFilter_companyId().equals(gateway.getFilter_companyId())){
                                isChange();
                                cmd= getParamsJson("scan_filter_comp_ids",gatewayConfig,gateway.getAddress(),null,null);
                            }*/
                       /*   System.out.println("scan_filter_rssi");
                            if (!gatewayConfig.getFilter_rssi().equals(gateway.getFilter_rssi())){

                                cmd= getParamsJson("scan_filter_rssi",gatewayConfig,gateway.getAddress(),null,null);
                            }*/
                                System.out.println(gatewayConfig.getFilter_uuid() + "----" + gateway.getFilter_uuid());
                           /* if (isChange(gatewayConfig.getFilter_uuid(), gateway.getFilter_uuid())) {

                            }*/
                                System.out.println("debug="+gatewayConfig.getFilter_uuids()+"    "+gateway.getFilter_uuid());
                                if (gatewayConfig.getFilter_uuids() != null && gateway.getFilter_uuid() != null && !gatewayConfig.getFilter_uuids().equals(gateway.getFilter_uuid())&&gatewayConfig.isIs_filter_uuid()) {
                                    cmd = getParamsJson("scan_filter_ibcn_uuid", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }
                        /*   System.out.println("scan_filter_ibcn_uuid");
                            if(!gatewayConfig.getFilter_uuid().equals(gateway.getFilter_uuid())){
                                cmd= getParamsJson("scan_filte网关订阅一样r_ibcn_uuid",gatewayConfig,gateway.getAddress(),null,null);
                            }*/

                                //  System.out.println("adv_onoff");
                                if (gatewayConfig.getBroadcast() != gateway.getBroadcast()) {
                                    cmd = getParamsJson("adv_onoff", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }
                                //  System.out.println("scan_filter_ibcn_dev");
                                if (gatewayConfig.getFilter_ibeacon() != gateway.getFilter_ibeacon()) {
                                    cmd = getParamsJson("scan_filter_ibcn_dev", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }
                                //  System.out.println("scan_request_onoff");
                                if (gatewayConfig.getReport_type() != gateway.getReport_type()) {
                                    cmd = getParamsJson("scan_request_onoff", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }
                                //  System.out.println("scan_report_onoff");

                                if (isChange(gatewayConfig.getFilter_names(), gateway.getFilter_name())&&gatewayConfig.isIs_filter_name()) {
                                    cmd = getParamsJson("scan_filter_name", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }
                                if (isChange(gatewayConfig.getFilter_macs(), gateway.getFilter_dev_mac())&&gatewayConfig.isIs_filter_mac()) {
                                    cmd = getParamsJson("scan_filter_dev_mac", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }
                                if(isChange(gatewayConfig.getServices_uuids(),gateway.getScan_filter_serv_data_uuid())&&gatewayConfig.isIs_services_uuid()){
                                    cmd = getParamsJson("scan_filter_serv_data_uuid", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);

                                }
                                if (gatewayConfig.getScan_interval() != gateway.getScan_interval()) {
                                    cmd = getParamsJson("scan_report_interval", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }

                                System.out.println(gatewayConfig.getSub_topic() + "    " + gateway.getSub_topic());
                                if (isChange(gatewayConfig.getSub_topic(), gateway.getSub_topic())) {
                                    System.out.println("网关订阅不一样");
                                    if (gateway.getSub_topic().replace(gateway.getAddress(), "{blemac}").equals(gatewayConfig.getSub_topic())) {
                                        System.out.println("网关订阅规则一致");
                                    } else {
                                        cmd = getParamsJson("setTopic", gatewayConfig, gateway.getAddress(), null, null);
                                        cmds.add(cmd);
                                    }
                                } else {
                                    System.out.println("网关订阅一样");
                                }

                                System.out.println(gatewayConfig.getPub_topic() + "    " + gateway.getPub_topic());
                                if (isChange(gatewayConfig.getPub_topic(), gateway.getPub_topic())) {
                                    System.out.println("网关发布不一样");
                                    if (gateway.getPub_topic().replace(gateway.getAddress(), "{blemac}").equals(gatewayConfig.getPub_topic())) {
                                        System.out.println("网关发布规则一致");
                                    } else {
                                        cmd = getParamsJson("setTopic", gatewayConfig, gateway.getAddress(), null, null);
                                        cmds.add(cmd);
                                    }

                                    // 码石的不去更改主题消息
                                } else {
                                    System.out.println("网关订阅一样");
                                }
                                //  System.out.println("bleVersion"+gatewayConfig.getBle_version());
                                //   System.out.println("bleVersion"+gateway.getBle_version());
                                /*if (isChange(gatewayConfig.getBle_version(), gateway.getBle_version())) {
                                    if (gateway.getBle_version() != null && !gateway.getBle_version().equals("")) {
                                        redisUtil.set(redis_key_project_heart + gateway.getAddress(), heartState.getData().getTicks_cnt());
                                        Ble ble = new Ble();
                                        // System.out.println("bleVersionaaa");
                                        Ble_firmware ble_firmware = ble.getVersionByKey(bleMapper, gatewayConfig.getUser_key(), gatewayConfig.getBle_version());
                                        cmd = getParamsJson("bleVersion", gatewayConfig, gateway.getAddress(), ble_firmware, null);
                                        cmds.add(cmd);
                                    }
                                }

                                System.out.println("WifiVersion" + gateway.getWifi_version() + "    " + gatewayConfig.getWifi_version());
                                if (isChange(gatewayConfig.getWifi_version(), gateway.getWifi_version())) {
                                    System.out.println("123");
                                    if (gateway.getWifi_version() != null && !gateway.getWifi_version().equals("")) {
                                        System.out.println("456");
                                        if (redisUtil.get(redis_key_updateing_gateway) == null || redisUtil.get(redis_key_updateing_gateway).equals("")) {
                                            System.out.println("789");
                                            redisUtil.set(redis_key_project_heart + gateway.getAddress(), heartState.getData().getTicks_cnt());
                                            Wifi wifi = new Wifi();
                                            Wifi_firmware wifi_firmware = wifi.getVersionByKey(wifiMapper, gatewayConfig.getUser_key(), gatewayConfig.getWifi_version());
                                            cmd = getParamsJson("wifiVersion", gatewayConfig, gateway.getAddress(), null, wifi_firmware);
                                            cmds.add(cmd);
                                            redisUtil.set(redis_key_updateing_gateway, gateway.getAddress());
                                            writeLog("网关：" + gateway.getAddress() + " 正在升级");
                                        }
                                    }
                                } else {
                                    if (redisUtil.get(redis_key_updateing_gateway) != null && redisUtil.get(redis_key_updateing_gateway).equals(gateway.getAddress())) {
                                        redisUtil.set(redis_key_updateing_gateway, null);
                                        writeLog("网关：" + gateway.getAddress() + " 升级完成");
                                    }
                                }*/
                                if (gatewayConfig.getScan_out() != gateway.getScan_out()) {
                                    cmd = getParamsJson("scan_report_onoff", gatewayConfig, gateway.getAddress(), null, null);
                                    cmds.add(cmd);
                                }

                                //当心跳包来的时候，检查网关与项目的配置，不一致的话就更新网关
                                System.out.println("CMDS=" + cmds.size());
                                if (cmds.size() > 0) {

                                    System.out.println("有状态未同步");
                                    for (String cmddata : cmds) {
                                        String topic = gateway.getSub_topic();
                                        //就以网关保存的主题为准，下发，前提是网关保存的主题是正确的
                                   /* if (!topic.equals("SrvData") && !topic.contains(gateway.getAddress())) {
                                        topic = topic + "/" + gateway.getAddress();
                                    }*/
                                        redisUtil.set(redis_key_project_sys + gateway.getAddress(), cmddata);

                                        System.out.println("发送的指令=" + cmddata + "===" + topic);
                                        RabbitMessage rabbitMessage = new RabbitMessage(topic, cmddata);
                                        // directExchangeProducer.send(rabbitMessage.toString(), go_to_connect);
                                        directExchangeProducer.send(rabbitMessage.toString(), "sendToGateway");
                                        if(cmddata!=null&&cmddata.contains("sys_upgrade")){
                                            break;
                                        }
                                    }
                                } else {
                                    redisUtil.set(redis_key_project_sys + gateway.getAddress(), "ok");
                                    System.out.println("网关:" + gateway.getName() + "同步完成了");
                                }
                                }
                                else{
                                    System.out.println("此网关不需要同步"+gateway.getAddress());
                                }
                            } else {
                                //System.out.println("项目是空的");
                                writeLog("项目是空的,此时对应的项目key是：" + gateway.getConfig_key() + "名称是=" + gateway.getConfig_name() + "网关是：" + gateway.getAddress());
                                System.out.println("项目是空的,此时对应的项目key是：" + gateway.getConfig_key() + "名称是=" + gateway.getConfig_key() + "网关是：" + gateway.getAddress());
                            }
                        }catch (Exception e){
                            System.out.println("error"+e);
                        }
                    }
                    return heartState;
                case state_sta_device_state:
                    type = new TypeToken<ConnectState<ConnectDetail>>() {
                    }.getType();
                    ConnectState<ConnectDetail> connectState = gson.fromJson(jsonRaw.toString(), type);
                    System.out.println("连接状态=" + connectState.getData().getDevice_state());
                    return connectState;
            }
        } catch (Exception e) {
           // System.out.println("222解析异常=" + jsonRaw.toString());
        }
        return null;
    }

    private boolean isChange(String p, String g) {
        if (p != null && p.equals("null")) {
            return false;
        }
        if (p != null && p.contains("und") && p.contains("Un")) {
            return false;
        }
        if (p == null) {
            return false;
        } else if (g == null && p.length() > 0) {
            return true;
        } else if (p.length() > 0 && !p.equals(g)) {
            return true;
        }



        return false;
    }

    //解析全部的主动扫描上报
    private com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report analysisScanReport(String jsonRaw) {
        try {
            Type type = new TypeToken<com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report<Scan_report_data>>() {
            }.getType();
            com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report<Scan_report_data> scan = gson.fromJson(jsonRaw, type);
            //   System.out.println(scan.getData().getDev_infos()[0].getAddr());
            return scan;
        } catch (Exception e) {
            System.out.println("解析扫描上报数据异常");
            return null;
        }
    }


    private String getParamsJson(String type, Gateway_config gatewayConfig, String address, Ble_firmware ble_firmware, Wifi_firmware wifi_firmware) {
        String cmd = "";
        switch (type) {
            case "scan_report_interval":
                cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_report_interval\", \"value\": " + gatewayConfig.getScan_interval() + "}}";
                break;
            case "scan_report_onoff":
                if (gatewayConfig.getScan_out() == 1) {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_report_onoff\", \"enable\": true}}";
                } else {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_report_onoff\", \"enable\": false}}";
                }
                break;
            case "scan_request_onoff":
                if (gatewayConfig.getReport_type() == 4) {
                    cmd = "{\"pkt_type\":\"command\",\n" +
                            "    \"gw_addr\":\"" + address + "\",\n" +
                            "    \"data\":{\n" +
                            "        \"msgId\":3,\n" +
                            "        \"cmd\":\"scan_stuff_card_onoff\",\n" +
                            "        \"enable\":true,\n" +
                            "        \"report_ibcn_batt\":true,\n" +
                            "        \"report_batt_interval\":360\n" +
                            "    }\n" +
                            "}";
                } else if (gatewayConfig.getReport_type() == 2) {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_request_onoff\", \"enable\": true}}";
                } else if(gatewayConfig.getReport_type() == 1) {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_request_onoff\", \"enable\": false}}";
                }
                else if(gatewayConfig.getReport_type() == 3) {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_report_rssi_only\", \"enable\": true}}";
                }
                break;

            case "scan_filter_ibcn_dev":
                if (gatewayConfig.getFilter_ibeacon() == 1) {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_ibcn_dev\", \"enable\": true}}";
                } else {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_ibcn_dev\", \"enable\": false}}";
                }
                break;
            case "adv_onoff":
                if (gatewayConfig.getBroadcast() == 1) {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"adv_onoff\", \"enable\": true}}";
                } else {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"adv_onoff\", \"enable\": false}}";
                }
                break;
            case "scan_filter_ibcn_uuid":
                if (gatewayConfig.getFilter_uuid() != null && gatewayConfig.getFilter_uuids().length() > 0) {
                   // cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_ibcn_uuid\", \"enable\": true, \"value\": \"" + gatewayConfig.getFilter_uuids().toLowerCase() + "\"}}";
                    String[] scan_filter_ibcn_uuid = gatewayConfig.getFilter_uuids().split("-");

                    if (scan_filter_ibcn_uuid != null && scan_filter_ibcn_uuid.length >= 1) {
                        cmd = "{\"pkt_type\": \"command\",\"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4,\"cmd\": \"scan_filter_ibcn_uuid\",\"enable\": true,\"num\":" + (scan_filter_ibcn_uuid.length ) + ",\"value\":[";
                        String ss = "";
                        for (int i = 0; i < scan_filter_ibcn_uuid.length; i++) {
                            if (i == 0) {
                                if (i == scan_filter_ibcn_uuid.length - 1) {
                                    ss = ss + "{\"uuid\":\"" + scan_filter_ibcn_uuid[i] + "\"}]}}";
                                }else
                                    ss = "{\"uuid\":\"" + scan_filter_ibcn_uuid[i] + "\"}";
                            } else {
                                if (i == scan_filter_ibcn_uuid.length - 1) {
                                    ss = ss + ",{\"uuid\":\"" + scan_filter_ibcn_uuid[i] + "\"}]}}";
                                } else {
                                    ss = ss + ",{\"uuid\":\"" + scan_filter_ibcn_uuid[i] + "\"}";
                                }
                            }

                        }
                        cmd = cmd + ss;
                    }

                } else {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_ibcn_uuid\", \"enable\": false}}";
                }
                break;
            case "scan_filter_name":
                String[] names = gatewayConfig.getFilter_names().split("-");

                if (names != null && names.length >=1) {
                    cmd = "{\"pkt_type\": \"command\",\"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4,\"cmd\": \"scan_filter_name\",\"enable\": true,\"num\":" + (names.length ) + ",\"value\":[";
                    String ss = "";
                    for (int i =0; i < names.length; i++) {
                        if (i == 0) {
                            if (i == names.length - 1) {
                                ss = ss + "{\"name\":\"" + names[i] + "\"}]}}";
                            } else
                                ss = "{\"name\":\"" + names[i] + "\"}";
                        } else {
                            if (i == names.length - 1) {
                                ss = ss + ",{\"name\":\"" + names[i] + "\"}]}}";
                            } else {
                                ss = ss + ",{\"name\":\"" + names[i] + "\"}";
                            }
                        }

                    }
                    cmd = cmd + ss;
                } else {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_name\", \"enable\": false, \"num\": 0}}";
                }
                break;
            case "scan_filter_dev_mac":
                String[] scan_filter_dev_mac = gatewayConfig.getFilter_macs().split("-");
                if (scan_filter_dev_mac != null && scan_filter_dev_mac.length >= 1) {
                    cmd = "{\"pkt_type\": \"command\",\"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4,\"cmd\": \"scan_filter_dev_mac\",\"enable\": true,\"start\":\""+scan_filter_dev_mac[0]+",\"end\":"+scan_filter_dev_mac[1]+"\"}}";
                } else {
                    cmd = "{\"pkt_type\": \"command\",\"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4,\"cmd\": \"scan_filter_dev_mac\",\"enable\": false}}";
                }
                break;
            case "scan_filter_comp_ids":
                String[] companyids = gatewayConfig.getFilter_companyids().split("-");

                if (companyids != null && companyids.length >= 1) {
                    cmd = "{\"pkt_type\": \"command\",\"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4,\"cmd\": \"scan_filter_comp_ids\",\"enable\": true,\"num\":" + (companyids.length ) + ",\"value\":[";
                    String ss = "";
                    for (int i = 0; i < companyids.length; i++) {
                        if (i == 0) {
                                ss = "{\"id\":\"" + companyids[i] .toLowerCase() + "\"}";
                        } else {
                            if (i == companyids.length - 1) {
                                ss = ss + ",{\"id\":\"" + companyids[i].toLowerCase()  + "\"}]}}";
                            } else {
                                ss = ss + ",{\"id\":\"" + companyids[i].toLowerCase()  + "\"}";
                            }
                        }
                    }
                    cmd = cmd + ss;
                } else {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_comp_ids\", \"enable\": false, \"num\": 0}}";
                }
                break;
            case "scan_filter_serv_data_uuid":
                String[] serv_data_uuid = gatewayConfig.getServices_uuids().split("-");

                if (serv_data_uuid != null && serv_data_uuid.length >= 1) {
                    cmd = "{\"pkt_type\": \"command\",\"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4,\"cmd\": \"scan_filter_serv_data_uuid\",\"enable\": true,\"num\":" + (serv_data_uuid.length) + ",\"value\":[";
                    String ss = "";
                    for (int i = 0; i < serv_data_uuid.length; i++) {
                        if (i == 0) {
                            if (i == serv_data_uuid.length - 1) {
                                ss = ss + "{\"uuid\":\"" + serv_data_uuid[i].toLowerCase() + "\"}]}}";
                            } else {
                                ss = "{\"uuid\":\"" + serv_data_uuid[i] .toLowerCase() + "\"}";
                            }
                        } else {
                            if (i == serv_data_uuid.length - 1) {

                                ss = ss + ",{\"uuid\":\"" + serv_data_uuid[i].toLowerCase()  + "\"}]}}";
                            } else {

                                ss = ss + ",{\"uuid\":\"" + serv_data_uuid[i].toLowerCase()  + "\"}";
                            }
                        }
                    }
                    cmd = cmd + ss;
                } else {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_serv_data_uuid\", \"enable\": false, \"num\": 0}}";
                }
                break;
            case "scan_filter_rssi":
                if (gatewayConfig.isIs_filter_rssi() ) {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_rssi\", \"enable\": true, \"value\": " + gatewayConfig.getFilter_rssi() + "}}";
                } else {
                    cmd = "{\"pkt_type\": \"command\", \"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4, \"cmd\": \"scan_filter_rssi\", \"enable\": false}}";
                }
                break;
            /*case "bleVersion":
                cmd = "{\"pkt_type\":\"command\",\n" +
                        "\"gw_addr\":\"" + address + "\",\n" +
                        "\"data\":{\"msgId\":1234,\n" +
                        "\"cmd\":\"sys_upgrade_ble\",\n" +
                        "\"url\":\"" + ble_firmware.getUrl() + "?userKey=" + ble_firmware.getUser_key() + "&version=" + ble_firmware.getVersion() + "&type=ble" + "\",\n" +
                        "\"reload_default\":false\n" +
                        "}}";
                break;
            case "wifiVersion":
                cmd = "{\"pkt_type\":\"command\",\n" +
                        "\"gw_addr\":\"" + address + "\",\n" +
                        "\"data\":{\"msgId\":1234,\n" +
                        "\"cmd\":\"sys_upgrade_wifi\",\n" +
                        "\"url\":\"" + wifi_firmware.getUrl() + "?userKey=" + wifi_firmware.getUser_key() + "&version=" + wifi_firmware.getVersion() + "&type=wifi" + "\",\n" +
                        "\"reload_default\":false\n" +
                        "}}";
                break;*/
            case "setTopic":

                String sub=gatewayConfig.getSub_topic().replace("{blemac}",address).replace("bleMac",address).replace("$","");
                String pub=gatewayConfig.getPub_topic().replace("{blemac}",address).replace("bleMac",address).replace("$","");
                cmd = "{\"pkt_type\":\"command\",\n" +
                        "\"gw_addr\":\"" + address + "\",\n" +
                        "\"data\":{\"msgId\":1234,\n" +
                        "\"cmd\":\"sys_app_server\",\n" +
                        "\"op\":\"set\",\n" +
                        "\"type\":\"MQTT\",\n" +
                        "\"port\":"+check_sheetMap.get(gatewayConfig.getUser_key()).getPort()+",\n" +
                        "\"host\":\"" + check_sheetMap.get(gatewayConfig.getUser_key()).getHost()+ "\",\n" +
                        "\"mqtt\":{\n" +
                        "\"pub\":\"" + pub + "\",\n" +
                        "\"sub\":\"" + sub  + "\",\n" +
                        "\"usr\":\"\",\n" +
                        "\"pw\":\"\",\n" +
                        "\"clientId\":\"" + address + "\",\n" +
                        "\"qos\":1\n" +
                        "}\n" +
                        "}\n" +
                        "}";

        }
        gatewayConfig = null;
        return cmd;


    }
    private void handleSos_AOA(Beacon beacon){
        if(beacon.getIsbind()==1){
            if(beacon.getBind_type()==1){
                String res=(String) redisUtil.get(device_check_sos_status_res + beacon.getDevice_sn());
                if((res==null||res.equals("0"))&&beacon.getSos()==1){
                    redisUtil.setnoTimeOut(device_check_sos_status_res + beacon.getDevice_sn(),"1");
                    Alarm_Sql alarm_sql = new Alarm_Sql();
                    Devicep devicep=devicePMap.get(beacon.getDevice_sn());
                    alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_key, Alarm_object.device, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", devicep.getName(), devicep.getSn(), devicep.getProject_key()));
                }else if(beacon.getSos()==0){
                    redisUtil.setnoTimeOut(device_check_sos_status_res + beacon.getDevice_sn(),"0");
                }
            }
           else  if(beacon.getBind_type()==2){
                String res=(String) redisUtil.get(person_check_sos_status_res + beacon.getDevice_sn());
                if((res==null||res.equals("0"))&&beacon.getSos()==1){
                    Person person=personMap.get(beacon.getDevice_sn());
                    redisUtil.setnoTimeOut(person_check_sos_status_res + beacon.getDevice_sn(),"1");
                    Alarm_Sql alarm_sql = new Alarm_Sql();
                    alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_key, Alarm_object.person, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", person.getName(), person.getIdcard(), person.getProject_key()));

                }else if(beacon.getSos()==0){
                    redisUtil.setnoTimeOut(person_check_sos_status_res + beacon.getDevice_sn(),"0");
                }
            }
        }
    }
    private void handleBt_AOA(Beacon beacon){
        if(beacon.getIsbind()==1){
            if(beacon.getBind_type()==1){
                String res=(String) redisUtil.get(device_check_bt_status_res + beacon.getDevice_sn());
                if((res==null||res.equals("0"))&&beacon.getBt()<=2.5){
                    redisUtil.setnoTimeOut(device_check_bt_status_res + beacon.getDevice_sn(),"1");
                    Alarm_Sql alarm_sql = new Alarm_Sql();
                    Devicep devicep=devicePMap.get(beacon.getDevice_sn());
                    alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_bt, Alarm_object.device, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", devicep.getName(), devicep.getSn(), devicep.getProject_key()));
                }else if(beacon.getBt()>2.5){
                    redisUtil.setnoTimeOut(device_check_bt_status_res + beacon.getDevice_sn(),"0");
                }
            }
            else  if(beacon.getBind_type()==2){
                String res=(String) redisUtil.get(person_check_bt_status_res + beacon.getDevice_sn());
            //    System.out.println("电量记录="+res);
                if((res==null||res.equals("0"))&&beacon.getBt()<=2.5){
                //    System.out.println("保存记录"+res);
                    redisUtil.setnoTimeOut(person_check_bt_status_res + beacon.getDevice_sn(),"1");
                    Alarm_Sql alarm_sql = new Alarm_Sql();
                    Person person=personMap.get(beacon.getDevice_sn());
                    alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_bt, Alarm_object.person, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", person.getName(), person.getIdcard(), person.getProject_key()));
                  //  alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_bt, Alarm_object.device, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", person.getName(), person.getIdcard(), person.getProject_key()));
                }else if(beacon.getBt()>2.5){
                   // System.out.println("保存记录66"+res);
                    redisUtil.setnoTimeOut(person_check_bt_status_res + beacon.getDevice_sn(),"0");
                }
            }
        }
    }
    private void handleRun_AOA(Beacon beacon){
        if(beacon.getIsbind()==1){
            if(beacon.getBind_type()==1){
                String res=(String) redisUtil.get(device_check_run_status_res + beacon.getDevice_sn());
             //   System.out.println("运动检测="+res);
                if((res==null||res.equals("0"))&&beacon.getRun()==1){
                    redisUtil.setnoTimeOut(device_check_run_status_res + beacon.getDevice_sn(),"1");
                    Alarm_Sql alarm_sql = new Alarm_Sql();
                    Devicep devicep=devicePMap.get(beacon.getDevice_sn());
                    alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_run, Alarm_object.device, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", devicep.getName(), devicep.getSn(), devicep.getProject_key()));
                }else if(beacon.getRun()==0){
                    redisUtil.setnoTimeOut(device_check_run_status_res + beacon.getDevice_sn(),"0");
                }
            }
            //人员基本用不到移动警告/暂时屏蔽
            /*else  if(beacon.getBind_type()==2){
                String res=(String) redisUtil.get(person_check_run_status_res + beacon.getDevice_sn());
                //    System.out.println("电量记录="+res);
                if((res==null||res.equals("0"))&&beacon.getRun()==1){
                    //    System.out.println("保存记录"+res);
                    redisUtil.setnoTimeOut(person_check_run_status_res + beacon.getDevice_sn(),"1");
                    Alarm_Sql alarm_sql = new Alarm_Sql();
                    Person person=personMap.get(beacon.getDevice_sn());
                    alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_bt, Alarm_object.person, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", person.getName(), person.getIdcard(), person.getProject_key()));
                    //  alarm_sql.addAlarm(alarmMapper, new Alarm(Alarm_Type.sos_bt, Alarm_object.device, beacon.getMap_key(), 0, "", beacon.getBt(), 0, "", person.getName(), person.getIdcard(), person.getProject_key()));
                }else{
                    // System.out.println("保存记录66"+res);
                    redisUtil.setnoTimeOut(person_check_run_status_res + beacon.getDevice_sn(),"0");
                }
            }*/
        }
    }
    private void handleFence(Beacon beacon,double pos){
       // System.out.println("围栏");
        if(beacon.getIsbind()==1&&beacon.getDevice_sn()!=null){
            int type=beacon.getBind_type();
            if(type==1){
                Devicep devicep=devicePMap.get(beacon.getDevice_sn());
                if(devicep==null){

                    System.out.println("handleFence()资产缓存异常");
                    return;
                }else if(devicep.getFence_id()==0||devicep.getFence_id()==-1){
                   // System.out.println("没有绑定围栏");
                    return;
                }else{
                    //资产有绑定围栏

                    Fence fence=fenceMap.get(devicep.getFence_id());
                    if(fence!=null){
                        Area area=area_Map.get(fence.getArea_id());
                        if(area!=null){
                            String points=area.getPoint();
                            if(points!=null){
                                String[] pointss=points.split(" ");
                                if(pointss.length>0){
                                    double xx;
                                    double yy;
                                    List<Point2D.Double> polygons=new ArrayList<>();

                                    for(String xy:pointss){
                                        String x=xy.split(",")[0];
                                        String y=xy.split(",")[1];
                                        if(x!=null&&!x.equals("")){
                                            xx=Double.parseDouble(x);
                                            yy=Double.parseDouble(y);
                                            polygons.add(new Point2D.Double(xx,yy));
                                        }
                                    }
                                    boolean status= isPointInPolygon(new Point2D.Double(beacon.getX()*pos,beacon.getY()*pos),polygons);
                                    FenceType fenceType=fence.getFence_type();
                                      // System.out.println("真实状态="+fenceType+status+devicep.getBind_mac());
                                   // int count= (int) redisUtil.get(fence_check_de+beacon.getDevice_sn());
                                    Integer count = (Integer) redisUtil.get(fence_check_device + beacon.getDevice_sn());
                                     //System.out.println(count);
                                    if(count==null){
                                        count=0;
                                    }
                                    if((status&&fenceType==FenceType.OUT)||(!status&&fenceType==FenceType.ON)){

                                        String res= (String) redisUtil.get(fence_check_device_res+beacon.getDevice_sn());
                                        //if(count!=0){
                                        // count=0;
                                        //}

                                        if(count<fence.getTimeout()){
                                            count++;
                                        }
                                        redisUtil.set(fence_check_device+beacon.getDevice_sn(),count);
                                        if(count>=fence.getTimeout()&&(res==null||!res.equals("1"))){
                                            redisUtil.setnoTimeOut(fence_check_device_res+beacon.getDevice_sn(),"1");
                                            StringUtil.sendFenceSosDevice(devicep);
                                            System.out.println(beacon.getMac()+"触发围栏报警");
                                            Alarm_Sql alarm_sql=new Alarm_Sql();
                                            alarm_sql.addAlarm(alarmMapper,new Alarm(status?Alarm_Type.fence_on_sos:Alarm_Type.fence_out_sos,Alarm_object.device,beacon.getMap_key(),devicep.getFence_id(),fenceMap.get(devicep.getFence_id()).getName(),beacon.getBt(),0,"",devicep.getName(),devicep.getSn(),devicep.getProject_key()));
                                        }
                                    }else{
                                        if(count>-fence.getTimeout()){
                                            count--;
                                            redisUtil.set(fence_check_device+beacon.getDevice_sn(),count);
                                        }else{
                                          //  System.out.println(beacon.getMac()+"正常");
                                            // redisUtil.set(fence_check_person+beacon.getDevice_sn(),0);
                                            redisUtil.set(fence_check_device_res+beacon.getDevice_sn(),"");
                                        }
                                        // System.out.println(beacon.getMac()+"-----------");
                                    }
                                }
                            }
                        }
                    }
                }
            }else if(type==2){
                    Person person=personMap.get(beacon.getDevice_sn());
                    if(person==null){
                        //handleFence()人员缓存异常
                        System.out.println("handleFence()人员缓存异常");
                        return;
                    }else if(person.getFence_id()==0||person.getFence_id()==-1){
                        //System.out.println("没有绑定围栏");
                        return;
                    }else{
                        //
                      //  System.out.println("人员有绑定围栏");
                        Fence fence=fenceMap.get(person.getFence_id());
                        if(fence!=null){
                            Area area=area_Map.get(fence.getArea_id());
                            if(area!=null){
                                String points=area.getPoint();
                                if(points!=null){
                                    String[] pointss=points.split(" ");
                                    if(pointss.length>0){
                                        double xx;
                                        double yy;
                                        List<Point2D.Double> polygons=new ArrayList<>();

                                        for(String xy:pointss){
                                            String x=xy.split(",")[0];
                                            String y=xy.split(",")[1];
                                            if(x!=null&&!x.equals("")){
                                                xx=Double.parseDouble(x);
                                                yy=Double.parseDouble(y);
                                                polygons.add(new Point2D.Double(xx,yy));
                                            }
                                        }
                                       boolean status= isPointInPolygon(new Point2D.Double(beacon.getX()*pos,beacon.getY()*pos),polygons);
                                        FenceType fenceType=fence.getFence_type();
                                     //  System.out.println("人员真实状态="+fenceType+status);
                                       try {
                                           Integer count = (Integer) redisUtil.get(fence_check_person + beacon.getDevice_sn());
                                        // System.out.println(count);
                                           if(count==null){
                                               count=0;
                                           }
                                           if ((status && fenceType == FenceType.OUT) || (!status && fenceType == FenceType.ON)) {

                                               String res = (String) redisUtil.get(fence_check_person_res + beacon.getDevice_sn());
                                               //if(count!=0){
                                               // count=0;
                                               //}
                                               if (count < fence.getTimeout()) {
                                                   count++;
                                               }
                                               redisUtil.set(fence_check_person + beacon.getDevice_sn(), count);
                                               if (count >= fence.getTimeout() && (res == null || !res.equals("1"))) {
                                                   redisUtil.setnoTimeOut(fence_check_person_res + beacon.getDevice_sn(), "1");
                                                   StringUtil.sendFenceSosPerson(person);
                                                   System.out.println(beacon.getMac() + "触发围栏报警");
                                                   Alarm_Sql alarm_sql = new Alarm_Sql();
                                                   alarm_sql.addAlarm(alarmMapper, new Alarm(status ? Alarm_Type.fence_on_sos : Alarm_Type.fence_out_sos, Alarm_object.person, beacon.getMap_key(), person.getFence_id(), fenceMap.get(person.getFence_id()).getName(), beacon.getBt(), 0, "", person.getName(), person.getIdcard(), person.getProject_key()));
                                               }
                                           } else {
                                               try {
                                                   if (count > -fence.getTimeout()) {
                                                       count--;
                                                       redisUtil.set(fence_check_person + beacon.getDevice_sn(), count);
                                                   } else {
                                                     //  System.out.println(beacon.getMac() + "正常");
                                                       // redisUtil.set(fence_check_person+beacon.getDevice_sn(),0);
                                                       redisUtil.set(fence_check_person_res + beacon.getDevice_sn(), "");
                                                   }
                                                   // System.out.println(beacon.getMac()+"-----------");
                                               } catch (Exception e) {
                                                   System.out.println("异常" + e);
                                               }
                                           }
                                       }catch (Exception e){
                                           System.out.println("异常="+e);
                                       }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }
    public static boolean isPointInPolygon(Point2D.Double point, List<Point2D.Double> polygon) {
        int count = 0;
        int size = polygon.size();
        for (int i = 0; i < size; i++) {
            Point2D.Double p1 = polygon.get(i);
            Point2D.Double p2 = polygon.get((i + 1) % size);
            if (p1.y == p2.y) {
                continue;
            }
            if (point.y < Math.min(p1.y, p2.y)) {
                continue;
            }
            if (point.y >= Math.max(p1.y, p2.y)) {
                continue;
            }
            double x = (point.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
            if (x > point.x) {
                count++;
            } else if (x == point.x) {
                return true;
            }
        }
        return count % 2 == 1;
    }
    public static void main(String[] a){
        String xy="46.61,61.83";
        String x=xy.split(",")[0];
        String y=xy.split(",")[1];
        System.out.println(Double.parseDouble(x)/3.2);
    }
}


