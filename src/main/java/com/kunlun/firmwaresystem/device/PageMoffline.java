package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Moffline;

import java.util.List;

public class PageMoffline {
    List<Moffline> mofflines;
    long page;
    long total;

    public PageMoffline(List<Moffline> mofflines,
                        long page,
                        long total) {
        this.mofflines = mofflines;
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

    public List<Moffline> getMofflines() {
        return mofflines;
    }

    public void setMofflines(List<Moffline> mofflines) {
        this.mofflines = mofflines;
    }
}