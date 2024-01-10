package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageDeviceCode;

import com.kunlun.firmwaresystem.entity.device.DeviceCode;

import com.kunlun.firmwaresystem.entity.device.Deviceptype;
import com.kunlun.firmwaresystem.entity.device.Supplier;
import com.kunlun.firmwaresystem.mappers.DeviceCodeMapper;
import com.kunlun.firmwaresystem.mappers.DevicepTypeMapper;
import com.kunlun.firmwaresystem.mappers.SupplierMapper;


import java.util.List;

public class DeviceCode_Sql {



        public boolean addDeviceCode(DeviceCodeMapper deviceCodeMapper, SupplierMapper supplierMapper, DevicepTypeMapper devicepTypeMapper, DeviceCode deviceCode) {
            boolean status = check(deviceCodeMapper, deviceCode);
            if (status) {
                return false;
            } else {
                Supplier_Sql supplierSql=new Supplier_Sql();
               Supplier supplier= supplierSql.getOneSupplier(supplierMapper,deviceCode.getSupplier_id());
                if(supplier!=null){
                    deviceCode.setSupplier_name(supplier.getName());

                }
                DevicePType_Sql devicePType_sql=new DevicePType_Sql();
               Deviceptype deviceptype= devicePType_sql.getDeviceP(devicepTypeMapper,deviceCode.getType_id());
                if(deviceptype!=null){
                    deviceCode.setType_name(deviceptype.getName());
                }
                deviceCodeMapper.insert(deviceCode);
               /* QueryWrapper<DeviceCode> queryWrapper = Wrappers.query();
                queryWrapper.eq("name",deviceCode.getName());
                DeviceCode deviceCode1 =deviceCodeMapper.selectOne(queryWrapper);
                //System.out.println("申请的ID="+ devicep1.getId());


                deviceCodeMapper.updateById(deviceCode1);*/
                return true;
            }
        }
    public DeviceCode getOrderbyId(DeviceCodeMapper devicePMapper){
        QueryWrapper<DeviceCode> queryWrapper = Wrappers.query();
        queryWrapper.last("limit 1");
        queryWrapper.orderByDesc("id");
        DeviceCode devicep= devicePMapper.selectOne(queryWrapper);
        return devicep;

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
        public void delete(DeviceCodeMapper deviceCodeMapper, int id) {
            QueryWrapper<DeviceCode> queryWrapper = Wrappers.query();
            queryWrapper.eq("id", id);
            deviceCodeMapper.delete(queryWrapper);
        }
    public int deletes(DeviceCodeMapper deviceCodeMapper, List<Integer> ids) {
        return deviceCodeMapper.deleteBatchIds(ids);
    }
        public int update(DeviceCodeMapper deviceCodeMapper, DeviceCode deviceCode) {

            return deviceCodeMapper.updateById(deviceCode);
        }

        public PageDeviceCode selectPageDeviceCode(DeviceCodeMapper deviceCodeMapper, int page, int limt, String userkey, String project_key, String search) {

            LambdaQueryWrapper<DeviceCode> userLambdaQueryWrapper = Wrappers.lambdaQuery();
            Page<DeviceCode> userPage = new Page<>(page, limt);
            IPage<DeviceCode> userIPage;
            userLambdaQueryWrapper.eq(DeviceCode::getUser_key, userkey).eq(DeviceCode::getProject_key, project_key).like(DeviceCode::getName, search)
            .or().eq(DeviceCode::getUser_key, userkey).eq(DeviceCode::getProject_key, project_key).like(DeviceCode::getCode, search)
                    .or().eq(DeviceCode::getUser_key, userkey).eq(DeviceCode::getProject_key, project_key).like(DeviceCode::getCode_sn, search)
                    .or().eq(DeviceCode::getUser_key, userkey).eq(DeviceCode::getProject_key, project_key).like(DeviceCode::getSupplier_name, search)
                    .or().eq(DeviceCode::getUser_key, userkey).eq(DeviceCode::getProject_key, project_key).like(DeviceCode::getType_name, search)
                    .or().eq(DeviceCode::getUser_key, userkey).eq(DeviceCode::getProject_key, project_key).like(DeviceCode::getModel, search);//.or().eq(DeviceCode::getUser_key, userkey).eq(DeviceCode::getProject_key, project_key).like(DeviceCode::getContacts, search);
            userIPage = deviceCodeMapper.selectPage(userPage, userLambdaQueryWrapper);
            PageDeviceCode pageDeviceCode = new PageDeviceCode(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
            return pageDeviceCode;
        }

        public boolean check(DeviceCodeMapper deviceCodeMapper, DeviceCode deviceCode) {
            QueryWrapper<DeviceCode> queryWrapper = Wrappers.query();
            queryWrapper.eq("code_sn", deviceCode.getCode_sn());
            queryWrapper.eq("project_key", deviceCode.getProject_key());
            List<DeviceCode> deviceCode1 = deviceCodeMapper.selectList(queryWrapper);
            if (deviceCode1 == null||deviceCode1.size()==0) {
                return false;
            } else {
                return true;
            }
        }
    public    List<DeviceCode> getAllCode(DeviceCodeMapper deviceCodeMapper,String project_key,String quickSearch) {
        QueryWrapper<DeviceCode> queryWrapper = Wrappers.query();
        queryWrapper.eq("project_key",project_key);
        if(quickSearch!=null){
            queryWrapper.like("code_sn",quickSearch).or().
            like("name",quickSearch).or().
            like("model",quickSearch).or().
            like("brand",quickSearch);
        }
        List<DeviceCode> deviceCode1 = deviceCodeMapper.selectList(queryWrapper);
        return  deviceCode1;
    }
        public  DeviceCode getOneDeviceCode(DeviceCodeMapper deviceCodeMapper, int id) {

            return deviceCodeMapper.selectById(id);

    }
    public    List<DeviceCode> getOneDeviceCode(DeviceCodeMapper deviceCodeMapper, String code_sn) {
        QueryWrapper<DeviceCode> queryWrapper = Wrappers.query();
        queryWrapper.eq("code_sn",code_sn);
        List<DeviceCode> deviceCodes=deviceCodeMapper.selectList(queryWrapper);
        return deviceCodes;

    }
}
