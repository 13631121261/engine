package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Outbound;

import java.util.List;

public class PageOutBound {
    List<Outbound> outboundList;
    long page;
    long total;

    public PageOutBound(List<Outbound> outboundList,
                        long page,
                        long total) {
        this.outboundList = outboundList;
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

    public List<Outbound> getOutboundList() {
        return outboundList;
    }

    public void setOutboundList(List<Outbound> outboundList) {
        this.outboundList = outboundList;
    }

}