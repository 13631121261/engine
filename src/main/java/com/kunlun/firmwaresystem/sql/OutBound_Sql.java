package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kunlun.firmwaresystem.device.PageOutBound;
import com.kunlun.firmwaresystem.entity.Outbound;
import com.kunlun.firmwaresystem.mappers.OutBoundMapper;

public class OutBound_Sql {
    public boolean addOutBound(OutBoundMapper outBoundMapper, Outbound outBound) {
        boolean status = check(outBoundMapper, outBound);
        if (status) {
            return false;
        } else {
           int id= outBoundMapper.insert(outBound);
            QueryWrapper<Outbound> queryWrapper = Wrappers.query();
            queryWrapper.eq("device_sn",outBound.getDevice_sn());
            Outbound outbound1 =outBoundMapper.selectOne(queryWrapper);
            outBound.setId(outbound1.getId());
            return true;
        }
    }

    public boolean update(OutBoundMapper outBoundMapper, Outbound outBound) {
        outBoundMapper.updateById(outBound);
        return true;
    }
    public boolean update(OutBoundMapper outBoundMapper, String sn,String time) {
        UpdateWrapper<Outbound> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("device_sn",sn);
        updateWrapper.set("reborrow_time",time);
        outBoundMapper.update(null,updateWrapper);
        return true;
    }
/*

    public Map<String, Person> getAllPerson(PersonMapper PersonMapper) {
        List<Person> Persons = PersonMapper.selectList(null);
        HashMap<String, Person> PersonHashMap = new HashMap<>();
        for (Person Person : Persons) {
            //System.out.println("初始化"+gateway.getSub_topic()+"==="+gateway.getPub_topic());
            PersonHashMap.put(Person.getIdcard(), Person);
        }
        return PersonHashMap;
    }
*/


 /*   public Map<String, Person> getAllPerson(PersonMapper PersonMapper, String project_key) {

        List<Person> Persons = PersonMapper.selectList(null);
        HashMap<String, Person> PersonHashMap = new HashMap<>();
        for (Person Person : Persons) {
            //System.out.println("初始化"+gateway.getSub_topic()+"==="+gateway.getPub_topic());
            PersonHashMap.put(Person.getName(), Person);
        }
        return PersonHashMap;
    }
*/
/*    public List<Outbound> getAllOutBound(OutBoundMapper outBoundMapper, String sn, String name,int p_id) {
        LambdaQueryWrapper<Person> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        if(p_id!=-1){
            userLambdaQueryWrapper.eq(Person::getDepartmentid, p_id);
        }
        userLambdaQueryWrapper.like(Person::getIdcard, idcard);
        userLambdaQueryWrapper.like(Person::getName, name);
        List<Person> Persons = PersonMapper.selectList(userLambdaQueryWrapper);
        return Persons;
    }*/

    public void delete(OutBoundMapper outBoundMapper, String device_sn) {
        QueryWrapper<Outbound> queryWrapper = Wrappers.query();
        queryWrapper.eq("device_sn", device_sn);
        outBoundMapper.delete(queryWrapper);
    }
 /*   public void deletePerson(PersonMapper PersonMapper, String idcard) {
        QueryWrapper<Person> queryWrapper = Wrappers.query();
        queryWrapper.eq("idcard", idcard);
        PersonMapper.delete(queryWrapper);
    }*/

    public PageOutBound selectPageOutBound(OutBoundMapper outBoundMapper, int page, int limt, String sn, String name, int remove_type,String userkey) {
        Page<Outbound> userPage = new Page<>(page, limt);
        IPage<Outbound> userIPage;
        LambdaQueryWrapper<Outbound> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        if(remove_type!=-1){//出库类型
            userLambdaQueryWrapper.eq(Outbound::getRemove_type, remove_type);
        }else if(remove_type==-1){
            userLambdaQueryWrapper.ne(Outbound::getRemove_type, 1);
            userLambdaQueryWrapper.ne(Outbound::getRemove_type, 2);
        }
        userLambdaQueryWrapper.like(Outbound::getDevice_sn, sn);
        userLambdaQueryWrapper.eq(Outbound::getUserkey, userkey);
        userLambdaQueryWrapper.like(Outbound::getName, name);
        userIPage = outBoundMapper.selectPage(userPage, userLambdaQueryWrapper);
        PageOutBound pageOutBound = new PageOutBound(userIPage.getRecords(), userIPage.getPages(), userIPage.getTotal());
        return pageOutBound;
    }

    public boolean check(OutBoundMapper outBoundMapper, Outbound outBound) {
        QueryWrapper<Outbound> queryWrapper = Wrappers.query();
        queryWrapper.eq("device_sn", outBound.getDevice_sn());
        Outbound Person1 = outBoundMapper.selectOne(queryWrapper);
        if (Person1 == null) {
            return false;
        } else {
            return true;
        }
    }
}