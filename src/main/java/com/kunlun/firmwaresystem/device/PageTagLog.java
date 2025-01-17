package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Beacon_tag;
import com.kunlun.firmwaresystem.entity.Tag_log;

import java.util.List;

public class PageTagLog {
    List<Tag_log> tagLogs;
    long page;
    long total;

    public PageTagLog(List<Tag_log> tagLogs,
                      long page,
                      long total) {
        this.tagLogs = tagLogs;
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

    public void setTagLogs(List<Tag_log> tagLogs) {
        this.tagLogs = tagLogs;
    }

    public List<Tag_log> getTagLogs() {
        return tagLogs;
    }

    @Override
    public String toString() {
        return "PageTagLog{" +
                "tagLogs=" + tagLogs +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}