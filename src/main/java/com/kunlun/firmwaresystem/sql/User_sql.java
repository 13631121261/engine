package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kunlun.firmwaresystem.entity.User;
import com.kunlun.firmwaresystem.mappers.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_sql {

    public List<User> getCustomer(UserMapper userMapper, User user) {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.eq("password", user.getPassword());
        queryWrapper.eq("username", user.getCustomername());
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<User> a = userMapper.selectList(queryWrapper);
        return a;
    }

    public HashMap<String, User> getAllUser(UserMapper userMapper) {
        QueryWrapper<User> queryWrapper = Wrappers.query();
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<User> a = userMapper.selectList(queryWrapper);
        HashMap<String, User> map = new HashMap<>();
        for (User user : a) {
            map.put(user.getUserkey(), user);
        }
        return map;
    }

    /**
     * 查询是否已存在此用户
     */
    public boolean checkUser(UserMapper userMapper, User user) {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.eq("username", user.getCustomername());
        // queryWrapper.eq("username", user.getCustomername());
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<User> a = userMapper.selectList(queryWrapper);
        if (a != null && a.size()>0) {
            return true;
        } else
            return false;
    }

    /**
     * 创建用户
     */
    public int addUser(UserMapper userMapper, User user) {
        boolean status=checkUser(userMapper,user);
        if(!status){
            int result = userMapper.insert(user);
            return result;
        }
       else{
           return -1;
        }
    }
}
