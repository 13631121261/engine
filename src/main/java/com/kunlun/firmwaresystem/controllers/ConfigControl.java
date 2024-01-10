package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.NewSystemApplication;
import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.Gateway_config;
import com.kunlun.firmwaresystem.entity.Map;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.GatewayMapper;
import com.kunlun.firmwaresystem.mappers.Gateway_configMapper;
import com.kunlun.firmwaresystem.mappers.MapMapper;
import com.kunlun.firmwaresystem.mappers.ProjectMapper;
import com.kunlun.firmwaresystem.sql.Customer_sql;
import com.kunlun.firmwaresystem.sql.GatewayConfig_sql;
import com.kunlun.firmwaresystem.sql.Gateway_sql;
import com.kunlun.firmwaresystem.sql.Map_Sql;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.GatewayMap;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class ConfigControl {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private Gateway_configMapper gatewayConfigMapper;
    @Resource
    private GatewayMapper gatewayMapper;

    @RequestMapping(value = "userApi/config/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getAllConfig(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "project_Key") String project_Key) {
        long a=System.currentTimeMillis();
        Customer user1 = getCustomer(request);
        GatewayConfig_sql gatewayConfigSql = new GatewayConfig_sql();
        List<Gateway_config> gateway_configs=gatewayConfigSql.getAllConfig(gatewayConfigMapper,user1.getUserkey(),project_Key);
        int sum=0;
        for(Gateway_config gatewayConfig:gateway_configs){
            for(String key:GatewayMap.keySet()){
                Gateway gateway=GatewayMap.get(key);
                if(gateway.getConfig_key()!=null&&gateway.getConfig_key().equals(gatewayConfig.getConfig_key())){
                    sum++;
                }
            }
            gatewayConfig.setGateway_count(sum);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", gateway_configs.size());
        jsonObject.put("data", gateway_configs);
        long b=System.currentTimeMillis();

        return jsonObject;
    }
    @RequestMapping(value = "userApi/config/index1", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getAllConfig1(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "project_Key") String project_Key) {
        long a=System.currentTimeMillis();
        Customer user1 = getCustomer(request);
        GatewayConfig_sql gatewayConfigSql = new GatewayConfig_sql();
        List<Gateway_config> gateway_configs=gatewayConfigSql.getAllConfig(gatewayConfigMapper,user1.getUserkey(),project_Key);
        Gateway_config gatewayConfig=new Gateway_config();
        gatewayConfig.setName("不关联配置");
        gatewayConfig.setConfig_key("noConfig_key");
        gateway_configs.add(0,gatewayConfig);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", gateway_configs.size());
        jsonObject.put("data", gateway_configs);
        long b=System.currentTimeMillis();
        System.out.println("getAllConfig="+(b-a));
        return jsonObject;
    }
    @RequestMapping(value = "userApi/config/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addConfig(HttpServletRequest request, @RequestBody  JSONObject jsonObject) {
        System.out.println("上报json="+jsonObject.toString());
        JSONObject json=null;
        long a=System.currentTimeMillis();
        Customer customer = getCustomer(request);
        GatewayConfig_sql gatewayConfigSql = new GatewayConfig_sql();
        Gateway_config gatewayConfig=new Gson().fromJson(jsonObject.toString(),new  TypeToken<Gateway_config>(){}.getType());
        gatewayConfig.setProject_key(customer.getProject_key());
        gatewayConfig.setUser_key(customer.getUserkey());
        gatewayConfig.setCustomer_key(customer.getCustomerkey());
        gatewayConfig.setCreate_time(System.currentTimeMillis()/1000);
        gatewayConfig.setConfig_key1();
        gatewayConfig.setFilter_companyid(gatewayConfig.getFilter_companyid());
        gatewayConfig.setFilter_name(gatewayConfig.getFilter_name());
        gatewayConfig.setFilter_uuid(gatewayConfig.getFilter_uuid());
        gatewayConfig.setServices_uuid(gatewayConfig.getServices_uuid());
        gatewayConfig.setFilter_mac(gatewayConfig.getFilter_mac());
       boolean status= gatewayConfigSql.addGatewayConfig(gatewayConfigMapper,gatewayConfig);
        if(status){
             json = JsonConfig.getJsonObj(JsonConfig.CODE_OK,null);
        }else{
            json = JsonConfig.getJsonObj(JsonConfig.CODE_REPEAT,null);
        }
        return json;
    }
    @RequestMapping(value = "userApi/config/edit", method = RequestMethod.POST, produces = "application/json")
    public JSONObject editConfig(HttpServletRequest request, @RequestBody  JSONObject jsonObject) {
        System.out.println("上报json="+jsonObject.toString());
        JSONObject json=null;
        long a=System.currentTimeMillis();
        Customer customer = getCustomer(request);
        GatewayConfig_sql gatewayConfigSql = new GatewayConfig_sql();
        Gateway_config gatewayConfig=new Gson().fromJson(jsonObject.toString(),new  TypeToken<Gateway_config>(){}.getType());
        System.out.println(gatewayConfig);
        gatewayConfig.setProject_key(customer.getProject_key());
        gatewayConfig.setUser_key(customer.getUserkey());
        gatewayConfig.setCustomer_key(customer.getCustomerkey());
        gatewayConfig.setUpdate_time(System.currentTimeMillis()/1000);
        gatewayConfig.setFilter_companyid(gatewayConfig.getFilter_companyid());
        gatewayConfig.setFilter_name(gatewayConfig.getFilter_name());
        gatewayConfig.setFilter_uuid(gatewayConfig.getFilter_uuid());
        gatewayConfig.setServices_uuid(gatewayConfig.getServices_uuid());
        gatewayConfig.setFilter_mac(gatewayConfig.getFilter_mac());
        if(!customer.getProject_key().equals(gatewayConfig.getProject_key())){
            json = JsonConfig.getJsonObj(JsonConfig.CODE_SQL_ERROR,null);
        }else{
            int status=gatewayConfigSql.updateGatewayConfig(gatewayConfigMapper,gatewayConfig);
            if(status>-1){
                NewSystemApplication.gatewayConfigMap=gatewayConfigSql.getAllConfig(gatewayConfigMapper);
                json = JsonConfig.getJsonObj(JsonConfig.CODE_OK,null);
            }else{
                json = JsonConfig.getJsonObj(JsonConfig.CODE_REPEAT,null);
            }
        }

        return json;
    }
    @RequestMapping(value = "userApi/config/del", method = RequestMethod.GET, produces = "application/json")
    public JSONObject editConfig(HttpServletRequest request,@RequestParam("config_key") @ParamsNotNull String config_key ) {
        JSONObject response;
        long a = System.currentTimeMillis();
        Customer customer = getCustomer(request);
        GatewayConfig_sql gatewayConfigSql=new GatewayConfig_sql();
        int status=gatewayConfigSql.delete(gatewayConfigMapper,config_key,customer.getUserkey());
        if(status!=-1){
            response=JsonConfig.getJsonObj(CODE_OK,null);
            Gateway_sql gateway_sql=new Gateway_sql();
            gateway_sql.updateGateway(redisUtil,gatewayMapper,config_key);
        }else{
            response=JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
        }
        return response;
    }
    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   System.out.println("customer="+customer);
        return customer;
    }

}
