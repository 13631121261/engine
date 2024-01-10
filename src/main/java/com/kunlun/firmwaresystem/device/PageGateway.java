package com.kunlun.firmwaresystem.device;

import java.util.List;

public class PageGateway {
    List<Gateway> gatewayList;
    long page;
    long total;

    public PageGateway(List<Gateway> gatewayList,
                       long page,
                       long total) {
        this.gatewayList = gatewayList;
        this.page = page;
        this.total = total;
    }

    public List<Gateway> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(List<Gateway> gatewayList) {
        this.gatewayList = gatewayList;
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

    @Override
    public String toString() {
        return "PageGateway{" +
                "gatewayList=" + gatewayList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}