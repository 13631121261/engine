package com.kunlun.firmwaresystem.controllers;

import com.kunlun.firmwaresystem.NewSystemApplication;
import com.kunlun.firmwaresystem.device.*;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.*;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class DeviceControl1111 {
    @Autowired
    private WifiMapper wifiMapper;
    @Autowired
    private BleMapper bleMapper;
    @Autowired
    private Gateway_configMapper gatewayConfigMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RulesMapper rulesMapper;
    @Autowired
    private GatewayMapper gatewayMapper;
    @Autowired
    private BeaconMapper beaconMapper;

    @Autowired
    private RedisUtils redisUtil;

    @RequestMapping(value = "/userApi/addWordCarda", method = RequestMethod.POST, produces = "text/plain")
    public String addWordCarda(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "mac") String mac, @ParamsNotNull @RequestParam(value = "project_key") String project_key, @ParamsNotNull @RequestParam(value = "type") Integer type) {
        String response = "";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Wordcard_a wordCard_a = new Wordcard_a( mac,  user.getUserkey(), type,user.getCustomerkey());
        WordCarda_Sql wordCarda_sql = new WordCarda_Sql();
        if (wordCarda_sql.addWordCarda(wordCardaMapper, wordCard_a)) {
            response = JsonConfig.getJson(JsonConfig.CODE_OK, null);
        } else {
            response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
        }
        wordcard_aMap = wordCarda_sql.getAllWordCarda(wordCardaMapper);
        redisUtil.set(redis_key_tag_map + mac, new ArrayList<Record>(50));
        return response;
    }

    @RequestMapping(value = "/userApi/editWordCarda", method = RequestMethod.POST, produces = "text/plain")
    public String editWordCarda(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "mac") String mac, @ParamsNotNull @RequestParam(value = "project_key") String project_key, @ParamsNotNull @RequestParam(value = "type") Integer type, @ParamsNotNull @RequestParam(value = "id") int id) {
        String response = "";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Wordcard_a wordCard_a = new Wordcard_a( mac, user.getUserkey(),type,user.getCustomerkey(), id);
        WordCarda_Sql wordCarda_sql = new WordCarda_Sql();
        int code = wordCarda_sql.update(wordCardaMapper, wordCard_a);
        System.out.println("COde=" + code);
        response = JsonConfig.getJson(JsonConfig.CODE_OK, null);
        wordcard_aMap = wordCarda_sql.getAllWordCarda(wordCardaMapper);
        redisUtil.set(redis_key_tag_map + mac, new ArrayList<Record>(50));
        return response;
    }

    @RequestMapping(value = "/userApi/deleteWordCarda", method = RequestMethod.POST, produces = "text/plain")
    public String deleteWordCarda(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "address") String address) {
        String response = "";
        Customer user = getCustomer(request);

        WordCarda_Sql wordCarda_sql = new WordCarda_Sql();
        wordCarda_sql.delete(wordCardaMapper, address);
        wordcard_aMap = wordCarda_sql.getAllWordCarda(wordCardaMapper);
        redisUtil.set(redis_key_tag_map + address, new ArrayList<Record>(50));
        return response;
    }


    @RequestMapping(value = "/userApi/addBeacon", method = RequestMethod.POST, produces = "text/plain")
    public String addBeacon(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "type") int type, @ParamsNotNull @RequestParam(value = "mac") String mac) {
        String response = "";
        Customer user = getCustomer(request);
        mac=mac.replaceAll(":","");
        mac=mac.toLowerCase();
        if(user.getPermission()==null||user.getPermission().getEditbeacon()==0){
            return JsonConfig.getJson(CODE_noP,null);
        }
        else if(user.getPermission().getEditbeacon()==1 ){
            Beacon beacon = new Beacon(mac, user.getUserkey(),type,user.getCustomerkey());
            Beacon_Sql beacon_sql = new Beacon_Sql();
            if (beacon_sql.addBeacon(beaconMapper, beacon)) {
                response = JsonConfig.getJson(JsonConfig.CODE_OK, null);
            } else {
                response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
            }
            beaconsMap.put(mac,beacon);
           // NewSystemApplication.beaconsMap = beacon_sql.getAllBeacon(beaconMapper);
       //     redisUtil.set(redis_key_tag_map + mac, new ArrayList<Record>(50));
            return response;
        }
        else{
            return JsonConfig.getJson(CODE_noP,null);
        }

    }

    @RequestMapping(value = "/userApi/editBeacon", method = RequestMethod.POST, produces = "text/plain")
    public String editBeacon(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "mac") String mac,@ParamsNotNull @RequestParam(value = "id") int id) {
        String response = "";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Beacon beacon = beaconsMap.get(mac);
        Beacon_Sql beacon_sql = new Beacon_Sql();
        if (beacon_sql.update(beaconMapper, beacon)) {
            response = JsonConfig.getJson(JsonConfig.CODE_OK, null);
        } else {
            response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
        }
        NewSystemApplication.beaconsMap = beacon_sql.getAllBeacon(beaconMapper);
        redisUtil.set(redis_key_tag_map + mac, new ArrayList<Record>(50));
        return response;
    }

    @RequestMapping(value = "/userApi/getBeacon", method = RequestMethod.GET, produces = "text/plain")
    public String getBeacon(HttpServletRequest request, @RequestParam(value = "mac") String mac, @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "page") String page,
                            @ParamsNotNull @RequestParam(value = "limit") String limit) {
        System.out.println("请求getBeacon");
        String response = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Customer user = getCustomer(request);
        Beacon_Sql beacon_sql = new Beacon_Sql();
        PageBeacon pageBeacon = beacon_sql.selectPageBeacon(beaconMapper, Integer.valueOf(page), Integer.valueOf(limit), mac, user.getUserkey(), name);
       // List<Record> recordList = new ArrayList<>();
        for (Beacon beacon : pageBeacon.getBeaconList()) {
            Beacon beacon1 = beaconsMap.get(beacon.getMac());
            if(beacon.getIsbind()==1){
                beacon.setDevice_name(devicePMap.get(beacon.getDevice_sn()).getName());
            }
            beacon.setRssi(beacon1.getRssi());
            beacon.setLastTime(beacon1.getLastTime());
            beacon.setBt(beacon1.getBt());
            beacon.setOnline(beacon1.getOnline());
            beacon.setSos(beacon1.getSos());
            beacon.setGateway_address(beacon1.getGateway_address());

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageBeacon.getTotal());
        jsonObject.put("data", pageBeacon.getBeaconList());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/userApi/getBeaconTag", method = RequestMethod.GET, produces = "text/plain")
    public String getBeaconTag(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "page") String page,
                               @ParamsNotNull @RequestParam(value = "limit") String limit, @RequestParam(value = "project_name") String project_name, @RequestParam(value = "device_name") String device_name) {
        String response = "";
        Customer user = getCustomer(request);
        Btag_Sql btag_sql = new Btag_Sql();
        PageBeaconTag pageBeaconTag = btag_sql.selectPageTag(bTagMapper, Integer.valueOf(page), Integer.valueOf(limit), project_name, user.getCustomerkey(), device_name);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageBeaconTag.getTotal());
        jsonObject.put("data", pageBeaconTag.getBeacon_tags());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/userApi/deleteBeaconTag", method = RequestMethod.GET, produces = "text/plain")
    public String deleteBeaconTag(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "id") int id) {
        String response = "";
        //  User user=getCustomer(request);
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Btag_Sql btag_sql = new Btag_Sql();
        btag_sql.delete(bTagMapper, id);

        response = JsonConfig.getJson(CODE_OK, null);
        return response;
    }

    @RequestMapping(value = "/userApi/deleteBeacon", method = RequestMethod.GET, produces = "text/plain")
    public String deleteBeacon(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "mac") String mac) {
        Customer user = getCustomer(request);

        String response = "";

      /*  if (user.getCustomername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }*/
        if(user.getPermission()==null||user.getPermission().getEditbeacon()==0){
            return JsonConfig.getJson(CODE_noP,null);
        }else{
            String[] macs=mac.split(",");
            if(macs.length>0){
                for(String address:macs){
                    beaconsMap.remove(address);
                    Beacon_Sql beacon_sql = new Beacon_Sql();
                    beacon_sql.delete(beaconMapper, address);
                }
            }
            response = JsonConfig.getJson(CODE_OK, null);
            return response;
        }
        // User user=getCustomer(request);

    }

   /* @RequestMapping(value = "/userApi/getBeaconBt", method = RequestMethod.GET, produces = "text/plain")
    public String getBeaconBt(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "project_key") String project_key) {
        String response = "";
        User user = getCustomer(request);
        Beacon_Sql beacon_sql = new Beacon_Sql();
        Map<String, Beacon> allBeacon = beacon_sql.getAllBeacon(beaconMapper, project_key);

        List<Beacon> recordList = new ArrayList<>();
        for (String key : allBeacon.keySet()) {
            if (recordMap.get(key) != null) {
                Beacon beacon = allBeacon.get(key);
                beacon.setBt(Double.valueOf(recordMap.get(key).getBt()));
                recordList.add(beacon);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("data", recordList);
        return jsonObject.toString();
    }
*/
 /*   @RequestMapping(value = "/userApi/getTagLocation", method = RequestMethod.GET, produces = "text/plain")
    public String getTagLocation(HttpServletRequest request, @RequestParam(value = "mac") String mac, @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "page") String page,
                                 @ParamsNotNull @RequestParam(value = "limit") String limit) {
        String response = "";
        User user = getCustomer(request);
        Beacon_Sql beacon_sql = new Beacon_Sql();
        PageBeacon pageBeacon = beacon_sql.selectPageBeacon(beaconMapper, Integer.valueOf(page), Integer.valueOf(limit), mac, user.getCustomerkey(), name);
        List<Record> recordList = new ArrayList<>();
        for (Beacon beacon : pageBeacon.getBeaconList()) {
            String address = beacon.getMac();
            recordList.add(recordMap.get(address));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageBeacon.getTotal());
        jsonObject.put("data", recordList);
        return jsonObject.toString();
    }

*/
    @RequestMapping(value = "/userApi/getWordcarda", method = RequestMethod.GET, produces = "text/plain")
    public String getWordcarda(HttpServletRequest request, @RequestParam(value = "mac") String mac, @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "page") String page,
                               @ParamsNotNull @RequestParam(value = "limit") String limit) {
        String response = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Customer user = getCustomer(request);
        WordCarda_Sql wordCardaSql = new WordCarda_Sql();
        PageWordcarda pageWordcarda = wordCardaSql.selectPageWordcard(wordCardaMapper, Integer.valueOf(page), Integer.valueOf(limit), mac, user.getCustomerkey(), name);
        List<Record> recordList = new ArrayList<>();
        for (Wordcard_a wordCard_a : pageWordcarda.getWordcard_as()) {
            String address = wordCard_a.getMac();
            Wordcard_a wordcard_a1 = wordcard_aMap.get(address);

            long thisTime = System.currentTimeMillis();
            long oldtime=0;
            try{
                oldtime= wordcard_a1.getLastTime();
            }
            catch (Exception e){

            }
            if (thisTime - oldtime <30 * 1000) {
                wordCard_a.setOnline(1);
            }else{
                wordCard_a.setOnline(0);
            }
            wordCard_a.setX(wordcard_a1.getX());
            wordCard_a.setY(wordcard_a1.getY());

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageWordcarda.getTotal());
        jsonObject.put("data", recordList);
        return jsonObject.toString();
    }
