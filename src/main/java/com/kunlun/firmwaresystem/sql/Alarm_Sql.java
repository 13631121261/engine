package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageAlarm;
import com.kunlun.firmwaresystem.entity.Alarm;
import com.kunlun.firmwaresystem.entity.History;
import com.kunlun.firmwaresystem.mappers.AlarmMapper;
import com.kunlun.firmwaresystem.mappers.HistoryMapper;

import java.util.List;

public class Alarm_Sql {
    public boolean addAlarm(AlarmMapper alarmMapper, Alarm alarm) {
        alarmMapper.insert(alarm);
            return true;
    }

    public PageAlarm selectPageAlarm(AlarmMapper alarmMapper, int page, int limt, String project_key,String object,String alarm_type,String name) {

        LambdaQueryWrapper<Alarm> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Alarm> userPage = new Page<>(page, limt);
        IPage<Alarm> userIPage;

        if(alarm_type!=null&&!alarm_type.equals("sos_all")){
            userLambdaQueryWrapper.like(Alarm::getAlarm_type, alarm_type).like(Alarm::getAlarm_object, object).like(Alarm::getSn, name).eq(Alarm::getProject_key,project_key)
            .or().like(Alarm::getAlarm_type, alarm_type).like(Alarm::getAlarm_object, object).like(Alarm::getName, name).eq(Alarm::getProject_key,project_key);
        }else{
            userLambdaQueryWrapper.like(Alarm::getAlarm_object, object).like(Alarm::getSn, name).eq(Alarm::getProject_key,project_key)
            .or().like(Alarm::getAlarm_object, object).like(Alarm::getName, name).eq(Alarm::getProject_key,project_key);
        }
      /*  userLambdaQueryWrapper.eq(Alarm::getProject_key, project_key).like(Alarm::getAlarm_object, object).like(Alarm::getAlarm_type, alarm_type).like(Alarm::getSn, name)
                .or().eq(Alarm::getProject_key, project_key).like(Alarm::getAlarm_object, object).like(Alarm::getAlarm_type, alarm_type).like(Alarm::getName, name);
         */ userIPage = alarmMapper.selectPage(userPage, userLambdaQueryWrapper);
        PageAlarm pageAlarm = new PageAlarm(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
        return pageAlarm;
    }
    public  void deletes(List<Integer> ids){

    }
}