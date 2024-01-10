package com.kunlun.firmwaresystem.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.kunlun.firmwaresystem.MyWebSocket;
import com.kunlun.firmwaresystem.MyWebSocketTag;
import com.kunlun.firmwaresystem.NewSystemApplication;
import com.kunlun.firmwaresystem.entity.Gateway_config;
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

import static com.kunlun.firmwaresystem.NewSystemApplication.rulesMap;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.Push;

@Component
public class DirectExchangeConsumer {
    MyMqttClient myMqttClient;
    MyWebSocket webSocket = MyWebSocket.getWebSocket();
    MyWebSocketTag webSockettag = MyWebSocketTag.getWebSocket();
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
        // System.out.println("下发消息log");
        if(myMqttClient==null){
            if(NewSystemApplication.check_sheetMap==null){
                    return;
            }
            //System.out.println("1111myMqttClient=null");
            //System.out.println(NewSystemApplication.check_sheetMap);
           // System.out.println(NewSystemApplication.check_sheetMap.get("admin"));
            myMqttClient = MyMqttClient.getMyMqttClient(NewSystemApplication.check_sheetMap.get("admin").getHost(),NewSystemApplication.check_sheetMap.get("admin").getPort());
        }
        if(myMqttClient==null){
            System.out.println("2222myMqttClient=null");
            return;
        }
        //全部的下发给网关的消息都在这里集中下发。
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String topic = jsonObject.getString("pubTopic");
        String data = jsonObject.getString("msg");
        if (topic != null && data != null) {
            myMqttClient.sendToTopic(topic, data, 11);
        }   JSONObject jsonObject1=JSONObject.parseObject(data);
   //     System.out.println(sdf.format(new Date())+"发给网关关关了"+"id="+jsonObject1.getInteger("id"));

    }

    //发给网页websocket
    @RabbitListener(queues = "sendtoHtml")
    @RabbitHandler
    public void getQueue3Message(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String key = jsonObject.getString("pubTopic");
        String data = jsonObject.getString("msg");

        if (key != null && data != null) {
            webSocket.sendData(key, data);
        }
    }
    //发给网页websocket
    @RabbitListener(queues = "sendtoMap")
    @RabbitHandler
    public void getQueue6Message(String msg) {
       // System.out.println("这里"+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String key = jsonObject.getString("pubTopic");
        String data = jsonObject.getString("msg");

        if (key != null && data != null) {

            webSockettag.sendData(key, data);
        }
    }

        @RabbitListener(queues = "mqtt_topic")
        @RabbitHandler
        public void getQueue5Message(String msg) {
          //  System.out.println("收到订阅主题"+msg);
            if(myMqttClient!=null){
               // System.out.println("收到订阅主题"+msg);
                JSONObject jsonObject = JSONObject.parseObject(msg);
                String topic = jsonObject.getString("msg");
                myMqttClient.addSubTopic(topic);
            }else{
                myMqttClient = MyMqttClient.getMyMqttClient(NewSystemApplication.check_sheetMap.get("admin").getHost(),NewSystemApplication.check_sheetMap.get("admin").getPort());
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
            // System.out.println("准备转发");
            if (gatewayConfig != null) {
                if (rulesMap == null) {
                    //   System.out.println("规则都是空的");
                    return;
                }
                Rules rules = rulesMap.get(gatewayConfig.getRules_key());
                if (rules == null) {
                    //  System.out.println("没有绑定转发规则");
                    return;
                }
                if (rules.getType() == 1) {
                    sendUdp(data, rules.getServer(), rules.getPort());
                } else if (rules.getType() == 2) {
                    httpPost(rules.getServer(), data);
                }
            }

        }else{
            System.out.println("项目参数一直为空");
        }

    }

    @RabbitListener(queues = Push)
    @RabbitHandler
    public void getQueue4Message(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String udp = jsonObject.getString("udp");
        String data = jsonObject.getString("msg");
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        //udp  192.168.1.1:5656
        System.out.println(sdf.format(new Date())+"发给UDP"+"id="+jsonObject1.getInteger("id"));
        sendUdp(data,udp.split(":")[0],Integer.parseInt(udp.split(":")[1]));
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
            System.out.println("UDP发送完成");
        } catch (Exception e) {
            System.out.println("转发异常");
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