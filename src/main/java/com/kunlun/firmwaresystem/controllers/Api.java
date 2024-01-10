package com.kunlun.firmwaresystem.controllers;

import com.kunlun.firmwaresystem.NewSystemApplication;
import com.kunlun.firmwaresystem.device.*;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.*;
import com.kunlun.firmwaresystem.mqtt.MyMqttClient;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import com.kunlun.firmwaresystem.util.constant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;


import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class Api {
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
    private static int ExpireTime = 600;   // redis中存储的过期时间60s
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RedisUtils redisUtil;

    @RequestMapping(value = "Api/Login", method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse httpServletResponse, @RequestParam("userName") String userName, @RequestParam("passWord") String passWord) {
        /*httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");*/
        String response = null;
        //账号密码固定超过5个字符，后续在优化

        Customer user = new Customer(userName, passWord);
        Customer_sql user_sql = new Customer_sql();
        List<Customer> userList = user_sql.getCustomer(customerMapper, user);
        if (userList == null || userList.size() == 0) {
            response = JsonConfig.getJson(JsonConfig.CODE_RESPONSE_NULL, null);
        } else if (userList.size() > 1) {
            response = JsonConfig.getJson(JsonConfig.CODE_RESPONSE_MORE, null);
        } else {
            String toketn = Base64.getEncoder().encodeToString((user.getCustomerkey() + "_" + System.currentTimeMillis()).getBytes()).replaceAll("\\+", "");

            user = userList.get(0);
                Permission_Sql permissionSql = new Permission_Sql();
                List<Permission>  permissions=permissionSql.seleceByOne(permissionMapper,user.getPermission_key(),user.getUserkey());
                if(permissions!=null&&permissions.size()==1){
                    user.setPermission(permissions.get(0));
                    System.out.println("设置了权限");
                }

              /*  String key= redisTemplate.opsForValue().get("userKey:" +session.getAttribute("userKey"));
                System.out.println("打印Session"+key);*/
            GatewayConfig_sql project_sql = new GatewayConfig_sql();
            Gateway_config gatewayConfig = project_sql.selectByKey(gatewayConfigMapper, "projectdefault_" + user.getCustomerkey(), user.getCustomerkey());
            if (gatewayConfig == null) {
                gatewayConfig = new Gateway_config();
                gatewayConfig.setConfig_key("projectdefault_" + user.getCustomerkey());
                gatewayConfig.setUser_key(user.getCustomerkey());
                gatewayConfig.setSub_topic(gatewayConfig.getConfig_key() + "/sub");
                gatewayConfig.setPub_topic(gatewayConfig.getConfig_key() + "/pub");
                gatewayConfig.setUser_key(user.getCustomerkey());
                gatewayConfig.setName("默认配置");
                gatewayConfig.setFilter_rssi(0);
               // gatewayConfig.setCreate_time(user.getCreate_time());
                project_sql.addGatewayConfig(gatewayConfigMapper, gatewayConfig);
                gatewayConfigMap.put(gatewayConfig.getConfig_key(), gatewayConfig);
                MyMqttClient client = MyMqttClient.getMyMqttClient();
                client.addSubTopic(gatewayConfig.getPub_topic());
            }else{
                System.out.println("不新建默认项目");
            }
            redisUtil.set("tokenId:" + toketn, user, ExpireTime);
            //response = JsonConfig.getJsonToken(CODE_OK, userList.get(0), toketn);
            System.out.println("登录信息=" + response);
        }

        response(httpServletResponse, response);
        // return response;
    }

    /*@RequestMapping(value = "Api/CreateUser", method = RequestMethod.POST, produces = "text/plain")
    public void createUser(HttpServletRequest request, HttpServletResponse httpServletResponse, @RequestParam("userName") @ParamsNotNull String userName, @RequestParam("passWord") @ParamsNotNull String passWord, @RequestParam("email") @ParamsNotNull String email, @RequestParam("companyName") @ParamsNotNull String companyName, @RequestParam("phoneNumber") @ParamsNotNull String phoneNumber) {
        String response = null;
        Customer user1 = getCustomer(request);
        if (user1.getCustomername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            response(httpServletResponse, response);
            return;
        }
        System.out.println("对象为" + user1.getCustomername());
        if (!user1.getCustomername().equals("admin")) {
            ////预留后续的权限，只有管理员才能创建用户
        }
        User user = new User(userName, passWord, email, companyName, phoneNumber);
        User_sql user_sql = new User_sql();
        if (!user_sql.checkUser(userMapper, user)) {
            int result = user_sql.addUser(userMapper, user);
            response = JsonConfig.getJson(CODE_OK, null);
        } else {
            response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
            response(httpServletResponse, response);
        }

    }
*/
    /*@RequestMapping(value = "Api/AddProject", method = RequestMethod.POST)
    public void addProject(HttpServletRequest request, HttpServletResponse httpServletResponse, @RequestParam("name") @ParamsNotNull String name, @RequestParam("address") String address, @RequestParam("wifi_version") String wifi_version, @RequestParam("ble_version") String ble_version) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            response(httpServletResponse, response);
            return;
        }
        System.out.println("key=" + user.getCustomerkey());
        if (user.getCustomerkey() != null && user.getCustomerkey().length() > 0) {
            Gateway_config gatewayConfig = new Gateway_config(name, user.getUserkey(), address, ble_version, wifi_version,user.getCustomerkey());
            gatewayConfig.setSub_topic(gatewayConfig.getConfig_key() + "/sub");
            gatewayConfig.setPub_topic(gatewayConfig.getConfig_key() + "/pub");
            GatewayConfig_sql project_sql = new GatewayConfig_sql();
            if (project_sql.addGatewayConfig(gatewayConfigMapper, gatewayConfig)) {
                gatewayConfig = project_sql.selectByKey(gatewayConfigMapper, gatewayConfig.getConfig_key(), gatewayConfig.getCustomer_key());
                redisUtil.set("redis_key_project" + gatewayConfig.getConfig_key(), gatewayConfig);
                gatewayConfigMap.put(gatewayConfig.getConfig_key(), gatewayConfig);
                response = JsonConfig.getJson(CODE_OK, null);
                MyMqttClient client = MyMqttClient.getMyMqttClient();
                client.addSubTopic(gatewayConfig.getPub_topic());

            } else {
                response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
            }
        }

    }

    @RequestMapping(value = "Api/SetGatewayProject", method = RequestMethod.POST, produces = "text/plain")
    public String setGatewayProject(HttpServletRequest request, @RequestParam("address") @ParamsNotNull String address, @RequestParam("config_key") String config_key, @RequestParam("config_name") String config_name) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        if (user != null) {
            Gateway_sql gateway_sql = new Gateway_sql();
            Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + address);
            gateway.setConfig_key(config_key);
            gateway.setConfig_name(config_name);
            redisUtil.set(redis_key_gateway + address, gateway);
            gateway_sql.updateGateway(gatewayMapper, gateway);
            response = JsonConfig.getJson(CODE_OK, null);
        }
        return response;
    }*/

    /*@RequestMapping(value = "Api/AddGateway", method = RequestMethod.POST, produces = "text/plain")
    public String addGateway(HttpServletRequest request, @RequestParam("name") @ParamsNotNull String name, @RequestParam("address") @ParamsNotNull String address, @RequestParam("wifi_address") @ParamsNotNull String wifi_address, @RequestParam("config_key") @ParamsNotNull String config_key,
                             @RequestParam("config_name") @ParamsNotNull String config_name
                         ) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        if (user.getCustomerkey() != null && user.getCustomerkey().length() > 0) {
            address = address.replaceAll(":", "");
            address = address.toLowerCase();

            wifi_address = wifi_address.replaceAll(":", "");
            wifi_address = wifi_address.toLowerCase();
            Gateway gateway = new Gateway(name, address, wifi_address, config_key, user.getUserkey(), config_name, 0, 0,0,user.getCustomerkey());
            gateway.setSub_topic("SrvData");
            gateway.setPub_topic("GwData");
            Gateway_sql gateway_sql = new Gateway_sql();
            if (gateway_sql.addGateway(gatewayMapper, gateway)) {
                response = JsonConfig.getJson(CODE_OK, null);
                redisUtil.set(redis_key_gateway + gateway.getAddress(), gateway);
                gatewayMap.put(gateway.getAddress(), gateway.getAddress());
            } else {
                response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
            }
        }
        return response;
    }
*/

    @RequestMapping(value = "Api/EditGateway", method = RequestMethod.POST, produces = "text/plain")
    public String editGateway(HttpServletRequest request, @RequestParam("name") @ParamsNotNull String name, @RequestParam("address") @ParamsNotNull String address, @RequestParam("wifi_address") @ParamsNotNull String wifi_address, @RequestParam("config_key") @ParamsNotNull String config_key, @RequestParam("config_name") @ParamsNotNull String config_name
            , @RequestParam("ed_x") double x
            , @RequestParam("ed_y") double y) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        if (user.getCustomerkey() != null && user.getCustomerkey().length() > 0) {
            Gateway_sql gateway_sql = new Gateway_sql();
            gateway_sql.updateGateway(gatewayMapper, address, name, wifi_address, config_key, config_name, Double.valueOf(x), Double.valueOf(y));
            Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + address);
            gateway.setName(name);
            gateway.setWifi_address(wifi_address);
            gateway.setConfig_name(config_name);
            gateway.setConfig_key(config_key);
            gateway.setX(x);
            gateway.setX(y);
            redisUtil.set(redis_key_gateway + address, gateway);
            response = JsonConfig.getJson(CODE_OK, null);
        }
        return response;
    }

    @RequestMapping(value = "Api/AddRules", method = RequestMethod.POST, produces = "text/plain")
    public String addRules(HttpServletRequest request, @RequestParam("name") @ParamsNotNull String name, @ParamsNotNull @RequestParam("type") int type, @ParamsNotNull @RequestParam("server") String server, @ParamsNotNull @RequestParam("port") int port) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        if (user.getCustomerkey() != null && user.getCustomerkey().length() > 0) {
            Rules rules = new Rules(name, type, server, port, user.getUserkey(),user.getCustomerkey());
            Rules_sql rules_sql = new Rules_sql();
            if (rules_sql.addRules(rulesMapper, rules)) {
                rulesMap.put(rules.getRule_key(), rules);
                response = JsonConfig.getJson(CODE_OK, null);
            } else {
                response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
            }
        }
        return response;
    }

    @RequestMapping(value = "Api/GetStatus", method = RequestMethod.GET, produces = "text/plain")
    public String getStatus() {
        String response = "默认参数";
        response = JsonConfig.getJson(CODE_OK, null);
        return response;
    }


    @RequestMapping(value = "Api/BindRules", method = RequestMethod.POST, produces = "text/plain")
    public String bindRules(HttpServletRequest request, @RequestParam("projectKey") @ParamsNotNull String projectKey, @ParamsNotNull @RequestParam("ruleKey") String ruleKey, @ParamsNotNull @RequestParam("rules_name") String rules_name) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        if (user.getCustomerkey() != null && user.getCustomerkey().length() > 0) {
            Gateway_config gatewayConfig = gatewayConfigMap.get(projectKey);

            gatewayConfig.setRules_name(rules_name);
            gatewayConfig.setRules_key(ruleKey);
            GatewayConfig_sql project_sql = new GatewayConfig_sql();
            if (project_sql.bindRules(gatewayConfigMapper, gatewayConfig, ruleKey, rules_name)) {
                gatewayConfigMap.put(projectKey, gatewayConfig);
                response = JsonConfig.getJson(CODE_OK, null);

            } else {
                response = JsonConfig.getJson(CODE_SQL_ERROR, null);
            }
        }
        return response;
    }
