package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.NewSystemApplication;
import com.kunlun.firmwaresystem.device.*;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.entity.device.*;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.*;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.redis_key_gateway;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class DeviceControl {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private DevicepTypeMapper devicepTypeMapper;
    @Resource
    private DevicePMapper devicePMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private DeviceCodeMapper deviceCodeMapper;
    @Resource
    private DeviceBarnMapper deviceBarnMapper;
    @Resource
    private DeviceOutBarnMapper deviceOutBarnMapper;

    @RequestMapping(value = "userApi/DeviceType/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getDeviceType(HttpServletRequest request) {
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
            limit="20";
        }

        Customer customer = getCustomer(request);
        DevicePType_Sql devicePType_sql=new DevicePType_Sql();
        PageDevicePtype pageDevicePtype=devicePType_sql.selectPageDeviceP(devicepTypeMapper,Integer.valueOf(page),Integer.valueOf(limit),quickSearch,customer.getUserkey(),customer.getProject_key());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageDevicePtype.getTotal());
        jsonObject.put("data", pageDevicePtype.getDeviceList());
         return jsonObject;
    }

    @RequestMapping(value = "userApi/DeviceType/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addDeviceType(HttpServletRequest request,@RequestBody JSONObject jsonObject) {

        Customer customer = getCustomer(request);
        Deviceptype deviceptype=new Gson().fromJson(jsonObject.toString(), new TypeToken<Deviceptype>(){}.getType());

        deviceptype.setUpdate_time(System.currentTimeMillis()/1000);
        deviceptype.setCreatetime(System.currentTimeMillis()/1000);
        deviceptype.setUserkey(customer.getUserkey());
        deviceptype.setCustomer_name(customer.getNickname());
        deviceptype.setProject_key(customer.getProject_key());
        deviceptype.setCustomer_key(customer.getCustomerkey());
        DevicePType_Sql devicePType_sql=new DevicePType_Sql();
        boolean status= devicePType_sql.addDevicePType(devicepTypeMapper,deviceptype);
        if(status){
            return JsonConfig.getJsonObj(CODE_OK,null);
        }else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null);
        }
    }

    @RequestMapping(value = "userApi/DeviceType/edit", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getOneDeviceType(HttpServletRequest request) {
        String ids= request.getParameter("id");
        int id=Integer.parseInt(ids);
        DevicePType_Sql devicePType_sql=new DevicePType_Sql();
        Deviceptype deviceptype=devicePType_sql.getDeviceP(devicepTypeMapper,id);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("code", CODE_OK);
        jsonObject1.put("msg", CODE_OK_txt);
        jsonObject1.put("data", deviceptype);
        return jsonObject1;
    }
    @RequestMapping(value = "userApi/DeviceType/edit", method = RequestMethod.POST, produces = "application/json")
    public JSONObject updateDeviceType(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        Customer customer = getCustomer(request);
        Deviceptype deviceptype=new Gson().fromJson(jsonObject.toString(), new TypeToken<Deviceptype>(){}.getType());
        deviceptype.setUpdate_time(System.currentTimeMillis()/1000);
        deviceptype.setUserkey(customer.getUserkey());
        deviceptype.setCustomer_name(customer.getNickname());
        deviceptype.setProject_key(customer.getProject_key());
        deviceptype.setCustomer_key(customer.getCustomerkey());
        DevicePType_Sql devicePType_sql=new DevicePType_Sql();
        devicePType_sql.update(devicepTypeMapper,deviceptype);

        return JsonConfig.getJsonObj(CODE_OK,null);
    }

    @RequestMapping(value = "userApi/DeviceType/del", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSONObject deleteDeviceType(HttpServletRequest request, @RequestBody JSONArray json) {
        System.out.println(json.toString());
        Customer customer=getCustomer(request);
        List<Integer> list=new ArrayList<>();
        if(json==null||json.size()==0){
            return JsonConfig.getJsonObj(CODE_DR,CODE_DR_txt);
        }else{
            int id =0;
            DevicePType_Sql devicePTypeSql=new DevicePType_Sql();
            for(Object jsonObject:json){
                try {
                    System.out.println(jsonObject.toString());
                    id = Integer.parseInt(jsonObject.toString());
                    list.add(id);

                }catch (Exception e){
                    System.out.println("2"+e.getMessage());
                    return JsonConfig.getJsonObj(CODE_DR,CODE_DR_txt);
                }
            }
           boolean status=  devicePTypeSql.deletes(devicepTypeMapper,list);
             if(status){
                 return JsonConfig.getJsonObj(CODE_OK,"");
             }else{
                 return JsonConfig.getJsonObj(CODE_SQL_ERROR,"");
             }

        }

    }

    @RequestMapping(value = "userApi/DeviceCode/index1", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getAllDeviceCode1(HttpServletRequest request) {
        String quickSearch=request.getParameter("quickSearch");
       Customer customer = getCustomer(request);
       DeviceCode_Sql devicePType_sql=new DeviceCode_Sql();
       List<DeviceCode> deviceCodes= devicePType_sql.getAllCode(deviceCodeMapper,customer.getProject_key(),quickSearch);
       for(DeviceCode deviceCode:deviceCodes){
           deviceCode.setName( deviceCode.getName()+"/"+deviceCode.getCode_sn()+"/"+deviceCode.getBrand()+"/"+deviceCode.getModel());
       }
       JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", deviceCodes.size());
        jsonObject.put("data", deviceCodes);
        return jsonObject;
    }
    @RequestMapping(value = "userApi/DeviceCode/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getPageDeviceCode(HttpServletRequest request) {
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
            limit="20";
        }

        Customer customer = getCustomer(request);
        DeviceCode_Sql devicePType_sql=new DeviceCode_Sql();
        PageDeviceCode pageDevicePtype=devicePType_sql.selectPageDeviceCode(deviceCodeMapper,Integer.valueOf(page),Integer.valueOf(limit),customer.getUserkey(),customer.getProject_key(),quickSearch);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageDevicePtype.getTotal());
        jsonObject.put("data", pageDevicePtype.getDeviceCodes());
        return jsonObject;
    }
    @RequestMapping(value = "userApi/DeviceCode/getCode", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject getCode(HttpServletRequest request,@RequestParam("type_id") @ParamsNotNull String type_id) {
        DeviceCode_Sql deviceCodeSql=new DeviceCode_Sql();
        DeviceCode devicep=deviceCodeSql.getOrderbyId(deviceCodeMapper);
        JSONObject jsonObject = new JSONObject();
        if(devicep!=null){
            jsonObject.put("code", CODE_OK);
            jsonObject.put("msg", CODE_OK_txt);
            String sn=10000+type_id+""+10000+(devicep.getId()+1);
            jsonObject.put("sn", sn);
        }else{
            jsonObject.put("code", CODE_OK);
            jsonObject.put("msg", CODE_OK_txt);
            String sn=10000+type_id+""+10000+(1);
            jsonObject.put("sn", sn);
        }
        return jsonObject;
    }
    @RequestMapping(value = "userApi/DeviceCode/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addDeviceCode(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        System.out.println("实际收到="+jsonObject.toString());
        Customer customer = getCustomer(request);
        DeviceCode deviceCode=new Gson().fromJson(jsonObject.toString(), new TypeToken<DeviceCode>(){}.getType());
        deviceCode.setCustomer_name(customer.getNickname());
        deviceCode.setUpdate_time(System.currentTimeMillis()/1000);
        deviceCode.setCreate_time(System.currentTimeMillis()/1000);
        deviceCode.setUser_key(customer.getUserkey());
        deviceCode.setProject_key(customer.getProject_key());
        deviceCode.setCustomer_key(customer.getCustomerkey());
        DeviceCode_Sql devicePType_sql=new DeviceCode_Sql();
        boolean status= devicePType_sql.addDeviceCode(deviceCodeMapper,supplierMapper,devicepTypeMapper,deviceCode);
        if(status){
            return JsonConfig.getJsonObj(CODE_OK,null);
        }else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null);
        }
    }

    @RequestMapping(value = "userApi/DeviceCode/edit", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getOneDeviceCode(HttpServletRequest request) {
        String ids= request.getParameter("id");
        int id=Integer.parseInt(ids);
        DeviceCode_Sql devicePType_sql=new DeviceCode_Sql();
        DeviceCode deviceptype=devicePType_sql.getOneDeviceCode(deviceCodeMapper,id);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("code", CODE_OK);
        jsonObject1.put("msg", CODE_OK_txt);
        jsonObject1.put("data", deviceptype);
        return jsonObject1;
    }
    @RequestMapping(value = "userApi/DeviceCode/edit", method = RequestMethod.POST, produces = "application/json")
    public JSONObject updateDeviceCode(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        Customer customer = getCustomer(request);
        DeviceCode deviceCode=new Gson().fromJson(jsonObject.toString(), new TypeToken<DeviceCode>(){}.getType());
        deviceCode.setUpdate_time(System.currentTimeMillis()/1000);
        deviceCode.setUser_key(customer.getUserkey());
        deviceCode.setCustomer_name(customer.getNickname());
        deviceCode.setProject_key(customer.getProject_key());
        deviceCode.setCustomer_key(customer.getCustomerkey());
        DeviceCode_Sql deviceCodeSql=new DeviceCode_Sql();
        Supplier_Sql supplierSql=new Supplier_Sql();
        Supplier supplier= supplierSql.getOneSupplier(supplierMapper,deviceCode.getSupplier_id());
        if(supplier!=null){
            deviceCode.setSupplier_name(supplier.getName());

        }
        DevicePType_Sql devicePType_sql=new DevicePType_Sql();
        Deviceptype deviceptype1= devicePType_sql.getDeviceP(devicepTypeMapper,deviceCode.getType_id());
        if(deviceptype1!=null){
            deviceCode.setType_name(deviceptype1.getName());
        }
        deviceCodeSql.update(deviceCodeMapper,deviceCode);

        return JsonConfig.getJsonObj(CODE_OK,null);
    }

    @RequestMapping(value = "userApi/DeviceCode/del", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSONObject deleteDeviceCode(HttpServletRequest request, @RequestBody JSONArray json) {
        System.out.println(json.toString());
        Customer customer=getCustomer(request);
        List<Integer> list=new ArrayList<>();
        if(json==null||json.size()==0){
            return JsonConfig.getJsonObj(CODE_DR,CODE_DR_txt);
        }else{
            int id =0;
            DeviceCode_Sql devicePTypeSql=new DeviceCode_Sql();
            for(Object jsonObject:json){
                try {
                    System.out.println(jsonObject.toString());
                    id = Integer.parseInt(jsonObject.toString());
                    list.add(id);

                }catch (Exception e){
                    System.out.println("2"+e.getMessage());
                    return JsonConfig.getJsonObj(CODE_DR,CODE_DR_txt);
                }
            }
            int status=  devicePTypeSql.deletes(deviceCodeMapper,list);
            if(status>-1){
                return JsonConfig.getJsonObj(CODE_OK,"");
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,"");
            }

        }

    }



    @RequestMapping(value = "userApi/DeviceBarn/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getPageDeviceBarn(HttpServletRequest request) {
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
            limit="20";
        }

        Customer customer = getCustomer(request);
        DeviceBarn_Sql deviceBarnSql=new DeviceBarn_Sql();
        PageDeviceBarn pageDeviceBarn=deviceBarnSql.selectPageDeviceBarn(deviceBarnMapper,Integer.valueOf(page),Integer.valueOf(limit),customer.getUserkey(),customer.getProject_key(),quickSearch);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageDeviceBarn.getTotal());
        jsonObject.put("data", pageDeviceBarn.getBarnList());
        return jsonObject;
    }

    @RequestMapping(value = "userApi/DeviceBarn/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addDeviceBarn(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        System.out.println("实际收到="+jsonObject.toString());
        Customer customer = getCustomer(request);
        DeviceBarn deviceBarn=new Gson().fromJson(jsonObject.toString(), new TypeToken<DeviceBarn>(){}.getType());
        deviceBarn.setCustomer_name(customer.getNickname());
        deviceBarn.setUpdate_time(System.currentTimeMillis()/1000);
        deviceBarn.setCreate_time(System.currentTimeMillis()/1000);
        deviceBarn.setUser_key(customer.getUserkey());
        deviceBarn.setSurplus(deviceBarn.getAdd_sum());
        deviceBarn.setProject_key(customer.getProject_key());
        deviceBarn.setCustomer_key(customer.getCustomerkey());
        deviceBarn.setBarnType(BarnType.Added);
        DeviceCode_Sql deviceCodeSql=new DeviceCode_Sql();
        List<DeviceCode> deviceCodes=deviceCodeSql.getOneDeviceCode(deviceCodeMapper,deviceBarn.getCode_sn());
        if(deviceCodes==null||deviceCodes.size()>1){
            return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
        }
      //  System.out.println(deviceCodes.get(0));
        deviceBarn.setCode_name(deviceCodes.get(0).getName());
        deviceBarn.setModel(deviceCodes.get(0).getModel());
        deviceBarn.setBrand(deviceCodes.get(0).getBrand());
        deviceBarn.setUnit(deviceCodes.get(0).getUnit());
        DeviceBarn_Sql deviceBarnSql=new DeviceBarn_Sql();
        boolean status= deviceBarnSql.addDeviceBarn(deviceBarnMapper,deviceBarn);
        if(status){
            DeviceCode deviceCode=deviceCodes.get(0);
            //System.out.println(deviceCode.toString());

            double count=0;
            if(deviceCode.getStock()>0){

                count=deviceCode.getStock()*deviceCode.getPrice();
                deviceCode.setStock(deviceCode.getStock()+deviceBarn.getAdd_sum());
                double count1=deviceBarn.getAdd_sum()*deviceBarn.getPrice();
                double all=count+count1;
                double price=all/(deviceCode.getStock());
                deviceCode.setPrice(price);
            }
            else{
                deviceCode.setStock(deviceBarn.getAdd_sum());
                deviceCode.setPrice(deviceBarn.getPrice());
            }
            deviceCode.setSum(deviceCode.getSum()+deviceBarn.getAdd_sum());
            deviceCodeSql.update(deviceCodeMapper,deviceCode);
            return JsonConfig.getJsonObj(CODE_OK,null);
        }else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null);
        }
    }

    @RequestMapping(value = "userApi/DeviceBarn/edit", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getOneDeviceBarn(HttpServletRequest request) {
        String ids= request.getParameter("id");
        int id=Integer.parseInt(ids);
        DeviceBarn_Sql deviceBarnSql=new DeviceBarn_Sql();
        DeviceBarn deviceBarn=deviceBarnSql.getOneDeviceBarn(deviceBarnMapper,id);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("code", CODE_OK);
        jsonObject1.put("msg", CODE_OK_txt);
        jsonObject1.put("data", deviceBarn);
        return jsonObject1;
    }
    @RequestMapping(value = "userApi/DeviceBarn/edit", method = RequestMethod.POST, produces = "application/json")
    public JSONObject updateDeviceBarn(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        Customer customer = getCustomer(request);
        DeviceBarn deviceBarn=new Gson().fromJson(jsonObject.toString(), new TypeToken<DeviceBarn>(){}.getType());
        deviceBarn.setUpdate_time(System.currentTimeMillis()/1000);
        deviceBarn.setUser_key(customer.getUserkey());
        deviceBarn.setCustomer_name(customer.getNickname());
        deviceBarn.setProject_key(customer.getProject_key());
        deviceBarn.setCustomer_key(customer.getCustomerkey());
        DeviceBarn_Sql deviceBarnSql=new DeviceBarn_Sql();
        deviceBarnSql.update(deviceBarnMapper,deviceBarn);
        return JsonConfig.getJsonObj(CODE_OK,null);
    }

    @RequestMapping(value = "userApi/DeviceBarn/del", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSONObject deleteDeviceBarn(HttpServletRequest request, @RequestBody JSONArray json) {
        System.out.println(json.toString());
        Customer customer=getCustomer(request);
        List<Integer> list=new ArrayList<>();
        if(json==null||json.size()==0){
            return JsonConfig.getJsonObj(CODE_DR,CODE_DR_txt);
        }else{
            int id =0;
            DeviceBarn_Sql deviceBarnSql=new DeviceBarn_Sql();
            for(Object jsonObject:json){
                try {
                    System.out.println(jsonObject.toString());
                    id = Integer.parseInt(jsonObject.toString());
                    list.add(id);

                }catch (Exception e){
                    System.out.println("2"+e.getMessage());
                    return JsonConfig.getJsonObj(CODE_DR,CODE_DR_txt);
                }
            }
            int status=  deviceBarnSql.deletes(deviceBarnMapper,list);
            if(status>-1){
                return JsonConfig.getJsonObj(CODE_OK,"");
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,"");
            }
        }
    }
    //根据资产编码，获取全部入库记录
    @RequestMapping(value = "userApi/DeviceBarn/index1", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject getDeviceBarnByCode(HttpServletRequest request, @RequestParam("code_sn") @ParamsNotNull String code_sn) {

        DeviceBarn_Sql deviceBarnSql=new DeviceBarn_Sql();
        List<DeviceBarn> deviceBarns=deviceBarnSql.getDeviceBarn(deviceBarnMapper,code_sn);
      /*  DeviceBarn deviceBarn=new DeviceBarn();
        deviceBarn.setCreate_time("请选择批次");
        deviceBarns.add(0,deviceBarn);*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("data", deviceBarns);
        return jsonObject;
    }
    //常规获取出库记录分页
    @RequestMapping(value = "userApi/DeviceOutBarn/index", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject getDeviceOutBarn(HttpServletRequest request) {
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
            limit="20";
        }
        Customer customer = getCustomer(request);
        DeviceOutBarn_Sql deviceBarnSql=new DeviceOutBarn_Sql();
        PageDeviceOutBarn pageDeviceBarn=deviceBarnSql.selectPageDeviceOutBarn(deviceOutBarnMapper,Integer.valueOf(page),Integer.valueOf(limit),customer.getUserkey(),customer.getProject_key(),quickSearch);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageDeviceBarn.getTotal());
        jsonObject.put("data", pageDeviceBarn.getBarnList());
        return jsonObject;
    }

    @RequestMapping(value = "userApi/DeviceOutBarn/getSn", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject getSn(HttpServletRequest request,@RequestParam("code_sn") @ParamsNotNull String code_sn) {
        DeviceP_Sql deviceP_sql=new DeviceP_Sql();
        Devicep devicep=deviceP_sql.getOrderbyId(devicePMapper);
        JSONObject jsonObject = new JSONObject();
        if(devicep!=null){
            jsonObject.put("code", CODE_OK);
            jsonObject.put("msg", CODE_OK_txt);
            String sn=code_sn+(devicep.getId()+1);
            jsonObject.put("sn", sn);
        }else{
            jsonObject.put("code", CODE_OK);
            jsonObject.put("msg", CODE_OK_txt);
            String sn=code_sn+(1);
            jsonObject.put("sn", sn);
        }
        return jsonObject;
    }
    @RequestMapping(value = "userApi/DeviceOutBarn/del", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSONObject deleteDeviceOutBarn(HttpServletRequest request, @RequestBody JSONArray json) {
        System.out.println(json.toString());
        Customer customer=getCustomer(request);
        List<Integer> list=new ArrayList<>();
        if(json==null||json.size()==0){
            return JsonConfig.getJsonObj(CODE_DR,CODE_DR_txt);
        }else{
            int id =0;
            DeviceOutBarn_Sql deviceOutBarn_sql=new DeviceOutBarn_Sql();
            for(Object jsonObject:json){
                try {
                    System.out.println(jsonObject.toString());
                    id = Integer.parseInt(jsonObject.toString());
                    list.add(id);

                }catch (Exception e){
                    System.out.println("2"+e.getMessage());
                    return JsonConfig.getJsonObj(CODE_DR,CODE_DR_txt);
                }
            }
            int status=  deviceOutBarn_sql.deletes(deviceOutBarnMapper,list);
            if(status>0){
                return JsonConfig.getJsonObj(CODE_OK,"");
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,"");
            }

        }

    }

    @RequestMapping(value = "userApi/DeviceOutBarn/add", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSONObject addDeviceOutBarn(HttpServletRequest request,@RequestBody JSONObject json) {
        System.out.println(json.toString());
        Customer customer = getCustomer(request);
        DeviceOutBarn deviceOutBarn=new Gson().fromJson(json.toString(), new TypeToken<DeviceOutBarn>(){}.getType());
        System.out.println(deviceOutBarn.toString());
        deviceOutBarn.setCreate_time(System.currentTimeMillis()/1000);
        deviceOutBarn.setUser_key(customer.getUserkey());
        deviceOutBarn.setCustomer_name(customer.getNickname());
        deviceOutBarn.setProject_key(customer.getProject_key());
        deviceOutBarn.setCustomer_key(customer.getCustomerkey());

        if(deviceOutBarn.getIdcard()!=null){
            Person_Sql person_sql=new Person_Sql();
            List<Person> personList= person_sql.getPersonByIdCard(personMapper,deviceOutBarn.getIdcard(),customer.getUserkey(),customer.getProject_key());
            if(personList!=null&&personList.size()==1){
                deviceOutBarn.setPerson_name(personList.get(0).getName());
            }
            else{
                deviceOutBarn.setIdcard(null);
            }
        }
        DeviceCode_Sql deviceCodeSql=new DeviceCode_Sql();
        List<DeviceCode> deviceCodes=deviceCodeSql.getOneDeviceCode(deviceCodeMapper,deviceOutBarn.getCode_sn());
        if(deviceCodes!=null&&deviceCodes.size()==1){
            DeviceCode deviceCode=deviceCodes.get(0);
            deviceOutBarn.setModel(deviceCode.getModel());
            deviceOutBarn.setType_id(deviceCode.getType_id());
            deviceOutBarn.setCode_name(deviceCode.getName());
            deviceOutBarn.setBrand(deviceCode.getBrand());
            DeviceOutBarn_Sql deviceOutBarn_sql=new DeviceOutBarn_Sql();
            boolean status= deviceOutBarn_sql.addDeviceOutBarn(deviceOutBarnMapper,deviceOutBarn);
            if(status){

                deviceCode.setStock(deviceCode.getStock()-deviceOutBarn.getOut_sum());
                deviceCodeSql.update(deviceCodeMapper,deviceCode);

                //销毁时是不会有具体的资产的
                if(deviceOutBarn.getBarnType()!=BarnType.Destruction&&deviceOutBarn.getBarnType()!=BarnType.Check){
                    DeviceP_Sql deviceP_sql=new DeviceP_Sql();
                    for(int i=0;i<deviceOutBarn.getOut_sum();i++){
                        Devicep deviceP=new Devicep();
                        deviceP.setCode_sn(deviceOutBarn.getCode_sn());
                        deviceP.setModel(deviceCode.getModel());
                        deviceP.setBrand(deviceCode.getBrand());
                        deviceP.setSn(json.getString("sn"));
                        deviceP.setUserkey(customer.getUserkey());
                        deviceP.setCreatetime(System.currentTimeMillis()/1000);
                        deviceP.setCustomer_key(customer.getCustomerkey());
                        deviceP.setName(deviceCode.getName());
                        deviceP.setPerson_name(deviceOutBarn.getPerson_name());
                        deviceP.setIdcard(deviceOutBarn.getIdcard());
                        deviceP.setProject_key(customer.getProject_key());
                        deviceP.setType_id(deviceOutBarn.getType_id());
                        deviceP.setCreatetime(System.currentTimeMillis()/1000);
                        deviceP.setBarnType(deviceOutBarn.getBarnType());
                        boolean aa=deviceP_sql.addDeviceP(devicePMapper,deviceP);
                        devicePMap.put(deviceP.getSn(),deviceP);
                        if(aa){
                            continue;
                        }
                        else{
                            System.out.println("新增资产异常");
                            return JsonConfig.getJsonObj(CODE_REPEAT,null);
                        }
                    }
                }

                return JsonConfig.getJsonObj(CODE_OK,null);
            }else{
                return JsonConfig.getJsonObj(CODE_REPEAT,null);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
        }


    }




    //获取资产分页
    @RequestMapping(value = "userApi/Devicep/index", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject getDeviceP(HttpServletRequest request) {
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
            limit="20";
        }
        Customer customer = getCustomer(request);
        DeviceP_Sql  deviceP_sql=new DeviceP_Sql();
        PageDeviceP pageDeviceP=deviceP_sql.selectPageDeviceP(devicePMapper,Integer.valueOf(page),Integer.valueOf(limit),quickSearch,customer.getUserkey(),customer.getProject_key());
        for(Devicep devicep:pageDeviceP.getDeviceList()){
            if(devicePMap.get(devicep.getSn())==null){
                continue;
            }

           // System.out.println("输出="+devicep.getSn());
            devicep.setLasttime(devicePMap.get(devicep.getSn()).getLasttime());
            try {
                if(devicep.getIs_area()==1){
                    if(area_Map.get(devicep.getArea_id())!=null){
                        devicep.setArea_name(area_Map.get(devicep.getArea_id()).getName());
                    }

                }else{
                    devicep.setArea_name("未绑定");
                }
                System.out.println(devicep.getSn());
                Devicep devicep1=devicePMap.get(devicep.getSn());
                System.out.println(devicep1);
                if(devicep1!=null){
                    devicep.setGateway_name(devicep1.getGateway_name());
                    devicep.setB_area_id(devicep1.getB_area_id());
                    devicep.setB_area_name(devicep1.getB_area_name());
                    devicep.setGateway_mac(devicep1.getGateway_mac());
                    devicep.setLasttime(devicep1.getLasttime());
                    devicep.setOnline(devicep1.getOnline());
                    devicep.setBt(devicep1.getBt());
                    devicep.setSos(devicep1.getSos());
                    devicep.setRun(devicep1.getRun());
                    devicep.setX(devicep1.getX());
                    devicep.setY(devicep1.getY());
                    devicep.setRssi(devicep1.getRssi());
                    devicep.setMap_name(devicep1.getMap_name());
                    devicep.setOnline(devicep1.getOnline());
                    System.out.println("正常");
                }

            }catch (Exception e){
                System.out.println("有点异常"+e.toString());
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", pageDeviceP.getTotal());
        jsonObject.put("data", pageDeviceP.getDeviceList());
        return jsonObject;
    }


    @RequestMapping(value = "userApi/getDevicebyArea", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject getDeviceByArea(HttpServletRequest request,@RequestParam("area_id") @ParamsNotNull int area_id) {


        DeviceP_Sql  deviceP_sql=new DeviceP_Sql();
        List<Devicep> deviceps=deviceP_sql.getDeviceByAreaID(devicePMapper,area_id);

       for(Devicep devicep:deviceps){
            try {
                if (devicep.getIsbind()==1&&devicep.getBind_mac()!=null) {
                    Beacon beacon= beaconsMap.get(devicep.getBind_mac());
                    if(beacon!=null){
                        devicep.setOnline(beacon.getOnline());
                        devicep.setBt(beacon.getBt());
                        devicep.setSos(beacon.getSos());
                        devicep.setRun(beacon.getRun());
                        devicep.setX(beacon.getX());
                        devicep.setY(beacon.getY());
                        devicep.setRssi(beacon.getRssi());
                        devicep.setGateway_mac(beacon.getGateway_address());
                        devicep.setArea_name(area_Map.get(area_id).getName());
                        if(beacon.getGateway_address()!=null&&beacon.getGateway_address().length()>0){
                            Gateway gateway=(Gateway) redisUtil.get(redis_key_gateway+beacon.getGateway_address());
                            int areaid= gateway.getArea_id();
                            devicep.setB_area_id(areaid);
                            devicep.setB_area_name(area_Map.get(area_id).getName());
                        }
                    }
                }
            }catch (Exception e){
            }
        }
        DeviceTree online_deviceTree=new DeviceTree();
        online_deviceTree.setId(-1);
        online_deviceTree.setLabel("在线资产：0");
        DeviceTree offline_deviceTree=new DeviceTree();
        offline_deviceTree.setId(-2);
        offline_deviceTree.setLabel("离线资产：0");

        DeviceTree deviceTree1=null;
        DeviceTree deviceTree9=null;
        for(Devicep devicep:deviceps){
            if(devicep.getOnline()==1){
                deviceTree1=new DeviceTree();
                deviceTree1.setId(devicep.getId());
                deviceTree1.setLabel(devicep.getName());
               // deviceTree1.setChildren(new ArrayList<DeviceTree>());
                DeviceTree deviceTree2=new DeviceTree();
                deviceTree2.setId(-3);
                deviceTree2.setLabel("编号"+devicep.getSn());
                DeviceTree deviceTree3=new DeviceTree();
                deviceTree3.setId(-4);
                deviceTree3.setLabel("标签:"+devicep.getBind_mac());
                DeviceTree deviceTree4=new DeviceTree();
                deviceTree4.setId(-5);
                deviceTree4.setLabel("区域:"+devicep.getArea_name());
                DeviceTree deviceTree5=new DeviceTree();
                deviceTree5.setId(-6);
                deviceTree5.setLabel("X坐标:"+devicep.getX());
                DeviceTree deviceTree6=new DeviceTree();
                deviceTree6.setId(-6);
                deviceTree6.setLabel("Y坐标:"+devicep.getY());
                deviceTree1.addChildren(deviceTree2);
                deviceTree1.addChildren(deviceTree3);
                deviceTree1.addChildren(deviceTree4);
                deviceTree1.addChildren(deviceTree5);
                deviceTree1.addChildren(deviceTree6);
                online_deviceTree.addChildren(deviceTree1);
            }
            else{

                    deviceTree9=new DeviceTree();
                    deviceTree9.setId(devicep.getId());
                    deviceTree9.setLabel(devicep.getName());
                 //   deviceTree9.setChildren(new ArrayList<DeviceTree>());
                    DeviceTree deviceTree2=new DeviceTree();
                    deviceTree2.setId(-3);
                    deviceTree2.setLabel("编号"+devicep.getSn());
                    DeviceTree deviceTree3=new DeviceTree();
                    deviceTree3.setId(-4);
                    deviceTree3.setLabel("标签:"+devicep.getBind_mac());
                    DeviceTree deviceTree4=new DeviceTree();
                    deviceTree4.setId(-5);
                    deviceTree4.setLabel("区域:"+devicep.getArea_name());
                    DeviceTree deviceTree5=new DeviceTree();
                    deviceTree5.setId(-6);
                    deviceTree5.setLabel("X坐标:"+devicep.getX());
                    DeviceTree deviceTree6=new DeviceTree();
                    deviceTree6.setId(-6);
                    deviceTree6.setLabel("Y坐标:"+devicep.getY());
                    deviceTree9.addChildren(deviceTree2);
                    deviceTree9.addChildren(deviceTree3);
                    deviceTree9.addChildren(deviceTree4);
                    deviceTree9.addChildren(deviceTree5);
                    deviceTree9.addChildren(deviceTree6);
                    offline_deviceTree.addChildren(deviceTree9);
                }
        }
        if(online_deviceTree.getChildren()!=null){
            online_deviceTree.setLabel("在线资产："+online_deviceTree.getChildren().size());
        }
        if(offline_deviceTree.getChildren()!=null){
            offline_deviceTree.setLabel("离线资产："+offline_deviceTree.getChildren().size());
        }

        ArrayList<DeviceTree> list=new ArrayList<>();
        list.add(online_deviceTree);
        list.add(offline_deviceTree);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CODE_OK);
        jsonObject.put("msg", CODE_OK_txt);
        jsonObject.put("count", list.size());
        jsonObject.put("data", list);
        return jsonObject;
    }
    @RequestMapping(value = "userApi/getDevicebyMap", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject getDevicebyMap(HttpServletRequest request,@RequestParam("map_key") @ParamsNotNull String map_key) {
        try {
            Customer customer = getCustomer(request);
            DeviceP_Sql deviceP_sql = new DeviceP_Sql();
            List<Devicep> onlineDeviceps = new ArrayList<>();
            List<Devicep> offlineDeviceps = new ArrayList<>();
            int online = 0;
            int offline = 0;
            for (Map.Entry entry : devicePMap.entrySet()) {
                Devicep devicep = (Devicep) entry.getValue();
                if (devicep.getProject_key().equals(customer.getProject_key())) {
                    Gateway gateway = (Gateway) redisUtil.get(redis_key_gateway + devicep.getGateway_mac());
                    if (gateway != null && gateway.getMap_key().equals(map_key)) {
                    try {
                        if (devicep.getOnline() == 1) {
                            online++;
                            onlineDeviceps.add(devicep);
                        } else {
                            offlineDeviceps.add(devicep);
                            offline++;
                        }
                    }catch (Exception e){
                        System.out.println("异常--="+e);
                    }
                    }
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", CODE_OK);
            jsonObject.put("msg", CODE_OK_txt);
            jsonObject.put("offlineDevicep", offlineDeviceps);
            jsonObject.put("onlineDevicep", onlineDeviceps);
            return jsonObject;
        }catch (Exception e){
            System.out.println("异常="+e);
            return null;
        }
    }
    @RequestMapping(value = "userApi/bindTag", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSONObject bindTag(HttpServletRequest request,@RequestBody JSONObject json) {
        System.out.println(json.toString());
        Customer customer=getCustomer(request);
        DeviceP_Sql deviceP_sql=new DeviceP_Sql();
        Devicep devicep=null;
      List<Devicep> deviceps=  deviceP_sql.getDevicePBySn(devicePMapper,json.getString("sn"));
      if(deviceps==null||deviceps.size()!=1){
          return JsonConfig.getJsonObj(CODE_REPEAT,null);
      }else{
          devicep=deviceps.get(0);
          devicep.setOpen_run(json.getInteger("open_run"));
          if(json.getInteger("area_id")!=-1){
              devicep.setArea_id(json.getInteger("area_id"));
              devicep.setIs_area(1);
          }else{
              devicep.setArea_id(-1);
              devicep.setIs_area(0);
          }
      }
        if(!json.getString("idcard").equals("-1")){
            String idcard=json.getString("idcard");
            if(devicep.getIdcard()!=null&&idcard.equals(devicep.getIdcard())){
                //没有变更绑定的人
                System.out.println("没有变更绑定的人");
            }else{
                Person_Sql person_sql=new Person_Sql();
                List<Person> personList=person_sql.getPersonByIdCard(personMapper,idcard,customer.getUserkey(),customer.getProject_key());
                if(personList==null||personList.size()!=1){
                    return JsonConfig.getJsonObj(CODE_REPEAT,null);
                }else{
                    Person person=personList.get(0);
                    devicep.setPerson_name(person.getName());
                    devicep.setIdcard(person.getIdcard());
                }
            }
        }else{
            System.out.println("解除绑定的人");
            devicep.setPerson_name("");
            devicep.setIdcard("");
        }
        Beacon_Sql beacon_sql=new Beacon_Sql();
        String mac=json.getString("mac");
        if(devicep.getIsbind()==1&&devicep.getBind_mac()!=null){
            String old_mac=devicep.getBind_mac();
            //mac不变，说明没有变更绑定。不需要操作
            if(old_mac.equals(mac)){
                deviceP_sql.update(devicePMapper,devicep);
              //  return JsonConfig.getJsonObj(CODE_OK,null);
            }
        }
        if(mac.equals("不绑定信标")&&devicep.getIsbind()==0){
            deviceP_sql.update(devicePMapper,devicep);
           // return JsonConfig.getJsonObj(CODE_OK,null);
        }

            //原有的绑定
            if(devicep.getIsbind()==1){

               List<Beacon> beaconList1= beacon_sql.getBeaconByMac(beaconMapper,customer.getUserkey(),customer.getProject_key(),devicep.getBind_mac());
                if(beaconList1==null||beaconList1.size()!=1){
                    return JsonConfig.getJsonObj(CODE_REPEAT,null);
                }else{
                    Beacon beacon1=beaconList1.get(0);
                    beacon1.setIsbind(0);
                    beacon1.setBind_type(0);
                    beacon1.setDevice_sn("");
                    beacon1.setDevice_name("");
                    beacon_sql.update(beaconMapper,beacon1);
                }
            }
            if(!mac.equals("不绑定信标")){
                    List<Beacon> beaconList= beacon_sql.getBeaconByMac(beaconMapper,customer.getUserkey(),customer.getProject_key(),mac);
                if(beaconList==null||beaconList.size()!=1){
                    return JsonConfig.getJsonObj(CODE_REPEAT,null);
                }else{
                    //更新信标绑定的资产
                    Beacon beacon=beaconList.get(0);
                    beacon.setDevice_sn(json.getString("sn"));
                    beacon.setIsbind(1);
                    beacon.setBind_type(1);
                    beacon.setDevice_name(devicep.getName());
                    beacon_sql.update(beaconMapper,beacon);
                    //更新资产
                    devicep.setBind_mac(beacon.getMac());
                    devicep.setIsbind(1);
                    deviceP_sql.update(devicePMapper,devicep);
                }
             }else{
                    devicep.setBind_mac("");
                    devicep.setIsbind(0);
                    deviceP_sql.update(devicePMapper,devicep);
                }
            String fence_id=json.getString("fence_id");
            if(fence_id==null||fence_id.equals("")){
                fence_id=json.getInteger("fence_id")+"";
            }
            if(!fence_id.equals("")&&fence_id.length()>0){
                devicep.setFence_id(Integer.parseInt(fence_id));
                deviceP_sql.update(devicePMapper,devicep);
            }
            System.out.println("围栏id="+fence_id);
            devicePMap=deviceP_sql.getAllDeviceP(devicePMapper);
            beaconsMap=beacon_sql.getAllBeacon(beaconMapper);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", CODE_OK);
            jsonObject.put("msg", CODE_OK_txt);
        return jsonObject;
    }
    @RequestMapping(value = "userApi/unBindTag", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject unBindTag(HttpServletRequest request, @RequestParam("sn") @ParamsNotNull String sn) {
        Customer customer=getCustomer(request);
        DeviceP_Sql deviceP_sql=new DeviceP_Sql();
        Devicep devicep=null;
        List<Devicep> deviceps=  deviceP_sql.getDevicePBySn(devicePMapper,sn);
        if(deviceps==null||deviceps.size()!=1){
            return JsonConfig.getJsonObj(CODE_REPEAT,null);
        }else{
            devicep=deviceps.get(0);
            devicep.setOpen_run(0);
            Beacon_Sql beacon_sql=new Beacon_Sql();
            if(devicep.getIsbind()==1){
                List<Beacon> beaconList1= beacon_sql.getBeaconByMac(beaconMapper,customer.getUserkey(),customer.getProject_key(),devicep.getBind_mac());
                if(beaconList1==null||beaconList1.size()!=1){
                    return JsonConfig.getJsonObj(CODE_REPEAT,null);
                }else{
                    Beacon beacon1=beaconList1.get(0);
                    beacon1.setIsbind(0);
                    beacon1.setDevice_sn("");
                    beacon_sql.update(beaconMapper,beacon1);
                    Beacon beacon=beaconsMap.get(beacon1.getMac());
                    beacon.setIsbind(0);
                    beacon.setDevice_sn("");
                }
            }
            devicep.setIsbind(0);
            devicep.setBind_mac("");
            devicep.setIdcard("");
            devicep.setPerson_name("");
            deviceP_sql.update(devicePMapper,devicep);
            devicePMap.put(devicep.getSn(),devicep);
            return getJsonObj(CODE_OK,null);
        }
    }


    @RequestMapping(value = "userApi/reback", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSONObject reback(HttpServletRequest request, @RequestParam("sn") @ParamsNotNull String sn) {
        Customer customer=getCustomer(request);
        DeviceBarn_Sql deviceBarnSql=new DeviceBarn_Sql();
        DeviceCode_Sql deviceCodeSql=new DeviceCode_Sql();
        DeviceP_Sql deviceP_sql=new DeviceP_Sql();
        List<Devicep> devicepList=deviceP_sql.getDevicePBySn(devicePMapper,sn);
        if(devicepList==null||devicepList.size()!=1){
            return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
        }
        Devicep devicep=devicepList.get(0);
        if(devicep.getIsbind()==1||(devicep.getIdcard()!=null&&!devicep.getIdcard().equals(""))){
            return JsonConfig.getJsonObj(CODE_10,null);
        }
        List<DeviceCode> deviceCodeList=deviceCodeSql.getOneDeviceCode(deviceCodeMapper,devicep.getCode_sn());
        if(deviceCodeList==null||deviceCodeList.size()!=1){
            return JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
        }
        DeviceCode deviceCode=deviceCodeList.get(0);
      //  DeviceBarn deviceBarn=deviceBarnSql.getOneDeviceBarn(deviceBarnMapper,Long.parseLong(devicep.getBatch()));
        deviceCode.setStock(deviceCode.getStock()+1);
      // deviceBarn.setSurplus(deviceBarn.getSurplus()+1);
      //  deviceBarnSql.update(deviceBarnMapper,deviceBarn);
        deviceCodeSql.update(deviceCodeMapper,deviceCode);
        //记得实现一个归还入库记录。
        System.out.println("新增一个鬼=归还入库");
        deviceP_sql.delete(devicePMapper,sn);
        System.out.println("新增一个鬼=归还入库");
        DeviceBarn deviceBarn=new DeviceBarn();
        deviceBarn.setBrand(deviceCode.getBrand());
        deviceBarn.setNotes("归还");
        deviceBarn.setModel(deviceCode.getModel());
        deviceBarn.setCode_sn(deviceCode.getCode_sn());
        deviceBarn.setCode_name(deviceCode.getName());
      //  deviceBarn.setSurplus(deviceCode.getSupplier_id());
        deviceBarn.setAdd_sum(1);
        deviceBarn.setBarnType(BarnType.Return);
        deviceBarn.setPrice(deviceCode.getPrice());
        deviceBarn.setCreate_time(System.currentTimeMillis()/1000);
        deviceBarn.setUnit(deviceCode.getUnit());
        deviceBarn.setCustomer_key(customer.getCustomerkey());
        deviceBarn.setProject_key(customer.getProject_key());
        deviceBarn.setCustomer_name(customer.getNickname());
        deviceBarn.setUser_key(customer.getUserkey());
        System.out.println(deviceBarn.toString());
        try{
        boolean status=deviceBarnSql.addDeviceBarn(deviceBarnMapper,deviceBarn);
        if(status){
            System.out.println("新增");
            return  JsonConfig.getJsonObj(CODE_OK,null);
        }else{
            System.out.println("新增失败");
            return  JsonConfig.getJsonObj(CODE_SQL_ERROR,null);
        }}
        catch (Exception e){
            System.out.println("异常="+e.toString());
            return null;
        }



    }


    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   System.out.println("customer="+customer);
        return customer;
    }

    class DeviceTree{
        int id;
        String label;
        List<DeviceTree> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<DeviceTree> getChildren() {
            return children;
        }

        public void setChildren(List<DeviceTree> children) {
            this.children = children;
        }
        public void addChildren(DeviceTree deviceTree){
            if(deviceTree==null){
                return;
            }
            if(children!=null){
                children.add(deviceTree);
            }
            else{
                children=new ArrayList<>();
                children.add(deviceTree);
            }
        }
    }

}
