package com.kunlun.firmwaresystem.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@ApiModel(description = "用户信息")
public class Project implements Serializable {
    @ApiModelProperty(value = "用户ID", example = "1")
    int id;
    @ApiModelProperty(value = "用户名", example = "JohnDoe")
    String project_name;
    String project_key;
    String create_time;
    String info;
    String update_time;
    String user_key;
    int arssi;
    double n;
    double z;
    int used;
    public Project(){

       this. project_name="项目名称";
        this.info="项目的详细信息";
    }
    public Project(String project_name,
            String info,
            String user_key){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        this.create_time = sdf.format(date);
        this.update_time = sdf.format(date);
        this.project_name=project_name;
        this.  info=info;
        this.  user_key=user_key;
        project_key = Base64.getEncoder().encodeToString((project_name + "_" + System.currentTimeMillis()).getBytes()).replaceAll("\\+", "");
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getUsed() {
        return used;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return z;
    }

    public void setArssi(int arssi) {
        this.arssi = arssi;
    }

    public int getArssi() {
        return arssi;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getN() {
        return n;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getCreate_time() {
        if(create_time!=null&&create_time.length()>19){
          return create_time.substring(0, 19);
        }else
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time=create_time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUpdate_time() {
        if(update_time!=null&&update_time.length()>19){
            return update_time.substring(0, 19);
        }else
            return update_time;

    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", project_name='" + project_name + '\'' +
                ", project_key='" + project_key + '\'' +
                ", create_time='" + create_time + '\'' +
                ", info='" + info + '\'' +
                ", update_time='" + update_time + '\'' +
                ", user_key='" + user_key + '\'' +
                ", arssi=" + arssi +
                ", n=" + n +
                ", z=" + z +
                ", used=" + used +
                '}';
    }
}
