package com.kunlun.firmwaresystem.interface_;

public interface MqttStatusCallback {
    boolean status = false;

    void onStatus(boolean status);

}
