package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.PageBeaconTag;
import com.kunlun.firmwaresystem.device.PageFWordcard;
import com.kunlun.firmwaresystem.entity.Beacon_tag;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.FWordcard;
import com.kunlun.firmwaresystem.entity.Map;
import com.kunlun.firmwaresystem.gatewayJson.Constant;
import com.kunlun.firmwaresystem.mappers.BTagMapper;
import com.kunlun.firmwaresystem.mappers.FWordcardMapper;
import com.kunlun.firmwaresystem.sql.Btag_Sql;
import com.kunlun.firmwaresystem.sql.FWordcard_Sql;
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

import static com.kunlun.firmwaresystem.NewSystemApplication.mapMapper;
import static com.kunlun.firmwaresystem.NewSystemApplication.println;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class FWordcardControl {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private FWordcardMapper fWordcardMapper;



    @RequestMapping(value = "userApi/fwordcard/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getPageFwordcard(HttpServletRequest request) {
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
        FWordcard_Sql fWordcardSql = new FWordcard_Sql();
        PageFWordcard pageFWordcard = fWordcardSql.selectPageFWordcard(fWordcardMapper ,Integer.parseInt(page),Integer.parseInt(limit), user1.getProject_key(),quickSearch);
        for(FWordcard fWordcard:pageFWordcard.getfWordcardList()){
            FWordcard fWordcard1= (FWordcard) redisUtil.get(Constant.fwordcard+fWordcard.getMac());
            if(fWordcard1!=null){
                fWordcard.setOnline(fWordcard1.getOnline());
                fWordcard.setLastTime(fWordcard1.getLastTime());
                fWordcard.setSos(fWordcard1.getSos());
                fWordcard.setBt(fWordcard1.getBt());
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageFWordcard.getTotal());
        jsonObject.put("data",  pageFWordcard.getfWordcardList());
        return jsonObject;
    }
    @RequestMapping(value = "/userApi/fwordcard/del", method = RequestMethod.POST, produces = "application/json")
    public JSONObject deleteFwordcard(HttpServletRequest request, @RequestBody JSONArray jsonArray) {
        Customer customer=getCustomer(request);
        String lang=customer.getLang();
        List<Integer> id=new ArrayList<Integer>();

        for(Object ids:jsonArray){
            if(ids!=null&&ids.toString().length()>0){
                id.add(Integer.parseInt(ids.toString()));
            }
        }
        if(id.size()>0){
            int status = fWordcardMapper.deleteBatchIds(id);
            if(status!=-1){
                return JsonConfig.getJsonObj(CODE_OK,null,lang);
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,null,lang);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null,lang);
        }
    }
    @RequestMapping(value = "userApi/fwordcard/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addFwordcard(HttpServletRequest request, @RequestBody JSONObject json) {
        println(json.toString());
        Customer customer = getCustomer(request);
        String lang=customer.getLang();
        FWordcard_Sql fWordcardSql=new FWordcard_Sql();
        FWordcard  fWordcard=new Gson().fromJson(json.toString(),new TypeToken<FWordcard>(){}.getType());
        fWordcard.setUser_key(customer.getUserkey());
        fWordcard.setProject_key(customer.getProject_key());
        fWordcard.setCreate_time(System.currentTimeMillis()/1000);
        fWordcard.setCustomer_key(customer.getCustomerkey());
        fWordcard.setUser_key(customer.getUserkey());
        println("工卡="+fWordcard);
        boolean status=fWordcardSql.addWordcard(fWordcardMapper,fWordcard);
        if(status){
            return JsonConfig.getJsonObj(CODE_OK,null,lang);
        }
        else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null,lang);
        }
    }



    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   println("customer="+customer);
        return customer;
    }
}
