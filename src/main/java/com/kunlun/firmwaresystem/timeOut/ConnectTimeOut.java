package com.kunlun.firmwaresystem.timeOut;

import com.kunlun.firmwaresystem.device.Device;
import com.kunlun.firmwaresystem.interface_.iConnectTimeOut;

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
            println("超时-断开连接数据=" + json);
            RabbitMessage rabbitMessage = new RabbitMessage(gateway_device.getSubTopic(), json);
            directExchangeProducer.send(rabbitMessage.toString(), go_to_connect);
        } else {
            println("没有超时");
        }
*/

    }
}
