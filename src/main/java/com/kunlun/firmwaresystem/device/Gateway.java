package com.kunlun.firmwaresystem.device;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Gateway implements Serializable {
    int syn = 0;
    String synStr = "";
    int id;
    String name;
    String address;
    String wifi_address;
    String wifi_version;
    String ble_version;
    String config_key;
    String config_name;
    String user_key;
    long create_time;
    long update_time;
    String ip;
    int port;
    String sub_topic;
    String pub_topic;
    int server_type;
    int scan_out;
    int scan_interval;
    int report_type;
    String filter_rssi;
    String filter_name;
    String filter_dev_mac;
    int filter_ibeacon;
    String filter_uuid;
    String filter_companyId;
    int useplatform;
    int online;
    String online_txt;
    long lasttime;
    int broadcast;
    double x, y,z;


    long onlinetime;
    long revicecount;
    double n;
    int area_id;
    String area_name;
    String customerkey;
    String project_key;
    String map_key;
    String map_name;
    @TableField(exist = false)
    int reboot=0;
    int isyn=0;
    String scan_filter_serv_data_uuid;
    String wifi_onoff;
    String wlan_ssid;
    String wlan_ip;
    String wlan_mask;
    String op_mode;
    String wan_mode;
    String wan_ip;
    String wan_mask;
    String wan_gw;
    @TableField(exist = false)
    String CRC_FLAG;
    @TableField(exist = false)
    String up_time;
    int arssi;





    public Gateway(String name, String address, String wifi_address, String config_key, String user_key, String config_name, double x, double y,int area_id,  String customerkey,String project_key) {

        this.create_time =System.currentTimeMillis()/1000;

        this.update_time=System.currentTimeMillis()/1000;
        this.name = name;
        this.address = address;
        this.config_key = config_key;
        this.user_key = user_key;
        this.config_name = config_name;
        this.wifi_address = wifi_address;
        this.x = x;
        this.y = y;
        this.area_id=area_id;
        this.customerkey=customerkey;

        this.project_key = project_key;
    }
    public Gateway(String name, String address,  double x, double y) {
        this.create_time =System.currentTimeMillis()/1000;
        this.lasttime = System.currentTimeMillis()/1000;
        this.name = name;
        this.address = address;
        this.x = x;
        this.y = y;
    }
    public Gateway() {


    }

    public void setArssi(int arssi) {
        this.arssi = arssi;
    }

    public int getArssi() {
        return arssi;
    }

    public void setCRC_FLAG(String CRC_FLAG) {
        this.CRC_FLAG = CRC_FLAG;
    }

    public String getCRC_FLAG() {
        return CRC_FLAG;
    }

    public String getWifi_onoff() {
        return wifi_onoff;
    }

    public void setWifi_onoff(String wifi_onoff) {
        this.wifi_onoff = wifi_onoff;
    }

    public String getWlan_ssid() {
        return wlan_ssid;
    }

    public void setWlan_ssid(String wlan_ssid) {
        this.wlan_ssid = wlan_ssid;
    }

    public String getWlan_ip() {
        return wlan_ip;
    }

    public void setWlan_ip(String wlan_ip) {
        this.wlan_ip = wlan_ip;
    }

    public String getWlan_mask() {
        return wlan_mask;
    }

    public void setWlan_mask(String wlan_mask) {
        this.wlan_mask = wlan_mask;
    }

    public String getOp_mode() {
        return op_mode;
    }

    public void setOp_mode(String op_mode) {
        this.op_mode = op_mode;
    }

    public String getWan_mode() {
        return wan_mode;
    }

    public void setWan_mode(String wan_mode) {
        this.wan_mode = wan_mode;
    }

    public String getWan_ip() {
        return wan_ip;
    }

    public void setWan_ip(String wan_ip) {
        this.wan_ip = wan_ip;
    }

    public String getWan_mask() {
        return wan_mask;
    }

    public void setWan_mask(String wan_mask) {
        this.wan_mask = wan_mask;
    }

    public String getWan_gw() {
        return wan_gw;
    }

    public void setWan_gw(String wan_gw) {
        this.wan_gw = wan_gw;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public String getMap_key() {
        return map_key;
    }

    public String getProject_key() {
        return project_key;
    }


    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }


    public int getReboot() {
        return reboot;
    }

    public void setReboot(int reboot) {
        this.reboot = reboot;
    }

    public void setCustomerkey(String customerkey) {
        this.customerkey = customerkey;
    }

    public int getIsyn() {
        return isyn;
    }

    public void setIsyn(int isyn) {
        this.isyn = isyn;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getN() {
        return n;
    }

    public long getRevicecount() {
        return revicecount;
    }

    public void setRevicecount(long revicecount) {
        this.revicecount = revicecount;
    }

    public void setOnlinetime(long onlinetime) {
        this.onlinetime = onlinetime;
    }

    public long getOnlinetime() {
        return onlinetime;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setWifi_address(String wifi_address) {
        this.wifi_address = wifi_address;
    }

    public String getWifi_address() {
        return wifi_address;
    }

    public void setSynStr(String synStr) {
        this.synStr = synStr;
    }

    public String getSynStr() {
        return synStr;
    }

    public void setSyn(int syn) {
        this.syn = syn;
    }

    public int getSyn() {
        return syn;
    }

    public void setBroadcast(int broadcast) {
        this.broadcast = broadcast;
    }

    public void Broadcast1(boolean broadcast) {
        if (broadcast) {
            this.broadcast = 1;
        } else {
            this.broadcast = 0;
        }

    }

    public int getBroadcast() {
        return broadcast;
    }

    public void setConfig_name(String config_name) {
        this.config_name = config_name;
    }

    public String getConfig_name() {
        return config_name;
    }

    public int getOnline() {
        return online;
    }



    public void setOnline(int online) {
        this.online = online;
    }

    public String getOnline_txt() {
        return online_txt;
    }

    public void setOnline_txt(String online_txt) {
        this.online_txt = online_txt;
    }

    public Gateway(String address) {
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWifi_version() {
        return wifi_version;
    }

    public void setUp_time(String up_time) {
        this.up_time = up_time;
    }

    public String getUp_time() {
        return up_time;
    }

    public void setWifi_version(String wifi_version) {
        if(wifi_version==null||wifi_version.length()<5){
            this.wifi_version="";
        }else
        this.wifi_version = wifi_version;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public String getBle_version() {
        return ble_version;
    }

    public void setBle_version(String ble_version) {
        if(ble_version==null||ble_version.length()<5){
            this.ble_version="";
        }else
        this.ble_version = ble_version;
    }

    public String getConfig_key() {
        return config_key;
    }

    public void setConfig_key(String config_key) {
        this.config_key = config_key;
    }

    public void setScan_interval(int scan_interval) {
        this.scan_interval = scan_interval;
    }

    public String getUser_key() {
        return user_key;
    }

    public String getCustomerkey() {
        return customerkey;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
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
        this.sub_topic = sub_topic;
    }

    public String getPub_topic() {
        return pub_topic;
    }

    public void setPub_topic(String pub_topic) {
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

    public void Scan_out1(boolean scan_out) {
        //    println("扫描输出="+scan_out);
        if (scan_out) {
            this.scan_out = 1;
        } else {
            this.scan_out = 0;
        }

    }

    public int getScan_interval() {
        return scan_interval;
    }

    public void Scan_interval(int scan_interval) {
        this.scan_interval = scan_interval;
    }

    public int getReport_type() {
        return report_type;
    }

    public void setReport_type(int report_type) {
        this.report_type = report_type;
    }

    public void Report_type1(boolean request, boolean card) {
        if (card) {
            this.report_type = 4;
            return;
        }
        if (request) {
            this.report_type = 2;
        } else {
            this.report_type = 1;
        }

    }

    public String getFilter_rssi() {
        return filter_rssi;
    }

    public void setFilter_rssi(String filter_rssi) {
        //  println("过滤信号="+filter_rssi);
        this.filter_rssi = filter_rssi;
    }

    public String getFilter_name() {
        return filter_name;
    }

    public void Filter_name1(String[] filter_names) {
        String filter_name = "";
        if (filter_names != null && filter_names.length > 0) {
            for (int i = 0; i < filter_names.length; i++) {
                if(i==0){
                    filter_name = filter_names[i];
                }else{
                    filter_name = filter_name + "-" + filter_names[i];
                }

            }
        }
        if (filter_names!=null) {
            int l=filter_names.length;
            if(l<8){
                l=8-l;
                String sq="-";
                for(int i=0;i<l;i++){
                    filter_name=filter_name+sq;
                }
            }
        }else{
            filter_name="-------";
        }

        this.filter_name = filter_name;
    }

    public void setFilter_dev_mac(String filter_dev_mac) {
        this.filter_dev_mac = filter_dev_mac;
    }

    public String getFilter_dev_mac() {
        return filter_dev_mac;
    }
    public void setFilter_dev_mac1(String[] filter_dev_macs) {
        String filter_dev_mac = "";
        if (filter_dev_macs != null && filter_dev_macs.length > 0) {

           // for (int i = 0; i < filter_dev_macs.length; i++) {
                filter_dev_mac = filter_dev_macs[0] + "-" + filter_dev_macs[1];
           // }
        }
    if(filter_dev_macs==null){
        filter_dev_mac="-";
    }

        this.filter_dev_mac = filter_dev_mac.toLowerCase();
    }

    public void setFilter_name(String filter_name) {

        this.filter_name = filter_name;
    }

    public int getFilter_ibeacon() {
        return filter_ibeacon;
    }

    public void Filter_ibeacon1(boolean filter_ibeacon) {
        if (filter_ibeacon) {
            this.filter_ibeacon = 1;
        } else {
            this.filter_ibeacon = 0;
        }

    }

    public void setFilter_ibeacon(int filter_ibeacon) {

        this.filter_ibeacon = filter_ibeacon;


    }

    public String getFilter_uuid() {
        return filter_uuid;
    }

    public void setFilter_uuid(String filter_uuid) {
        //println("设置UUID过滤"+filter_uuid);
        this.filter_uuid = filter_uuid;
    }
    public void setFilter_uuid1(String[] filter_uuids) {
        String filter_uuid = "";
        if (filter_uuids != null && filter_uuids.length > 0) {

            for (int i = 0; i < filter_uuids.length; i++) {
                if(i==0)
                {
                    filter_uuid = filter_uuids[i].replaceAll("-","");
                }
                else {
                    filter_uuid = filter_uuid + "-" + filter_uuids[i].replaceAll("-","");
                }

            }
        }
        if(filter_uuids!=null){
            int l=filter_uuids.length;
            if(l<8){
                l=8-l;    String sq="-";
                for(int i=0;i<l;i++){
                    filter_uuid=filter_uuid+sq;
                }
            }
        }else{
            filter_uuid="-------";
        }

        this.filter_uuid = filter_uuid.toLowerCase();

    }

    public String getScan_filter_serv_data_uuid() {
        return scan_filter_serv_data_uuid;
    }

    public void setScan_filter_serv_data_uuid(String scan_filter_serv_data_uuid) {
        this.scan_filter_serv_data_uuid = scan_filter_serv_data_uuid;
    }
    public void setScan_filter_serv_data_uuid1(String[] scan_filter_serv_data_uuids) {
        String scan_filter_serv_data_uuid = "";
        if (scan_filter_serv_data_uuids != null && scan_filter_serv_data_uuids.length > 0) {

            for (int i = 0; i < scan_filter_serv_data_uuids.length; i++) {
                if(i==0){
                    scan_filter_serv_data_uuid =scan_filter_serv_data_uuids[i];
                }else{
                    scan_filter_serv_data_uuid = scan_filter_serv_data_uuid + "-" + scan_filter_serv_data_uuids[i];
                }

            }
            if(scan_filter_serv_data_uuids!=null){
                int l=scan_filter_serv_data_uuids.length;
                if(l<8){
                    l=8-l;    String sq="-";
                    for(int i=0;i<l;i++){
                        scan_filter_serv_data_uuid=scan_filter_serv_data_uuid+sq;
                    }
                }
            }else{
                scan_filter_serv_data_uuid="-------";
            }

        }

        this.scan_filter_serv_data_uuid = scan_filter_serv_data_uuid.toLowerCase();

    }

    public String getFilter_companyId() {
        return filter_companyId;
    }

    public void setFilter_companyId(String filter_companyIds) {

        this.filter_companyId = filter_companyIds;

    }

    public void Filter_companyId1(String[] filter_companyIds) {
      //  println("Filter_companyId1长度="+filter_companyId.length());
        String filter_companyId = "";
        if (filter_companyIds != null && filter_companyIds.length > 0) {

            for (int i = 0; i < filter_companyIds.length; i++) {
                if(i==0){
                    filter_companyId = filter_companyIds[i];
                }else{
                    filter_companyId = filter_companyId + "-" + filter_companyIds[i];
                }


            }
        }
        if(filter_companyIds!=null){
            int l=filter_companyIds.length;
            if(l<8){
                l=8-l;    String sq="-";
                for(int i=0;i<l;i++){
                    filter_companyId=filter_companyId+sq;
                }
            }
        }else{
            filter_companyId="-------";
        }
       // println("测试一样="+filter_companyId);
        this.filter_companyId = filter_companyId.toLowerCase();

    }

    public int getUseplatform() {
        return useplatform;
    }

    public void setUseplatform(int useplatform) {
        this.useplatform = useplatform;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public long getCreate_time() {
        return create_time;
    }

    public long getLasttime() {
        return lasttime;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public void setLasttime(long lasttime) {
    //    System.out.println("最后时间"+lasttime);
        this.lasttime = lasttime;
    }

    @Override
    public String toString() {
        return "Gateway{" +
                "syn=" + syn +
                ", synStr='" + synStr + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", wifi_address='" + wifi_address + '\'' +
                ", wifi_version='" + wifi_version + '\'' +
                ", ble_version='" + ble_version + '\'' +
                ", config_key='" + config_key + '\'' +
                ", config_name='" + config_name + '\'' +
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
                ", filter_rssi='" + filter_rssi + '\'' +
                ", filter_name='" + filter_name + '\'' +
                ", filter_dev_mac='" + filter_dev_mac + '\'' +
                ", filter_ibeacon=" + filter_ibeacon +
                ", filter_uuid='" + filter_uuid + '\'' +
                ", filter_companyId='" + filter_companyId + '\'' +
                ", useplatform=" + useplatform +
                ", online=" + online +
                ", online_txt='" + online_txt + '\'' +
                ", lasttime=" + lasttime +
                ", broadcast=" + broadcast +
                ", x=" + x +
                ", y=" + y +
                ", onlinetime=" + onlinetime +
                ", revicecount=" + revicecount +
                ", n=" + n +
                ", area_id=" + area_id +
                ", customerkey='" + customerkey + '\'' +
                ", project_key='" + project_key + '\'' +
                ", map_key='" + map_key + '\'' +
                ", map_name='" + map_name + '\'' +
                ", reboot=" + reboot +
                ", isyn=" + isyn +
                ", scan_filter_serv_data_uuid='" + scan_filter_serv_data_uuid + '\'' +
                ", wifi_onoff='" + wifi_onoff + '\'' +
                ", wlan_ssid='" + wlan_ssid + '\'' +
                ", wlan_ip='" + wlan_ip + '\'' +
                ", wlan_mask='" + wlan_mask + '\'' +
                ", op_mode='" + op_mode + '\'' +
                ", wan_mode='" + wan_mode + '\'' +
                ", wan_ip='" + wan_ip + '\'' +
                ", wan_mask='" + wan_mask + '\'' +
                ", wan_gw='" + wan_gw + '\'' +
                ", CRC_FLAG='" + CRC_FLAG + '\'' +
                ", up_time='" + up_time + '\'' +
                '}';

    }
}
