package com.kunlun.firmwaresystem.mqtt;

import com.google.gson.Gson;
import com.kunlun.firmwaresystem.util.RedisUtils;
import com.kunlun.firmwaresystem.util.SpringUtil;
import org.eclipse.paho.mqttv5.client.IMqttDeliveryToken;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import java.text.SimpleDateFormat;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class MessageCallback implements MqttCallback {


    private static int ExpireTime = 60;   // redis中存储的过期时间60s
    Gson gson = new Gson();

    private RedisUtils redisUtil;
    MyMqttClient mqttClient;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    String time;

    public MessageCallback(MyMqttClient mqttClient) {
        this.mqttClient = mqttClient;
        // redisUtil=  (FirmwaresystemApplication)FirmwaresystemApplication.applicationContext.getBean(TestService.class);
        redisUtil = SpringUtil.getBean(RedisUtils.class);

    }




    @Override
    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
        // 连接丢失后，一般在这里面进行重连
        println("连接断开，可以做重连" + mqttDisconnectResponse.getReasonString());
        try {
            mqttClient.reconnect();
        } catch (MqttException e) {
            println("重连失败" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mqttErrorOccurred(MqttException e) {

    }

    public void messageArrived(String topic, MqttMessage message) {

        mqttClient.executorService.submit(new CallBackHandlers(topic, message));


    }

    @Override
    public void deliveryComplete(IMqttToken iMqttToken) {

    }

    @Override
    public void connectComplete(boolean b, String s) {

    }

    @Override
    public void authPacketArrived(int i, MqttProperties mqttProperties) {

    }


}