package com.kunlun.firmwaresystem.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.NewSystemApplication;
import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.device.Gateway_devices;
import com.kunlun.firmwaresystem.entity.*;
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
import com.kunlun.firmwaresystem.location_util.backup.Gateway_device;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.StringUtil;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.awt.geom.Point2D;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.gatewayStatusTask.writeLog;
import static com.kunlun.firmwaresystem.util.StringUtil.*;

public class CallBackHandlers implements Runnable {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    SimpleDateFormat dfaa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");//设置日期格式
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    long times=System.currentTimeMillis()/1000;
    long time;
    String topic;
    MqttMessage message;
    TagLogSql tagLogSql;
    Gson gson = new Gson();
    History_Sql history_sql=new History_Sql();
    Gateway gateway = null;
    public CallBackHandlers(String topic, MqttMessage message) {
        this.message = message;
        this.topic = topic;
        tagLogSql=new TagLogSql();
    }
    @Override
    public void run() {
        // subscribe后得到的消息会执行到这里面
   //  println("接收消息主题:" + new String(message.getPayload()));

        if(topic.equals("connected")){

        }

        String data = new String(message.getPayload());

        //   println("接收消息Qos:" + data);
        time = System.currentTimeMillis()/1000;// new Date()为获取当前系统时间
        if (!data.contains("pkt_type")){
            return;
        }

        JSONObject jsonObject = null;
        String pkt_type = null;
        String gatewayAddress = null;
        try {
            jsonObject = JSONObject.parseObject(data);
            pkt_type = jsonObject.getString("pkt_type");
        /*    if(pkt_type!=null&&pkt_type.equals("scan_report")){
                //不处理与扫描数据有关的消息
                println("是扫描数据");
                return;
            }else{
             //   println("非扫描数据"+topic);
            }*/
       //
            gatewayAddress = jsonObject.getString("gw_addr");
            if(gatewayAddress!=null&&gatewayAddress.equals("ffffffffffff")){
                return;
            }
          //  println("吃吃吃"+data);

            if(redisUtil==null){
                println("redis 空引用");
                return;
            }
            if(gatewayMapper==null){
                println("gatewayMapper 空引用");
                return;
            }
            if(gatewayMap==null){
                println("gatewayMap 空引用");
                return;
            }
            if(gatewayMap.get(gatewayAddress)!=null){

                gateway = (Gateway) redisUtil.get(redis_key_gateway + gatewayAddress);

                if(gateway==null){
                    Gateway_sql gateway_sql=new Gateway_sql();
                    gateway=gateway_sql.getGatewayByMac(gatewayMapper,gatewayAddress);
                    redisUtil.set(redis_key_gateway + gatewayAddress,gateway);
                }
            }


            //标准版本不做自动添加网关功能
           /* if (gateway == null&&!topic.equals("AlphaRsp")) {

               // println("项目长度=="+gatewayConfigMap.size());
                //println("主题==="+topic);
                   writeLog("新增加一个网关="+gatewayAddress);

                   for(String key:gatewayConfigMap.keySet()){
                       String a=gatewayConfigMap.get(key).getPub_topic();
                       String b=topic.toLowerCase().replace(gatewayAddress,"");
                     //  println("项目主题="+a);
                      // println("网关主题="+b);
                       if(a.contains(b)){
                         //  println("网关主题一致========");
                           gateway = new Gateway("", gatewayAddress, "", key, "admin", gatewayConfigMap.get(key).getName(), 0,0,0);
                          // println("跳出循环"+gateway.getConfig_key());
                           break;
                       }else{
                        //   println("网关主题不一致1111111111");
                         //  println("继续循环");
                       }

                   }
                   if(gateway==null){
                     //  println("最后循环"+topic);
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
                        println("增加一个网关"+gateway.getAddress());
                        gatewayMap.put(gateway.getAddress(), gateway.getAddress());
                        println("最新长度"+gateway.getAddress());
                    }else{
                        println("插入问题");
                    }
                    // println(2);
                } finally {
                    lock.unlock();//解锁
                }

                return;
            }*/
            if(gateway==null){
                return;

              /*  Gateway_sql gateway_sql=new Gateway_sql();
                gatewayMap= gateway_sql.getAllGateway(redisUtil,gatewayMapper);
                gateway = (Gateway) redisUtil.get(redis_key_gateway + gatewayAddress);
                Gateway gateway1= (Gateway) redisUtil.get(redis_key_gateway + "de2106499131");
                if(gateway1!=null){
                    println("数据库读取时间"+ "旧的时间"+df.format(new Date(gateway1.getLasttime()*1000)) );

                }
                if(gateway==null){
                    return;
                }*/
               // if(gatewayAddress.equals("de2106499131")){



                //   return;
            }
            //上一次离线时间
            if (gateway.getOnline() == 0) {
                redisUtil.set(redis_key_gateway_onLine_time + gatewayAddress, time);
                redisUtil.set(redis_key_gateway_revice_count + gatewayAddress, 0);
                println("网关上线数据="+jsonObject);
                writeLog("网关：" + gateway.getAddress() + "重新上线  在线时间为" + time);
            } else if (redisUtil.get(redis_key_gateway_onLine_time + gatewayAddress) == null) {
                println("更新一次在线的时间"+time);
                redisUtil.set(redis_key_gateway_onLine_time + gatewayAddress, time);
                redisUtil.set(redis_key_gateway_revice_count + gatewayAddress, 0);
            }
            //
            if(gateway.getReboot()>0){
                int reboot=gateway.getReboot()-1;
                println("Reboot="+gateway.getReboot());
                gateway.setReboot(reboot);
                redisUtil.set(redis_key_gateway + gatewayAddress,gateway);
                return;
            }

         /*   if(gateway.getAddress().equals("e1502ec0cdcc")){
                println(df.format(new Date())+"收到e1502ec0cdcc"+data);
            }*/

            gateway.setLasttime(System.currentTimeMillis()/1000);
            //GatewayMap.put(gatewayAddress,gateway);
            //  gateway.setOnline(1);
            if(gateway.getOnline()!=1){

                // StringUtil.saveRecord(gateway.getAddress(),gateway.getLasttime(),gateway.getUser_key(),1,1,gateway.getProject_key());
                Alarm_Sql alarm_sql = new Alarm_Sql();
                alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_online,Alarm_object.gateway,gateway.getMap_key(),0,"", 0,0,"",gateway.getName(),gateway.getAddress(),gateway.getProject_key(),gateway.getLasttime()));
            }
         //   println("状态="+gateway.getOnline());
            StringUtil.sendGatewayPush(gateway,1);
            GatewayMap.put(gatewayAddress,gateway);


