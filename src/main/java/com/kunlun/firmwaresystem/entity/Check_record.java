package com.kunlun.firmwaresystem.entity;

public class Check_record {
    int id;
    String createtime;
    int online;
    int offline;
    int count;
    int check_type;
    int sos_count;
    int unbind;
    int onbind;
    int check_count;
    String record_key;
    int check_countout;
    String userkey;
    String customer_key;
    public Check_record(){

    }
    public Check_record(
            String createtime,
            int online,
            int offline,
            int count,
            int check_count,
            int check_countout,
            int check_type,
            int sos_count,
               int unbind,
                    int onbind,String key,String userkey, String customer_key){
        this.check_type=check_type;
        this.count=count;
        this.createtime=createtime;
        this.offline=offline;
        this.onbind=onbind;
        this.unbind=unbind;
        this.online=online;
        this.sos_count=sos_count;
        this.record_key=key;
        this.check_count=check_count;
        this.check_countout=check_countout;
        this.userkey=userkey;
        this.customer_key=customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public String getCreatetime() {
        return createtime;
    }

    public int getId() {
        return id;
    }

    public int getOnline() {
        return online;
    }

    public int getCount() {
        return count;
    }

    public int getCheck_type() {
        return check_type;
    }

    public int getOffline() {
        return offline;
    }

    public int getSos_count() {
        return sos_count;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getCheck_count() {
        return check_count;
    }

    public void setCheck_count(int check_count) {
        this.check_count = check_count;
    }

    public void setCheck_type(int check_type) {
        this.check_type = check_type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }

    public int getCheck_countout() {
        return check_countout;
    }

    public void setCheck_countout(int check_countout) {
        this.check_countout = check_countout;
    }

    public void setSos_count(int sos_count) {
        this.sos_count = sos_count;
    }

    public void setOnbind(int onbind) {
        this.onbind = onbind;
    }

    public void setUnbind(int unbind) {
        this.unbind = unbind;
    }

    public int getOnbind() {
        return onbind;
    }

    public int getUnbind() {
        return unbind;
    }

    public String getRecord_key() {
        return record_key;
    }

    public void setRecord_key(String record_key) {
        this.record_key = record_key;
    }
}
