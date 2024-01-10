package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Alarm;
import java.util.List;

public class PageAlarm {
    List<Alarm> alarmList;
    long page;
    long total;

    public PageAlarm(List<Alarm> alarmList,
                     long page,
                     long total) {
        this.alarmList = alarmList;
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

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }
    public List<Alarm> getAlarmList() {
        return alarmList;
    }

}