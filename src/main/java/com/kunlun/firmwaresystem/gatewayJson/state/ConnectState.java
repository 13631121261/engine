package com.kunlun.firmwaresystem.gatewayJson.state;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

//连接过程中上报的状态信息
public class ConnectState<T> extends GatewayHand {

    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
