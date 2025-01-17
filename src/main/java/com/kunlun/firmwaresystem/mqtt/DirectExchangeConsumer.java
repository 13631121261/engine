package com.kunlun.firmwaresystem.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.kunlun.firmwaresystem.MyWebSocket;
import com.kunlun.firmwaresystem.MyWebSocketTag;
import com.kunlun.firmwaresystem.MyWebSocket_debug;
import com.kunlun.firmwaresystem.NewSystemApplication;
import com.kunlun.firmwaresystem.entity.Check_sheet;
import com.kunlun.firmwaresystem.entity.Gateway_config;
import com.kunlun.firmwaresystem.entity.Push_device;
import com.kunlun.firmwaresystem.entity.Rules;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.Push;

@Component
public class DirectExchangeConsumer {
    MyMqttClient myMqttClient1;
    MyWebSocket webSocket = MyWebSocket.getWebSocket();
    MyWebSocketTag webSockettag = MyWebSocketTag.getWebSocket();
    MyWebSocket_debug webSocket_debug = MyWebSocket_debug.getWebSocket();
    DatagramSocket ds;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DirectExchangeConsumer() {
        try {
            ds = new DatagramSocket();
        } catch (Exception e) {

        }
    }

    @RabbitListener(queues = "sendToGateway")
    @RabbitHandler
    public void getQueue1Message(String msg) {
            if (NewSystemApplication.check_sheetMap == null) {
                return;
            }
            for (Map.Entry<String, MyMqttClient> entry : myMqttClientMap.entrySet()) {
                myMqttClient1= entry.getValue();
               // println("key="+entry.getKey());
                if (myMqttClient1 == null||!myMqttClient1.getStatus()) {
                    println("2222myMqttClient=null");
                    return;
                }
                //全部的下发给网关的消息都在这里集中下发。
                JSONObject jsonObject = JSONObject.parseObject(msg);
                String topic = jsonObject.getString("pubTopic");
                String data = jsonObject.getString("msg");
                if (topic != null && data != null) {
                    myMqttClient1.sendToTopic(topic, data, 11);
                }
              //  JSONObject jsonObject1 = JSONObject.parseObject(data);
                //     println(sdf.format(new Date())+"发给网关关关了"+"id="+jsonObject1.getInteger("id"));
            }

    }

    //发给网页websocket
    @RabbitListener(queues = "sendtoHtml")
    @RabbitHandler
    public void getQueue3Message(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String key = jsonObject.getString("project_key");
        String data = jsonObject.getString("msg");

        if (key != null && data != null) {
            webSocket.sendData(key, data);
        }
    }
    //发给网页websocket
    @RabbitListener(queues = "sendtoMap")
    @RabbitHandler
    public void getQueue6Message(String msg) {
       // println("这里"+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String key = jsonObject.getString("project_key");
        String data = jsonObject.getString("msg");

        if (key != null && data != null) {

            webSockettag.sendData(key, data);
        }
    }

    //发给网页websocket
    @RabbitListener(queues = "sendtoMap_debug")
    @RabbitHandler
    public void getQueue7Message(String msg) {
        // println("这里"+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String key = jsonObject.getString("project_key");
        String data = jsonObject.getString("msg");

        if (key != null && data != null) {

            webSocket_debug.sendData(key, data);
        }
    }

    @RabbitListener(queues = "mqtt_topic")
        @RabbitHandler
        public void getQueue5Message(String msg) {

                JSONObject jsonObject = JSONObject.parseObject(msg);
                String topic = jsonObject.getString("pubTopic");
                String project_key=jsonObject.getString("project_key");
                if(myMqttClientMap !=null){
                    myMqttClient1=myMqttClientMap.get(project_key);
                    if(myMqttClient1!=null){
                        myMqttClient1.addSubTopic(topic);
                    }
                    return;
                }

        }

