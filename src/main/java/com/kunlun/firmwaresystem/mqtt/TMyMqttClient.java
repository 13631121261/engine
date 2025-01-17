package com.kunlun.firmwaresystem.mqtt;

import com.kunlun.firmwaresystem.NewSystemApplication;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;


public class TMyMqttClient {

    private  MqttClient client = null;
    private String content = "Hello World";
    private int qos = 2;
    private String host;
    private int port;
    private String clientId = "Client12_Paal";
    private String sub,pub,user,password;
    public  ExecutorService executorService;
    public   int connect_count=0;
    static int id=0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    public TMyMqttClient(String host, int port, String sub, String pub, int Qos, String user, String password, String project_key) {
         qos=Qos;
         this.pub=pub;
         this.sub=sub;
         this.user=user;
         this.password=password;
        this.host=host;
        this.port=port;

         this.clientId="Push_clientId_"+project_key+System.currentTimeMillis()/1000;
        if(host==null){
            return;
        }
        executorService = Executors.newCachedThreadPool();
        TMyMqttClient(host,port);
    }

   /* public  MyMqttClient regetMyMqttClient(String host,int port) {
        //   executorService = Executors.newCachedThreadPool();
        if (client != null) {
            disConnect();
            client=null;
            new MyMqttClient(host,port);
            return this;
        }else{
          new MyMqttClient(host,port);
            return this;
        }
    }*/
    public void TMyMqttClient(String host,int port) {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            println("地址=tcp://"+host+":"+port);
            client = new MqttClient("tcp://"+host+":"+port, clientId, persistence);
            println("地址="+  client.getServerURI());
            println("地址="+  client);
        } catch (MqttException me) {
            println("reason " + me.getReasonCode());
            println("msg " + me.getMessage());
            println("loc " + me.getLocalizedMessage());
            println("cause " + me.getCause());
            println("excep " + me);
        }
    }
    public boolean start() {
        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            if(password!=null&&password.length()>0){
                connOpts.setPassword(password.toCharArray());
            }
            if(user!=null&&user.length()>0){
                connOpts.setUserName(user);
            }
            connOpts.setCleanSession(true);
            println("客户端="+host);

            // 建立连接
             println("Connecting to broker: " + client);
            client.connect(connOpts);

            println("转发mqtt + client已连接");
            connect_count=0;
        } catch (MqttException e) {
            println("重连="+host);
            connect_count++;
            if(connect_count>5){
                println("启动异常,不在重连");
            }
            else{
                println("启动异常,尝试重连计数="+connect_count);
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

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port=port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    //发布消息
    public void sendToTopic(String topic, String msg) {
        try {
            id++;
            if(id>65535){
                id=0;
            }
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setId(id);
            message.setQos(qos);
            //println("发布主题消息"+topic);
            if (topic == null || topic.length() == 0) {
                println("主题有问题，return返回" + topic);
                return;
            }
            client.publish(topic, message);
        } catch (Exception e) {
            println("发布消息异常 Topic=" + topic + "  meg=" + msg);
        }
    }



    public void disConnect() {
        try {

            client.disconnect();
            client.close();
            client=null;
        } catch (MqttException e) {
            println("断开MQTT连接异常=" + e.getMessage());
        }
    }

}
