package com.kunlun.firmwaresystem.sql;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.User;
import com.kunlun.firmwaresystem.mappers.CustomerMapper;
import com.kunlun.firmwaresystem.mappers.UserMapper;

import java.util.HashMap;
import java.util.List;

public class Customer_sql {

    public List<Customer> getCustomer(CustomerMapper customerMapper, Customer customer) {
        QueryWrapper<Customer> queryWrapper = Wrappers.query();
        queryWrapper.eq("password", customer.getPassword());
        queryWrapper.eq("username", customer.getUsername());
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Customer> a = customerMapper.selectList(queryWrapper);
        return a;
    }
    public Customer getCustomer(CustomerMapper customerMapper, int id) {
        QueryWrapper<Customer> queryWrapper = Wrappers.query();
        queryWrapper.eq("id", id);

//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        Customer a = customerMapper.selectById( id);
        return a;
    }
    public int updateCustomer(CustomerMapper customerMapper, Customer customer) {


    //若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
    int a = customerMapper.updateById(customer);
        return a;
}

    public int deleteCustomer(CustomerMapper customerMapper, int id) {
        int a = customerMapper.deleteById(id);
        return a;
    }

    public List<Customer> getCustomer(CustomerMapper customerMapper,String user_key,String project_key,String quickSearch) {
        QueryWrapper<Customer> queryWrapper = Wrappers.query();
        queryWrapper.eq("userkey", user_key).eq("project_key", project_key).like("username",quickSearch).or().eq("userkey", user_key).eq("project_key", project_key).like("nickname",quickSearch);

//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Customer> a = customerMapper.selectList(queryWrapper);
        return a;
    }
    public HashMap<String, Customer> getAllCustomer(CustomerMapper customerMapper) {
        QueryWrapper<Customer> queryWrapper = Wrappers.query();
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Customer> a = customerMapper.selectList(queryWrapper);
        HashMap<String, Customer> map = new HashMap<>();
        for (Customer customer : a) {
            map.put(customer.getCustomerkey(), customer);
        }
        return map;
    }

    /**
     * 查询是否已存在此用户
     */
    public boolean checkUser(CustomerMapper customerMapper, Customer customer) {
        QueryWrapper<Customer> queryWrapper = Wrappers.query();
        queryWrapper.eq("username", customer.getUsername());

        // queryWrapper.eq("username", user.getCustomername());
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Customer> a = customerMapper.selectList(queryWrapper);
        if (a != null && a.size()>0) {
            return true;
        } else
            return false;
    }
//根据角色查询是否有关联管理账号
    public boolean check(CustomerMapper customerMapper, int roles_id,String user_key) {
        QueryWrapper<Customer> queryWrapper = Wrappers.query();
        queryWrapper.like("roles_id", roles_id);
        queryWrapper.eq("userkey", user_key);
        // queryWrapper.eq("username", user.getCustomername());
//若是数据库中符合传入的条件的记录有多条，那就不能用这个方法，会报错
        List<Customer> a = customerMapper.selectList(queryWrapper);
        if (a != null && a.size()>0) {
            return true;
        } else
            return false;
    }
    /**
     * 创建用户
     */
    public int addUser(CustomerMapper customerMapper, Customer customer) {
        boolean status=checkUser(customerMapper,customer);
        if(!status){
            int result = customerMapper.insert(customer);
            return result;
        }
       else{
           return -1;
        }
    }
}