//获取设备的离线记录，包括网关 信标等等
    @RequestMapping(value = "/userApi/getOffline", method = RequestMethod.GET, produces = "text/plain")
    public String getOffline(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "page") String page,
                            @ParamsNotNull @RequestParam(value = "limit") String limit,
                            @ParamsNotNull @RequestParam(value = "mac") String mac) {
        Customer user = getCustomer(request);
        Moffline_Sql mofflineSql = new Moffline_Sql();
        PageMoffline pageRecord = mofflineSql.selectPage(mofflineMapper, Integer.valueOf(page), Integer.valueOf(limit), mac,user.getUserkey());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageRecord.getTotal());
        jsonObject.put("data", pageRecord.getMofflines());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/userApi/getbeaconsos", method = RequestMethod.GET, produces = "text/plain")
    public String getbeaconsos(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "page") String page,
                               @ParamsNotNull @RequestParam(value = "limit") String limit,
                               @RequestParam(value = "mac") String mac, @RequestParam(value = "name") String name, @RequestParam(value = "handle") String handle) {
        Customer user = getCustomer(request);
        RecordSos_Sql record_sql = new RecordSos_Sql();
        PageRecordSos pageRecord = record_sql.selectPageRecord(recordSosMapper, Integer.valueOf(page), Integer.valueOf(limit), mac, name, Integer.valueOf(handle), user.getUserkey());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageRecord.getTotal());
        jsonObject.put("data", pageRecord.getRecordList());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/userApi/deletebeaconsos", method = RequestMethod.GET, produces = "text/plain")
    public String deletebeaconsos(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "id") int id) {
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            String response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        RecordSos_Sql recordSos_sql = new RecordSos_Sql();
        recordSos_sql.delete(recordSosMapper, id);
        String response = JsonConfig.getJson(CODE_OK, null);
        return response;
    }

