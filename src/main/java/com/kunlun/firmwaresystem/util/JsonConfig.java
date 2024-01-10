package com.kunlun.firmwaresystem.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;


import java.util.HashMap;

public class JsonConfig {

    //请求成功
    public static final int CODE_ReLogin = 302;
    //请求成功
    public static final int CODE_OK = 1;
    //数据格式不对
    public static final int CODE_DR = -11;
    //需要解绑
    public static final int CODE_10 = -10;
    //非底层部门，不能删除
    public static final int CODE_12 = -12;
    /**
     * 请求成功,需要解绑设备再操作
     */
    public static final String CODE_10_txt = "需要解绑设备或者解除相关人员再操作";
    public static final String CODE_12_txt = "不能删除上层部门";
    //无权限操作
    public static final int CODE_noP = -9;
    //用户名不能修改
    public static final int CODE_noC = -91;
    //用户名不能修改
    public static final String CODE_noC_txt= "用户名不能修改";
    //参数为空
    public static final int CODE_PARAMETER_NULL = -99;
    //服务器数据库查询异常
    public static final int CODE_SQL_ERROR = -1;
    //参数不合法，一般为超出长度，类型转换不正确
    public static final int CODE_PARAMETER_TYPE_ERROR = -2;
    /**
     * 请求成功,但是查询结果为空
     */
    public static final int CODE_RESPONSE_NULL = -3;
    /**
     * 请求成功,但是查询结果不符合预期，比如只要一个的结果，但是查到了两个
     */
    public static final int CODE_RESPONSE_MORE = -4;
    /**
     * 请求成功,但是数据有重复，不允许创建
     */
    public static final int CODE_REPEAT = -5;

    /**
     * 用户离线，需要重新登录
     */
    public static final int CODE_OFFLINE = -6;

    /**
     * 执行下发指令异常，主要是下发MQTT没有返回
     */
    public static final int CODE_SEND_MQTT_ERROR = -7;
    /**
     * 执行下发指令异常，没有找到匹配的网关
     */
    public static final int CODE_SEND_NoGateway = -8;


    /**
     * 请求成功
     */
    public static final String CODE_OK_txt = "执行成功";
    /**
     * 数据不符合预期
     */
    public static final String CODE_DR_txt = "数据格式不对，请重新输入";
    /**
     * 需要解绑设备
     */
    public static final String CODE_UNBIND = "有管理账号正在使用相应角色，请先解除关联的管理账号";

    /**
     * 无权限操作
     */
    public static final String CODE_noP_txt = "无权限操作：增加、修改、删除";
    //参数为空
    public static final String CODE_PARAMETER_NULL_txt = "传递参数不能为空";
    //服务器数据库查询异常
    public static final String CODE_SQL_ERROR_txt = "服务器异常，请联系管理员";
    //参数不合法，一般为超出长度，类型转换不正确
    public static final String CODE_PARAMETER_TYPE_ERROR_txt = "参数不合法，请重新提交参数";
    ///请求成功,但是查询结果为空
    public static final String CODE_RESPONSE_NULL_txt = "查询不到结果，请重新输入参数";

    //请求成功,但是查询结果不符合预期，比如只要一个的结果，但是查到了两个
    public static final String CODE_RESPONSE_MORE_txt = "结果不唯一，请重新输入参数";
    /**
     * 请求成功,但是数据有重复，不允许创建
     */
    public static final String CODE_REPEAT_txt = "数据重复，不能创建";
    /**
     * 用户离线，需要重新登录
     */
    public static final String CODE_OFFLINE_txt = "需要重新登录";
    /**
     * 执行下发指令异常，主要是下发MQTT没有返回
     */
    public static final String CODE_SEND_MQTT_ERROR_txt = "下发指令失败，请联系管理员";


    /**
     * 执行下发指令异常，没有找到匹配的网关
     */
    public static final String CODE_SEND_NoGateway_txt = "没有找到匹配的网关";

