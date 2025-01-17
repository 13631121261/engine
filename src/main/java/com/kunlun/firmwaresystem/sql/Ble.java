package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageBleVersion;
import com.kunlun.firmwaresystem.entity.Ble_firmware;
import com.kunlun.firmwaresystem.entity.Rules;
import com.kunlun.firmwaresystem.entity.Wifi_firmware;
import com.kunlun.firmwaresystem.mappers.BleMapper;
import com.kunlun.firmwaresystem.mappers.RulesMapper;

import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class Ble {
    public List<Ble_firmware> getVersionByKey(BleMapper bleMapper, String project_key) {
        QueryWrapper<Ble_firmware> queryWrapper = Wrappers.query();
        queryWrapper.eq("project_key", project_key);
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Ble_firmware> a = bleMapper.selectList(queryWrapper);
        return a;
    }

    public List<Ble_firmware> getCustomerVersion(BleMapper bleMapper, String userkey) {
        QueryWrapper<Ble_firmware> queryWrapper = Wrappers.query();
        queryWrapper.like("user_key", userkey);
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Ble_firmware> a = bleMapper.selectList(queryWrapper);
        return a;
    }

    public PageBleVersion selectPageBleVersion(BleMapper bleMapper, int page, int limt, String remake,  String project_key) {
        LambdaQueryWrapper<Ble_firmware> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Ble_firmware> userPage = new Page<>(page, limt);
        IPage<Ble_firmware> userIPage;
        userLambdaQueryWrapper.eq(Ble_firmware::getProject_key, project_key).like(Ble_firmware::getRemake, remake).or().eq(Ble_firmware::getProject_key, project_key).like(Ble_firmware::getVersion, remake);

        println("开始查好");
        try {
            userIPage = bleMapper.selectPage(userPage, userLambdaQueryWrapper);
            println("总页数： " + userIPage.getPages());
            println("总记录数： " + userIPage.getTotal());
            //   userIPage.getRecords().forEach(System.out::println);
            PageBleVersion pageBleVersion = new PageBleVersion(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
            return pageBleVersion;
        }catch (Exception e){
            println("selectPageBleVersion"+e.getMessage());
        }
        return null;
    }

    public boolean add(BleMapper bleMapper,Ble_firmware ble){
        if (!checkRules(bleMapper, ble)) {
            bleMapper.insert(ble);
            return true;
        } else {
            return false;
        }
    }
    private boolean checkRules(BleMapper bleMapper,Ble_firmware ble) {
        QueryWrapper<Ble_firmware> queryWrapper = Wrappers.query();
        queryWrapper.eq("version", ble.getVersion());

//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Ble_firmware> a = bleMapper.selectList(queryWrapper);
        if (a != null && a.size() > 0) {
            return true;
        } else
            return false;
    }
    public int delete(BleMapper bleMapper, String userKey, String version) {
        QueryWrapper<Ble_firmware> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key", userKey);
        queryWrapper.eq("version", version);
        return bleMapper.delete(queryWrapper);
    }
}
