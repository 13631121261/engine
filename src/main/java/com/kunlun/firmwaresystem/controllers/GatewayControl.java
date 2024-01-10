package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kunlun.firmwaresystem.device.*;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.device.DeviceModel;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;
import com.kunlun.firmwaresystem.mqtt.DirectExchangeProducer;
import com.kunlun.firmwaresystem.sql.Gateway_sql;
import com.kunlun.firmwaresystem.timeOut.ConnectTimeOut;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import com.kunlun.firmwaresystem.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.gatewayMapper;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.go_to_connect;
import static com.kunlun.firmwaresystem.util.JsonConfig.CODE_SEND_NoGateway;
import static com.kunlun.firmwaresystem.util.constant.getMsgId;

@RestController
public class GatewayControl {
    private static int ExpireTime = 60;   // redis中存储的过期时间60s
    @Autowired
    private DirectExchangeProducer directExchangeProducer;
    @Resource
    private RedisUtils redisUtil;

    @RequestMapping(value = "/GatewayControl/getVersion", method = RequestMethod.GET)
    @ResponseBody
    public String getVersion(@ParamsNotNull @RequestParam(value = "address") String address, @ParamsNotNull @RequestParam(value = "pubTopic") String pubTopic) {
        String response = "默认参数";
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time = sdf.format(date);
         /*   MyMqttClient myMqttClient=MyMqttClient.getMyMqttClient();
            myMqttClient.sendToTopic(pubTopic, "{\"pkt_type\":\"command\",\"gw_addr\":\"ffffffffffff\",\"data\":{\"msgId\":3,\"cmd\":\"scan_request_onoff\",\"enable\":true}}",111);
         */  // myMqttClient.sendToTopic(pubTopic, "{\"pkt_type\":\"command\",\"gw_addr\":\"ffffffffffff\",\"data\":{\"msgId\":3,\"cmd\":\"sys_get_ver\"}}",111);
        GatewayCmd gatewayCmd = new GatewayCmd();
        String json = gatewayCmd.getVersion(address);
        RabbitMessage rabbitMessage = new RabbitMessage(pubTopic, json);
        directExchangeProducer.send(rabbitMessage.toString(), go_to_connect);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("线程异常");
        }
        boolean status = (boolean) redisUtil.getAndClear(redis_key_sendToGateway);
        response = getResponse(status);
        return response;
    }

    @RequestMapping(value = "/GatewayControl/connectDevice", method = RequestMethod.GET)
    public String connectDevice(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "gaddress") String gaddress, @ParamsNotNull @RequestParam(value = "daddress") String daddress, @ParamsNotNull @RequestParam(value = "uuid") String uuid, @ParamsNotNull @RequestParam(value = "pubTopic") String pubTopic) {
        String response = "默认参数";
        DeviceModel deviceModel = null;
        String devices[] = daddress.split("#");
        String notifyUuid[] = uuid.split("#");
        Customer user1 = getCustomer(request);
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time = sdf.format(date);
        Gateway gateway = new Gateway(gaddress);
        List<DeviceModel> dd = (List<DeviceModel>) redisUtil.get("ModelList");
        for (DeviceModel d : dd) {
            if (d.getCustomer_key().equals(user1.getCustomerkey())) {
                deviceModel = d;
                break;
            }
        }
        int msgId = getMsgId();
        GatewayCmd gatewayCmd = new GatewayCmd();
        String connectCmd = gatewayCmd.getConnectCmd(gaddress, deviceModel, devices, 0, notifyUuid, 0, msgId);
        //  gateway.getConnectCmd(deviceModel,devices,0,notifyUuid,0,msgId);
        RabbitMessage rabbitMessage = new RabbitMessage(pubTopic, connectCmd);
        directExchangeProducer.send(rabbitMessage.toString(), go_to_connect);
        //把下发连接的状态存进缓存
        for (String address : devices) {
            Gateway_device gateway_device = getGateway(address, gaddress);
            if (gateway_device != null) {
             //   System.out.println("不为空，开始计时" + gateway_device.getSubTopic());
                Device device = new Device(address, state_gotoConnect, 3000, gateway_device);
                SystemUtil.getUtil().addConnectDevice(device, new ConnectTimeOut());
                System.out.println("不为空，开始计时1111");
            } else {
                System.out.println("此设备" + address + "没有被扫描到过");
                return JsonConfig.getJson(CODE_SEND_NoGateway, null);
            }

        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("线程异常");
        }
        boolean status = (boolean) redisUtil.getAndClear(redis_key_sendToGateway + msgId);
        response = getResponse(status);
        return response;
    }


    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   System.out.println("customer="+customer);
        return customer;
    }


    private String getResponse(boolean status) {
        String response = "";
        if (status) {
            response = JsonConfig.getJson(JsonConfig.CODE_OK, null);
        } else {
            response = JsonConfig.getJson(JsonConfig.CODE_SEND_MQTT_ERROR, null);

        }
        return response;
    }

    private Gateway_device getGateway(String device_address, String gAddress) {

        String json = (String) redisUtil.get(redis_key_device_gateways + device_address);
        if (json == null) {
            return null;
        }
        Gateway_devices gateway_devices = new Gson().fromJson(json, Gateway_devices.class);
        ArrayList<Gateway_device> gateways = gateway_devices.getGatewayDevices();
        if (gateways == null) {
            return null;
        }

        Gateway_device gateway_device = gateways.get(0);
        for (int i = 1; i < gateways.size(); i++) {
            if (gateways.get(i).getRssi() > gateway_device.getRssi()) {
                gateway_device = gateways.get(i);
            }
            if (gAddress != null) {
                if (gateways.get(i).getAddress().equals(gAddress)) {
                    return gateways.get(i);
                }
            }
        }
        return gateway_device;
    }

    @RequestMapping(value = "userApi/getGatewayByMap", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getGatewaybyMap(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "map_key") String map_key) {
        // System.out.println(System.currentTimeMillis());
        Customer customer = getCustomer(request);
        Gateway_sql gateway_sql=new Gateway_sql();

        try{
        List<Gateway> gatewayList=gateway_sql.getGatewayByMapKey(gatewayMapper,customer.getUserkey(),customer.getProject_key(),map_key);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", gatewayList.size());
        jsonObject.put("data", gatewayList);
         // System.out.println(System.currentTimeMillis());
        return jsonObject;}catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
