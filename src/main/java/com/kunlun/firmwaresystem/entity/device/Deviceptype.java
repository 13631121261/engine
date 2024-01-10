package com.kunlun.firmwaresystem.entity.device;

import com.baomidou.mybatisplus.annotation.TableField;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Deviceptype {
    int id;
    String name;
    long createtime,update_time;
    String userkey;
    String customer_key;
    String customer_name;
    String project_key;
    String describes;


    public Deviceptype(){

    }

    public Deviceptype(String name,
                       String userkey,  String project_key,String customer_key,  String customer_name,String describes){
        this.name=name;
        this.customer_name=customer_name;
        this.customer_key=customer_key;
        this.userkey=userkey;
        this.project_key=project_key;

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        this.describes=describes;
        this.createtime=System.currentTimeMillis()/1000;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setDescribes(String describe) {
        this.describes = describe;
    }

    public String getDescribes() {
        return describes;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getUpdate_time() {
        return update_time;
    }
}
