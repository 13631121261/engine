package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.PageBeacon;
import com.kunlun.firmwaresystem.device.PageSupplier;
import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.device.Supplier;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.GatewayMapper;
import com.kunlun.firmwaresystem.mappers.Gateway_configMapper;
import com.kunlun.firmwaresystem.mappers.SupplierMapper;
import com.kunlun.firmwaresystem.sql.Beacon_Sql;
import com.kunlun.firmwaresystem.sql.Supplier_Sql;
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
public class SupplierControl {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private SupplierMapper supplierMapper;


    @RequestMapping(value = "userApi/supplier/index", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public JSONObject getAllSupplier(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        Supplier_Sql supplierSql=new Supplier_Sql();
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
        PageSupplier pageSupplier=supplierSql.selectPageSupplier(supplierMapper,page,limit,customer.getUserkey(),customer.getProject_key(),quickSearch);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageSupplier.getTotal());
        jsonObject.put("data", pageSupplier.getSupplierList());
         return jsonObject;
    }

    @RequestMapping(value = "userApi/supplier/edit", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public JSONObject getSupplier(HttpServletRequest request, @RequestParam("id") @ParamsNotNull int id) {
        Customer customer = getCustomer(request);
        Supplier_Sql supplierSql=new Supplier_Sql();
        Supplier supplier=supplierSql.getOneSupplier(supplierMapper,id);
        return JsonConfig.getJsonObj(CODE_OK,supplier);
    }

    @RequestMapping(value = "userApi/supplier/edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONObject updateSupplier(HttpServletRequest request,@RequestBody JSONObject json) {

        Customer customer = getCustomer(request);
        Supplier_Sql supplierSql=new Supplier_Sql();
        Supplier supplier=new Gson().fromJson(json.toString(),new TypeToken<Supplier>(){}.getType());
        supplier.setUser_key(customer.getUserkey());
        supplier.setProject_key(customer.getProject_key());
        supplier.setUpdate_time(System.currentTimeMillis()/1000);
        supplier.setCustomer_key(customer.getCustomerkey());

        int status=supplierSql.update(supplierMapper,supplier);
        if(status>-1){
            return JsonConfig.getJsonObj(CODE_OK,null);
        }
        else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null);
        }
    }
    @RequestMapping(value = "userApi/supplier/del", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONObject deleteSupplier(HttpServletRequest request, @RequestBody JSONArray jsonArray) {
        Supplier_Sql supplierSql=new Supplier_Sql();
        List<Integer> id=new ArrayList<Integer>();
        for(Object ids:jsonArray){
            if(ids!=null&&ids.toString().length()>0){
                id.add(Integer.parseInt(ids.toString()));
            }
        }
        if(id.size()>0){
            int status = supplierSql.deletes(supplierMapper, id);
            if(status!=-1){
                return JsonConfig.getJsonObj(CODE_OK,null);
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null);
        }
    }
    @RequestMapping(value = "userApi/supplier/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONObject addSupplier(HttpServletRequest request, @RequestBody JSONObject json) {
        System.out.println(json.toString());
        Customer customer = getCustomer(request);
        Supplier_Sql supplierSql=new Supplier_Sql();
        Supplier supplier=new Gson().fromJson(json.toString(),new TypeToken<Supplier>(){}.getType());
        supplier.setUser_key(customer.getUserkey());
        supplier.setProject_key(customer.getProject_key());
        supplier.setCreate_time(System.currentTimeMillis()/1000);
        supplier.setCustomer_key(customer.getCustomerkey());

        boolean status=supplierSql.addSupplier(supplierMapper,supplier);
        if(status){
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
