package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageSupplier;
import com.kunlun.firmwaresystem.entity.device.Supplier;
import com.kunlun.firmwaresystem.mappers.SupplierMapper;

import java.util.List;

public class Supplier_Sql {



        public boolean addSupplier(SupplierMapper supplierMapper, Supplier supplier) {
            boolean status = check(supplierMapper, supplier);
            if (status) {
                return false;
            } else {
                supplierMapper.insert(supplier);
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
        public void delete(SupplierMapper supplierMapper, int id) {
            QueryWrapper<Supplier> queryWrapper = Wrappers.query();
            queryWrapper.eq("id", id);
            supplierMapper.delete(queryWrapper);
        }
    public int deletes(SupplierMapper supplierMapper, List<Integer> ids) {
        return supplierMapper.deleteBatchIds(ids);
    }
        public int update(SupplierMapper supplierMapper, Supplier supplier) {

            return supplierMapper.updateById(supplier);
        }

        public PageSupplier selectPageSupplier(SupplierMapper supplierMapper, int page, int limt, String userkey,String project_key, String search) {
            System.out.println("userkey="+userkey+" projectKey="+project_key+"  search="+search);
            LambdaQueryWrapper<Supplier> userLambdaQueryWrapper = Wrappers.lambdaQuery();
            Page<Supplier> userPage = new Page<>(page, limt);
            IPage<Supplier> userIPage;
            userLambdaQueryWrapper.eq(Supplier::getUser_key, userkey).eq(Supplier::getProject_key, project_key).like(Supplier::getName, search).or().eq(Supplier::getUser_key, userkey).eq(Supplier::getProject_key, project_key).like(Supplier::getContacts, search);
            userIPage = supplierMapper.selectPage(userPage, userLambdaQueryWrapper);
            PageSupplier pageWordcarda = new PageSupplier(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
            return pageWordcarda;
        }

        public boolean check(SupplierMapper supplierMapper, Supplier supplier) {
            QueryWrapper<Supplier> queryWrapper = Wrappers.query();
            queryWrapper.eq("name", supplier.getName());
            Supplier supplier1 = supplierMapper.selectOne(queryWrapper);
            if (supplier1 == null) {
                return false;
            } else {
                return true;
            }
        }
        public  Supplier getOneSupplier(SupplierMapper supplierMapper, int id) {


            return supplierMapper.selectById(id);

    }
}
