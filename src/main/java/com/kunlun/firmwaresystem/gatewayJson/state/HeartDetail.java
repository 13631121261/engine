package com.kunlun.firmwaresystem.gatewayJson.state;

public class HeartDetail extends StateHead {
    int flags;
    int ticks_cnt;

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getTicks_cnt() {
        return ticks_cnt;
    }

    public void setTicks_cnt(int ticks_cnt) {
        this.ticks_cnt = ticks_cnt;
    }
}