/*
    @RequestMapping(value = "/Api/GetAllGateway", method = RequestMethod.GET, produces = "text/plain")
    public String getAllGateway(HttpServletRequest request,
                                @ParamsNotNull @RequestParam(value = "page") String page,
                                @RequestParam(value = "mac") String mac,
                                @RequestParam(value = "project_name") String project_name,
                                @ParamsNotNull @RequestParam(value = "limit") String limit) {
        String response = "默认参数";
        Gateway_sql gateway_sql = new Gateway_sql();
        Customer user = getCustomer(request);
        PageGateway pageGateway = gateway_sql.selectPageGateway(gatewayMapper, Integer.valueOf(page), Integer.valueOf(limit), mac, project_name, user.getUserkey());
        //System.out.println("网关信息="+pageGateway.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);

        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageGateway.getTotal());
        for (Gateway gateway : pageGateway.getGatewayList()) {
            String syn = (String) redisUtil.get(redis_key_project_sys + gateway.getAddress());
            Integer revicecount = (Integer) redisUtil.get(redis_key_gateway_revice_count + gateway.getAddress());
            if (revicecount == null) {
                revicecount = 0;
            }
            gateway.setRevicecount(revicecount);
            if (syn != null && syn.equals("ok")) {
                gateway.setSyn(1);
            } else {
                gateway.setSyn(0);
                gateway.setSynStr(syn);
            }
        }
        jsonObject.put("data", pageGateway.getGatewayList());

        response = jsonObject.toString();
        return response;
    }*/
