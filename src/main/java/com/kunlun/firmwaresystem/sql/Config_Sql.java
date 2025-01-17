package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageAlarm;
import com.kunlun.firmwaresystem.entity.Alarm;
import com.kunlun.firmwaresystem.entity.Config;
import com.kunlun.firmwaresystem.mappers.AlarmMapper;
import com.kunlun.firmwaresystem.mappers.ConfigMapper;

import java.util.List;

public class Config_Sql {
    public Config updateHost(ConfigMapper configMapper, String host) {
        QueryWrapper<Alarm> queryWrapper = Wrappers.query();
            List<Config> configs=configMapper.selectList(null);
            if(configs!=null&&configs.size()==1){
                Config config=configs.get(0);
                config.setHost(host);
                configMapper.updateById(config);
                return config;
            }else{
                configMapper.delete(null);
                Config config=new Config();
                config.setHost(host);
                configMapper.insert(config);
                return config;
            }

    }
    public String getHost(ConfigMapper configMapper){
        List<Config> configs=configMapper.selectList(null);
        if(configs!=null&&configs.size()==1){
            Config config=configs.get(0);
            return config.getHost();
        }else{
            return null;
        }
    }

}