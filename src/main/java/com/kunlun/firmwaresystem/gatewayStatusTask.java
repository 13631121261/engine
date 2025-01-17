package com.kunlun.firmwaresystem;

import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.mqtt.MyMqttClient;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;
import com.kunlun.firmwaresystem.sql.Alarm_Sql;
import com.kunlun.firmwaresystem.sql.Gateway_sql;
import com.kunlun.firmwaresystem.util.StringUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;

@Component
public class gatewayStatusTask {
    static int runcount = 0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");//设置日期格式1

    @Scheduled(cron = "*/59 * * * * ?")
    public void execute() throws Exception {
        int n=0;
        //  println(df.format(new Date()));// new Date()为获取当前系统时间
        checkMqtt();
        long now = System.currentTimeMillis()/1000;
        long last;
        runcount++;
         println("网关定时任务" + runcount + "  id=" + Thread.currentThread().getId());
      //  writeLog("网关定时任务=" + runcount);
        for (String key : gatewayMap.keySet()) {
            //    println("redis="+redisUtil);

        //    Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + key);
            Gateway gateway = (Gateway)    GatewayMap.get(key);
            //println("自动更新网关="+gateway);
            if(gateway==null){
                println("此网关是空="+key);
                continue;
            }else if(gateway.getLasttime()==0){
                gateway.setOnline_txt("离线");
                gateway.setOnline(0);
                redisUtil.set(redis_key_gateway + key,gateway);
                StringUtil.sendGatewayPush(gateway,0);
                new Gateway_sql().updateGateway(gatewayMapper, gateway);
                continue;
            }
            last=gateway.getLasttime();

            Integer firstTime = (Integer) redisUtil.get(redis_key_gateway_onLine_time + gateway.getAddress());
            if(firstTime==null){
                firstTime=0;
            }
            //println("网关="+key+"  时间差="+(now-last)/1000);
            if ((now - last) > 30) {
                long onLineTime = last - firstTime;
                //   redisUtil.set(redis_key_gateway_onLine_time_count+gateway.getAddress(),onLineTime/1000);
                gateway.setOnlinetime(onLineTime );
                if (gateway.getOnline() == 1) {
                    n++;
                   // println("网关离线="+gateway.getAddress()+"  IP="+gateway.getWan_ip());
             /*       writeLog("网关：" + gateway.getAddress() + "离线 最后在线时间为" + gateway.getLasttime());
                    writeLog("网关：" + gateway.getAddress() + " 在线时长" + onLineTime  / 86400 + "天" + onLineTime % 86400 / 3600 + "时" + onLineTime  % 86400 % 3600 / 60 + "分" + onLineTime % 86400 % 3600 % 60 + "秒");
                    writeLog("网关：" + gateway.getAddress() + "一共接收包数量=" + redisUtil.get(redis_key_gateway_revice_count + gateway.getAddress()));
             */   }
                gateway.setOnline_txt("离线");
                redisUtil.set(redis_key_project_sys + gateway.getAddress(), "error");
                if(gateway.getOnline()!=0){
                    Alarm_Sql alarm_sql=new Alarm_Sql();
                    alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_offline, Alarm_object.gateway,gateway.getMap_key(),0,"",0,0,"",gateway.getName(),gateway.getAddress(),gateway.getProject_key(),gateway.getLasttime()));
                }
                StringUtil.sendGatewayPush(gateway,0);
                //gateway.setOnline(0);
                //println("离线");
            } else {
                long onLineTime = now - firstTime;

                //   redisUtil.set(redis_key_gateway_onLine_time_count+gateway.getAddress(),onLineTime/1000);
                gateway.setOnlinetime(onLineTime );
                //   println("当前时间="+df.format(now));
                // println("首次时间="+firstTime);
                // println("在线时间="+onLineTime/1000);
            }
            redisUtil.set(redis_key_gateway + key, gateway);
           // println("自动更新4"+gateway);
            new Gateway_sql().updateGateway(gatewayMapper, gateway);
        }
      //  println("一共离线网关数量="+n);
        //记录网关离线在线的项目状态
        //  println("网关长度"+gatewayMap.size());
        for (String config_key : gatewayConfigMap.keySet()) {
           //  println("当前的项目="+config_key);
            int count =0;
            int onLine = 0;

            for (String gateway_address : gatewayMap.keySet()) {
                Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + gateway_address);
                //  println(gateway_address+"网关="+gateway.getConfig_key());

                if (gateway!=null&&gateway.getConfig_key()!=null&&gateway.getConfig_key().equals(config_key)) {
                   //     println("符合项目的网关"+config_key);
                    count++;
                    if (gateway.getOnline() == 1) {
                        onLine++;
                    }
                    else{
                        // println("输出不在线网关="+gateway_address);
                    }
                }
                //更新到缓存
                //  String status = onLine + "/" + count;
                //redisUtil.set(redis_key_gatewayConfig_onLine+config_key, status, 120);
            }
          /*  if(gatewayMap==null||gatewayMap.size()==0){
                println("网关列表为空，重新获取=");
                writeLog("网关列表为空，重新获取");
                gatewayMap=new Gateway_sql().getAllGateway(redisUtil,gatewayMapper);
            }*/
            String status = onLine + "/" + count;
            redisUtil.set(redis_key_gatewayConfig_onLine+config_key, status, 120);


        }
       // Lock lock = new ReentrantLock();
       // lock.lock();//上锁

        getAllGatewayConfig();

    }




    public void checkMqtt() {
        for (Map.Entry<String, MyMqttClient> entry : myMqttClientMap.entrySet()) {
            if (entry.getValue() != null) {
                if (!entry.getValue().getStatus()) {

                    println("mqtt执行重连哦");
                    entry.getValue().start();
                }
            }

        }
    }
        //每三个定时周期，获取一次网关的配置，保持和服务器同步。
        private void getAllGatewayConfig () {

            if (runcount % 2 == 0) {

                println("每定时两次任务，同步一次网关配置" + runcount);
                // println("每两次次定时运行一次" + runcount / 2);
                if (runcount >= 1000) {
                    runcount = 0;
                }
           /* String sys_hb="{\"pkt_type\":\"command\",\n" +
                    "\"gw_addr\":\"%s\",\n" +
                    "\"data\":{\"msgId\":1234,\n" +
                    "\"cmd\":\"sys_hb\",\n" +
                    "\"op\":\"set\",\n" +
                    "\"value\":60\n" +
                    "}\n" +
                    "}";
            //   println("ddd="+sys_hb);
            String getScanParams = "{\"pkt_type\": \"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 1234, \"cmd\": \"scan_filter_get\"}}";
            String getFilterParams = "{\"pkt_type\": \"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 1234, \"cmd\": \"scan_params_get\"}}";
            String getAdvParams = "{\"pkt_type\":\"command\",\n" +
                    "  \"gw_addr\" : \"%s\",\n" +
                    "  \"data\" : {\n" +
                    "    \"msgId\" : 1234,\n" +
                    "    \"cmd\" : \"adv_params_get\"\n" +
                    "  }\n" +
                    "}";
            String getBleVersion = "{\"pkt_type\":\"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 1234, \"cmd\": \"sys_get_ver\"}}";
            String getWifiVersion = "{\"pkt_type\":\"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 1234, \"cmd\": \"sys_get_wifi_ver\"}}";
            String getTopic = "{\"pkt_type\":\"command\",\n" +
                    "\"gw_addr\":\"%s\",\n" +
                    "\"data\":{\"msgId\":1234,\n" +
                    "\"cmd\":\"sys_app_server\",\n" +
                    "\"op\":\"get\"\n" +
                    "}\n" +
                    "}";*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //  Map<String, String> tt = new HashMap<>();
                 /*   for (String key : projectMap.keySet()) {
                        tt.put(key, projectMap.get(key).getSub_topic());
                    }
                    tt.put("SrvData", "SrvData");*/
                        try {
                       /* for (String project_key : gatewayConfigMap.keySet()) {
                            Gateway_config gatewayConfig = gatewayConfigMap.get(project_key);
                            Gateway gateway;
                            for (String gatewayAddress : gatewayMap.keySet()) {
                                gateway = (Gateway) redisUtil.get(redis_key_gateway + gatewayAddress);
                              *//*  if(gateway.getOnline()==0){
                                    continue;
                                }*//*

                                if (gatewayConfig.getConfig_key().equals(gateway.getConfig_key())) {
                                    // String topic = gateway.getSub_topic();
                                    //下发查询的时候，默认配置项主题为准，因为一般来说，会先更新，然后查询
                                    String topic=gatewayConfig.getSub_topic();
                                    if (!topic.contains(gatewayAddress)) {
                                        topic = topic.replace("{blemac}",gatewayAddress);
                                    }
                                    else{
                                        topic = topic+"/"+gatewayAddress;
                                    }
                                    //   println("下发主题="+topic);
                                    Thread.sleep(2000);
                                    //  gatewayAddress="ffffffffffff";
                                    RabbitMessage rabbitMessage0 = new RabbitMessage(topic, String.format(sys_hb, "ffffffffffff"));
                                    directExchangeProducer.send(rabbitMessage0.toString(), "sendToGateway");
                                    Thread.sleep(2000);
                                    //  gatewayAddress="ffffffffffff";
                                    RabbitMessage rabbitMessage = new RabbitMessage(topic, String.format(getScanParams, "ffffffffffff"));
                                    directExchangeProducer.send(rabbitMessage.toString(), "sendToGateway");
                                    Thread.sleep(2000);
                                    RabbitMessage rabbitMessage1 = new RabbitMessage(topic, String.format(getFilterParams, "ffffffffffff"));
                                    directExchangeProducer.send(rabbitMessage1.toString(), "sendToGateway");
                                    Thread.sleep(2000);
                                    RabbitMessage rabbitMessage2 = new RabbitMessage(topic, String.format(getAdvParams, "ffffffffffff"));
                                    directExchangeProducer.send(rabbitMessage2.toString(), "sendToGateway");
                                    Thread.sleep(2000);
                                    RabbitMessage rabbitMessage3 = new RabbitMessage(topic, String.format(getBleVersion, "ffffffffffff"));
                                    directExchangeProducer.send(rabbitMessage3.toString(), "sendToGateway");
                                    Thread.sleep(2000);
                                    RabbitMessage rabbitMessage4 = new RabbitMessage(topic, String.format(getWifiVersion, "ffffffffffff"));
                                    directExchangeProducer.send(rabbitMessage4.toString(), "sendToGateway");
                                    Thread.sleep(2000);
                                    RabbitMessage rabbitMessage5 = new RabbitMessage(topic, String.format(getTopic, "ffffffffffff"));
                                    directExchangeProducer.send(rabbitMessage5.toString(), "sendToGateway");
                                }
                            }
                        }*/

                            String getScanParams = "{\"pkt_type\": \"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 65535, \"cmd\": \"scan_filter_get\"}}";
                            String sys_hb = "{\"pkt_type\":\"command\"," +
                                    "\"gw_addr\":\"%s\"," +
                                    "\"data\":{\"msgId\":65535," +
                                    "\"cmd\":\"sys_hb\"," +
                                    "\"op\":\"set\"," +
                                    "\"value\":30" +
                                    "}" +
                                    "}";
                            String getFilterParams = "{\"pkt_type\": \"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 65535, \"cmd\": \"scan_params_get\"}}";
                            String getAdvParams = "{\"pkt_type\":\"command\"," +
                                    "  \"gw_addr\" : \"%s\"," +
                                    "  \"data\" : {" +
                                    "    \"msgId\" : 65535," +
                                    "    \"cmd\" : \"adv_params_get\"" +
                                    "  }" +
                                    "}";
                            String getBleVersion = "{\"pkt_type\":\"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 65535, \"cmd\": \"sys_get_ver\"}}";
                            String getWifiVersion = "{\"pkt_type\":\"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 65535, \"cmd\": \"sys_get_wifi_ver\"}}";
                            String getTopic = "{\"pkt_type\":\"command\"," +
                                    "\"gw_addr\":\"%s\"," +
                                    "\"data\":{\"msgId\":65535," +
                                    "\"cmd\":\"sys_app_server\"," +
                                    "\"op\":\"get\"" +
                                    "}" +
                                    "}";
                            String network_status = "{\"pkt_type\": \"command\", \"gw_addr\": \"%s\", \"data\": {\"msgId\": 65535, \"cmd\": \"network_status\"}}";
                            //针对最新版本的网关，只需要发一下查询
                            String topic = "AlphaCmd";
                            Thread.sleep(2000);
                            String gatewayAddress = "ffffffffffff";
                            RabbitMessage rabbitMessage = new RabbitMessage(topic, String.format(getScanParams, gatewayAddress),"");
                            directExchangeProducer.send(rabbitMessage.toString(), "sendToGateway");
                            Thread.sleep(2000);
                            RabbitMessage rabbitMessage0 = new RabbitMessage(topic, String.format(sys_hb, gatewayAddress),"");
                            directExchangeProducer.send(rabbitMessage0.toString(), "sendToGateway");
                            Thread.sleep(2000);
                            RabbitMessage rabbitMessage1 = new RabbitMessage(topic, String.format(getFilterParams, gatewayAddress),"");
                            directExchangeProducer.send(rabbitMessage1.toString(), "sendToGateway");
                            Thread.sleep(2000);
                            RabbitMessage rabbitMessage2 = new RabbitMessage(topic, String.format(getAdvParams, gatewayAddress),"");
                            directExchangeProducer.send(rabbitMessage2.toString(), "sendToGateway");
                            Thread.sleep(2000);
                            RabbitMessage rabbitMessage3 = new RabbitMessage(topic, String.format(getBleVersion, gatewayAddress),"");
                            directExchangeProducer.send(rabbitMessage3.toString(), "sendToGateway");
                            Thread.sleep(2000);
                            RabbitMessage rabbitMessage4 = new RabbitMessage(topic, String.format(getWifiVersion, gatewayAddress),"");
                            directExchangeProducer.send(rabbitMessage4.toString(), "sendToGateway");
                            Thread.sleep(2000);
                            RabbitMessage rabbitMessage5 = new RabbitMessage(topic, String.format(getTopic, gatewayAddress),"");
                            directExchangeProducer.send(rabbitMessage5.toString(), "sendToGateway");
                            Thread.sleep(2000);
                            RabbitMessage rabbitMessage6 = new RabbitMessage(topic, String.format(network_status, gatewayAddress),"");
                            directExchangeProducer.send(rabbitMessage6.toString(), "sendToGateway");
                        } catch (Exception e) {
                            println("定时获取状态异常" + e.getMessage());
                        }
                    }

                }).start();
            }/*else if(runcount%3==0) {
           // Beacon_Sql beacon_sql=new Beacon_Sql();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(beaconsMap!=null){
                        for(Map.Entry<String, Beacon> entry : beaconsMap.entrySet()){
                            String mapKey = entry.getKey();
                            Beacon mapValue = entry.getValue();
                       //     println("循环输出"+mapKey+":"+mapValue);
                            List<Record> list=  (ArrayList<Record>)redisUtil.get(redis_key_tag_map+mapKey);
                          if(list!=null&&list.size()>0){
                           *//*   for(Record record:list){
                                  println("实际收到="+record.toString());
                              }*//*
                          }
                        }
                    }
                    else{
                        println("异常检测");
                    }
                }
            }).start();
        }
*/
        }

         public static void writeLog (String log){
             println(log);
      /* String fileName = paths + "log.txt";
        try {
            //  println(log);

            //println("文件名称=" + fileName);
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try {
                FileWriter writer = new FileWriter(fileName, true);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format.format(new Date());
                writer.write("\n\t" + time);
                writer.write("\n\t" + log);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            println(e.getMessage()+"文件夹创建失败"+fileName);
        }*/
        }


}