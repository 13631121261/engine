package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageDeviceBarn;
import com.kunlun.firmwaresystem.device.PageDeviceOutBarn;
import com.kunlun.firmwaresystem.entity.device.DeviceBarn;
import com.kunlun.firmwaresystem.entity.device.DeviceOutBarn;
import com.kunlun.firmwaresystem.mappers.DeviceBarnMapper;
import com.kunlun.firmwaresystem.mappers.DeviceOutBarnMapper;

import java.util.List;

public class DeviceOutBarn_Sql {



        public boolean addDeviceOutBarn(DeviceOutBarnMapper deviceBarnMapper, DeviceOutBarn deviceBarn) {
            deviceBarnMapper.insert(deviceBarn);
            return true;
          /*  boolean status = check(deviceBarnMapper, deviceBarn);
            if (status) {
                return false;
            } else {

            }*/
        }
/*
        public Map<String, Wordcard_a> getAllWordCarda(WordCardaMapper wordCardaMapper) {
            List<Wordcard_a> wordcard_aList = wordCardaMapper.selectList(null);
            HashMap<String, Wordcard_a> wordCardAHashMap = new HashMap<>();
            for (Wordcard_a wordCard_a : wordcard_aList) {
                //System.out.println("初始化"+gateway.getSub_topic()+"==="+gateway.getPub_topic());
                wordCardAHashMap.put(wordCard_a.getMac(), wordCard_a);
            }
            return wordCardAHashMap;
        }

        //查询未绑定的工卡
        public List<Wordcard_a> getAllWordCarda(WordCardaMapper wordCardaMapper, String project_key, String user_key) {
            QueryWrapper<Wordcard_a> userLambdaQueryWrapper = Wrappers.query();
            userLambdaQueryWrapper.eq("isbind",0);
            userLambdaQueryWrapper.eq("isbind",0);
            userLambdaQueryWrapper.eq("user_key",user_key);
            List<Wordcard_a> wordcard_aList = wordCardaMapper.selectList(userLambdaQueryWrapper);
            return wordcard_aList;
        }
*/
        public void delete(DeviceOutBarnMapper deviceBarnMapper, int id) {
            QueryWrapper<DeviceOutBarn> queryWrapper = Wrappers.query();
            queryWrapper.eq("id", id);
            deviceBarnMapper.delete(queryWrapper);
        }
    public int deletes(DeviceOutBarnMapper deviceBarnMapper, List<Integer> ids) {
        return deviceBarnMapper.deleteBatchIds(ids);
    }
        public int update(DeviceOutBarnMapper deviceBarnMapper, DeviceOutBarn deviceBarn) {

            return deviceBarnMapper.updateById(deviceBarn);
        }

        public PageDeviceOutBarn selectPageDeviceOutBarn(DeviceOutBarnMapper deviceBarnMapper, int page, int limt, String userkey, String project_key, String search) {

            LambdaQueryWrapper<DeviceOutBarn> userLambdaQueryWrapper = Wrappers.lambdaQuery();
            Page<DeviceOutBarn> userPage = new Page<>(page, limt);
            IPage<DeviceOutBarn> userIPage;
            userLambdaQueryWrapper.eq(DeviceOutBarn::getUser_key, userkey).eq(DeviceOutBarn::getProject_key, project_key).like(DeviceOutBarn::getNotes, search).or().
                    eq(DeviceOutBarn::getUser_key, userkey).eq(DeviceOutBarn::getProject_key, project_key).like(DeviceOutBarn::getCode_name, search);
            userIPage = deviceBarnMapper.selectPage(userPage, userLambdaQueryWrapper);
            PageDeviceOutBarn pageDeviceBarn = new PageDeviceOutBarn(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
            return pageDeviceBarn;
        }

        public boolean check(DeviceOutBarnMapper deviceBarnMapper, DeviceOutBarn deviceBarn) {
            QueryWrapper<DeviceOutBarn> queryWrapper = Wrappers.query();
            queryWrapper.eq("notes", deviceBarn.getNotes());
            List<DeviceOutBarn> deviceBarn1 = deviceBarnMapper.selectList(queryWrapper);
            if (deviceBarn1 == null||deviceBarn1.size()==0) {
                return false;
            } else {
                return true;
            }
        }
        public  DeviceOutBarn getOneDeviceOutBarn(DeviceOutBarnMapper deviceBarnMapper, int id) {


            return deviceBarnMapper.selectById(id);

    }
}
