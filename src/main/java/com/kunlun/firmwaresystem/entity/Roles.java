package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class Roles {
    //账号的菜单展示权限
    int id;
    String    name;
    String ruless;
    @TableField(exist=false)
    String[] rules;
    String  project_key;
    String user_key;
    String  status;
    String details;
    long create_time,update_time;
    public Roles(){
        create_time=System.currentTimeMillis()/1000;
        update_time=create_time;
    }
    public Roles(
            String    name,
            String rules,
            String  project_key,
            String details,
            String user_key,
            String  status){
        this.name=name;
        this.ruless=rules;
        this.project_key=project_key;
        this.status=status;
        this.details=details;
        this.user_key=user_key;
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

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public long getUpdate_time() {
        return update_time;
    }



    public void setName(String name) {
        this.name = name;
    }

/*    if(rules!=null&&rules.length()>0){
        if(rules.startsWith(",")){
            ruless=rules.substring(1).split(",");
        }else{
            ruless=rules.split(",");
        }
    }*/


    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRuless(String ruless) {
        this.ruless = ruless;
        if(ruless!=null&&ruless.length()>0){
            if(ruless.startsWith(",")){
                 rules=ruless.substring(1).split(",");

            }else{
                rules=ruless.split(",");

            }
        }
    }

    public void setRules(String[] rulesa) {
        if(ruless!=null&&ruless.length()>0){
            if(ruless.startsWith(",")){
                rules=ruless.substring(1).split(",");

            }else{
                rules=ruless.split(",");

            }
        }

    }

    public String getRuless() {
        return ruless;
    }

    public String[] getRules() {
        if(rules!=null)
        return rules;
        else{
            if(ruless!=null&&ruless.length()>0){
                if(ruless.startsWith(",")){
                    rules=ruless.substring(1).split(",");

                }else{
                    rules=ruless.split(",");

                }
            }
            return rules;
        }
    }
}