/*
    @RequestMapping(value = "/Api/AddWordCarda", method = RequestMethod.POST, produces = "text/plain")
    public String addWordCarda(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "mac") String mac, @ParamsNotNull @RequestParam(value = "project_key") String project_key, @ParamsNotNull @RequestParam(value = "type") Integer type) {
        String response = "";
        User user = getCustomer(request);
        if (user.getCustomername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Wordcard_a wordCard_a = new Wordcard_a(name, mac, user.getCustomerkey(), project_key, type);
        WordCarda_Sql wordCarda_sql = new WordCarda_Sql();
        if (wordCarda_sql.addWordCarda(wordCardaMapper, wordCard_a)) {
            response = JsonConfig.getJson(JsonConfig.CODE_OK, null);
        } else {
            response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
        }
        wordcard_aMap = wordCarda_sql.getAllWordCarda(wordCardaMapper);
        redisUtil.set(redis_key_tag_map + mac, new ArrayList<Record>(50));
        return response;
    }*/


    @RequestMapping(value = "/Api/SelectPageProject", method = RequestMethod.GET)
    public void selectPageProject(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                  @ParamsNotNull @RequestParam(value = "page") String page,
                                  @RequestParam(value = "project_name") String project_name,
                                  @ParamsNotNull @RequestParam(value = "limit") String limit) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        GatewayConfig_sql project_sql = new GatewayConfig_sql();
        PageGatewayConfig pageGatewayConfig = project_sql.selectPageConfig(gatewayConfigMapper, Integer.valueOf(page), Integer.valueOf(limit), project_name, user.getCustomerkey());
        //   System.out.println("网关信息="+pageGatewayConfig.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageGatewayConfig.getTotal());
     /*   for (Gateway_config gatewayConfig : pageGatewayConfig.getGatewayConfigList()) {
            gatewayConfig.setGateway_count((String) redisUtil.get(redis_key_gatewayConfig_onLine + gatewayConfig.getConfig_key()));
        }*/
        jsonObject.put("data", pageGatewayConfig.getGatewayConfigList());
        response = jsonObject.toString();
        response(httpServletResponse, response);

    }

    /*@RequestMapping(value = "/Api/GetWordcarda", method = RequestMethod.GET)
    public void getWordcarda(HttpServletRequest request, HttpServletResponse httpServletResponse, @RequestParam(value = "mac") String mac, @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "page") String page,
                             @ParamsNotNull @RequestParam(value = "limit") String limit) {
        String response = "";
        User user = getCustomer(request);
        WordCarda_Sql wordCardaSql = new WordCarda_Sql();
        PageWordcarda pageWordcarda = wordCardaSql.selectPageWordcard(wordCardaMapper, Integer.valueOf(page), Integer.valueOf(limit), mac, user.getCustomerkey(), name);
        List<Record> recordList = new ArrayList<>();
        for (Wordcard_a wordCard_a : pageWordcarda.getWordcard_as()) {
            String address = wordCard_a.getMac();
            Record record = recordMap.get(address);
            if (record != null) {
                record.setType(wordCard_a.getType());
                if (record.getTag() == null) {
                    record.setTag(wordCard_a);
                }
            }


            //   System.out.println("网页获取--------------"+recordMap.get(address).toString());
            //
            //   if(address.equals("e5a2674d14b5")||address.equals("cabb00640003")||address.equals("cabb00640002")||address.equals("cabb00640001")){
            Location location = (Location) redisUtil.get(redis_key_location_tag + address);
            if (location != null && record != null) {
                System.out.println(record);
                System.out.println(location);
                record.setType(location.getType());
                record.setGateway_name(location.getName());
                record.setD(location.getD());
                record.setX(location.getX());
                record.setY(location.getY());
                //  redisUtil.set(redis_key_location_tag+address,null);
            }
            // record.setType(3);
            recordList.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageWordcarda.getTotal());
        jsonObject.put("data", recordList);
        response(httpServletResponse, jsonObject.toString());

    }
*/
/*    @RequestMapping(value = "/Api/GetAllProject", method = RequestMethod.GET, produces = "text/plain")
    public String getAllProject(HttpServletRequest request) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        GatewayConfig_sql project_sql = new GatewayConfig_sql();
        List<Gateway_config> gatewayConfigList = project_sql.getAllConfig(gatewayConfigMapper, user.getCustomerkey());
        for (Gateway_config gatewayConfig : gatewayConfigList) {
            gatewayConfig.setGateway_count((String) redisUtil.get(redis_key_gatewayConfig_onLine + gatewayConfig.getConfig_key()));
        }
        response = JsonConfig.getJson(CODE_OK, gatewayConfigList);
        return response;
    }*/

    @RequestMapping(value = "/Api/GetBeacon", method = RequestMethod.GET)
    public void getBeacon(HttpServletRequest request, HttpServletResponse httpServletResponse, @RequestParam(value = "mac") String mac, @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "page") String page,
                          @ParamsNotNull @RequestParam(value = "limit") String limit) {
        String response = "";
        Customer user = getCustomer(request);
        Beacon_Sql beacon_sql = new Beacon_Sql();
        PageBeacon pageBeacon = beacon_sql.selectPageBeacon(beaconMapper, Integer.valueOf(page), Integer.valueOf(limit), mac, user.getUserkey(), name);

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
        response(httpServletResponse, jsonObject.toString());
    }

    @RequestMapping(value = "/Api/DeleteBeacon", method = RequestMethod.GET, produces = "text/plain")
    public String deleteBeacon(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "address") String address) {
        String response = "";
        Customer user = getCustomer(request);
      /*  if (user.getCustomername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }*/
        if(user.getPermission()==null||user.getPermission().getEditbeacon()==0){
            return JsonConfig.getJson(CODE_noP,null);
        }else{
            beaconsMap.remove(address);
            Beacon_Sql beacon_sql = new Beacon_Sql();
            beacon_sql.delete(beaconMapper, address);

            response = JsonConfig.getJson(CODE_OK, null);
            return response;
        }
        // User user=getCustomer(request);

    }
    @RequestMapping(value = "/Api/AddBeacon", method = RequestMethod.POST)
    public void addBeacon(HttpServletRequest request, HttpServletResponse httpServletResponse, @ParamsNotNull @RequestParam(value = "type") int type, @ParamsNotNull @RequestParam(value = "address") String address ) {
        String response = "";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            response(httpServletResponse, response);
            return;
        }

        Beacon beacon = new Beacon( address,user.getUserkey(),type,user.getCustomerkey());
        Beacon_Sql beacon_sql = new Beacon_Sql();
        if (beacon_sql.addBeacon(beaconMapper, beacon)) {
            response = JsonConfig.getJson(JsonConfig.CODE_OK, null);
        } else {
            response = JsonConfig.getJson(JsonConfig.CODE_REPEAT, null);
        }
        NewSystemApplication.beaconsMap = beacon_sql.getAllBeacon(beaconMapper);
        redisUtil.set(redis_key_tag_map + address, new ArrayList<Record>(50));
        response(httpServletResponse, response);

    }


    @RequestMapping(value = "/Api/AddBeaconTag", method = RequestMethod.POST, produces = "text/plain")
    public String addBeaconTag(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "major") Integer major, @ParamsNotNull @RequestParam(value = "minor") Integer minor, @ParamsNotNull @RequestParam(value = "x") Double x, @ParamsNotNull @RequestParam(value = "y") Double y, @ParamsNotNull @RequestParam(value = "projectKey") String projectKey, @ParamsNotNull @RequestParam(value = "projectName") String projectName) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Btag_Sql btag_sql = new Btag_Sql();
        boolean status = btag_sql.addBeaconTag(bTagMapper, new Beacon_tag(name, major, minor, projectKey, user.getUserkey(), x, y, projectName,user.getCustomerkey()));
        if (status) {
            response = JsonConfig.getJson(CODE_OK, null);
            //需要把信标信息添加到定位列表中、
        } else {
            response = JsonConfig.getJson(CODE_REPEAT, null);
        }
        return response;
    }

    @RequestMapping(value = "/Api/EditBeaconTag", method = RequestMethod.GET, produces = "text/plain")
    public String editBeaconTag(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "id") Integer id, @ParamsNotNull @RequestParam(value = "name") String name, @ParamsNotNull @RequestParam(value = "major") Integer major, @ParamsNotNull @RequestParam(value = "minor") Integer minor, @ParamsNotNull @RequestParam(value = "x") Double x, @ParamsNotNull @RequestParam(value = "y") Double y, @ParamsNotNull @RequestParam(value = "projectKey") String projectKey, @ParamsNotNull @RequestParam(value = "projectName") String projectName) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Btag_Sql btag_sql = new Btag_Sql();

        Beacon_tag beacon_tag = new Beacon_tag(id, name, major, minor, projectKey, user.getUserkey(), x, y, projectName,user.getCustomerkey());
        boolean status = btag_sql.update(bTagMapper, beacon_tag);
        beacon_tagMap = btag_sql.getAllBeacon(bTagMapper);
        if (status) {
            response = JsonConfig.getJson(CODE_OK, null);
            //需要把信标信息添加到定位列表中、
        } else {
            response = JsonConfig.getJson(CODE_SQL_ERROR, null);
        }
        return response;
    }

    @RequestMapping(value = "/Api/DeleteGateway", method = RequestMethod.GET, produces = "text/plain")
    public String deleteGateway(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "address") String address) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Gateway_sql gateway_sql = new Gateway_sql();
        int status = gateway_sql.delete(gatewayMapper, address);
        if (status >= 0) {
            response = JsonConfig.getJson(CODE_OK, null);
            Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + address);
            redisUtil.del(redis_key_gateway + address);

            redisUtil.del(redis_key_gatewayConfig_onLine + gateway.getConfig_key());
            gateway = null;
            gatewayMap.remove(address);

        } else {
            response = JsonConfig.getJson(CODE_SQL_ERROR, null);
        }
        return response;
    }

    @RequestMapping(value = "/Api/DeleteProject", method = RequestMethod.GET, produces = "text/plain")
    public String deleteProject(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "projectkey") String projectkey) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        GatewayConfig_sql project_sql = new GatewayConfig_sql();
        int status = project_sql.delete(gatewayConfigMapper, projectkey, user.getCustomerkey());
        if (status >= 0) {
            //删除缓存数据
            response = JsonConfig.getJson(CODE_OK, null);
            redisUtil.del(redis_key_gatewayConfig_onLine + projectkey);
            gatewayConfigMap.remove(projectkey);
            //把网关全部归类到默认项目下
            for (String address : gatewayMap.keySet()) {
                Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + address);
                Gateway_sql gateway_sql = new Gateway_sql();
                if (gateway.getConfig_key().equals(projectkey)) {
                    gateway.setConfig_key("projectdefault_" + user.getCustomerkey());
                    redisUtil.set(redis_key_gateway + address, gateway);
                    gateway_sql.updateGateway(gatewayMapper, gateway);
                }
            }

        } else {
            response = JsonConfig.getJson(CODE_SQL_ERROR, null);
        }
        return response;
    }


    @RequestMapping(value = "/Api/getCustomerFirmwareVersion", method = RequestMethod.GET, produces = "text/plain")
    public String getCustomerFirmwareVersion(HttpServletRequest request) {
        String response = "默认参数";
        Customer user = getCustomer(request);
        if (user == null) {
            response = JsonConfig.getJson(CODE_OFFLINE, null);
            return response;
        }
        Wifi wifi = new Wifi();
        List<Wifi_firmware> wifi_firmware = wifi.getCustomerVersion(wifiMapper, user.getCustomerkey());
        Ble ble = new Ble();
        List<Ble_firmware> ble_firmware = ble.getCustomerVersion(bleMapper, user.getCustomerkey());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", constant.code_ok);
        jsonObject.put("type", "getFirmwareResponse");
        if (wifi_firmware == null) {
            jsonObject.put("wifi", "null");
        } else {
            jsonObject.put("wifi", wifi_firmware);
        }
        if (ble_firmware == null) {
            jsonObject.put("ble", "null");
        } else {
            jsonObject.put("ble", ble_firmware);
        }
        //   System.out.println("json="+jsonObject.toString());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/Api/GetBleVersion", method = RequestMethod.GET, produces = "text/plain")
    public String getBleVersion(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "page") String page,
                                @RequestParam(value = "remake") String remake,
                                @RequestParam(value = "version") String version,
                                @ParamsNotNull @RequestParam(value = "limit") String limit) {
        Customer user = getCustomer(request);
        String response = "默认参数";
        Ble ble = new Ble();
        PageBleVersion pageBleVersion = ble.selectPageBleVersion(bleMapper, Integer.valueOf(page), Integer.valueOf(limit), remake, version, user.getCustomerkey());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageBleVersion.getTotal());
        jsonObject.put("data", pageBleVersion.getBle_firmwares());
        response = jsonObject.toString();
        return response;
    }

    @RequestMapping(value = "/Api/DeleteBleVersion", method = RequestMethod.GET, produces = "text/plain")
    public String deleteBleVersion(HttpServletRequest request, @RequestParam(value = "version") String version) {
        //   System.out.println("请求的地址"+request.getContextPath());
        // System.out.println("请求的地址"+request.getRequestURI());
        // System.out.println("请求的地址"+request.getServletPath());
        // System.out.println("请求的地址"+request.getServerPort());
        // System.out.println("请求的地址"+request.getLocalPort());
        Customer user = getCustomer(request);
        String response = "默认参数";
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Ble ble = new Ble();
        int status = ble.delete(bleMapper, user.getUserkey(), version);
        if (status != -1) {
            response = JsonConfig.getJson(CODE_OK, null);
        } else {
            response = JsonConfig.getJson(CODE_SQL_ERROR, null);
        }
        return response;

    }


    @RequestMapping(value = "/Api/GetWifiVersion", method = RequestMethod.GET, produces = "text/plain")
    public String getWifiVersion(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "page") String page,
                                 @RequestParam(value = "remake") String remake,
                                 @RequestParam(value = "version") String version,
                                 @ParamsNotNull @RequestParam(value = "limit") String limit) {
        Customer user = getCustomer(request);
        String response = "默认参数";
        Wifi ble = new Wifi();
        PageWifiVersion pageBleVersion = ble.selectPageWifiVersion(wifiMapper, Integer.valueOf(page), Integer.valueOf(limit), remake, version, user.getCustomerkey());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageBleVersion.getTotal());
        jsonObject.put("data", pageBleVersion.getWifi_firmwares());
        response = jsonObject.toString();
        return response;
    }

    @RequestMapping(value = "/Api/DeleteWifiVersion", method = RequestMethod.GET, produces = "text/plain")
    public String deleteWifiVersion(HttpServletRequest request, @RequestParam(value = "version") String version) {

        Customer user = getCustomer(request);
        String response = "默认参数";
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Wifi wifi = new Wifi();
        int status = wifi.delete(wifiMapper, user.getCustomerkey(), version);
        if (status != -1) {
            response = JsonConfig.getJson(CODE_OK, null);
        } else {
            response = JsonConfig.getJson(CODE_SQL_ERROR, null);
        }
        return response;

    }


    @RequestMapping(value = "/Api/UpdateProject", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getByJSON(HttpServletRequest request, @RequestBody JSONObject json) {
        String response = "";
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        // 直接将json信息打印出来

        //    System.out.println("收到"+json.toString());
        String type = json.getJSONObject("data").getJSONObject("data").getString("cmd");
        String key = json.getString("key");
        Gateway_config gatewayConfig = gatewayConfigMap.get(key);


        for (String gakey : gatewayMap.keySet()) {
            if (((Gateway) redisUtil.get(redis_key_gateway + gakey)).getConfig_key().equals(gatewayConfig.getConfig_key()))
                redisUtil.set(redis_key_project_sys + gakey, "nosyn");
        }


        boolean enable = false;
        int value = 0;
        switch (type) {
            case "scan_report_onoff":
                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    gatewayConfig.setScan_out(1);
                } else {
                    gatewayConfig.setScan_out(0);
                }
                break;
            case "scan_request_onoff":
                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    gatewayConfig.setReport_type(2);
                } else {
                    gatewayConfig.setReport_type(1);
                }
                break;
            case "scan_stuff_card_onoff":
                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    gatewayConfig.setReport_type(4);
                } else {
                    gatewayConfig.setReport_type(1);
                }
                break;
            case "scan_filter_ibcn_dev":
                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    gatewayConfig.setFilter_ibeacon(1);
                } else {
                    gatewayConfig.setFilter_ibeacon(0);
                }
                break;
            case "adv_onoff":
                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    gatewayConfig.setBroadcast(1);
                } else {
                    gatewayConfig.setBroadcast(0);
                }
                break;
            case "scan_report_interval":
                value = json.getJSONObject("data").getJSONObject("data").getInt("value");
                gatewayConfig.setScan_interval(value);
                break;
            case "scan_filter_rssi":
                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    value = json.getJSONObject("data").getJSONObject("data").getInt("value");
                    gatewayConfig.setFilter_rssi(value );
                } else {
                    gatewayConfig.setFilter_rssi(0);
                }
                break;
            case "scan_filter_ibcn_uuid":

                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    String uuid = json.getJSONObject("data").getJSONObject("data").getString("value");
                    gatewayConfig.setFilter_uuids(uuid);
                } else {
                    gatewayConfig.setFilter_uuids("");
                }
                break;
            case "scan_filter_name":
                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    String name = "1";
                    JSONArray jsonArray = json.getJSONObject("data").getJSONObject("data").getJSONArray("value");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        name = name + "-" + jsonArray.getJSONObject(i).getString("name");
                    }
                    gatewayConfig.setFilter_names(name);
                } else {
                    gatewayConfig.setFilter_names("");
                }
                break;
            case "scan_filter_comp_ids":
                enable = json.getJSONObject("data").getJSONObject("data").getBoolean("enable");
                if (enable) {
                    String ids = "1";
                    JSONArray jsonArray = json.getJSONObject("data").getJSONObject("data").getJSONArray("value");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ids = ids + "-" + jsonArray.getJSONObject(i).getString("id");
                    }
                    gatewayConfig.setFilter_companyids(ids);
                } else {
                    gatewayConfig.setFilter_companyids("0");
                }
                break;
        }
        //  redisUtil.set(redis_key_project+key,gatewayConfig);
        GatewayConfig_sql project_sql = new GatewayConfig_sql();
        gatewayConfigMap.put(key, gatewayConfig);
        int status = project_sql.updateGatewayConfig(gatewayConfigMapper, gatewayConfig);
        if (status != -1) {
            response = JsonConfig.getJson(CODE_OK, null);
        } else {
            response = JsonConfig.getJson(CODE_SQL_ERROR, null);
        }
        return response;
    }


    @RequestMapping(value = "/Api/Upload", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String upload(HttpServletRequest request, @RequestParam("file") MultipartFile file, @RequestParam("type") String type, @RequestParam("version") String version,
                         @RequestParam("company_name") String company_name,
                         @RequestParam("remake") String remake) throws IOException {

        InetAddress address = null;
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            String response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        File path = new File(paths + user.getCustomerkey());
        if (!path.exists()) {
            System.out.println("文件夹不存在创建=" + path.mkdirs());
        }
        try {
            address = InetAddress.getLocalHost();
            System.out.println("输出地址=" + address.getHostAddress() + "文件路径=" + path.getName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // System.out.println("[文件类型] - [{}]"+ file.getContentType());
        // System.out.println("[文件名称] - [{}]"+ file.getOriginalFilename());
        // System.out.println("[文件大小] - [{}]"+ file.getSize());

        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //  System.out.println("当前时间为: " + ft.format(dNow));
        //保存
        JSONObject jsonObject = new JSONObject();
        file.transferTo(new File(path.getPath() + "/" + type + "_" + version + ".firmware"));
        if (type.equals("ble")) {
            Ble_firmware ble_firmware = new Ble_firmware(url, remake, ft.format(dNow), company_name, user.getUserkey(), version,user.getCustomerkey());
            int d = bleMapper.insert(ble_firmware);
            // System.out.println("保存蓝牙固件数据的结果码="+d);
        } else if (type.equals("wifi")) {
            Wifi_firmware wifi_firmware = new Wifi_firmware(url, remake, ft.format(dNow), company_name, user.getUserkey(), version,user.getCustomerkey());

            int d = wifiMapper.insert(wifi_firmware);
            //  System.out.println("保存wifi数据的结果码="+d);
        }
        jsonObject.put("code", constant.code_ok);
        jsonObject.put("msg", "上传成功");


        return jsonObject.toString();
    }


    @RequestMapping(value = "/Api/UpdateProjectVersion", method = RequestMethod.GET, produces = "text/plain")
    public String updateProjectVersion(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "bleVersion") String bleVersion,
                                       @RequestParam(value = "wifiVersion") String wifiVersion,
                                       @RequestParam(value = "projectKey") String projectKey) {
        Customer user = getCustomer(request);
        if (user.getUsername().equals("test")) {
            String response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        String response = "默认参数";
        Gateway_config gatewayConfig = gatewayConfigMap.get(projectKey);
        if (gatewayConfig == null) {
            response = JsonConfig.getJson(CODE_RESPONSE_NULL, null);
            return response;
        } else {
            gatewayConfig.setBle_version(bleVersion);
            gatewayConfig.setWifi_version(wifiVersion);
            GatewayConfig_sql project_sql = new GatewayConfig_sql();
            project_sql.updateGatewayConfig(gatewayConfigMapper, gatewayConfig);
            gatewayConfigMap.put(projectKey, gatewayConfig);
            response = JsonConfig.getJson(CODE_OK, null);
            return response;
        }
    }

    @RequestMapping(value = "/Api/UpdateProjectName", method = RequestMethod.GET, produces = "text/plain")
    public String updateProjectName(HttpServletRequest request,
                                    @RequestParam(value = "config_name") String config_name, @RequestParam(value = "config_key") String config_key) {
        Customer user = getCustomer(request);
        String response = "默认参数";
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Gateway_config gatewayConfig = gatewayConfigMap.get(config_key);
        if (gatewayConfig == null) {
            response = JsonConfig.getJson(CODE_RESPONSE_NULL, null);
            return response;
        } else {
            gatewayConfig.setName(config_name);
            GatewayConfig_sql project_sql = new GatewayConfig_sql();
            project_sql.updateGatewayConfig(gatewayConfigMapper, gatewayConfig);
            gatewayConfigMap.put(config_key, gatewayConfig);
            response = JsonConfig.getJson(CODE_OK, null);
            Gateway_sql gateway_sql = new Gateway_sql();
            for (String key : gatewayMap.keySet()) {
                Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + key);
                if (gateway.getConfig_key().equals(config_key)) {
                    gateway.setConfig_name(config_name);
                    gateway_sql.updateGateway(gatewayMapper, gateway);
                    redisUtil.set(redis_key_gateway + key, gateway);
                }
            }
            return response;
        }
    }

    @RequestMapping(value = "/Api/SelectPageRules", method = RequestMethod.GET, produces = "text/plain")
    public String selectPageRules(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "page") String page,
                                  @ParamsNotNull @RequestParam(value = "limit") String limit) {
        Customer user = getCustomer(request);
        String response = "默认参数";
        Rules_sql rules_sql = new Rules_sql();
        PageRules pageRules = rules_sql.selectPageRules(rulesMapper, Integer.valueOf(page), Integer.valueOf(limit), user.getCustomerkey());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageRules.getTotal());
        jsonObject.put("data", pageRules.getRulesList());
        response = jsonObject.toString();
        return response;
    }


    @RequestMapping(value = "/Api/SelectAllRules", method = RequestMethod.GET, produces = "text/plain")
    public String selectAllRules(HttpServletRequest request) {
        Customer user = getCustomer(request);
        String response = "默认参数";
        Rules_sql rules_sql = new Rules_sql();
        List<Rules> list = rules_sql.selectAllRules(rulesMapper, user.getCustomerkey());

        response = JsonConfig.getJson(CODE_OK, list);
        return response;
    }

    @RequestMapping(value = "/Api/DeleteRules", method = RequestMethod.GET, produces = "text/plain")
    public String deleteRules(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "rulesKey") String rulesKey) {
        Customer user = getCustomer(request);
        String response = "默认参数";
        if (user.getUsername().equals("test")) {
            response = JsonConfig.getJson(JsonConfig.CODE_noP, null);
            return response;
        }
        Rules_sql rules_sql = new Rules_sql();
        rules_sql.delete(rulesMapper, rulesKey, user.getCustomerkey());
        response = JsonConfig.getJson(CODE_OK, null);
        for (String key : gatewayConfigMap.keySet()) {
            if (gatewayConfigMap.get(key).getRules_key().equals(rulesKey)) {
                Gateway_config gatewayConfig = gatewayConfigMap.get(key);
                gatewayConfig.setRules_key("");
                gatewayConfig.setRules_name("");
                gatewayConfigMap.put(key, gatewayConfig);
                gatewayConfigMapper.updateById(gatewayConfig);
            }
        }
        return response;
    }

    @RequestMapping(value = "/Api/SelectProject_rules", method = RequestMethod.GET, produces = "text/plain")
    public String selectProject_rules(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "rulesKey") String rulesKey) {
        Customer user = getCustomer(request);
        String response = "默认参数";
        GatewayConfig_sql project_sql = new GatewayConfig_sql();
        List<Gateway_config> list = project_sql.getProjectByRules(gatewayConfigMapper, rulesKey);
        response = JsonConfig.getJson(CODE_OK, list);
        return response;
    }

    @RequestMapping(value = "/aa/GetJsontest", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getJsontest(@RequestBody JSONObject json) {
        // System.out.println("收到="+json.toString());
        return "收";
    }

    @RequestMapping(value = "/Api/setNull", method = RequestMethod.GET)
    @ResponseBody
    public String setNull() {
        for (String key : gatewayMap.keySet()) {
            redisUtil.set(redis_key_gateway_revice_count + key, 0);
            redisUtil.set(redis_key_gateway_onLine_time + key, null);
        }
        return "ok";
    }

    private Customer getCustomer(HttpServletRequest request) {
        String token = request.getHeader("token");
        Customer user = (Customer) redisUtil.get("tokenId:" + token);
        if (user != null) {
            redisUtil.set("tokenId:" + token, user, ExpireTime);
        }
        return user;
    }

    //获取设备的离线记录，包括网关 信标等等
    @RequestMapping(value = "/Api/getOffline", method = RequestMethod.GET, produces = "text/plain")
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


    private void response(HttpServletResponse httpServletResponse, String response) {
        try {
            httpServletResponse.setContentType("application/json; charset=utf-8");
            httpServletResponse.getWriter().write(response);
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
        } catch (Exception e) {

        }
    }

}