    public static JSONObject getJsonToken(int code, Object object, String token) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("token", token);
        jsonObject.put("msg", "登录成功！");
        JSONObject jsonObject1=new JSONObject();
        String  d="\\";
        String b="/admin";
        String a=d+b;
        jsonObject1.put("routePath",a);
        System.out.println(a);
        jsonObject1.put("userInfo",object);
        jsonObject.put("data",jsonObject1);
        return jsonObject;
    }

    public static String getJson(int code, Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        switch (code) {
            case CODE_noP:
                jsonObject.put("msg", CODE_noP_txt);
                break;
            case CODE_OK:
                jsonObject.put("msg", CODE_OK_txt);
                break;
            case CODE_PARAMETER_NULL:
                jsonObject.put("msg", CODE_PARAMETER_NULL_txt);

                break;
            case CODE_SQL_ERROR:
                jsonObject.put("msg", CODE_SQL_ERROR_txt);
                break;
            case CODE_PARAMETER_TYPE_ERROR:
                jsonObject.put("msg", CODE_PARAMETER_TYPE_ERROR_txt);
                break;
            case CODE_RESPONSE_NULL:
                jsonObject.put("msg", CODE_RESPONSE_NULL_txt);
                break;
            case CODE_RESPONSE_MORE:
                jsonObject.put("msg", CODE_RESPONSE_MORE_txt);
                break;
            case CODE_REPEAT:
                jsonObject.put("msg", CODE_REPEAT_txt);
                break;
            case CODE_OFFLINE:
                jsonObject.put("msg", CODE_OFFLINE_txt);
                break;
            case CODE_SEND_MQTT_ERROR:
                jsonObject.put("msg", CODE_SEND_MQTT_ERROR_txt);
                break;
            case CODE_SEND_NoGateway:
                jsonObject.put("msg", CODE_SEND_NoGateway_txt);
                break;
            case CODE_10:
                jsonObject.put("msg", CODE_10_txt);
                break;
            case CODE_12:
                jsonObject.put("msg", CODE_12_txt);
                break;
            case CODE_DR:
                jsonObject.put("msg", CODE_DR_txt);
                break;
            case CODE_ReLogin:
                jsonObject.put("msg", "token过期或者不合法，重新登录");
                break;


        }
        if (object != null) {
            getJsonData(jsonObject, object);
        }
        return jsonObject.toString();
    }
    public static JSONObject getJsonObj(int code, Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        switch (code) {
            case CODE_noP:
                jsonObject.put("msg", CODE_noP_txt);
                break;
            case CODE_noC:
                jsonObject.put("msg", CODE_noC_txt);
                break;
            case CODE_OK:
                jsonObject.put("msg", CODE_OK_txt);
                break;
            case CODE_PARAMETER_NULL:
                jsonObject.put("msg", CODE_PARAMETER_NULL_txt);

                break;
            case CODE_SQL_ERROR:
                jsonObject.put("msg", CODE_SQL_ERROR_txt);
                break;
            case CODE_PARAMETER_TYPE_ERROR:
                jsonObject.put("msg", CODE_PARAMETER_TYPE_ERROR_txt);
                break;
            case CODE_RESPONSE_NULL:
                jsonObject.put("msg", CODE_RESPONSE_NULL_txt);
                break;
            case CODE_RESPONSE_MORE:
                jsonObject.put("msg", CODE_RESPONSE_MORE_txt);
                break;
            case CODE_REPEAT:
                jsonObject.put("msg", CODE_REPEAT_txt);
                break;
            case CODE_OFFLINE:
                jsonObject.put("msg", CODE_OFFLINE_txt);
                break;
            case CODE_SEND_MQTT_ERROR:
                jsonObject.put("msg", CODE_SEND_MQTT_ERROR_txt);
                break;
            case CODE_SEND_NoGateway:
                jsonObject.put("msg", CODE_SEND_NoGateway_txt);
                break;
            case CODE_10:
                jsonObject.put("msg", CODE_10_txt);
                break;
            case CODE_12:
                jsonObject.put("msg", CODE_12_txt);
                break;
            case CODE_DR:
                jsonObject.put("msg", CODE_DR_txt);
                break;
            case CODE_ReLogin:
                jsonObject.put("msg", "token过期或者不合法，重新登录");
                break;
        }
        if (object != null) {
            getJsonData(jsonObject, object);
        }
        return jsonObject;
    }

    public static String getJson1(int code, Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        switch (code) {
            case CODE_noP:
                jsonObject.put("msg", CODE_noP_txt);
                break;
            case CODE_OK:
                jsonObject.put("msg", CODE_OK_txt);
                break;
            case CODE_PARAMETER_NULL:
                jsonObject.put("msg", CODE_PARAMETER_NULL_txt);

                break;
            case CODE_SQL_ERROR:
                jsonObject.put("msg", CODE_SQL_ERROR_txt);
                break;
            case CODE_PARAMETER_TYPE_ERROR:
                jsonObject.put("msg", CODE_PARAMETER_TYPE_ERROR_txt);
                break;
            case CODE_RESPONSE_NULL:
                jsonObject.put("msg", CODE_RESPONSE_NULL_txt);
                break;
            case CODE_RESPONSE_MORE:
                jsonObject.put("msg", CODE_RESPONSE_MORE_txt);
                break;
            case CODE_REPEAT:
                jsonObject.put("msg", CODE_REPEAT_txt);
                break;
            case CODE_OFFLINE:
                jsonObject.put("msg", CODE_OFFLINE_txt);
                break;
            case CODE_SEND_MQTT_ERROR:
                jsonObject.put("msg", CODE_SEND_MQTT_ERROR_txt);
                break;
            case CODE_SEND_NoGateway:
                jsonObject.put("msg", CODE_SEND_NoGateway_txt);
                break;
            case CODE_10:
                jsonObject.put("msg", CODE_10_txt);
                break;
            case CODE_12:
                jsonObject.put("msg", CODE_12_txt);
                break;
            case CODE_DR:
                jsonObject.put("msg", CODE_DR_txt);
                break;
            case CODE_ReLogin:
                jsonObject.put("msg", "token过期或者不合法，重新登录");
                break;


        }
        if (object != null) {
            getJsonData(jsonObject, object);
        }
        return jsonObject.toString();
    }


    private static void getJsonData(JSONObject jsonObject, Object object) {
        String className = object.getClass().getName();
        if (className.contains("map")) {
            jsonObject.put("total", ((HashMap) object).size());
            jsonObject.put("data", object);
        } else if (className.contains("list")) {
            jsonObject.put("total", ((HashMap) object).size());
            jsonObject.put("data", object);
        } else {
            jsonObject.put("data", object);
        }
    }

}
