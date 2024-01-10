package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageDeviceBarn;
import com.kunlun.firmwaresystem.entity.device.DeviceBarn;
import com.kunlun.firmwaresystem.entity.device.Deviceptype;
import com.kunlun.firmwaresystem.entity.device.Supplier;
import com.kunlun.firmwaresystem.mappers.DeviceBarnMapper;
import com.kunlun.firmwaresystem.mappers.DevicepTypeMapper;
import com.kunlun.firmwaresystem.mappers.SupplierMapper;

import java.util.List;

public class DeviceBarn_Sql {



        public boolean addDeviceBarn(DeviceBarnMapper deviceBarnMapper, DeviceBarn deviceBarn) {
            boolean status = check(deviceBarnMapper, deviceBarn);
            if (status) {
                return false;
            } else {
                deviceBarnMapper.insert(deviceBarn);
                return true;
            }
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
*/
        //查询未绑定的工卡
        public List<DeviceBarn> getDeviceBarn(DeviceBarnMapper deviceBarnMapper,String code_sn) {
            QueryWrapper<DeviceBarn> userLambdaQueryWrapper = Wrappers.query();
            userLambdaQueryWrapper.gt("surplus",0);
            userLambdaQueryWrapper.eq("code_sn",code_sn);
            List<DeviceBarn> deviceBarns = deviceBarnMapper.selectList(userLambdaQueryWrapper);
            return deviceBarns;
        }

        public void delete(DeviceBarnMapper deviceBarnMapper, int id) {
            QueryWrapper<DeviceBarn> queryWrapper = Wrappers.query();
            queryWrapper.eq("id", id);
            deviceBarnMapper.delete(queryWrapper);
        }
    public int deletes(DeviceBarnMapper deviceBarnMapper, List<Integer> ids) {
        return deviceBarnMapper.deleteBatchIds(ids);
    }
        public int update(DeviceBarnMapper deviceBarnMapper, DeviceBarn deviceBarn) {

            return deviceBarnMapper.updateById(deviceBarn);
        }

        public PageDeviceBarn selectPageDeviceBarn(DeviceBarnMapper deviceBarnMapper, int page, int limt, String userkey, String project_key, String search) {

            LambdaQueryWrapper<DeviceBarn> userLambdaQueryWrapper = Wrappers.lambdaQuery();
            Page<DeviceBarn> userPage = new Page<>(page, limt);
            IPage<DeviceBarn> userIPage;
            userLambdaQueryWrapper.eq(DeviceBarn::getUser_key, userkey).eq(DeviceBarn::getProject_key, project_key).like(DeviceBarn::getNotes, search);
            userIPage = deviceBarnMapper.selectPage(userPage, userLambdaQueryWrapper);
            PageDeviceBarn pageDeviceBarn = new PageDeviceBarn(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
            return pageDeviceBarn;
        }

        public boolean check(DeviceBarnMapper deviceBarnMapper, DeviceBarn deviceBarn) {
            QueryWrapper<DeviceBarn> queryWrapper = Wrappers.query();
            queryWrapper.eq("create_time", deviceBarn.getCreate_time());
            List<DeviceBarn> deviceBarn1 = deviceBarnMapper.selectList(queryWrapper);
            if (deviceBarn1 == null||deviceBarn1.size()==0) {
                return false;
            } else {
                return true;
            }
        }
        public  DeviceBarn getOneDeviceBarn(DeviceBarnMapper deviceBarnMapper, int id) {


        return deviceBarnMapper.selectById(id);

    }
    public  DeviceBarn getOneDeviceBarn(DeviceBarnMapper deviceBarnMapper, long create_time) {
        QueryWrapper<DeviceBarn> queryWrapper = Wrappers.query();
        queryWrapper.eq("create_time", create_time);
        System.out.println("barn="+create_time);
        return deviceBarnMapper.selectOne(queryWrapper);

    }
}
