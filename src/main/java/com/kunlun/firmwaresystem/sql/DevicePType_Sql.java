package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageDevicePtype;
import com.kunlun.firmwaresystem.entity.device.Deviceptype;
import com.kunlun.firmwaresystem.mappers.DevicepTypeMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevicePType_Sql {
    public boolean addDevicePType(DevicepTypeMapper devicePMapper, Deviceptype devicePtype) {
        boolean status = check(devicePMapper, devicePtype);
        if (status) {
            return false;
        } else {
            devicePMapper.insert(devicePtype);
            QueryWrapper<Deviceptype> queryWrapper = Wrappers.query();
            queryWrapper.eq("name",devicePtype.getName());
            Deviceptype deviceptype1 =devicePMapper.selectOne(queryWrapper);
            System.out.println("申请的ID="+ deviceptype1.getId());
            devicePtype.setId(deviceptype1.getId());
            return true;
        }
    }

    public boolean update(DevicepTypeMapper devicePMapper, Deviceptype deviceP) {
        devicePMapper.updateById(deviceP);
        return true;
    }

    public Map<Integer, Deviceptype> getAllDeviceP(DevicepTypeMapper devicePMapper) {
        List<Deviceptype> devicePS = devicePMapper.selectList(null);
        HashMap<Integer, Deviceptype> beaconHashMap = new HashMap<>();
        for (Deviceptype deviceP : devicePS) {
            System.out.println("初始化"+deviceP.getName()+"==="+deviceP.getId());
            beaconHashMap.put(deviceP.getId(), deviceP);
        }
        return beaconHashMap;
    }

    public     List<Deviceptype> getAllDevicePlist(DevicepTypeMapper devicePMapper,String userkey) {
        LambdaQueryWrapper<Deviceptype> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(Deviceptype::getUserkey, userkey);
        List<Deviceptype> devicePS = devicePMapper.selectList(userLambdaQueryWrapper);
        return devicePS;
    }
 /*   public Map<String, Beacon> getAllBeacon(BeaconMapper beaconMapper, String project_key) {

        List<Beacon> beacons = beaconMapper.selectList(userLambdaQueryWrapper);
        HashMap<String, Beacon> beaconHashMap = new HashMap<>();
        for (Beacon beacon : beacons) {
            //System.out.println("初始化"+gateway.getSub_topic()+"==="+gateway.getPub_topic());
            beaconHashMap.put(beacon.getMac(), beacon);
        }
        return beaconHashMap;
    }*/

    public Deviceptype getDeviceP(DevicepTypeMapper devicePMapper,int id) {
        Deviceptype devicePtype=devicePMapper.selectById(id);
        return devicePtype;
    }

    public boolean delete(DevicepTypeMapper devicePMapper, Deviceptype devicePtype) {
        QueryWrapper<Deviceptype> queryWrapper = Wrappers.query();
        queryWrapper.eq("id", devicePtype.getId());
       int status= devicePMapper.delete(queryWrapper);
       if(status!=-1){
           return true;
       }else{
           return false;
       }

    }
    public boolean deletes(DevicepTypeMapper devicePMapper, List<Integer> list) {
        int code=devicePMapper.deleteBatchIds(list);

        if(code!=-1){
            return true;
        }else{
            return false;
        }

    }
    public PageDevicePtype selectPageDeviceP(DevicepTypeMapper devicePMapper, int page, int limt, String name,String userkey,String project_key) {
        LambdaQueryWrapper<Deviceptype> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Deviceptype> userPage = new Page<>(page, limt);
        IPage<Deviceptype> userIPage;

        userLambdaQueryWrapper.like(Deviceptype::getName, name);
        userLambdaQueryWrapper.eq(Deviceptype::getUserkey, userkey);
        userLambdaQueryWrapper.eq(Deviceptype::getProject_key, project_key);
        userIPage = devicePMapper.selectPage(userPage, userLambdaQueryWrapper);
        // userIPage.getRecords().forEach(System.out::println);
        PageDevicePtype pageDeviceP = new PageDevicePtype(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
        return pageDeviceP;
    }

    public boolean check(DevicepTypeMapper devicePMapper, Deviceptype deviceP) {
        QueryWrapper<Deviceptype> queryWrapper = Wrappers.query();
        queryWrapper.eq("name", deviceP.getName());
        Deviceptype deviceP1 = devicePMapper.selectOne(queryWrapper);
        if (deviceP1 == null) {
            return false;
        } else {
            return true;
        }
    }
}