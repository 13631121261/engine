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
public class BeaconControl {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private Gateway_configMapper gatewayConfigMapper;
    @Resource
    private GatewayMapper gatewayMapper;

    @RequestMapping(value = "userApi/beacon/index", method = RequestMethod.GET, produces = "application/json")
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
        PageBeacon pageBeacon=beacon_sql.selectPageBeacon(beaconMapper,page,limit,quickSearch,customer.getUserkey(),customer.getProject_key());
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

    @RequestMapping(value = "userApi/beacon/index1", method = RequestMethod.GET, produces = "application/json")
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



    @RequestMapping(value = "userApi/beacon/del", method = RequestMethod.POST, produces = "application/json")
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
    @RequestMapping(value = "userApi/beacon/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addBeacon(HttpServletRequest request, @RequestBody JSONObject json) {
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
            case 1:
                beacon.setRun(-1);
                beacon.setSos(-1);
                break;
             case 2:
                 beacon.setRun(-1);
                 beacon.setSos(0);
                    break;
            case 3:
                beacon.setRun(0);
                beacon.setSos(-1);
                break;
            case 4:
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
      @RequestMapping(value = "/userApi/uploadBeacon", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public File uploadBeacon(HttpServletRequest request, @RequestParam("data") MultipartFile file) throws IOException {
          System.out.println("ssssssssssss"+file.getName());
        String response;
        InetAddress address = null;
        Customer customer = getCustomer(request);
        File outfile=new File(file.getName()+".xlsx");
        outfile.createNewFile();
          System.out.println(""+file.getName());
       if(file==null){
           HashMap<String,String> error=new HashMap<>();
           error.put("result","失败，上传文件为空");
           ArrayList<  HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
           list.add(error);
           SystemUtil.writeExcel(outfile,new String[]{"结果"},list);
         return outfile;
       }
        ArrayList<HashMap<String, String>> data = SystemUtil.readExcel(file, new String[]{"mac","type"});
        if (data == null) {
            HashMap<String,String> error=new HashMap<String,String>();
            error.put("result","失败，表格内容不能为空");
            ArrayList<  HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
            list.add(error);
            SystemUtil.writeExcel(outfile,new String[]{"结果"},list);
            return outfile;
        } else {
            int ok = 0;
            int fail = 0;
            Beacon_Sql beacon_sql = new Beacon_Sql();
            for (Map<String, String> map : data) {
                System.out.println("循环");
                Beacon beacon = new Beacon();
                String types=map.get("type");
                int type=0;
                switch (types){
                    case "KTBB818":
                        type=1;
                        break;
                    case "KTBB818-K":
                        type=2;
                        break;
                    case "KTBB818-A":
                        type=3;
                        break;
                    case "KTBB818-KA":
                        type=4;
                        break;
                        default:
                         map.put("result","失败，信标类型不符合");
                        continue;
                }
                beacon.setType(type) ;
                beacon.setProject_key(customer.getProject_key());
                beacon.setUser_key(customer.getUserkey());
                beacon.setMac(map.get("mac"));
                boolean status = beacon_sql.addBeacon(beaconMapper, beacon);
                if (status) {
                    map.put("result","添加成功");
                } else {
                    map.put("result","添加失败，数据重复或者其他异常，请联系管理员");
                }
            }
            SystemUtil.writeExcel(outfile,new String[]{"结果"},data);
            return outfile;
        }

    }



    @RequestMapping(value = "userApi/getTagByMap", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getGatewaybyMap(HttpServletRequest request, @ParamsNotNull @RequestParam(value = "map_key") String map_key) {
        // System.out.println(System.currentTimeMillis());
        Customer customer = getCustomer(request);
        DeviceP_Sql deviceP_sql=new DeviceP_Sql();
        Map<String,Devicep> deviceps=deviceP_sql.getAllDeviceP(devicePMapper,customer.getUserkey(),customer.getProject_key());
        ArrayList<Devicep> deviceps1=new ArrayList<>();
        for(String key:deviceps.keySet()){
            Devicep devicep=deviceps.get(key);
           String mac= devicep.getBind_mac();
           if(mac!=null&&mac.length()>0){
            Beacon beacon=beaconsMap.get(mac);
            if(beacon.getOnline()==1){
                devicep.setX(beacon.getX());
                devicep.setY(beacon.getY());
                devicep.setGateway_mac(beacon.getGateway_address());
                devicep.setSos(beacon.getSos());
                devicep.setBt(beacon.getBt());
                deviceps1.add(devicep);
            }
           }
        }


        try{

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "ok");
            jsonObject.put("count", deviceps1.size());
            jsonObject.put("data", deviceps1);
            // System.out.println(System.currentTimeMillis());
            return jsonObject;}catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   System.out.println("customer="+customer);
        return customer;
    }


}
