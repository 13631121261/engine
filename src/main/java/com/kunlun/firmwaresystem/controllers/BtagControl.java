package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.PageAlarm;
import com.kunlun.firmwaresystem.device.PageBeaconTag;
import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.entity.Beacon_tag;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.Map;
import com.kunlun.firmwaresystem.mappers.AlarmMapper;
import com.kunlun.firmwaresystem.mappers.BTagMapper;
import com.kunlun.firmwaresystem.sql.Alarm_Sql;
import com.kunlun.firmwaresystem.sql.Beacon_Sql;
import com.kunlun.firmwaresystem.sql.Btag_Sql;
import com.kunlun.firmwaresystem.sql.Map_Sql;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class BtagControl {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private BTagMapper bTagMapper;



    @RequestMapping(value = "userApi/Btag/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getPageBtag(HttpServletRequest request) {
        String quickSearch=request.getParameter("quickSearch");
        String page=request.getParameter("page");
        String limit=request.getParameter("limit");
        if(quickSearch==null||quickSearch.equals("")){
            quickSearch="";
        }
        if(page==null||page.equals("")){
            page="1";
        }
        if(limit==null||limit.equals("")){
            limit="10";
        }

        Customer user1 = getCustomer(request);
        Btag_Sql btagSql = new Btag_Sql();
        PageBeaconTag pageAlarm = btagSql.selectPageTag(bTagMapper ,Integer.parseInt(page),Integer.parseInt(limit), user1.getProject_key(),user1.getUserkey(),quickSearch);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageAlarm.getTotal());
        jsonObject.put("data",  pageAlarm.getBeacon_tags());
        return jsonObject;
    }
    @RequestMapping(value = "/userApi/Btag/del", method = RequestMethod.POST, produces = "application/json")
    public JSONObject deleteAlarm(HttpServletRequest request, @RequestBody JSONArray jsonArray) {
        Customer customer=getCustomer(request);
        String lang=customer.getLang();
        List<Integer> id=new ArrayList<Integer>();

        for(Object ids:jsonArray){
            if(ids!=null&&ids.toString().length()>0){
                id.add(Integer.parseInt(ids.toString()));
            }
        }
        if(id.size()>0){
            int status = bTagMapper.deleteBatchIds(id);
            if(status!=-1){
                return JsonConfig.getJsonObj(CODE_OK,null,lang);
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,null,lang);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null,lang);
        }
    }
    @RequestMapping(value = "userApi/Btag/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addBeacon(HttpServletRequest request, @RequestBody JSONObject json) {
        println(json.toString());
        Customer customer = getCustomer(request);
        String lang=customer.getLang();
        Btag_Sql beacon_sql=new Btag_Sql();
        Beacon_tag  beacon=new Gson().fromJson(json.toString(),new TypeToken<Beacon_tag>(){}.getType());
        beacon.setUser_key(customer.getUserkey());
        beacon.setProject_key(customer.getProject_key());
        beacon.setCreate_time(System.currentTimeMillis()/1000);
        beacon.setCustomer_key(customer.getCustomerkey());
        beacon.setUser_key(customer.getUserkey());
        Map map=new Map_Sql().getMapByMapkey(mapMapper,beacon.getMap_key());
        if(map!=null){
            beacon.setMap_name(map.getName());
        }
        boolean status=beacon_sql.addBeaconTag(bTagMapper,beacon);
        if(status){
            return JsonConfig.getJsonObj(CODE_OK,null,lang);
        }
        else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null,lang);
        }
    }
    @RequestMapping(value = "userApi/Btag/edit", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getOneBtagEdit(HttpServletRequest request) {
        String ids= request.getParameter("id");
        int id=Integer.parseInt(ids);
        Beacon_tag beaconTag=bTagMapper.selectById(id);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("code", CODE_OK);
        jsonObject1.put("msg", CODE_OK_txt);
        jsonObject1.put("data", beaconTag);
        return jsonObject1;
    }

    @RequestMapping(value = "userApi/Btag/edit", method = RequestMethod.POST, produces = "application/json")
    public JSONObject updateBtag(HttpServletRequest request,@RequestBody com.alibaba.fastjson.JSONObject jsonObject) {
        JSONObject response = null;
        Customer customer = getCustomer(request);
        String lang=customer.getLang();
        Beacon_tag beaconTag =new Gson().fromJson(jsonObject.toString(),new TypeToken<Beacon_tag>(){}.getType());
        println(beaconTag.toString());
        Map map=new Map_Sql().getMapByMapkey(mapMapper,beaconTag.getMap_key());
        if(map!=null){
            beaconTag.setMap_name(map.getName());
        }
        beaconTag.setUser_key(customer.getUserkey());
        beaconTag.setProject_key(customer.getProject_key());
        beaconTag.setCustomer_key(customer.getCustomerkey());
        bTagMapper.updateById(beaconTag);
        response = JsonConfig.getJsonObj(CODE_OK, null,lang);
        return response;
    }
    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   println("customer="+customer);
        return customer;
    }
}
