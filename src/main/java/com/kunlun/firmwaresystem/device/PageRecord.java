package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Record;

import java.util.List;

public class PageRecord {
    List<Record> recordList;
    long page;
    long total;

    public PageRecord(List<Record> recordList,
                      long page,
                      long total) {
        this.recordList = recordList;
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

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    @Override
    public String toString() {
        return "PageRecord{" +
                "recordList=" + recordList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}