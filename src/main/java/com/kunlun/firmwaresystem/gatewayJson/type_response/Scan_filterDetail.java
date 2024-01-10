package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Scan_filterDetail extends ResponseHead {




    //{"pkt_type":"response","gw_addr":"c487106abb87","time":"2023-10-09 10:53:01","data":{"msgId":65535,"resp":"scan_filter_get","result":true,"scan_filter_rssi":{"enable":true,"value":-60},"scan_filter_name":{"enable":true,"num":8,"value":[{"name":"K*"},{"name":"B*"},{"name":"VV"},{"name":"44"},{"name":"55"},{"name":"66"},{"name":"77"},{"name":"88"}]},"scan_filter_ibcn_dev":{"enable":true},"scan_filter_ibcn_uuid":{"enable":false},"scan_filter_comp_ids":{"enable":false},"scan_filter_dev_mac":{"enable":true,"start":"000000000001","end":"000000000006"},"scan_filter_serv_data_uuid":{"enable":true,"num":3,"value":[{"uuid":"fff1"},{"uuid":"fff2"},{"uuid":"fff7"}]}}}
    boolean result;
    int filter_rssi;
    boolean filter_rssi_b;
    boolean filter_name_b;
    String[] filter_name;
    boolean filter_dev_mac_b;
    String[] filter_dev_mac;
    boolean filter_beacon_b;
    boolean filter_uuid_b;
    String[] filter_uuid;
    boolean filter_companyids_b;
    String[] filter_comp_ids;
    String[] scan_filter_serv_data_uuid;
    boolean scan_filter_serv_data_uuid_b;
    public Scan_filterDetail(String json) {
        try {
            analysis(json);
        }catch (Exception e){
            System.out.println("在这里异常了"+e.toString());
        }
    }

    public boolean isResult() {
        return result;
    }

    public int getFilter_rssi() {
        return filter_rssi;
    }

    public boolean isFilter_rssi_b() {
        return filter_rssi_b;
    }

    public boolean isFilter_name_b() {
        return filter_name_b;
    }

    public boolean isFilter_dev_mac_b() {
        return filter_dev_mac_b;
    }

    public String[] getFilter_dev_mac() {
        return filter_dev_mac;
    }

    public String[] getFilter_name() {
        return filter_name;
    }

    public boolean isFilter_beacon_b() {
        return filter_beacon_b;
    }

    public boolean isFilter_uuid_b() {
        return filter_uuid_b;
    }

    public String[] getFilter_uuid() {
        return filter_uuid;
    }

    public boolean isFilter_companyids_b() {
        return filter_companyids_b;
    }

    public String[] getFilter_comp_ids() {
        return filter_comp_ids;
    }

    public String[] getScan_filter_serv_data_uuid() {
        return scan_filter_serv_data_uuid;
    }

    private void analysis(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        msgId = jsonObject.getIntValue("msgId");
        result = jsonObject.getBoolean("result");
        resp = jsonObject.getString("resp");
        filter_rssi_b = jsonObject.getJSONObject("scan_filter_rssi").getBoolean("enable");
        //    System.out.println("信号过滤开关"+filter_rssi_b);
        if (filter_rssi_b) {
            filter_rssi = jsonObject.getJSONObject("scan_filter_rssi").getIntValue("value");
            //   System.out.println("信号过滤开关="+filter_rssi);
        }
        filter_name_b = jsonObject.getJSONObject("scan_filter_name").getBoolean("enable");
        if (filter_name_b) {
            int number = jsonObject.getJSONObject("scan_filter_name").getIntValue("num");
            filter_name = new String[number];
            //    System.out.println("1111");
            JSONArray jsonArray = jsonObject.getJSONObject("scan_filter_name").getJSONArray("value");
            for (int i = 0; i < number; i++) {
                filter_name[i] = jsonArray.getJSONObject(i).getString("name");
            }
        }
        filter_dev_mac_b=jsonObject.getJSONObject("scan_filter_dev_mac").getBoolean("enable");
        if(filter_dev_mac_b){
            filter_dev_mac=new String[2];
            filter_dev_mac[0]=jsonObject.getJSONObject("scan_filter_dev_mac").getString("start");
            filter_dev_mac[1]=jsonObject.getJSONObject("scan_filter_dev_mac").getString("end");
        }
        //   System.out.println("2222");
        filter_beacon_b = jsonObject.getJSONObject("scan_filter_ibcn_dev").getBoolean("enable");
        //  System.out.println("3333");
        filter_uuid_b = jsonObject.getJSONObject("scan_filter_ibcn_uuid").getBoolean("enable");
        //  System.out.println("4444");
        if (filter_uuid_b) {
            filter_uuid=new String[jsonObject.getJSONObject("scan_filter_ibcn_uuid").getIntValue("num")];
            if(filter_uuid.length==1){
                filter_uuid[0]=jsonObject.getJSONObject("scan_filter_ibcn_uuid").getString("value").replaceAll("-","");
            }else{
                JSONArray jsonArray = jsonObject.getJSONObject("scan_filter_ibcn_uuid").getJSONArray("value");
                for (int i = 0; i < filter_uuid.length; i++) {
                    filter_uuid[i] = jsonArray.getJSONObject(i).getString("uuid");
                }
            }

        }
        scan_filter_serv_data_uuid_b = jsonObject.getJSONObject("scan_filter_serv_data_uuid").getBoolean("enable");
        System.out.println("6666"+scan_filter_serv_data_uuid_b);
        if (scan_filter_serv_data_uuid_b) {
            scan_filter_serv_data_uuid=new String[jsonObject.getJSONObject("scan_filter_serv_data_uuid").getIntValue("num")];

                JSONArray jsonArray = jsonObject.getJSONObject("scan_filter_serv_data_uuid").getJSONArray("value");
                for (int i = 0; i < scan_filter_serv_data_uuid.length; i++) {
                    scan_filter_serv_data_uuid[i] = jsonArray.getJSONObject(i).getString("uuid");
                }


        }
         System.out.println("6666");
        filter_companyids_b = jsonObject.getJSONObject("scan_filter_comp_ids").getBoolean("enable");
        if (filter_companyids_b) {
              System.out.println("7777");
            int number = jsonObject.getJSONObject("scan_filter_comp_ids").getIntValue("num");
            filter_comp_ids = new String[number];
            //  System.out.println("8888");
            JSONArray jsonArray = jsonObject.getJSONObject("scan_filter_comp_ids").getJSONArray("value");
            for (int i = 0; i < number; i++) {
                filter_comp_ids[i] = jsonArray.getJSONObject(i).getString("id").toUpperCase();
            }
        }

    }
}
