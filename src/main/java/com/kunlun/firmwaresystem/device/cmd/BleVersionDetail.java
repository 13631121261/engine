package com.kunlun.firmwaresystem.device.cmd;

import com.kunlun.firmwaresystem.gatewayJson.Constant;

public class BleVersionDetail {
    int msgId;
    String cmd = Constant.cmd_sys_get_ver;

    public int getMsgId() {
        return msgId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }
}
