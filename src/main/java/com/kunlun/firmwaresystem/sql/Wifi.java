package com.kunlun.firmwaresystem.sql;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageBleVersion;
import com.kunlun.firmwaresystem.device.PageWifiVersion;
import com.kunlun.firmwaresystem.entity.Wifi_firmware;
import com.kunlun.firmwaresystem.entity.Wifi_firmware;
import com.kunlun.firmwaresystem.mappers.WifiMapper;

import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;
import static com.kunlun.firmwaresystem.util.constant.code_ok;
import static com.kunlun.firmwaresystem.util.constant.wifi_type;

public class Wifi {
    public List<Wifi_firmware> getVersionByKey(WifiMapper WifiMapper, String project_key) {
        QueryWrapper<Wifi_firmware> queryWrapper = Wrappers.query();
        queryWrapper.eq("project_key", project_key);
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Wifi_firmware> a = WifiMapper.selectList(queryWrapper);
        return a;
    }

    public List<Wifi_firmware> getCustomerVersion(WifiMapper WifiMapper, String userkey) {
        QueryWrapper<Wifi_firmware> queryWrapper = Wrappers.query();
        queryWrapper.like("user_key", userkey);
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Wifi_firmware> a = WifiMapper.selectList(queryWrapper);
        return a;
    }

    public PageWifiVersion selectPageWifiVersion(WifiMapper WifiMapper, int page, int limt, String remake, String project_key) {
        LambdaQueryWrapper<Wifi_firmware> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Wifi_firmware> userPage = new Page<>(page, limt);
        IPage<Wifi_firmware> userIPage;

            userLambdaQueryWrapper.eq(Wifi_firmware::getProject_key, project_key).like(Wifi_firmware::getRemake, remake).or().eq(Wifi_firmware::getProject_key, project_key).like(Wifi_firmware::getVersion, remake);

        println("开始查好");
        try {
            userIPage = WifiMapper.selectPage(userPage, userLambdaQueryWrapper);
            println("总页数： " + userIPage.getPages());
            println("总记录数： " + userIPage.getTotal());
            //   userIPage.getRecords().forEach(System.out::println);
            return new PageWifiVersion(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
        }catch (Exception e){
            println("异常="+e);
        }
        return null;
    }

    public boolean add(WifiMapper WifiMapper,Wifi_firmware ble){
        if (!checkRules(WifiMapper, ble)) {
            WifiMapper.insert(ble);
            return true;
        } else {
            return false;
        }
    }
    private boolean checkRules(WifiMapper WifiMapper,Wifi_firmware ble) {
        QueryWrapper<Wifi_firmware> queryWrapper = Wrappers.query();
        queryWrapper.eq("version", ble.getVersion());

//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Wifi_firmware> a = WifiMapper.selectList(queryWrapper);
        if (a != null && a.size() > 0) {
            return true;
        } else
            return false;
    }
    public int delete(WifiMapper WifiMapper, String userKey, String version) {
        QueryWrapper<Wifi_firmware> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key", userKey);
        queryWrapper.eq("version", version);
        return WifiMapper.delete(queryWrapper);
    }
}