            redisUtil.set(redis_key_gateway + gatewayAddress, gateway);



           // println("不可能生效");
        } catch (Exception e) {
            println("json格式不对=" +"ssd"+e);
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
            //   println("此消息未有解析，请更新服务器"+jsonObject.toString());
            return;
        }
        String className = object.getClass().getSimpleName();
        Gateway_sql gateway_sql;
        // println("ClassnMame="+className);
        switch (className) {
            case Constant.Scan_report:

                try {

                    com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report scan_report = ((com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report<Scan_report_data>) object);
                    //  println("设备数量="+((Scan_report<Scan_report_data>) object).getData().getDev_infos().length);
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
                    println("异常" + e.toString() + data);
                }
                break;
            case Constant.ConnectExecute:
                redisUtil.set("sendToGateway_id=" + ((com.kunlun.firmwaresystem.gatewayJson.type_response.ConnectExecute<ConnectExecuteDetail>) object).getData().getMsgId(), ((com.kunlun.firmwaresystem.gatewayJson.type_response.ConnectExecute<ConnectExecuteDetail>) object).getData().isResult());
                //  Util.add_user_device_one();
                break;
            case Constant.ConnectState:
                // ((ConnectState<ConnectDetail>)object).getData().getDevice_state();
                redisUtil.set(Constant.ConnectState + ((com.kunlun.firmwaresystem.gatewayJson.state.ConnectState<ConnectDetail>) object).getData().getDevice_addr(), ((ConnectState<ConnectDetail>) object).getData().getDevice_state());
                println("连接状态=" + ((ConnectState<ConnectDetail>) object).getData().getDevice_state());
                //准备就绪，推送
                if (((ConnectState<ConnectDetail>) object).getData().getDevice_state().equals(ConnectState_redy)) {
                    println("连接状态已就绪  开始推送至rabittmq=");
                }
                break;
            case Constant.Scan_filter:
                // println("进入两次");
                //取出网关
                //gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getGw_addr());
                //  println("缓存");
                gateway.Filter_name1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_name());
                // println("缓存66");
                gateway.setFilter_dev_mac1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_dev_mac());
                // println("缓存1");
                gateway.Filter_ibeacon1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().isFilter_beacon_b());
                // println("缓存2");
                try {
                    gateway.Filter_companyId1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_comp_ids());
                }catch (Exception e){
                    println("异常====="+e.getMessage());
                }
                // println("缓存3");
                gateway.setFilter_rssi(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_rssi() + "");
                //   println("缓存4");
                gateway.setFilter_uuid1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getFilter_uuid());
                gateway.setScan_filter_serv_data_uuid1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getData().getScan_filter_serv_data_uuid());
                //  println("过滤的UUID==="+((Scan_filter )object).getData().getFilter_uuid());
                //更新后再次缓存
                gateway.setLasttime(System.currentTimeMillis()/1000);
                redisUtil.set(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter) object).getGw_addr(), gateway);
                //存到数据库 同步
                gateway_sql = new Gateway_sql();

                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Network_Status:
                gateway.setWifi_address(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWlan_mac());
                gateway.setWan_gw(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWan_gw());
                gateway.setWan_ip(((com.kunlun.firmwaresystem.gatewayJson.type_response.Network_Status) object).getData().getWan_ip());
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
                gateway.setLasttime(System.currentTimeMillis()/1000);
                redisUtil.set(redis_key_gateway + ((Network_Status) object).getGw_addr(), gateway);
                //存到数据库 同步
                gateway_sql = new Gateway_sql();
               // println("22");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.Scan_params:
                //gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_params) object).getGw_addr());
                gateway.Scan_out1(((Scan_params) object).getData().isReport_onoff());
                gateway.Report_type1(((Scan_params) object).getData().isRequest_onoff(), ((Scan_params) object).getData().isStuff_card_onoff());
                gateway.Scan_interval(((Scan_params) object).getData().getReport_interval());
                //更新后再次缓存
                gateway.setLasttime(System.currentTimeMillis()/1000);
                redisUtil.set(redis_key_gateway + ((Scan_params) object).getGw_addr(), gateway);
                //存到数据库 同步
                gateway_sql = new Gateway_sql();
                // println("33");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.Adv_params:
                ///gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params) object).getGw_addr());
                gateway.Broadcast1(((com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params) object).getData().isAdv_onoff());
                gateway.setLasttime(System.currentTimeMillis()/1000);
                redisUtil.set(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params) object).getGw_addr(), gateway);
                gateway_sql = new Gateway_sql();
              //  println("44");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.WifiVersion:
               // gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.WifiVersion) object).getGw_addr());
                gateway.setWifi_version(((com.kunlun.firmwaresystem.gatewayJson.type_response.WifiVersion<WifiVersionDetail>) object).getData().getVersion());
                gateway.setLasttime(System.currentTimeMillis()/1000);
                redisUtil.set(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.WifiVersion) object).getGw_addr(), gateway);
                gateway_sql = new Gateway_sql();
              //  println("55");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.BleVersion:
               // gateway = (Gateway) redisUtil.get(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion) object).getGw_addr());
                gateway.setBle_version(((com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion<BleVersionDetail>) object).getData().getVersion());
                gateway.setLasttime(System.currentTimeMillis()/1000);
                redisUtil.set(redis_key_gateway + ((com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion) object).getGw_addr(), gateway);
                gateway_sql = new Gateway_sql();
               // println("66");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                break;
            case Constant.App_Server:
               // gateway = (Gateway) redisUtil.get(redis_key_gateway + ((App_Server) object).getGw_addr());
                if (gateway == null) {
                    return;
                }
              //  println("状态="+((App_Server) object).getData().toString());
                gateway.setSub_topic(((App_Server) object).getData().getSub());
                gateway.setIp((((App_Server) object).getData().getHost()));
                gateway.setPub_topic(((App_Server) object).getData().getPub());
                gateway.setLasttime(System.currentTimeMillis()/1000);
                redisUtil.set(redis_key_gateway + ((App_Server) object).getGw_addr(), gateway);
                gateway_sql = new Gateway_sql();
                gateway_sql.updateGateway(gatewayMapper, gateway);
                //println(""+gateway);
                RabbitMessage rabbitMessage1 = new RabbitMessage(gateway.getPub_topic(), "",gateway.getProject_key());
                directExchangeProducer.send(rabbitMessage1.toString(), "mqtt_topic");

                break;
        }
    }

    private void BeaconHandle(Gateway gateway, Scan_report_data scan_report_data) {
      //

            Scan_report_data_info[] devices = scan_report_data.getDev_infos();
            for (Scan_report_data_info device : devices) {
                Beacon beacon=beaconsMap.get(device.getAddr());
                Bracelet bracelet=braceletsMap.get(device.getAddr());
              /*  if(device.getAddr().equals("f0c890022079")){
                    println("收到这个设备=f0c890022079");
                }*/
                //信标不在系统内，跳过
                if(beacon == null&&bracelet==null){
                    continue;
                }else if(beacon!=null){
                    beacon.setGateway_address(gateway.getAddress());
                    if(beacon.getOnline()!=1){

                        // StringUtil.saveRecord(gateway.getAddress(),gateway.getLasttime(),gateway.getUser_key(),1,1,gateway.getProject_key());
                        Alarm_Sql alarm_sql = new Alarm_Sql();
                        alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_online,Alarm_object.beacon,gateway.getMap_key(),0,"", 0,0,"","Beacon",beacon.getMac(),beacon.getProject_key(),beacon.getLastTime()));
                    }
                    StringUtil.sendBeaconPush_onOff(beacon,1);
                    beacon.setLastTime(time);
                    beacon.setRssi(device.getRssi());
                    beacon.setGateway_address(gateway.getAddress());
                    String types[]=beacon.getType().split("-");
                    if(types.length>=2){
                        String type=types[types.length-1];
                        //服务数据表示电压值
                        if (type.equals("1")) {
                            //0x0201061AFF4C000215FDA50693A4E24FB1AFCFC6EB0764782510148238C5
                            // 12164C4B0AAD0500F0C810148238000000000008094B313438323338
                            if(device.getSrp_raw()!=null&&! device.getSrp_raw().isEmpty()){
                                byte[] btdata = StringUtil.hexToByteArr(device.getSrp_raw());
                                if(btdata.length >0){
                                    int bt=(btdata[4]&0xff)*256+(btdata[5]&0xff);
                                    beacon.setBt(bt+" MV");
                                }
                            }
                        }//0x7364表示电量
                        else if(type.equals("2")){
                            if(device.getSrp_raw()!=null&&!device.getSrp_raw().isEmpty()){
                                   byte[] btdata = StringUtil.hexToByteArr(device.getSrp_raw());
                                beacon.setBt((btdata[5]&0xff)+"%");
                            }

                        }
                        //
                        else if(type.equals("3")){

                        }
                        /* if (btdata.length == 30) {
                            String str = Integer.toBinaryString((btdata[29] & 0xFF) + 0x100).substring(1);
                            char[] bit = str.toCharArray();
                            //  println("二进制"+str);
                            beacon.setLastTime(time);
                            if(beacon.getType()==2){
                                str = str.substring(0, 6);
                                beacon.setBt(((double) (Byte.parseByte(str, 2) & 0xff)) / 10.0);
                                if (bit[7] == '1') {
                                    beacon.setSos(1);
                                }else{
                                    beacon.setSos(0);
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
                        }*/
                        //信标不为空，处理信标
                        Tag_log tagLog=new Tag_log();
                        tagLog.setBeacon_address(beacon.getMac());
                        tagLog.setGateway_address(beacon.getGateway_address());
                        tagLog.setCreate_time(time);
                        tagLog.setProject_key(beacon.getProject_key());
                        tagLog.setKeys1(beacon.getSos());
                        tagLog.setRun(beacon.getRun());
                        tagLog.setBt(beacon.getBt());
                        tagLog.setGateway_name(gateway.getName());
                        tagLog.setRssi(beacon.getRssi());
                        tagLog.setType(beacon.getType());
                        //  println("log="+tagLog);
                        tagLogSql.addLog(NewSystemApplication.tagLogMapper,tagLog);
                    }
          //          println("收到原始="+device.getAddr()+device.getSrp_raw()+"------"+type);
                    //4B4C紧跟电压


                }
                else{
                    bracelet.setOnline(1);
                    bracelet.setLast_time(time);
                    bracelet.setRssi(device.getRssi());
              //      println("原始手环数据="+device.getAdv_raw());
                    if(device.getAdv_raw().startsWith("02010617FF0001")){
                    //02 01 06 17 FF 00 01 5F 16 00 02 87 02 7B 52 00 00 48 0D 00 00 3F 0E 5A 00 14 01 03 09 43 32
                        byte[] btdata = StringUtil.hexToByteArr(device.getAdv_raw());
//                        byte[] btdata = StringUtil.hexToByteArr("02010617FF00015F16000287027B520000480D00003F0E5A00140103094332");

                        println("血氧="+(btdata[7]&0xff));
                        if((btdata[7]&0xff)!=0){
                            bracelet.setSpo(btdata[7]&0xff);
                            println("油纸");
                        }
                        else{
                            println("物质");
                        }

                        int c1=(btdata[16]&0xff)*256;
                        int d1=btdata[15]&0xff;
                        int e1=c1+d1;
                        if(e1!=0){
                            bracelet.setCalorie(e1);
                        }



                        int c=(btdata[20]&0xff)*256;
                        int d=btdata[19]&0xff;
                        int e=c+d;
                        if(e!=0){
                            bracelet.setSteps(e);
                        }
                        double a=(btdata[22]&0xff)*256.00;
                        double b=btdata[21]&0xff;
                        String t=(a+b)/100.00+"";
                        if(t.length()>3){
                            bracelet.setTemp(t);
                        }
                        if((btdata[23]&0xff)>0){
                            bracelet.setHeart_rate(btdata[23]&0xff);
                        }

                        bracelet.setSos(btdata[24]&0xff);
                        bracelet.setBt(btdata[25]&0xff);
                        Tag_log tagLog=new Tag_log();
                        tagLog.setBeacon_address(bracelet.getMac());
                        tagLog.setGateway_address(gateway.getAddress());
                        tagLog.setCreate_time(time);
                        tagLog.setProject_key(bracelet.getProject_key());
                        tagLog.setKeys1(bracelet.getSos());
                        tagLog.setRun(-1);
                        tagLog.setBt(bracelet.getBt()+"%");
                        tagLog.setGateway_name(gateway.getName());
                        tagLog.setRssi(bracelet.getRssi());
                        tagLog.setType(bracelet.getType()+"");
                        //  println("log="+tagLog);
                        tagLogSql.addLog(NewSystemApplication.tagLogMapper,tagLog);
                    }
                }
                /*if(gateway.getAddress().equals("f6938f3bfc57")){
                    println("fff收到这个设备=f6938f3bfc57");
                }*/
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
            Wordcard_a wordCard_a = wordcard_aMap.get(device.getAddr());
            //  println("输出设备="+device.toString()+device);
            int bt = device.getBatt();
            if (bt != -1) {
                wordCard_a.setBt(bt+"");
            }
            wordCard_a.setRun(device.getMotion());
            wordCard_a.setMac(device.getAddr());
            wordCard_a.setOnline(1);
            redisUtil.set(redis_key_device_sos+device.getAddr(),device.getKeys());
            //当前工卡SOS状态和之前的状态SOS不一致并且是报警，就更新推送，否则不推送
            Ibcn_infos[] ibcn_infos = device.getIbcn_infos();
            Gateway_devices gateways = null;
            String json = null;
            try {
                json = (String) redisUtil.get(redis_key_card_map + device.getAddr());
            } catch (Exception e) {
                println("法国红酒封口的" + e.getMessage());
            }
            if (json == null) {
                gateways = new Gateway_devices();
                ArrayList<Gateway_device> gatewayDevices = new ArrayList<>();
                gateways.setGatewayDevices(gatewayDevices);
                println("没有数据" + device.getAddr());
                //   println(data);
            } else {
                try {
                    gateways = new Gson().fromJson(json, Gateway_devices.class);
                    if (gateways.getGatewayDevices().size() >= 30) {
                        gateways.getGatewayDevices().remove(0);
                    }
                } catch (Exception e) {
                }
            }
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
                println("法国红酒封口的"+e.getMessage());
            }
            if (json == null) {
                gateways = new Gateway_devices();
                ArrayList<Gateway_device> gatewayDevices = new ArrayList<>();
                gateways.setGatewayDevices(gatewayDevices);
                println("没有数据11" + scanReportDataInfo.getAddr());
                //   println(data);
            } else {
                try {
                    gateways = new Gson().fromJson(json, Gateway_devices.class);
                    if (gateways.getGatewayDevices().size() >= 30) {
                        gateways.getGatewayDevices().remove(0);
                    }
                } catch (Exception e) {
                }
            }
            try {
                if (gateway != null) {
                    int used=0;
                    if(usedMap.get(gateway.getProject_key())!=null){
                        used=usedMap.get(gateway.getProject_key());
                    }
                    gateways.getGatewayDevices().add(new Gateway_device(gateway.getAddress(),scanReportDataInfo.getAddr(),scanReportDataInfo.getRssi(),gateway.getX(),gateway.getY(),times,gateway.getMap_key(),gateway.getArssi(),gateway.getN(),gateway.getZ(),used));

                    //   gateways.getGatewayDevices().add(new Gateway_device(gateway.getN(), gateway.getAddress(), scanReportDataInfo.getAddr(), scanReportDataInfo.getRssi(), gateway.getSub_topic(), gateway.getPub_topic(), gateway.getX(), gateway.getY(), gateway.getName()));
                    //  println("标签mac     "+scanReportDataInfo.getAddr()+"缓存="+gateways.toString());
                    redisUtil.set(redis_key_device_gateways + scanReportDataInfo.getAddr(), gateways.toString());
                }
            } catch (Exception e) {
                println("wewe" + e.getMessage());
            }
        }
    }
    private Object analysisResponse(JSONObject jsonRaw) {

        Type type = null;
        try {
            JSONObject data = jsonRaw.getJSONObject("data");
            String resp = data.getString("resp");
            //  println("具体的头="+jsonRaw.toString());
            switch (resp) {
                case response_sys_get_ver:
                    type = new TypeToken<com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion<BleVersionDetail>>() {
                    }.getType();
                    com.kunlun.firmwaresystem.gatewayJson.type_response.BleVersion<BleVersionDetail> Ble_version = gson.fromJson(jsonRaw.toString(), type);
                    //   println("蓝牙版本号=" + Ble_version.getData().getVersion());
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
                    // println("WIfi版本号=" +   Wifi_version.getData().getVersion());
                    return Wifi_version;


                case response_conn_addr_request:
                    type = new TypeToken<com.kunlun.firmwaresystem.gatewayJson.type_response.ConnectExecute<ConnectExecuteDetail>>() {
                    }.getType();
                    com.kunlun.firmwaresystem.gatewayJson.type_response.ConnectExecute<ConnectExecuteDetail> connectExecute = gson.fromJson(jsonRaw.toString(), type);
                    println("连接执行状态=" + connectExecute.getData().isResult());
                    return connectExecute;
                case response_scan_filter_get:
                    Scan_filterDetail scanFilterDetail = new Scan_filterDetail(jsonRaw.getJSONObject("data").toString());
                    com.kunlun.firmwaresystem.gatewayJson.type_response.Scan_filter scan_filter = new Scan_filter();
                    scan_filter.setData(scanFilterDetail);
                    scan_filter.setGw_addr(jsonRaw.getString("gw_addr"));
                    scan_filter.setPkt_type(jsonRaw.getString("pkt_type"));
                    scan_filter.setTime(jsonRaw.getString("time"));
                    if(scanFilterDetail.getFilter_comp_ids()!=null&&scanFilterDetail.getFilter_comp_ids().length>10){
                        println("原始数据="+jsonRaw.toString());
                    }
                    return scan_filter;

                case response_scan_params_get:
                    try {
                        // println("log");
                        Scan_paramsDetail scan_paramsDetail = new Scan_paramsDetail(jsonRaw.getJSONObject("data").toString());
                        //  println("log1222");
                        Scan_params scanParams = new Scan_params();
                        //  println("333");
                        scanParams.setData(scan_paramsDetail);
                        //  println("444");
                        scanParams.setGw_addr(jsonRaw.getString("gw_addr"));
                        //  println("555");
                        scanParams.setPkt_type(jsonRaw.getString("pkt_type"));
                        // println("666");
                        scanParams.setTime(jsonRaw.getString("time"));
                        // println("777");
                        return scanParams;
                    } catch (Exception e) {
                        println("response_scan_params_get异常输出=" + e.toString());
                        return null;
                    }
                case response_adv_params_get:
                    Adv_paramsDetail adv_paramsDetail = new Adv_paramsDetail(jsonRaw.getJSONObject("data").toString());
                    com.kunlun.firmwaresystem.gatewayJson.type_response.Adv_params adv_params = new Adv_params();
                    adv_params.setData(adv_paramsDetail);
                    adv_params.setGw_addr(jsonRaw.getString("gw_addr"));
                    //  println("555");
                    adv_params.setPkt_type(jsonRaw.getString("pkt_type"));
                    //  println("666");
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
            println("解析异常=" + jsonRaw.toString());
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
            //  println("具体的头111="+state);

            switch (state) {
                case state_sta_gw_hb:
                    type = new TypeToken<HeartState<HeartDetail>>() {
                    }.getType();
                    HeartState<HeartDetail> heartState = gson.fromJson(jsonRaw.toString(), type);
                    //  println(heartState.getGw_addr() + "心跳状态=" + heartState.getData().getTicks_cnt());

                  //  Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + heartState.getGw_addr());
                    String synStr = (String) redisUtil.get(redis_key_project_sys + gateway.getAddress());
                    //心跳包更新
                    sendGatewayHeartbeatPush(gateway);
                 //   println("当前心跳=" + heartState.getData().getTicks_cnt());
                    //println(jsonRaw);
                    Integer heart = (Integer) redisUtil.get(redis_key_project_heart + gateway.getAddress());
                    if (heart != null) {
                        println("记录心跳心跳=" + heart);
                    }
                    if (heart != null && Math.abs(heartState.getData().getTicks_cnt() - heart) < 10) {
                    println("刚更新过固件，不再执行");
                    return heartState;
                }


                    Gateway_config gatewayConfig = gatewayConfigMap.get(gateway.getConfig_key());
                    if (gateway != null) {
                        gateway.setLasttime(System.currentTimeMillis()/1000);
                        redisUtil.set(redis_key_gateway + heartState.getGw_addr(), gateway);
                        // println("网关配置="+gateway.toString());
                        // println("项目配置="+gatewayConfig.toString());
                        try {
                            if (gatewayConfig != null) {

                                List<String> cmds = new ArrayList<>();
                                println("项目不为空");
                                String cmd = "";
                                //      println("scan_filter_comp_ids");
                                if(gateway.getIsyn()==1&&gatewayConfig.getIsyn()==1){
                                    println("有网关需要同步");


                                    if (isChange(gatewayConfig.getFilter_companyids(), gateway.getFilter_companyId())&&gatewayConfig.isIs_filter_companyid()) {
                                        cmd = getParamsJson("scan_filter_comp_ids", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                          /*  if (isChange(gatewayConfig.getFilter_rssi(), gateway.getFilter_rssi())) {
                                cmd = getParamsJson("scan_filter_comp_ids", gatewayConfig, gateway.getAddress(), null, null);
                                cmds.add(cmd);
                            }*/
                                    if (isChange(gatewayConfig.getFilter_rssi()+"", gateway.getFilter_rssi())&&gatewayConfig.isIs_filter_rssi()) {
                                        cmd = getParamsJson("scan_filter_rssi", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                          /*  if(!gatewayConfig.getFilter_companyId().equals(gateway.getFilter_companyId())){
                                isChange();
                                cmd= getParamsJson("scan_filter_comp_ids",gatewayConfig,gateway.getAddress(),null,null);
                            }*/
                       /*   println("scan_filter_rssi");
                            if (!gatewayConfig.getFilter_rssi().equals(gateway.getFilter_rssi())){

                                cmd= getParamsJson("scan_filter_rssi",gatewayConfig,gateway.getAddress(),null,null);
                            }*/
                                  //  println(gatewayConfig.getFilter_uuid() + "----" + gateway.getFilter_uuid());
                           /* if (isChange(gatewayConfig.getFilter_uuid(), gateway.getFilter_uuid())) {

                            }*/
                                  //  println("debug="+gatewayConfig.getFilter_uuids()+"    "+gateway.getFilter_uuid());
                                    if (gatewayConfig.getFilter_uuids() != null && gateway.getFilter_uuid() != null && !gatewayConfig.getFilter_uuids().equals(gateway.getFilter_uuid())&&gatewayConfig.isIs_filter_uuid()) {
                                        cmd = getParamsJson("scan_filter_ibcn_uuid", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                        /*   println("scan_filter_ibcn_uuid");
                            if(!gatewayConfig.getFilter_uuid().equals(gateway.getFilter_uuid())){
                                cmd= getParamsJson("scan_filte网关订阅一样r_ibcn_uuid",gatewayConfig,gateway.getAddress(),null,null);
                            }*/

                                    //  println("adv_onoff");
                                    if (gatewayConfig.getBroadcast() != gateway.getBroadcast()) {
                                        cmd = getParamsJson("adv_onoff", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                                    //  println("scan_filter_ibcn_dev");
                                    if (gatewayConfig.getFilter_ibeacon() != gateway.getFilter_ibeacon()) {
                                        cmd = getParamsJson("scan_filter_ibcn_dev", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                                    //  println("scan_request_onoff");
                                    if (gatewayConfig.getReport_type() != gateway.getReport_type()) {
                                        cmd = getParamsJson("scan_request_onoff", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                                    //  println("scan_report_onoff");

                                    if (isChange(gatewayConfig.getFilter_names(), gateway.getFilter_name())&&gatewayConfig.isIs_filter_name()) {
                                        cmd = getParamsJson("scan_filter_name", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                                    if (isChange(gatewayConfig.getFilter_macs(), gateway.getFilter_dev_mac())) {
                                        cmd = getParamsJson("scan_filter_dev_mac", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                                    if(isChange(gatewayConfig.getServices_uuids(),gateway.getScan_filter_serv_data_uuid())&&gatewayConfig.isIs_services_uuid()){
                                        cmd = getParamsJson("scan_filter_serv_data_uuid", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }
                                    if (gatewayConfig.getScan_interval() != gateway.getScan_interval()) {
                                        cmd = getParamsJson("scan_report_interval", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }

                                 //   println(gatewayConfig.getSub_topic() + "    " + gateway.getSub_topic());
                                    if (isChange(gatewayConfig.getSub_topic(), gateway.getSub_topic())) {
                                      //  println("网关订阅不一样");
                                        if (gateway.getSub_topic().replace(gateway.getAddress(), "{blemac}").equals(gatewayConfig.getSub_topic())) {
                                            println("网关订阅规则一致");
                                        } else {
                                            cmd = getParamsJson("setTopic", gatewayConfig, gateway.getAddress(), "");
                                            cmds.add(cmd);
                                        }
                                    } else {
                                        println("网关订阅一样");
                                    }
                                 //   println(gatewayConfig.getPub_topic() + "    " + gateway.getPub_topic());
                                    if (isChange(gatewayConfig.getPub_topic(), gateway.getPub_topic())) {
                                       // println("网关发布不一样");
                                        if (gateway.getPub_topic().replace(gateway.getAddress(), "{blemac}").equals(gatewayConfig.getPub_topic())) {
                                            println("网关发布规则一致");
                                        } else {
                                            cmd = getParamsJson("setTopic", gatewayConfig, gateway.getAddress(), "");
                                            cmds.add(cmd);
                                        }
                                        // 码石的不去更改主题消息
                                    } else {
                                        println("网关订阅一样");
                                    }
                               /*else {
                                    if (redisUtil.get(redis_key_updateing_gateway) != null && redisUtil.get(redis_key_updateing_gateway).equals(gateway.getAddress())) {
                                        redisUtil.set(redis_key_updateing_gateway, null);
                                        writeLog("网关：" + gateway.getAddress() + " 升级完成");
                                    }
                                }*/



                                    if (gatewayConfig.getScan_out() != gateway.getScan_out()) {
                                        cmd = getParamsJson("scan_report_onoff", gatewayConfig, gateway.getAddress(), "");
                                        cmds.add(cmd);
                                    }

                                    //当心跳包来的时候，检查网关与项目的配置，不一致的话就更新网关
                                    println("CMDS=" + cmds.size());
                                    if (cmds.size() > 0) {

                                        println("有状态未同步");
                                        for (String cmddata : cmds) {
                                            String topic = gateway.getSub_topic();
                                            //就以网关保存的主题为准，下发，前提是网关保存的主题是正确的
                                   /* if (!topic.equals("SrvData") && !topic.contains(gateway.getAddress())) {
                                        topic = topic + "/" + gateway.getAddress();
                                    }*/
                                            redisUtil.set(redis_key_project_sys + gateway.getAddress(), cmddata);
                                            if(topic.contains("${blemac}")){
                                                topic=topic.replace("${blemac}",gateway.getAddress());
                                            }
                                            cmddata=cmddata.replaceAll(" ","");
                                            println("发送的指令=" + cmddata + "===" + topic);
                                            RabbitMessage rabbitMessage = new RabbitMessage(topic, cmddata,gateway.getProject_key());
                                            // directExchangeProducer.send(rabbitMessage.toString(), go_to_connect);
                                            directExchangeProducer.send(rabbitMessage.toString(), "sendToGateway");
                                            if(cmddata!=null&&cmddata.contains("sys_upgrade")){
                                                break;
                                            }
                                        }
                                    } else {
                                        redisUtil.set(redis_key_project_sys + gateway.getAddress(), "ok");
                                       // println("网关:" + gateway.getName() + "同步完成了");
                                    }
                                }
                                else{
                                    println("此网关不需要同步"+gateway.getAddress());
                                }
                            }
                            Firmware_task firmwareTask_ble=firmwareTaskHashMap.get("ble_"+gateway.getAddress());
                            if(firmwareTask_ble!=null&&firmwareTask_ble.getCount()>0){

                             //   println("项目不为空");
                                String cmd = "";
                                println("bleVersion"+gateway.getBle_version()+firmwareTask_ble.getVersion());
                                if (gateway.getBle_version()!=null&&!(gateway.getBle_version().equals(firmwareTask_ble.getVersion()))) {
                                        redisUtil.set6000(redis_key_project_heart + gateway.getAddress(), heartState.getData().getTicks_cnt());
                                        firmwareTask_ble.setCount(firmwareTask_ble.getCount()-1);
                                        if(firmwareTask_ble.getCount()<=0){
                                            firmwareTaskHashMap.remove("ble_"+gateway.getAddress());
                                        }
                                        cmd = getParamsJson("bleVersion", gatewayConfig, gateway.getAddress(),firmwareTask_ble.getUrl());
                                        println("有状态未同步");
                                            String topic = gateway.getSub_topic();
                                            redisUtil.set(redis_key_project_sys + gateway.getAddress(), cmd);
                                            if(topic.contains("${blemac}")){
                                                topic=topic.replace("${blemac}",gateway.getAddress());
                                            }
                                             cmd=cmd.replaceAll(" ","");
                                            println("蓝牙升级发送的指令=" + cmd + "===" + topic);
                                            RabbitMessage rabbitMessage = new RabbitMessage(topic, cmd,gateway.getProject_key());
                                            directExchangeProducer.send(rabbitMessage.toString(), "sendToGateway");
                                }else{
                                    println("应该升级完成");
                                    firmwareTaskHashMap.remove("ble_"+gateway.getAddress());
                                }
                            }
                            Firmware_task firmwareTask_wifi=firmwareTaskHashMap.get("wifi_"+gateway.getAddress());
                            if(firmwareTask_wifi!=null&&firmwareTask_wifi.getCount()>0){
                                String cmd = "";
                                println("WifiVersion"+gateway.getWifi_version()+"999"+firmwareTask_wifi.getVersion());
                                if (gateway.getWifi_version()!=null&&!(gateway.getWifi_version().equals(firmwareTask_wifi.getVersion())))  {
                                            redisUtil.set6000(redis_key_project_heart + gateway.getAddress(), heartState.getData().getTicks_cnt());
                                            firmwareTask_wifi.setCount(firmwareTask_wifi.getCount()-1);
                                            if(firmwareTask_wifi.getCount()<=0){
                                                firmwareTaskHashMap.remove("wifi_"+gateway.getAddress());
                                            }
                                            cmd = getParamsJson("wifiVersion", gatewayConfig, gateway.getAddress(), firmwareTask_wifi.getUrl());
                                            println("有状态未同步");
                                            String topic = gateway.getSub_topic();
                                            redisUtil.set(redis_key_project_sys + gateway.getAddress(), cmd);
                                            if(topic.contains("${blemac}")){
                                                topic=topic.replace("${blemac}",gateway.getAddress());
                                            }
                                            cmd=cmd.replaceAll(" ","");
                                            println("Wifi升级发送的指令=" + cmd + "===" + topic);
                                            RabbitMessage rabbitMessage = new RabbitMessage(topic, cmd,gateway.getProject_key());
                                            directExchangeProducer.send(rabbitMessage.toString(), "sendToGateway");
                                }else{
                                    println("应该升级完成");
                                    firmwareTaskHashMap.remove("wifi_"+gateway.getAddress());
                                }
                            }
                        }catch (Exception e){
                            println("error"+e);
                        }
                    }
                    return heartState;
                case state_sta_device_state:
                    type = new TypeToken<ConnectState<ConnectDetail>>() {
                    }.getType();
                    ConnectState<ConnectDetail> connectState = gson.fromJson(jsonRaw.toString(), type);
                    println("连接状态=" + connectState.getData().getDevice_state());
                    return connectState;
            }
        } catch (Exception e) {
            // println("222解析异常=" + jsonRaw.toString());
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
            //   println(scan.getData().getDev_infos()[0].getAddr());
            return scan;
        } catch (Exception e) {
            println("解析扫描上报数据异常");
            return null;
        }
    }


    private String getParamsJson(String type, Gateway_config gatewayConfig, String address,String url) {
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
                    cmd = "{\"pkt_type\": \"command\",\"gw_addr\": \"" + address + "\", \"data\": {\"msgId\": 4,\"cmd\": \"scan_filter_dev_mac\",\"enable\": true,\"start\":\""+scan_filter_dev_mac[0]+"\",\"end\":\""+scan_filter_dev_mac[1]+"\"}}";
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
                //{
            //    "pkt_type": "command",
            //    "gw_addr": "de2106499131",
            //    "data": {
            //        "msgId": 1234,
            //        "cmd": "sys_upgrade_ble",
            //        "url": "http://192.168.1.14:7001/download?path=I124716595Code6595IdeaProject6595engine6595blefirmware6595KLB0012V4.3.1.bin",
            //        "reload_default": false
            //    }
            //}
            case "bleVersion":
                cmd = "{\"pkt_type\":\"command\",\n" +
                        "\"gw_addr\":\"" + address + "\",\n" +
                        "\"data\":{\"msgId\":1234,\n" +
                        "\"cmd\":\"sys_upgrade_ble\",\n" +
                        "\"url\":\"" + url +"\","+
                        "\"reload_default\":false\n" +
                        "}}";
                break;
            case "wifiVersion":
                cmd = "{\"pkt_type\":\"command\",\n" +
                        "\"gw_addr\":\"" + address + "\",\n" +
                        "\"data\":{\"msgId\":1234,\n" +
                        "\"cmd\":\"sys_upgrade_wifi\",\n" +
                        "\"url\":\""+url+"\","+
                        "\"reload_default\":false\n" +
                        "}}";
                break;
            case "setTopic":

                String sub=gatewayConfig.getSub_topic().replace("{blemac}",address).replace("bleMac",address).replace("$","");
                String pub=gatewayConfig.getPub_topic().replace("{blemac}",address).replace("bleMac",address).replace("$","");
                cmd = "{\"pkt_type\":\"command\",\n" +
                        "\"gw_addr\":\"" + address + "\",\n" +
                        "\"data\":{\"msgId\":1234,\n" +
                        "\"cmd\":\"sys_app_server\",\n" +
                        "\"op\":\"set\",\n" +
                        "\"type\":\"MQTT\",\n" +
                        "\"port\":"+check_sheetMap.get(gatewayConfig.getProject_key()).getPort()+",\n" +
                        "\"host\":\"" + check_sheetMap.get(gatewayConfig.getProject_key()).getHost()+ "\",\n" +
                        "\"mqtt\":{\n" +
                        "\"pub\":\"" + pub + "\",\n" +
                        "\"sub\":\"" + sub  + "\",\n" +
                        "\"usr\":\"\",\n" +
                        "\"pw\":\"\",\n" +
                        "\"clientId\":\"" + address + "\",\n" +
                        "\"qos\":1\n" +
                        "}\n" +
                        "}\n" +
                        "}";//、{"pkt_type":"command","gw_addr":"e6e846c73687","data":{"msgId":3,"cmd":"sys_app_server","op":"set","type":"udp","port":7628,"host":"192.168.1.14"}}
        }
        gatewayConfig = null;
        return cmd;


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

}