/*
    //查询今天的报警情况
    @RequestMapping(value = "/userApi/getbeaconsosday", method = RequestMethod.GET, produces = "text/plain")
    public String getbeaconsosday(HttpServletRequest request) {
        User user = getCustomer(request);
        RecordSos_Sql record_sql = new RecordSos_Sql();
        List<Record_sos> list = record_sql.selectPageRecordDay(recordSosMapper, user.getCustomerkey());
        return JsonConfig.getJson(CODE_OK, list);
    }*/

    @RequestMapping(value = "/userApi/editbeaconsos", method = RequestMethod.GET, produces = "text/plain")
    public String editbeaconsos(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "id") int id, @ParamsNotNull @RequestParam(value = "mac") String mac) {

        Customer user = getCustomer(request);

        RecordSos_Sql record_sql = new RecordSos_Sql();
        record_sql.editBeaconSos(recordSosMapper, id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        return jsonObject.toString();
    }

 /*   @RequestMapping(value = "/userApi/getBeaconOnline", method = RequestMethod.GET, produces = "text/plain")
    public String getBeaconOnline(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "project_key") String project_key) {
        User user = getCustomer(request);
        int online = (int) redisUtil.get(redis_key_beacon_onLine + project_key);

        int count = 0;
        for (String key : beaconsMap.keySet()) {
            if (beaconsMap.get(key).getProject_key().equals(project_key)) {
                count++;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("online", online);
        jsonObject.put("unline", count - online);
        return jsonObject.toString();
    }
*/
/*

    @RequestMapping(value = "/userApi/getGatewayOnline", method = RequestMethod.GET, produces = "text/plain")
    public String getGatewayOnline(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "project_key") String project_key) {
        User user = getCustomer(request);
        int gatewayUnline = 0;
        int gatewayOnline = 0;
        for (String key : gatewayMap.keySet()) {
            Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + key);
            if (gateway.getProject_key().equals(project_key)) {
                if (gateway.getOnline() == 0) {
                    gatewayUnline++;
                } else
                    gatewayOnline++;
            }
        }

        RecordSos_Sql record_sql = new RecordSos_Sql();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("gatewayunline", gatewayUnline);
        jsonObject.put("gatewayOnline", gatewayOnline);
        return jsonObject.toString();
    }
*/

/*

    @RequestMapping(value = "/userApi/getAllLocation", method = RequestMethod.GET, produces = "text/plain")
    public String getAllLocation(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "project_key") String project_key) {
        User user = getCustomer(request);
        WordCarda_Sql wordCardaSql = new WordCarda_Sql();
        List<Wordcard_a> wordCard_aList = wordCardaSql.getAllWordCarda(wordCardaMapper, project_key, user.getCustomerkey());
        Beacon_Sql beacon_sql = new Beacon_Sql();
        List<Beacon> beaconList = beacon_sql.getAllBeacon(beaconMapper, user.getCustomerkey(), project_key);
        List<Record> recordList_wordcard = new ArrayList<>();
        List<Record> recordList_beacon = new ArrayList<>();
        for (Wordcard_a wordCard_a : wordCard_aList) {
            String address = wordCard_a.getMac();
            Record record = recordMap.get(address);
            if (record == null) {
                record = new Record();
            }
            Location location = (Location) redisUtil.get(redis_key_location_tag + address);
            if (location != null) {
                record.setType(location.getType());
                record.setGateway_name(location.getName());
                record.setD(location.getD());
                record.setX(location.getX());
                record.setY(location.getY());
                // redisUtil.set(redis_key_location_tag+address,null);
            }
            // recor
            // d.setType(3);
            recordList_wordcard.add(record);
        }
        for (Beacon beacon : beaconList) {
            String address = beacon.getMac();
            recordList_beacon.add(recordMap.get(address));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("wordcard", recordList_wordcard);
        jsonObject.put("beacon", recordList_beacon);
        return jsonObject.toString();
    }
*/

    private Customer getCustomer(HttpServletRequest request) {
        Customer user = (Customer) redisUtil.get("sessionId:" + request.getSession().getId());
        if (user != null) {
            redisUtil.set("sessionId:" + request.getSession().getId(), user);
        }
        return user;
    }
}
