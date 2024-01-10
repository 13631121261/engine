package com.kunlun.firmwaresystem.mqtt;

import com.kunlun.firmwaresystem.NewSystemApplication;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.kunlun.firmwaresystem.NewSystemApplication.check_sheetMap;

public class MyMqttClient {
    public static MyMqttClient myclient = null;
    private static MqttClient client = null;
    private String content = "Hello World";
    private int qos = 2;
    //   private String broker = "tcp://120.77.232.76:1883";

    private String clientId = "Client_Platform";
    private MessageCallback messageCallback;
    public static ExecutorService executorService;
    public  static int connect_count=0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    public static MyMqttClient getMyMqttClient(String host,String port) {
        if(host==null){
            return null;
        }
        executorService = Executors.newCachedThreadPool();
        if (myclient != null) {
            return myclient;
        } else {
            myclient = new MyMqttClient(host,port);
            return myclient;
        }
    }
    public static MyMqttClient getMyMqttClient() {
            if(check_sheetMap==null){
                return null;
            }

        executorService = Executors.newCachedThreadPool();
        if (myclient != null) {
            return myclient;
        } else {
            System.out.println("check_sheetMap.get(\"admin\")"+check_sheetMap);
            myclient = new MyMqttClient(check_sheetMap.get("admin").getHost(),check_sheetMap.get("admin").getPort());
            return myclient;
        }
    }
    public static MyMqttClient regetMyMqttClient(String host,String port) {
        //   executorService = Executors.newCachedThreadPool();
        if (myclient != null) {
            myclient.disConnect();
            myclient=null;
            myclient = new MyMqttClient(host,port);
            return myclient;
        }else{
            myclient = new MyMqttClient(host,port);

            return myclient;
        }
    }
    private MyMqttClient(String host,String port) {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            clientId = clientId + "_" + System.currentTimeMillis() + "";
            System.out.println("地址=tcp://"+host+":"+port);
            client = new MqttClient("tcp://"+host+":"+port, clientId, persistence);
            System.out.println("地址="+  client.getServerURI());

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public boolean start() {
        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("Client_Platform");
            connOpts.setPassword("emqx_test_password".toCharArray());
            // 保留会话
            connOpts.setCleanSession(true);
            // 设置回调
            client.setCallback(new MessageCallback(myclient));
            // 建立连接
            System.out.println("Connecting to broker: " + check_sheetMap.get("admin").getHost());
            client.connect(connOpts);
            System.out.println("Connected");
            addSubTopic("GwData");
            addSubTopic("/cle/mqtt");
            addSubTopic("AlphaRsp");
            addSubTopic("connected");
            addSubTopic("disconnected");
            if(check_sheetMap.get("admin").getSub()!=null&&check_sheetMap.get("admin").getSub().length()>0){
                String sub[]=check_sheetMap.get("admin").getSub().split(",");
                for(String subs:sub){
                    if(subs!=null&&subs.length()>0){
                        addSubTopic(subs);
                    }
                }
            }
            // addSubTopic(check_sheet.getSub());
            for (String key : NewSystemApplication.gatewayConfigMap.keySet()) {
                System.out.println("订阅主题=" + NewSystemApplication.gatewayConfigMap.get(key).getPub_topic());
                addSubTopic(NewSystemApplication.gatewayConfigMap.get(key).getPub_topic());
            }
            connect_count=0;
        } catch (MqttException e) {
            connect_count++;
            if(connect_count>5){
                System.out.println("启动异常,不在重连");
            }
            else{
                System.out.println("启动异常,尝试重连计数="+connect_count);
                start();
            }

            return false;
        }
        return true;
    }
public boolean getStatus(){
        if(client!=null){
            return client.isConnected();
        }else{
            return  false;
        }
}
    //主题订阅
    public boolean addSubTopic(String topic) {
        try {
            if(topic==null){
                return false;
            }
            else if(topic.contains("${blemac}")){
                topic=topic.replace("${blemac}","#");
            }
            else if(topic.contains("{blemac}")){
                topic=topic.replace("{blemac}","#");
            }
            System.out.println("实际订阅主题=" + topic);
            client.subscribe(topic);
            return true;
        } catch (MqttException e) {
            System.out.println("订阅主题异常 Topic=" + topic);
            return false;
        }
    }

    //发布消息
    public void sendToTopic(String topic, String msg, int id) {
        try {
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setId(id);
            message.setQos(qos);
            //System.out.println("发布主题消息"+topic);
            if (topic == null || topic.length() == 0) {
                System.out.println("主题有问题，return返回" + topic);

                return;
            }
            //  System.out.println(df.format(new Date())+"真实发布 Topic=" + topic + "  meg=" + msg);
            client.publish(topic, message);
            //  System.out.println("Message published");
        } catch (Exception e) {
            System.out.println("发布消息异常 Topic=" + topic + "  meg=" + msg);
        }
    }

    /*   //发布消息
       public void sendToTopic(String topic, String msg, MqttStatusCallback mqttStatusCallback){
           try {
               messageCallback.setMqttCallback(mqttStatusCallback);
               MqttMessage message = new MqttMessage(msg.getBytes());
               message.setQos(qos);
               client.publish(topic, message);
               System.out.println("Message published");
           }catch(MqttException e){
               System.out.println("发布消息异常 Topic="+topic+"  meg="+msg);
           }
       }*/
    public void disConnect() {
        try {
            client.disconnect();
            client.close();
        } catch (MqttException e) {
            System.out.println("断开MQTT连接异常=" + e.getMessage());
        }
    }

}
