package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Check_record;

import java.util.List;

public class PageCheckRecord {
    List<Check_record> checkRecordList;
    long page;
    long total;

    public PageCheckRecord(List<Check_record> checkRecordList,
                           long page,
                           long total) {
        this.checkRecordList = checkRecordList;
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

    public void setCheckRecordList(List<Check_record> checkRecordList) {
        this.checkRecordList = checkRecordList;
    }

    public List<Check_record> getCheckRecordList() {
        return checkRecordList;
    }

    @Override
    public String toString() {
        return "PageCheckRecord{" +
                "checkRecordList=" + checkRecordList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}