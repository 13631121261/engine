package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.PageWifiVersion;

import com.kunlun.firmwaresystem.entity.Ble_firmware;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.Wifi_firmware;
import com.kunlun.firmwaresystem.mappers.WifiMapper;

import com.kunlun.firmwaresystem.sql.Ble;
import com.kunlun.firmwaresystem.sql.Wifi;
import com.kunlun.firmwaresystem.util.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class WifiFirmwareControl {
    @Autowired
    private WifiMapper wifiMapper;

    @RequestMapping(value = "userApi/WifiFirmware/index1", method = RequestMethod.GET)
    public JSONObject getWifiFirmware1(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        Wifi wifi_sql=new Wifi();
        List<Wifi_firmware> list= wifi_sql.getVersionByKey(wifiMapper,customer.getProject_key());
        Wifi_firmware wifiFirmware=new Wifi_firmware();
        switch (customer.getLang()){
            case "en":
                wifiFirmware.setVersion("No need to upgrade");
                break;
            default:
                wifiFirmware.setVersion("不需要升级");
                break;
        }

        list.add(0,wifiFirmware);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", list.size());
        jsonObject.put("data",list);
        return jsonObject;
    }

    @RequestMapping(value = "userApi/WifiFirmware/index", method = RequestMethod.GET)
    public JSONObject getWifiFirmware(HttpServletRequest request) {

        Customer customer = getCustomer(request);
        Wifi wifi_sql=new Wifi();
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
          println("1111111"+quickSearch);
        PageWifiVersion pageWifi=wifi_sql.selectPageWifiVersion(wifiMapper,page,limit,quickSearch,customer.getProject_key());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageWifi.getTotal());
        jsonObject.put("data", pageWifi.getWifi_firmwares());
        return jsonObject;
    }
    @RequestMapping(value = "userApi/WifiFirmware/upload", method = RequestMethod.POST, produces = "application/json")
    public String upload(MultipartHttpServletRequest request){
        String file_key ="";
        Map<String, MultipartFile> map = request.getFileMap();
       // List<MultipartFile> files=new ArrayList<>();
        for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {
            if(entry.getKey().equals("wifi_firmware")){
                byte[] file_data = null;
                try {
                  //  entry.getValue().transferTo(new File(path.getPath() + "/" + type + "_" + version + ".firmware"));
                    file_data = entry.getValue().getBytes();

                    // println(svg_data);
                        file_key = Base64.getEncoder().encodeToString(("wifi_firmware" + "_" + System.currentTimeMillis()).getBytes()).replaceAll("\\+", "");
                    if(file_data.length>0){
                        file_cache.put(file_key,file_data);
                    }
                    //    redisUtil.set(map_key,svg_data);
                    return file_key;
                } catch (IOException ioException) {
                    System.err.println("File Error!");
                    return  "File Error!";
                }
            }else{
                return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null,"en").toString();
            }
            //    println(entry.getKey());
            //   println(entry.getValue().getSize());
          //  files.add(entry.getValue());
        }

        return  "";
    }

    @RequestMapping(value = "userApi/WifiFirmware/add", method = RequestMethod.POST)
    public JSONObject addWifiFirmware(HttpServletRequest request,@RequestBody JSONObject json) {
        String userDir = System.getProperty("user.dir");
        println("json="+json.toString());
        Customer customer = getCustomer(request);
        Wifi wifi_sql=new Wifi();
        Wifi_firmware wifiFirmware=new Gson().fromJson(json.toString(),new TypeToken<Wifi_firmware>(){}.getType());
        println("wifi="+wifiFirmware);
        String file_key=wifiFirmware.getProject_key();
        if(file_key==null|| file_key.isEmpty()){

            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,"",customer.getLang());
        }else{
            byte[] data=file_cache.get(file_key);
            if(data==null){


                return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,"",customer.getLang());
            }else{
               File file=new File(userDir+"/wififirmware/");
               if(!file.exists()){
                 file.mkdirs();
               }
                file=new File(userDir+"/wififirmware/"+wifiFirmware.getVersion()+".bin");
               try {
                   String path=file.getAbsolutePath();
                   file.createNewFile();
                   FileOutputStream out=new FileOutputStream(file);
                   out.write(data);
                   out.close();
                   data=null;
                   file_cache.remove(file_key);
                   wifiFirmware.setUrl(path);
                   wifiFirmware.setProject_key(customer.getProject_key());
                   wifiFirmware.setUploadtime(System.currentTimeMillis()/1000);
                   boolean status=wifi_sql.add(wifiMapper,wifiFirmware);
                   if(status){
                       return JsonConfig.getJsonObj(CODE_OK,"",customer.getLang());
                   }else{
                       return JsonConfig.getJsonObj(CODE_REPEAT,"",customer.getLang());
                   }
               }catch (Exception e){
                   return JsonConfig.getJsonObj(CODE_SQL_ERROR,"",customer.getLang());
               }
            }
        }
    }

    @RequestMapping(value = "userApi/WifiFirmware/del", method = RequestMethod.POST, produces = "application/json")
    public JSONObject deleteBWifiFirmware(HttpServletRequest request, @RequestBody JSONArray jsonArray) {
        Customer customer = getCustomer(request);
        String lang=customer.getLang();

        List<Integer> id=new ArrayList<Integer>();
        for(Object ids:jsonArray){
            if(ids!=null&& !ids.toString().isEmpty()){
                id.add(Integer.parseInt(ids.toString()));
            }
        }
        if(id.size()>0){
            int status =wifiMapper.deleteBatchIds(id);
            if(status!=-1){
                return JsonConfig.getJsonObj(CODE_OK,"",lang);
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,null,lang);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null,lang);
        }
    }

    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   println("customer="+customer);
        return customer;
    }
}
