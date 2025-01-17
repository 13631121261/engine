package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kunlun.firmwaresystem.device.PageAlarm;
import com.kunlun.firmwaresystem.device.PageTagLog;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.mappers.AlarmMapper;
import com.kunlun.firmwaresystem.mappers.TagLogMapper;
import com.kunlun.firmwaresystem.sql.Alarm_Sql;
import com.kunlun.firmwaresystem.sql.TagLogSql;
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

import static com.kunlun.firmwaresystem.NewSystemApplication.println;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class TaglogControl {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private TagLogMapper tagLogMapper;



    @RequestMapping(value = "userApi/Taglog/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getAllMap(HttpServletRequest request) {
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
        TagLogSql tagLogSql = new TagLogSql();
        PageTagLog pageTagLog = tagLogSql.selectPageLog(tagLogMapper,Integer.parseInt(page),Integer.parseInt(limit),quickSearch, user1.getProject_key());
        println("输出不了");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageTagLog.getTotal());
        jsonObject.put("data",  pageTagLog.getTagLogs());
        return jsonObject;
    }

    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   println("customer="+customer);
        return customer;
    }
}
