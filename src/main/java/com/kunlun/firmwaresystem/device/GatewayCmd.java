package com.kunlun.firmwaresystem.device;

import com.google.gson.Gson;
import com.kunlun.firmwaresystem.device.cmd.BleVersion;
import com.kunlun.firmwaresystem.device.cmd.BleVersionDetail;
import com.kunlun.firmwaresystem.device.cmd.Connect;
import com.kunlun.firmwaresystem.device.cmd.ConnectDetail;
import com.kunlun.firmwaresystem.entity.device.DeviceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GatewayCmd {
    //notify_type：0 表示为notify
    //notify_type：1表示为indicate
    public String getConnectCmd(String gaddress, DeviceModel deviceModel, String[] target, int notify_type, String notify_uuid[], int keepTime, int msgId) {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        Connect connect = new Connect();
        connect.setGw_addr(gaddress);
        connect.setTime(sdf.format(date));
        ConnectDetail connectDetail = new ConnectDetail();
        ArrayList<HashMap<String, String>> devices = new ArrayList<>();
        for (String address : target) {
            HashMap devices_map = new HashMap<String, String>();
            devices_map.put("addr", address);
            devices.add(devices_map);
        }
        connectDetail.setDevices(devices);
        connectDetail.setMsgId(msgId);
        connectDetail.setDevices_num(target.length);
        if (notify_type == 0) {
            connectDetail.setNotify_chars_num(notify_uuid.length);
            ArrayList<Map> list = new ArrayList<>();
            for (String uuid : notify_uuid) {
                String handle_cccd[] = deviceModel.getUuids_handles().get(uuid).split("-");
                HashMap<String, Integer> hashMap = new HashMap<>();
                hashMap.put("handle", Integer.valueOf(handle_cccd[0]));
                hashMap.put("cccd", Integer.valueOf(handle_cccd[1]));
                list.add(hashMap);
            }
            connectDetail.setNotify_chars(list);
        } else if (notify_type == 1) {
            connectDetail.setIndicate_chars_num(notify_uuid.length);
            ArrayList<Map> list = new ArrayList<>();
            for (String uuid : notify_uuid) {
                String handle_cccd[] = deviceModel.getUuids_handles().get(uuid).split("-");
                HashMap<String, Integer> hashMap = new HashMap<>();
                hashMap.put("handle", Integer.valueOf(handle_cccd[0]));
                hashMap.put("cccd", Integer.valueOf(handle_cccd[1]));
                list.add(hashMap);
            }
            connectDetail.setIndicate_chars(list);
        }
        connectDetail.setKeep_time(keepTime);
        connect.setData(connectDetail);
        Gson gson = new Gson();
        return gson.toJson(connect);
    }

    public String getVersion(String gaddress) {
        BleVersion bleVersion = new BleVersion();
        bleVersion.setGw_addr(gaddress);
        BleVersionDetail bleVersionDetail = new BleVersionDetail();
        bleVersionDetail.setMsgId(0);
        bleVersion.setData(bleVersionDetail);
        String json = bleVersion.toString();
        return json;
    }

    class Message {
        String type;
        int handle;
        String raw;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getHandle() {
            return handle;
        }

        public void setHandle(int handle) {
            this.handle = handle;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }
    }

}