    @RabbitListener(queues = "transpond")
    @RabbitHandler
    public void getQueue2Message(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String project_key = jsonObject.getString("pubTopic");
        String data = jsonObject.getString("msg");
        if(NewSystemApplication.gatewayConfigMap!=null){
            Gateway_config gatewayConfig = NewSystemApplication.gatewayConfigMap.get(project_key);
            // println("准备转发");
            if (gatewayConfig != null) {
                if (rulesMap == null) {
                    //   println("规则都是空的");
                    return;
                }
                Rules rules = rulesMap.get(gatewayConfig.getRules_key());
                if (rules == null) {
                    //  println("没有绑定转发规则");
                    return;
                }
                if (rules.getType() == 1) {
                    sendUdp(data, rules.getServer(), rules.getPort());
                } else if (rules.getType() == 2) {
                    httpPost(rules.getServer(), data);
                }
            }

        }else{
            println("项目参数一直为空");
        }

    }

    @RabbitListener(queues = Push)
    @RabbitHandler
    public void getQueue4Message(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        jsonObject=JSONObject.parseObject(jsonObject.getString("msg"));
        String device_type=jsonObject.getString("device_type");
        String push_type=jsonObject.getString("push_type");
        Push_device device=new Push_device();
        device.setAddress(jsonObject.getString("address"));
        String bt=jsonObject.getString("bt");
        Double x=jsonObject.getDouble("x");
        Double y=jsonObject.getDouble("y");
        if(x!=null&&y!=null){
            device.setX(x);
            device.setY(y);
            String map_key=jsonObject.getString("map_key");
            device.setMap_key(map_key);
        }
        device.setDevice_type(device_type);
        Long last_time=jsonObject.getLong("last_time");
        if(last_time!=null){
            device.setLast_time(last_time);
        }
      else{
            System.out.println("异常时间");
        }
        device.setBt(bt);
        device.setPush_type(push_type);
        String project_key=jsonObject.getString("project_key");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("data",device);
        String data=jsonObject1.toString();
        if(!project_key.isEmpty()&&check_sheetMap!=null){
            Check_sheet check_sheet= check_sheetMap.get(project_key);
            if(check_sheet!=null){
                int type=check_sheet.getRelay_type();
                if(type==0){
                    sendUdp(data,check_sheet.getUdp(),check_sheet.getR_port());
                }else if(type==1){
                //    println("MQTT");
                    if(t_myMqttClientMap==null){
                        return;
                    }
                    TMyMqttClient client= t_myMqttClientMap.get(project_key);
                    if(client!=null&&client.getStatus()){
                     //   println("推送开始");
                        client.sendToTopic(check_sheet.getR_pub(),JSONObject.toJSONString(device));
                    }
                }
            }
        }
    }
    private void sendUdp(String raw, String address, int port) {
        try {
            //创建数据包对象，封装要发送的数据，接受端IP,端口
            byte[] data = raw.getBytes();
            //创建InetAddress对象，封装自己的IP地址
            InetAddress inet = InetAddress.getByName(address);
            DatagramPacket dp = new DatagramPacket(data, data.length, inet, port);
            //创建DatagramSocket对象，数据包的发送和接受对象
            //调用ds对象的方法send，发送数据包
            ds.send(dp);
            println("UDP发送完成");
        } catch (Exception e) {
            println("sendUdp转发异常"+e);
        }
    }
    private void sendMqtt(String raw, String address, int port) {
        try {
            //创建数据包对象，封装要发送的数据，接受端IP,端口
            byte[] data = raw.getBytes();
            //创建InetAddress对象，封装自己的IP地址
            InetAddress inet = InetAddress.getByName(address);
            DatagramPacket dp = new DatagramPacket(data, data.length, inet, port);
            //创建DatagramSocket对象，数据包的发送和接受对象
            //调用ds对象的方法send，发送数据包
            ds.send(dp);
            println("UDP发送完成");
        } catch (Exception e) {
            println("sendMqtt转发异常"+e);
        }
    }
    private void httpPost(String url, String entity) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            StringEntity se = new StringEntity(entity, "UTF-8");
            se.setContentType("application/json");
            httpPost.setEntity(se);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity1 = response.getEntity();
            String resStr = null;
            if (entity1 != null) {
                resStr = EntityUtils.toString(entity1, "UTF-8");
            }
            httpClient.close();
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}