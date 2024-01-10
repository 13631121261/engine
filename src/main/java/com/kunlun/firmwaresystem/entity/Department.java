package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Department {
    int id;
    String project_key;
    String name;
    long createtime;
    String userkey;
    int p_id;
    @TableField(exist = false)
    public List<Department> departmentlist;
    String customer_key;
    @TableField(exist = false)
    public  int root =1;
    public Department(){

    }

    public Department( String name,
            String userkey,
            int p_id,    String customer_key){
    this.name=name;
    this.customer_key=customer_key;
    this.userkey=userkey;
    this.p_id=p_id;
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        this.createtime=System.currentTimeMillis()/1000;
        this.customer_key=customer_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    public int getRoot() {
        return root;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getCustomer_key() {
        return customer_key;
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

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public void setDepartmentlist(List<Department> departmentlist) {
        this.departmentlist = departmentlist;
    }

    public List<Department> getDepartmentlist() {
        return departmentlist;
    }
    public void addDepartment(Department department){
        if(departmentlist==null){
            setDepartmentlist(new ArrayList<Department>());
            departmentlist.add(department);
        }else
        departmentlist.add(department);
    }

}
