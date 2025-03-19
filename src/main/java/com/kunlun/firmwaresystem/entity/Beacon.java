package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kunlun.firmwaresystem.location_util.backup.Gateway_device;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Beacon extends Tag {
    int id;
    double n;
    String map_key;
    @TableField(exist = false)
    int sos=-1;
    @TableField(exist = false)
    int run=-1;
    long createtime;
    String customer_key;
    String project_key;
    @TableField(exist = false)
    double x=0;
    @TableField(exist = false)
    double y=0;

    int l_type;

    @TableField(exist = false)
    ArrayList<Gateway_device> gatewayDevices;
    @TableField(exist = false)
    ArrayList<Gateway_device> useDatalist;

    public Beacon() {

    }
    public Beacon(String mac) {
    this.mac=mac;
    }
    public Beacon( String mac,
                  String user_key,String type, String customer_key) {

        this.mac = mac;
        this.type=type;
        this.user_key = user_key;
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        this.createtime = System.currentTimeMillis()/1000;
        this.customer_key=customer_key;

    }

    public void setGatewayDevices(ArrayList<Gateway_device> gatewayDevices) {
        this.gatewayDevices = gatewayDevices;
    }

    public ArrayList<Gateway_device> getGatewayDevices() {
        return gatewayDevices;
    }

    public void setUseDatalist(ArrayList<Gateway_device> useDatalist) {
        this.useDatalist = useDatalist;
    }

    public ArrayList<Gateway_device> getUseDatalist() {
        return useDatalist;
    }

    public void setL_type(int l_type) {
        this.l_type = l_type;
    }

    public int getL_type() {
        return l_type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }


    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getRun() {
        return run;
    }

    public Beacon(int id, String mac,
                  String user_key, String type) {
        this.id = id;

        this.mac = mac;


        this.user_key = user_key;
        this.type = type;

    }



    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getProject_key() {
        return project_key;
    }

    @Override
    public void setBt(String bt) {
        super.setBt(bt);
    }

    @Override
    public String getBt() {
        return super.getBt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSos(int sos) {
        this.sos = sos;
    }


    public int getSos() {
        return sos;
    }


    public String getMap_key() {
        return map_key;
    }

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getCreatetime() {
        return createtime;
    }



    @Override
    public String toString() {
        return "Beacon{" +
                "id=" + id +
                ", n=" + n +
                ", map_key='" + map_key + '\'' +
                ", sos=" + sos +
                ", run=" + run +
                ", createtime=" + createtime +
                ", customer_key='" + customer_key + '\'' +
                ", project_key='" + project_key + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", type=" + type +
                ", mac='" + mac + '\'' +
                ", bt=" + bt +
                ", user_key='" + user_key + '\'' +
                ", gateway_address='" + gateway_address + '\'' +
                ", online=" + online +
                ", lastTime=" + lastTime +
                ", rssi=" + rssi +
                '}';
    }
}
