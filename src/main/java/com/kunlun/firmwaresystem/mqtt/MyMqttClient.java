package com.kunlun.firmwaresystem.mqtt;

import com.kunlun.firmwaresystem.NewSystemApplication;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;


public class MyMqttClient {

    private MqttClient client = null;
    private String content = "Hello World";
    private int qos = 2;
    private String host;
    private int port;
    private String clientId = "ClientID";
    private MessageCallback messageCallback;
    private String sub,pub,user,password;
    public  ExecutorService executorService;
    public   int connect_count=0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss 000");//设置日期格式
    public   MyMqttClient(String host,int port,String sub,String pub,int Qos,String user,String password,String project_key) {
         qos=Qos;
         this.pub=pub;
         this.sub=sub;
         this.user=user;
         this.password=password;
        this.host=host;
        this.port=port;

         this.clientId=clientId+"_"+project_key+System.currentTimeMillis()/1000;
        if(host==null){
            return;
        }
        executorService = Executors.newCachedThreadPool();
         MyMqttClient1(host,port);
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
    public void reconnect() throws MqttException {
        client.reconnect();
    }
    public void MyMqttClient1(String host,int port) {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            println("地址=tcp://"+host+":"+port);
            client = new MqttClient("tcp://"+host+":"+port, clientId, persistence);
            println("地址="+  client.getServerURI());
            println("地址ID="+  clientId);
        } catch (MqttException me) {
            println("reason " + me.getReasonCode());
            println("msg " + me.getMessage());
            println("loc " + me.getLocalizedMessage());
            println("cause " + me.getCause());
            println("excep " + me);
            me.printStackTrace();
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean start() {
        try {
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            if(password!=null&&password.length()>0){
                connOpts.setPassword(password.getBytes());
            }
            if(user!=null&&user.length()>0){
                connOpts.setUserName(user);
            }
            connOpts.setKeepAliveInterval(60);
            println("客户端="+host);
            // 设置回调
            MessageCallback c=new  MessageCallback(this);
            println("回调="+c);
            client.setCallback(c);
            // 建立连接
          println("Connecting to broker: " + client);
            client.connect(connOpts);
            println("项目 MQTT 已连接");
            if(sub!=null){
                addSubTopic(sub);
            }
          //  addSubTopic("/cle/mqtt");
            addSubTopic("GwData");
            addSubTopic("GwData12");
            addSubTopic("AlphaRsp");
            addSubTopic("connected");
            addSubTopic("disconnected");
            if(sub!=null&&sub.length()>0){
                String subs[]=sub.split(",");
                for(String sub1:subs){
                    if(sub1!=null&&sub1.length()>0){
                        addSubTopic(sub1);
                    }
                }
            }
            // addSubTopic(check_sheet.getSub());
            for (String key : NewSystemApplication.gatewayConfigMap.keySet()) {
                println("订阅主题=" + NewSystemApplication.gatewayConfigMap.get(key).getPub_topic());
                addSubTopic(NewSystemApplication.gatewayConfigMap.get(key).getPub_topic());
            }
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
            println("实际订阅主题=" + topic);
            if(topic.equals("#")){
                return false;
            }
            client.subscribe(topic,0);
            return true;
        } catch (MqttException e) {
            println("订阅主题异常 Topic=" + topic);
            return false;
        }
    }
    //发布消息
    public void sendToTopic(String topic, String msg, int id) {
        try {
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setId(id);
            message.setQos(qos);
            //println("发布主题消息"+topic);
            if (topic == null || topic.length() == 0) {
                println("主题有问题，return返回" + topic);
                return;
            }
           //  println(df.format(new Date())+"真实发布 Topic=" + topic + "  meg=" + msg);
            client.publish(topic, message);
            //  println("Message published");
        } catch (Exception e) {
            println("发布消息异常 Topic=" + topic + "  meg=" + msg);
        }
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setConnect_count(int connect_count) {
        this.connect_count = connect_count;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDf(SimpleDateFormat df) {
        this.df = df;
    }

    public MqttClient getClient() {
        return client;
    }

    public String getContent() {
        return content;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getClientId() {
        return clientId;
    }

    public MessageCallback getMessageCallback() {
        return messageCallback;
    }

    public void setMessageCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public String getSub() {
        return sub;
    }

    public String getPub() {
        return pub;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public int getConnect_count() {
        return connect_count;
    }

    public SimpleDateFormat getDf() {
        return df;
    }

    /*   //发布消息
                                       public void sendToTopic(String topic, String msg, MqttStatusCallback mqttStatusCallback){
                                           try {
                                               messageCallback.setMqttCallback(mqttStatusCallback);
                                               MqttMessage message = new MqttMessage(msg.getBytes());
                                               message.setQos(qos);
                                               client.publish(topic, message);
                                               println("Message published");
                                           }catch(MqttException e){
                                               println("发布消息异常 Topic="+topic+"  meg="+msg);
                                           }
                                       }*/
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
