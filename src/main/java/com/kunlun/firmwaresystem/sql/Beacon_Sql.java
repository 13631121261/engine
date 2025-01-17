package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageBeacon;
import com.kunlun.firmwaresystem.entity.Area;
import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.mappers.BeaconMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class Beacon_Sql {
    public boolean addBeacon(BeaconMapper beaconMapper, Beacon beacon) {
        boolean status = check(beaconMapper, beacon);
        if (status) {
            return false;
        } else {
            beaconMapper.insert(beacon);
            QueryWrapper<Beacon> queryWrapper = Wrappers.query();
            queryWrapper.eq("mac",beacon.getMac());
            Beacon beacon1 =beaconMapper.selectOne(queryWrapper);
            //println("申请的ID="+ devicep1.getId());
            beacon.setId(beacon1.getId());
            return true;
        }
    }

    public boolean update(BeaconMapper beaconMapper, Beacon beacon) {
        beaconMapper.updateById(beacon);
        return true;
    }

    public List<Beacon> getBeaconByMac(BeaconMapper beaconMapper,String user_key,String project_key,String mac) {
        QueryWrapper<Beacon> queryWrapper = Wrappers.query();
        queryWrapper.eq("user_key", user_key);
        queryWrapper.eq("project_key", project_key);
        queryWrapper.eq("mac", mac);
        List<Beacon> beacons = beaconMapper.selectList(queryWrapper);
        return beacons;
    }


    public Map<String, Beacon> getAllBeacon(BeaconMapper beaconMapper) {
        LambdaQueryWrapper<Beacon> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        List<Beacon> beacons = beaconMapper.selectList(userLambdaQueryWrapper);
        HashMap<String, Beacon> beaconHashMap = new HashMap<>();
        for (Beacon beacon : beacons) {
            //println("初始化"+gateway.getSub_topic()+"==="+gateway.getPub_topic());
            beaconHashMap.put(beacon.getMac(), beacon);
        }
        return beaconHashMap;
    }

    public List<Beacon> getAllBeacon(BeaconMapper beaconMapper, String userkey, String project_key) {
        LambdaQueryWrapper<Beacon> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(Beacon::getUser_key, userkey);
        userLambdaQueryWrapper.eq(Beacon::getProject_key, project_key);
        List<Beacon> beacons = beaconMapper.selectList(userLambdaQueryWrapper);
        return beacons;
    }
    public List<Beacon> getAllBeaconbyMac(BeaconMapper beaconMapper, String userkey, String project_key,String mac) {
        LambdaQueryWrapper<Beacon> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(Beacon::getUser_key, userkey).eq(Beacon::getProject_key, project_key).like(Beacon::getMac,mac);
        List<Beacon> beacons = beaconMapper.selectList(userLambdaQueryWrapper);
        return beacons;
    }

    public List<Beacon> getunAllBeacon(BeaconMapper beaconMapper, String userkey,String project_key,String type) {
        QueryWrapper<Beacon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isbind",0);
        queryWrapper.eq("user_key",userkey);
        queryWrapper.eq("type",type);
        queryWrapper.eq("project_key",project_key);
        List<Beacon> beaconList=beaconMapper.selectList(queryWrapper);
        return beaconList;
    }
    public List<Beacon> getAllBeacon(BeaconMapper beaconMapper, String userkey,String project_key,String type) {
        QueryWrapper<Beacon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_key",userkey);
        queryWrapper.eq("type",type);
        queryWrapper.eq("project_key",project_key);
        List<Beacon> beaconList=beaconMapper.selectList(queryWrapper);
        return beaconList;
    }
    public void delete(BeaconMapper beaconMapper, String mac) {
        QueryWrapper<Beacon> queryWrapper = Wrappers.query();
        queryWrapper.eq("mac", mac);
        beaconMapper.delete(queryWrapper);
    }

    public PageBeacon selectPageBeacon(BeaconMapper beaconMapper, int page, int limt, String like, String userkey, String project_key) {
        LambdaQueryWrapper<Beacon> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Beacon> userPage = new Page<>(page, limt);
        IPage<Beacon> userIPage;
        userLambdaQueryWrapper.ne(Beacon::getType,5).eq(Beacon::getProject_key,project_key).eq(Beacon::getUser_key, userkey).like(Beacon::getMac, like);
        userIPage = beaconMapper.selectPage(userPage, userLambdaQueryWrapper);
        println("总页数： " + userIPage.getPages());
        println("总记录数： " + userIPage.getTotal());
        // userIPage.getRecords().forEach(System.out::println);
        PageBeacon pageBeacon = new PageBeacon(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
        return pageBeacon;
    }

    public PageBeacon selectPageBeacon_AOA(BeaconMapper beaconMapper, int page, int limt, String like, String userkey, String project_key) {
        LambdaQueryWrapper<Beacon> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Beacon> userPage = new Page<>(page, limt);
        IPage<Beacon> userIPage;
        userLambdaQueryWrapper.eq(Beacon::getProject_key,project_key).eq(Beacon::getType,5).eq(Beacon::getUser_key, userkey).like(Beacon::getMac, like);
        userIPage = beaconMapper.selectPage(userPage, userLambdaQueryWrapper);
        println("总页数： " + userIPage.getPages());
        println("总记录数： " + userIPage.getTotal());
        // userIPage.getRecords().forEach(System.out::println);
        PageBeacon pageBeacon = new PageBeacon(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
        return pageBeacon;
    }
    public int deletes(BeaconMapper beaconMapper, List<Integer> id) {
        return beaconMapper.deleteBatchIds(id);
    }
    public boolean check(BeaconMapper beaconMapper, Beacon beacon) {
        QueryWrapper<Beacon> queryWrapper = Wrappers.query();
        queryWrapper.eq("mac", beacon.getMac());
        Beacon beacon1 = beaconMapper.selectOne(queryWrapper);
        if (beacon1 == null) {
            return false;
        } else {
            return true;
        }
    }
}