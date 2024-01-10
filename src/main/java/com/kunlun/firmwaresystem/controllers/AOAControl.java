package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.PageBeacon;
import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.Person;
import com.kunlun.firmwaresystem.entity.device.Devicep;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.GatewayMapper;
import com.kunlun.firmwaresystem.mappers.Gateway_configMapper;
import com.kunlun.firmwaresystem.sql.Beacon_Sql;
import com.kunlun.firmwaresystem.sql.DeviceP_Sql;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import com.kunlun.firmwaresystem.util.SystemUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class AOAControl {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private Gateway_configMapper gatewayConfigMapper;
    @Resource
    private GatewayMapper gatewayMapper;

    @RequestMapping(value = "userApi/AOA/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getAllBeacon(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        Beacon_Sql beacon_sql=new Beacon_Sql();
        String quickSearch=request.getParameter("quickSearch");
        String pages=request.getParameter("page");
        String limits=request.getParameter("limit");
        int page=1;
        int limit=10;
        if (!StringUtils.isBlank(pages)) {
            page=Integer.parseInt(pages);
        }
        if (!StringUtils.isBlank(limits)) {
            limit=Integer.parseInt(limits);
        }
        if (StringUtils.isBlank(quickSearch)) {
            quickSearch="";
        }
        PageBeacon pageBeacon=beacon_sql.selectPageBeacon_AOA(beaconMapper,page,limit,quickSearch,customer.getUserkey(),customer.getProject_key());
        if(pageBeacon.getBeaconList().size()>0){
            for(Beacon beacon:pageBeacon.getBeaconList()){
                Beacon beacon1=beaconsMap.get(beacon.getMac());
                beacon.setMap_key(beacon1.getMap_key());
                beacon.setSos(beacon1.getSos());
                beacon.setRun(beacon1.getRun());
                beacon.setBt(beacon1.getBt());
                beacon.setLastTime(beacon1.getLastTime());
                beacon.setOnline(beacon1.getOnline());
                if(beacon.getIsbind()==1&&beacon.getBind_type()==1){
                    if(beacon.getDevice_sn()!=null){
                        System.out.println(beacon.getDevice_sn());
                        Devicep devicep=devicePMap.get(beacon.getDevice_sn());
                        if(devicep!=null){
                            beacon.setDevice_name(devicep.getName());
                        }
                    }
                }
                if(beacon.getIsbind()==1&&beacon.getBind_type()==2){
                    if(beacon.getDevice_sn()!=null){
                        System.out.println(beacon.getDevice_sn());
                        Person person=personMap.get(beacon.getDevice_sn());
                        if(person!=null){
                            beacon.setDevice_name(person.getName());
                        }

                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageBeacon.getTotal());
        jsonObject.put("data", pageBeacon.getBeaconList());
         return jsonObject;
    }

    @RequestMapping(value = "userApi/AOA/index1", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getAllBeacon1(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        String type=request.getParameter("type");
        Beacon_Sql beacon_sql=new Beacon_Sql();
        System.out.println("类型="+type);
        List<Beacon> beaconList=beacon_sql.getunAllBeacon(beaconMapper,customer.getUserkey(),customer.getProject_key(),type);
        JSONObject jsonObject = new JSONObject();
        beaconList.add(0,new Beacon("不绑定标签"));
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", beaconList.size());
        jsonObject.put("data",beaconList);
        return jsonObject;
    }



    @RequestMapping(value = "userApi/AOA/del", method = RequestMethod.POST, produces = "application/json")
    public JSONObject deleteBeacon(HttpServletRequest request, @RequestBody JSONArray jsonArray) {
        Customer customer = getCustomer(request);
        Beacon_Sql beacon_sql=new Beacon_Sql();
        List<Integer> id=new ArrayList<Integer>();
        for(Object ids:jsonArray){
            if(ids!=null&&ids.toString().length()>0){
                id.add(Integer.parseInt(ids.toString()));
                for(String key:beaconsMap.keySet()){
                    Beacon beacon=beaconsMap.get(key);
                    if(beacon!=null&&beacon.getId()==Integer.parseInt(ids.toString())&&beacon.getIsbind()==1){
                        return JsonConfig.getJsonObj(CODE_10,null);
                    }
                }
            }
        }
        if(id.size()>0){
            int status = beacon_sql.deletes(beaconMapper, id);
            beaconsMap=beacon_sql.getAllBeacon(beaconMapper);
            if(status!=-1){
                return JsonConfig.getJsonObj(CODE_OK,null);
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null);
        }
    }
    @RequestMapping(value = "userApi/AOA/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addAOA(HttpServletRequest request, @RequestBody JSONObject json) {
        System.out.println(json.toString());
        Customer customer = getCustomer(request);
        Beacon_Sql beacon_sql=new Beacon_Sql();
        Beacon beacon=new Gson().fromJson(json.toString(),new TypeToken<Beacon>(){}.getType());
        beacon.setUser_key(customer.getUserkey());
        beacon.setProject_key(customer.getProject_key());
        beacon.setCreatetime(System.currentTimeMillis()/1000);
        beacon.setCustomer_key(customer.getCustomerkey());
        if(beacon.getMac()!=null){
            beacon.setMac(beacon.getMac().replaceAll(" ","").toLowerCase());
        }

        switch (beacon.getType()){

            case 5:
                beacon.setRun(0);
                beacon.setSos(0);
                break;
        }

        boolean status=beacon_sql.addBeacon(beaconMapper,beacon);
        if(status){
            beaconsMap.put(beacon.getMac(),beacon);
            return JsonConfig.getJsonObj(CODE_OK,null);
        }
          else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null);
            }
    }


    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   System.out.println("customer="+customer);
        return customer;
    }


}
