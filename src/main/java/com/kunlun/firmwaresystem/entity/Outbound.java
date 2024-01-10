package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Outbound {

    int id;
    String device_sn;
    String remake;
    int remove_type;
    @TableField(exist = false)
    String type_name;
    String idcard;
    String person_name;
    String userkey;
    @TableField(exist = false)
    String douserkey_name;
    @TableField(exist = false)
    String userkey_name;
    String createtime;
    String name;
    String customer_key;
    String reborrow_time;
    public Outbound(){

    }
    public Outbound(String device_sn,
                    String remake,
                    int remove_type,
                    String idcard,
                    String person_name,
                    String userkey,
                    String name, String customer_key
            ){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        this.createtime = sdf.format(date);
        this.device_sn=device_sn;
        this.idcard=idcard;
        this.person_name=person_name;
        this.remake=remake;
        this.userkey=userkey;
        this.customer_key=customer_key;
        this.remove_type=remove_type;
        this.name=name;
    }

    public void setReborrow_time(String reborrow_time) {
        this.reborrow_time = reborrow_time;
    }

    public String getUserkey_name() {
        return userkey_name;
    }

    public String getReborrow_time() {
        return reborrow_time;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public void setDevice_sn(String device_sn) {
        this.device_sn = device_sn;
    }


    public void setRemake(String remake) {
        this.remake = remake;
    }

    public void setRemove_type(int remove_type) {
        this.remove_type = remove_type;
    }

    public int getId() {
        return id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getUserkey() {
        return userkey;
    }

    public int getRemove_type() {
        return remove_type;
    }

    public String getDevice_sn() {
        return device_sn;
    }

    public String getIdcard() {
        return idcard;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }



    public String getRemake() {
        return remake;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDouserkey_name(String douserkey_name) {
        this.douserkey_name = douserkey_name;
    }

    public String getDouserkey_name() {
        return douserkey_name;
    }

    public void setUserkey_name(String userkey_name) {
        this.userkey_name = userkey_name;
    }

    public String getCustomerkey_name() {
        return userkey_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
