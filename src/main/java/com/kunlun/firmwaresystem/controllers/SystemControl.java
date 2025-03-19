package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.entity.Check_sheet;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.Project;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.CheckSheetMapper;
import com.kunlun.firmwaresystem.mqtt.MyMqttClient;
import com.kunlun.firmwaresystem.mqtt.TMyMqttClient;
import com.kunlun.firmwaresystem.sql.CheckSheet_Sql;
import com.kunlun.firmwaresystem.sql.Config_Sql;
import com.kunlun.firmwaresystem.sql.Map_Sql;
import com.kunlun.firmwaresystem.sql.Project_Sql;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.redis_key_gateway;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class SystemControl {
    @Autowired
    private RedisUtils redisUtil;
    @Autowired
    private CheckSheetMapper checkSheetMapper;
    @RequestMapping(value = "/userApi/SystemSet", method = RequestMethod.POST, produces = "application/json")
    public JSONObject SystemSet(HttpServletRequest request, @RequestBody JSONObject json) {
        Customer customer=getCustomer(request);
        println(json.toString());
        String jsons=json.toString();
        jsons=jsons.replaceAll("true","1");
        jsons=jsons.replaceAll("false","0");
        json=JSONObject.parseObject(jsons);
        Check_sheet check_sheet=new Gson().fromJson(json.toString(),new TypeToken<Check_sheet>(){}.getType());
        println("check_sheet"+check_sheet);
        check_sheet.setProject_key(customer.getProject_key());
        check_sheet.setUserkey(customer.getUserkey());
        JSONObject jsonObject= JsonConfig.getJsonObj(JsonConfig.CODE_OK,"",customer.getLang());
        CheckSheet_Sql checkSheet_sql=new CheckSheet_Sql();
        checkSheet_sql.update(checkSheetMapper,check_sheet);
        check_sheetMap=checkSheet_sql.getCheckSheet(checkSheetMapper);
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                check_sheetMap.put(customer.getProject_key(),check_sheet);
                MyMqttClient myMqttClient=myMqttClientMap.get(customer.getProject_key());
                if(myMqttClient!=null&&myMqttClient.getStatus()){
                    println("sdsd");
                    myMqttClient.disConnect();
                    myMqttClient.setHost(check_sheet.getHost());
                    myMqttClient.setPort(check_sheet.getPort());
                    myMqttClient.setPassword(check_sheet.getPassword());
                    myMqttClient.setUser(check_sheet.getUser());
                    myMqttClient.setSub(check_sheet.getSub());
                    myMqttClient.MyMqttClient1(check_sheet.getHost(),check_sheet.getPort());
                    myMqttClient.start();
                }
                else{
                    println("sss");
                    myMqttClient = new MyMqttClient(check_sheet.getHost(),check_sheet.getPort(),check_sheet.getSub(),check_sheet.getPub(),check_sheet.getQos(),check_sheet.getUser(),check_sheet.getPassword(),check_sheet.getProject_key());
                    myMqttClient.start();
                    myMqttClientMap.put(customer.getProject_key(),myMqttClient);
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {

              //  TMyMqttClient myMqttClient=t_myMqttClientMap.get(customer.getProject_key());
                if(client!=null&&client.getStatus()){
                    println("sdsd");
                    client.disConnect();
                    client.setHost(check_sheet.getHost());
                    client.setPort(check_sheet.getPort());
                    client.setPassword(check_sheet.getPassword());
                    client.setUser(check_sheet.getUser());

                    myMqttClient.TMyMqttClient(check_sheet.getHost(),check_sheet.getPort());
                    client.start();
                }
                else{
                    println("sss");
                    myMqttClient = new TMyMqttClient(check_sheet.getHost(),check_sheet.getPort(),check_sheet.getSub(),check_sheet.getPub(),check_sheet.getQos(),check_sheet.getUser(),check_sheet.getPassword(),check_sheet.getProject_key());
                    myMqttClient.start();
                    t_myMqttClientMap.put(customer.getProject_key(),myMqttClient);
                }

            }
        }).start();*/
        return jsonObject;

    }
    @RequestMapping(value = "/userApi/SystemGet", method = RequestMethod.GET, produces = "application/json")
    public JSONObject SystemGet(HttpServletRequest request) {
        Customer customer=getCustomer(request);
        println(customer.getProject_key());
        println("SystemGet"+check_sheetMap);
       Check_sheet check_sheet= check_sheetMap.get(customer.getProject_key());
        JSONObject jsonObject= JsonConfig.getJsonObj(JsonConfig.CODE_OK,check_sheet,customer.getLang());
        try{
            boolean status=  client.getStatus();
            jsonObject.put("mqtt_status",status);
        } catch (RuntimeException e) {
            jsonObject.put("mqtt_status",false);
        }
        return jsonObject;
    }

    @RequestMapping(value = "/userApi/setDownloadUrl", method = RequestMethod.GET, produces = "application/json")
    public JSONObject setDownloadUrl(HttpServletRequest request,@ParamsNotNull @RequestParam(value = "host") String host) {
        Customer customer=getCustomer(request);
        Config_Sql configSql=new Config_Sql();
        configSql.updateHost(configMapper,host);
        return JsonConfig.getJsonObj(CODE_OK,"",customer.getLang());
    }


    @RequestMapping(value = "userApi/setInterval", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getOneMapByKey(HttpServletRequest request) {
        Customer customer=getCustomer(request);

        String interval = request.getParameter("interval");
        if(interval!=null){
            com.kunlun.firmwaresystem.getLocationTask.interval=Integer.parseInt(interval);
        }else{
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("code", CODE_10);
            jsonObject1.put("msg", CODE_OK_txt);
            return jsonObject1;
        }
        Check_sheet check_sheet=check_sheetMap.get(customer.getProject_key());
        if(check_sheet!=null){
            check_sheet.setIntervals( com.kunlun.firmwaresystem.getLocationTask.interval);
        }else{
            check_sheet=new Check_sheet();
            check_sheet.setUserkey(customer.getUserkey());
            check_sheet.setProject_key(customer.getProject_key());
            check_sheet.setIntervals(com.kunlun.firmwaresystem.getLocationTask.interval);
        }
        CheckSheet_Sql sheetSql=new CheckSheet_Sql();
        sheetSql.update(checkSheetMapper,check_sheet);
        check_sheetMap=sheetSql.getCheckSheet(checkSheetMapper);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("code", CODE_OK);
        jsonObject1.put("msg", CODE_OK_txt);
        return jsonObject1;
    }
    //设置定位引擎参数的A值和N值
    @RequestMapping(value = "userApi/configSet", method = RequestMethod.GET, produces = "application/json")
    public JSONObject configSet(HttpServletRequest request,@ParamsNotNull @RequestParam(value = "A_Rssi") String A_Rssi,@ParamsNotNull @RequestParam(value = "N") String N,@ParamsNotNull @RequestParam(value = "used") int used,@ParamsNotNull @RequestParam(value = "z") double z) {
        JSONObject jsonObject1 = new JSONObject();
        Customer customer=getCustomer(request);
        String lang=customer.getLang();
        println("a="+A_Rssi+"  N="+N+"  Z="+z+"  used="+used);
        int a_rssi=0;
        double n=0;
        a_rssi=Integer.parseInt(A_Rssi);
        n=Double.parseDouble(N);
        println("a="+a_rssi+"  N="+n+"  Z="+z+"  used="+used);

        try {
            if (a_rssi < 0 && n > 0) {
                Project_Sql projectSql = new Project_Sql();
                Project project = projectSql.getProjectByKey(projectMapper, customer.getProject_key());

                project.setN(n);
                project.setArssi(a_rssi);
                usedMap.put(customer.getProject_key(), used);
                project.setUsed(used);
                project.setZ(z);
                projectMapper.updateById(project);
                println("系统更新"+project);
                int i=0;
                for (String address : GatewayMap.keySet()) {
                    Gateway gateway = GatewayMap.get(address);
                 //   println("GatewayMap size="+GatewayMap.size()+  "  I="+i);
                    if (gateway.getProject_key().equals(customer.getProject_key())) {
                        gateway.setArssi(a_rssi);
                        gateway.setN(n);

                        gateway.setZ(z);

                        redisUtil.set(redis_key_gateway + address, gateway);
                        gatewayMapper.updateById(gateway);
                    }

                //    i++;
                }
                jsonObject1.put("code", CODE_OK);
                if (lang != null && lang.equals("en")) {
                    jsonObject1.put("msg", CODE_OK_txt_en);
                } else {
                    jsonObject1.put("msg", CODE_OK_txt);
                }
            } else {
                jsonObject1.put("code", CODE_DR);
                if (lang != null && lang.equals("en")) {
                    jsonObject1.put("msg", CODE_DR_txt_en);
                } else {
                    jsonObject1.put("msg", CODE_DR_txt);
                }
                return jsonObject1;
            }
        }catch (Exception e){
            println("异常="+e);
        }
        return jsonObject1;
    }
    @RequestMapping(value = "/userApi/configGet", method = RequestMethod.GET, produces = "application/json")
    public JSONObject configGet(HttpServletRequest request) {
        Customer customer=getCustomer(request);
        Project_Sql projectSql=new Project_Sql();
        Project project = projectSql.getProjectByKey(projectMapper,customer.getProject_key());
        JSONObject jsonObject= JsonConfig.getJsonObj(JsonConfig.CODE_OK,project,customer.getLang());
        return jsonObject;
    }


    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   println("customer="+customer);
        return customer;
    }
}