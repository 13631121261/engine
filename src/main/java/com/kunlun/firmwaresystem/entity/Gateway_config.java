package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class Gateway_config implements Serializable{
    int id;
    String name;
    String wifi_version;
    String ble_version;
    String user_key;
    long create_time;
    long update_time;
    String ip;
    int port;
    String sub_topic;
    String pub_topic;
    int server_type;
    int scan_out = 1;
    int scan_interval = 1000;
    int report_type = 2;
    int filter_rssi=0;
    boolean is_filter_rssi;
    @TableField(exist = false)
    String[] filter_name=new String[8];
    boolean is_filter_name=false;
    String filter_names;
    int filter_ibeacon = 0;
    @TableField(exist = false)
    String[] filter_uuid=new String[8];
    String filter_uuids;
    boolean is_filter_uuid=false;
    @TableField(exist = false)
    String filter_companyid[]=new String[8];
    String filter_companyids;
    int gateway_count;
    String customer_key;
    String config_key;
    String rules_key;
    String rules_name;
    int broadcast;
    String project_key;
    @TableField(exist = false)
    String services_uuid[]=new String[8];
    String services_uuids;
    boolean is_services_uuid;
    boolean is_filter_mac;
    @TableField(exist = false)
    String[] filter_mac=new String[2];
    String filter_macs;
    boolean is_filter_companyid;
    int isyn=0;
    public Gateway_config() {
    }

    public void setIsyn(int isyn) {
        this.isyn = isyn;
    }

    public int getIsyn() {
        return isyn;
    }
    /*  public Gateway_config(String name, String user_key,String address,String customer_name,String rules_key){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        this.create_time=sdf.format(date);
        this.name=name;
        this.user_key=user_key;
        this.address=address;
        this.customer_name=customer_name;
        project_key=  Base64.getEncoder().encodeToString((name+"_"+date.getTime()).getBytes()).replaceAll("/+","");
        this.rules_key=rules_key;
    }*/
/*
    public Gateway_config(String name, String user_key, String address, String ble_version, String wifi_version, String customer_key) {

        this.create_time = System.currentTimeMillis()/1000;
        this.name = name;
        this.user_key = user_key;
        this.ble_version = ble_version;
        this.customer_key=customer_key;
        this.wifi_version = wifi_version;
        config_key = Base64.getEncoder().encodeToString((name + "_" + create_time).getBytes()).replaceAll("\\+", "");
    }*/

    public void setConfig_key1(){
        config_key = Base64.getEncoder().encodeToString((name + "_" + create_time).getBytes()).replaceAll("\\+", "");
    }
    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setIs_filter_name(boolean is_filter_name) {

        this.is_filter_name = is_filter_name;
    }

    public boolean isIs_filter_name() {
        return is_filter_name;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public String getFilter_names() {

        return filter_names;
    }

    public void setFilter_names(String filter_names) {
        this.filter_names = filter_names;
        if(filter_names!=null||filter_names.length()>0){
           this.filter_name=toStringArray(filter_names,8);
        }
    }

    public void setIs_filter_uuid(boolean is_filter_uuid) {
        this.is_filter_uuid = is_filter_uuid;
    }

    public boolean isIs_filter_uuid() {
        return is_filter_uuid;
    }

    public String getRules_key() {
        return rules_key;
    }

    public void setRules_key(String rules_key) {
        this.rules_key = rules_key;
    }

    public String getRules_name() {
        return rules_name;
    }

    public void setRules_name(String rules_name) {
        this.rules_name = rules_name;
    }

    public void setBroadcast(int broadcast) {
        this.broadcast = broadcast;
    }

    public int getBroadcast() {
        return broadcast;
    }

    public int getGateway_count() {
        return gateway_count;
    }

    public void setGateway_count(int gateway_count) {
        this.gateway_count = gateway_count;
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

    public String getWifi_version() {
        return wifi_version;
    }

    public void setWifi_version(String wifi_version) {
        if(wifi_version==null||wifi_version.length()<10){
            this.wifi_version="";
        }else
        this.wifi_version = wifi_version;
    }

    public String getBle_version() {
        return ble_version;
    }

    public void setBle_version(String ble_version) {
        if(ble_version==null||ble_version.length()<10){
            this.ble_version="";
        }else
        this.ble_version = ble_version;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSub_topic() {
        return sub_topic;
    }

    public void setSub_topic(String sub_topic) {
        println("设置订阅主题="+sub_topic);
        this.sub_topic = sub_topic;
    }

    public String getPub_topic() {
        return pub_topic;
    }

    public void setPub_topic(String pub_topic) {
        println("设置发布主题="+pub_topic);
        this.pub_topic = pub_topic;
    }

    public int getServer_type() {
        return server_type;
    }

    public void setServer_type(int server_type) {
        this.server_type = server_type;
    }

    public int getScan_out() {
        return scan_out;
    }

    public void setScan_out(int scan_out) {
        this.scan_out = scan_out;
    }

    public int getScan_interval() {
        return scan_interval;
    }

    public void setScan_interval(int scan_interval) {
        this.scan_interval = scan_interval;
    }

    public int getReport_type() {
        return report_type;
    }

    public void setReport_type(int report_type) {
        this.report_type = report_type;
    }

    public int getFilter_rssi() {
        return filter_rssi;
    }

    public boolean isIs_filter_rssi() {
        return is_filter_rssi;
    }

    public boolean isIs_filter_mac() {
        return is_filter_mac;
    }

    public void setFilter_rssi(int filter_rssi) {
        this.filter_rssi = filter_rssi;
    }

    public String[] getFilter_name() {
   /*     if(filter_names!=null&&filter_names.length()>0){
            String[] names=filter_names.split("-");
            int i=0;
            for(String name:names){
                if(name!=null&&name.length()>0){
                    filter_name[i]=name;
                    i++;
                }
            }
        }*/
        return filter_name;
    }


    public void setFilter_name(String[] filter_name) {
    if(filter_name!=null&&filter_name.length>0){
        this.filter_names = toString(filter_name);
    }
        this.filter_name = filter_name;

    }

    public int getFilter_ibeacon() {
        return filter_ibeacon;
    }

    public void setFilter_ibeacon(int filter_ibeacon) {
        this.filter_ibeacon = filter_ibeacon;
    }
    public String getFilter_uuids() {
        if(filter_uuids!=null){
             filter_uuids.toLowerCase();
        }
        return filter_uuids;

    }

    public void setFilter_uuids(String filter_uuid) {
        if(filter_uuid!=null){
            this.filter_uuids=filter_uuid.toLowerCase();
        }

        if(filter_uuid!=null&&filter_uuid.length()>0){
          this.filter_uuid= toStringArray(filter_uuid,8);
        }

    }

    public void setIs_filter_rssi(boolean is_filter_rssi) {
        this.is_filter_rssi = is_filter_rssi;
    }



    public void setFilter_macs(String filter_macs) {

        this.filter_macs = filter_macs;
        if(filter_macs!=null&&filter_macs.length()>0){
            this.filter_mac=toStringArray(filter_macs,2);
        }
    }

    public void setIs_filter_mac(boolean is_filter_mac) {
        this.is_filter_mac = is_filter_mac;

    }

    public String getFilter_macs() {


        return filter_macs;
    }

    public void setFilter_mac(String[] filter_mac) {
        this.filter_mac = filter_mac;
        if(filter_mac!=null&&filter_mac.length>0){
            this.filter_macs=toString(filter_mac);
        }
    }

    public String[] getFilter_mac() {
        if(filter_mac[0]!=null&&filter_mac[1]!=null){
            this.filter_macs=filter_mac[0].toLowerCase()+"-"+filter_mac[1].toLowerCase();
        }else{
            this.filter_macs=filter_mac[0]+"-"+filter_mac[1];
        }

        return filter_mac;
    }

    public void setFilter_uuid(String[] filter_uuid) {
        println("运行这里");
        this.filter_uuid = filter_uuid;
        if(filter_uuid!=null&&filter_uuid.length>0){
            this.filter_uuids=toString(filter_uuid);
        }


    }

    public String[]  getFilter_uuid() {

        return filter_uuid;
    }


    public void setFilter_companyid(String[] filter_companyid) {
        println("运行123");
        this.filter_companyid=filter_companyid;
        if(filter_companyid!=null&&filter_companyid.length>0){
            this.filter_companyids=toString(filter_companyid);
        }
    }



    public void setFilter_companyids(String filter_companyids) {
        this.filter_companyids = filter_companyids;
       if(filter_companyids!=null&&filter_companyids.length()>0){
          this.filter_companyid= toStringArray(filter_companyids,8);
       }

    }

    public String[] getFilter_companyid() {
        return filter_companyid;
    }

    public String getFilter_companyids() {
        return filter_companyids;
    }

    public String getConfig_key() {
        return config_key;
    }

    public void setConfig_key(String config_key) {
        this.config_key = config_key;
    }

    public void setIs_services_uuid(boolean is_services_uuid) {
        this.is_services_uuid = is_services_uuid;
    }

    public String getServices_uuids() {
        return services_uuids;
    }

    public void setServices_uuid(String[] services_uuid) {
        this.services_uuid = services_uuid;
        if(services_uuid!=null&&services_uuid.length>0){
            this.services_uuids=toString(services_uuid);
        }
    }

    public void setServices_uuids(String services_uuids) {
        this.services_uuids = services_uuids;
        if(services_uuids!=null&&services_uuids.length()>0){
            this.services_uuid=toStringArray(services_uuids,8);
        }
    }

    public String[] getServices_uuid() {

        return services_uuid;
    }

    public boolean isIs_services_uuid() {
        return is_services_uuid;
    }



    private String toString(String[] a){
        println("执行这里了"+a.toString());
        String b="";
        int i=0;
        if(a!=null&&a.length>0){
            for(String c:a){
                if(c==null||c.equals("null")){
                    c="";
                }
                if(i<a.length-1){
                    b=b+c+"-";
                }else{
                    b=b+c;
                }
                i++;
            }
        }
        return b;
    }
    private String[] toStringArray(String a,int len){
        println("执行这里"+a);
        if(a!=null){
            a=a.replaceAll("null","");

        }
        String[] b=new String[len];
        if(a!=null&&a.length()>0){
            String[] c=a.split("-");
            if(c!=null&&c.length<=len){
                for(int i=0;i<c.length;i++){
                    b[i]=c[i];
                }
            }
        }
        return b;
    }


    public void setIs_filter_companyid(boolean is_filter_companyid) {
        this.is_filter_companyid = is_filter_companyid;
    }

    public boolean isIs_filter_companyid() {
        return is_filter_companyid;
    }

    @Override
    public String toString() {
        return "Gateway_config{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", wifi_version='" + wifi_version + '\'' +
                ", ble_version='" + ble_version + '\'' +
                ", user_key='" + user_key + '\'' +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", sub_topic='" + sub_topic + '\'' +
                ", pub_topic='" + pub_topic + '\'' +
                ", server_type=" + server_type +
                ", scan_out=" + scan_out +
                ", scan_interval=" + scan_interval +
                ", report_type=" + report_type +
                ", filter_rssi=" + filter_rssi +
                ", is_filter_rssi=" + is_filter_rssi +
                ", filter_name=" + Arrays.toString(filter_name) +
                ", is_filter_name=" + is_filter_name +
                ", filter_names='" + filter_names + '\'' +
                ", filter_ibeacon=" + filter_ibeacon +
                ", filter_uuid=" + Arrays.toString(filter_uuid) +
                ", filter_uuids='" + filter_uuids + '\'' +
                ", is_filter_uuid=" + is_filter_uuid +
                ", filter_companyid=" + Arrays.toString(filter_companyid) +
                ", filter_companyids='" + filter_companyids + '\'' +
                ", gateway_count='" + gateway_count + '\'' +
                ", customer_key='" + customer_key + '\'' +
                ", config_key='" + config_key + '\'' +
                ", rules_key='" + rules_key + '\'' +
                ", rules_name='" + rules_name + '\'' +
                ", broadcast=" + broadcast +
                ", project_key='" + project_key + '\'' +
                ", services_uuid=" + Arrays.toString(services_uuid) +
                ", services_uuids='" + services_uuids + '\'' +
                ", is_services_uuid=" + is_services_uuid +
                ", is_filter_mac=" + is_filter_mac +
                ", filter_mac=" + Arrays.toString(filter_mac) +
                ", filter_macs='" + filter_macs + '\'' +
                ", is_filter_companyid=" + is_filter_companyid +
                '}';
    }
}
