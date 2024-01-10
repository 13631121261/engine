package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Gateway_config;

import java.util.List;

public class PageGatewayConfig {
    List<Gateway_config> gatewayConfigList;
    long page;
    long total;

    public PageGatewayConfig(List<Gateway_config> gatewayConfigList,
                             long page,
                             long total) {
        this.gatewayConfigList = gatewayConfigList;
        this.page = page;
        this.total = total;
    }


    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Gateway_config> getGatewayConfigList() {
        return gatewayConfigList;
    }

    public void setGatewayConfigList(List<Gateway_config> gatewayConfigList) {
        this.gatewayConfigList = gatewayConfigList;
    }

    @Override
    public String toString() {
        return "PageGatewayConfig{" +
                "gatewayConfigList=" + gatewayConfigList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}