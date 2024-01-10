package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class Map {
    int id;
    String user_key;
    String project_key;
    String name;
    String data;
    long create_time;
    String map_key;
    String map_id;
    String project_name;
    double width;
    double height;
    double proportion;
    String customer_key;
    @TableField(exist = false)
    int sum=0;
    public Map() {

    }

    public Map(double width,
               double height,
               String user_key,
               String project_key,
               String name,
               String data, String project_name,    String customer_key) {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记

        this.create_time = System.currentTimeMillis()/1000;
        this.data = data;
        this.user_key = user_key;
        this.project_key = project_key;
        this.name = name;
        this.project_name = project_name;
        this.width = width;
        this.height = height;
        this.customer_key=customer_key;
        map_key = Base64.getEncoder().encodeToString((name + "_" + create_time).getBytes()).replaceAll("\\+", "");
        proportion = 30;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public String getMap_key() {
        return map_key;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getMap_id() {
        return map_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setProject_key(String projet_key) {
        this.project_key = projet_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public double getProportion() {
        return proportion;
    }

    public double getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "Map{" +
                "id=" + id +
                ", user_key='" + user_key + '\'' +
                ", projet_key='" + project_key + '\'' +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", create_time='" + create_time + '\'' +
                ", map_key='" + map_key + '\'' +
                ", project_name='" + project_name + '\'' +
                '}';
    }
}
