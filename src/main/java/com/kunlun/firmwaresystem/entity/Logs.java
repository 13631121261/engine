package com.kunlun.firmwaresystem.entity;

public class Logs {
    int id;
    String   operation;
    String   data;
    String url;
    String ip;
    String agent;
    String  userkey;
    String   username;
    String customer_key;
    String project_key;
    long create_time;
    String    nickname;
    public Logs(){

    }
    public Logs(
            String   operation,
            String   data,
            String url,
            String ip,
            String agent,
            String  userkey,
            String   username,
            String customer_key,
            String project_key,
            long create_time,
            String    nickname){
    this.create_time=create_time;
    this.customer_key=customer_key;
    this.data=data;
    this.nickname=nickname;
    this.project_key=project_key;
    this.url=url;
    this.operation=operation;
    this.ip=ip;
    this.agent=agent;
    this.userkey=userkey;
    this.nickname=nickname;
    this.username=username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
