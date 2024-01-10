package com.kunlun.firmwaresystem.timeOut;

import com.google.gson.Gson;
import com.kunlun.firmwaresystem.device.Device;
import com.kunlun.firmwaresystem.device.Gateway_device;
import com.kunlun.firmwaresystem.device.cmd.DisConnect;
import com.kunlun.firmwaresystem.device.cmd.DisConnectDetail;
import com.kunlun.firmwaresystem.gatewayJson.Constant;
import com.kunlun.firmwaresystem.interface_.iConnectTimeOut;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kunlun.firmwaresystem.NewSystemApplication.directExchangeProducer;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.go_to_connect;

public class ConnectTimeOut implements iConnectTimeOut {


    @Override
    public void timeout(Device device) {/*
        if (device.getState().equals(Constant.ConnectState_searching)) {
            Gateway_device gateway_device = device.getGateway_device();
            DisConnect disConnect = new DisConnect();
            disConnect.setGw_addr(gateway_device.getgAddress());
            DisConnectDetail disConnectDetail = new DisConnectDetail();
            ArrayList list = new ArrayList<>();
            HashMap hash = new HashMap<>();
            hash.put("addr", device.getdAddress());
            list.add(hash);
            disConnectDetail.setDevices(list);
            disConnectDetail.setDevices_num(1);
            disConnect.setData(disConnectDetail);
            Gson gson = new Gson();
            String json = gson.toJson(disConnect);
            System.out.println("超时-断开连接数据=" + json);
            RabbitMessage rabbitMessage = new RabbitMessage(gateway_device.getSubTopic(), json);
            directExchangeProducer.send(rabbitMessage.toString(), go_to_connect);
        } else {
            System.out.println("没有超时");
        }
*/

    }
}
