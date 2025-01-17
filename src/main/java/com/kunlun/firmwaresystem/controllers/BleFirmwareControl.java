package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.PageBleVersion;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.gatewayJson.Constant;
import com.kunlun.firmwaresystem.mappers.BleMapper;
import com.kunlun.firmwaresystem.mappers.WifiMapper;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class BleFirmwareControl {
    @Autowired
    private WifiMapper wifiMapper;
    @Autowired
    private BleMapper bleMapper;
    @Autowired
    private ResourceLoader resourceLoader;
    /*  @RequestMapping(value = "/getFirmwareVersion",method = RequestMethod.GET)
      public String getFirmwareVersion(@RequestParam("type") String type,@RequestParam("mac") String mac,@RequestParam("wifi_version") String wifi_version,@RequestParam("ble_version") String ble_version) {
          println("Mac="+mac);
          println("type="+type);
          println("wifi_version="+wifi_version);
          println("ble_version="+ble_version);
          Wifi wifi=new Wifi();
          Wifi_firmware wifi_firmware= wifi.getVersionOnAuto(wifiMapper);
          Ble ble=new Ble();
          Ble_firmware ble_firmware= ble.getVersionOnAuto(bleMapper);
          JSONObject jsonObject=new JSONObject();
          jsonObject.put("code", constant.code_ok);
          jsonObject.put("type","getFirmwareResponse");
          if(wifi_firmware==null){
              jsonObject.put("wifi","null");
          }else {
              jsonObject.put("wifi", wifi_firmware);
          }
          if(ble_firmware==null){
              jsonObject.put("ble","null");
          }
          else {
              jsonObject.put("ble", ble_firmware);
          }
          println("json="+jsonObject.toString());
          return jsonObject.toString();
      }*/
    @RequestMapping(value = "userApi/BleFirmware/index1", method = RequestMethod.GET)
    public JSONObject getBleFirmware1(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        Ble ble_sql=new Ble();
       List<Ble_firmware> list= ble_sql.getVersionByKey(bleMapper,customer.getProject_key());
       Ble_firmware bleFirmware=new Ble_firmware();
       switch (customer.getLang()){
           case "en":
               bleFirmware.setVersion("No need to upgrade");
               break;
           default:
               bleFirmware.setVersion("不需要升级");
               break;
       }

        list.add(0,bleFirmware);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", list.size());
        jsonObject.put("data",list);
        return jsonObject;
    }

    @RequestMapping(value = "userApi/BleFirmware/index", method = RequestMethod.GET)
    public JSONObject getBleFirmware(HttpServletRequest request) {

        Customer customer = getCustomer(request);
        Ble ble_sql=new Ble();
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
        PageBleVersion pageBle=ble_sql.selectPageBleVersion(bleMapper,page,limit,quickSearch,customer.getProject_key());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageBle.getTotal());
        jsonObject.put("data", pageBle.getBle_firmwares());
        return jsonObject;
    }
    @RequestMapping(value = "userApi/BleFirmware/upload", method = RequestMethod.POST, produces = "application/json")
    public String upload(MultipartHttpServletRequest request){
        String file_key ="";
        java.util.Map<String, MultipartFile> map = request.getFileMap();
      //  List<MultipartFile> files=new ArrayList<>();
        for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {
            if(entry.getKey().equals("ble_firmware")){
                byte[] file_data = null;
                try {
                  //  entry.getValue().transferTo(new File(path.getPath() + "/" + type + "_" + version + ".firmware"));
                    file_data = entry.getValue().getBytes();

                    // println(svg_data);
                        file_key = Base64.getEncoder().encodeToString(("ble_firmware" + "_" + System.currentTimeMillis()).getBytes()).replaceAll("\\+", "");
                    if(file_data.length>0){
                        file_cache.put(file_key,file_data);
                    }
                    //    redisUtil.set(map_key,svg_data);
                    return file_key;
                } catch (IOException ioException) {
                    System.err.println("File Error!");
                    return  "";
                }
            }
            //    println(entry.getKey());
            //   println(entry.getValue().getSize());
         //   files.add(entry.getValue());
        }

        return  "";
    }

    @RequestMapping(value = "userApi/BleFirmware/add", method = RequestMethod.POST)
    public JSONObject addBleFirmware(HttpServletRequest request,@RequestBody JSONObject json) {
        String userDir = System.getProperty("user.dir");
        println("json="+json.toString());
        Customer customer = getCustomer(request);
        Ble ble_sql=new Ble();
        JSONObject jsonObject = new JSONObject();
        Ble_firmware bleFirmware=new Gson().fromJson(json.toString(),new TypeToken<Ble_firmware>(){}.getType());
        println("ble="+bleFirmware);
        String file_key=bleFirmware.getProject_key();
        if(file_key==null|| file_key.isEmpty()){
            jsonObject.put("code", CODE_PARAMETER_NULL);
            jsonObject.put("msg", CODE_PARAMETER_NULL_txt);
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,"",customer.getLang());
        }else{
            byte[] data=file_cache.get(file_key);
            if(data==null){
                jsonObject.put("code", CODE_PARAMETER_NULL);
                jsonObject.put("msg", CODE_PARAMETER_NULL_txt);
                return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,"",customer.getLang());
            }else{
               File file=new File(userDir+"/blefirmware/");
               if(!file.exists()){
                 file.mkdirs();
               }
                file=new File(userDir+"/blefirmware/"+bleFirmware.getVersion()+".bin");
               try {
                   String path=file.getAbsolutePath();
                   file.createNewFile();
                   FileOutputStream out=new FileOutputStream(file);
                   out.write(data);
                   out.close();

                   file_cache.remove(file_key);
                   bleFirmware.setUrl(path);
                   bleFirmware.setProject_key(customer.getProject_key());
                   bleFirmware.setUploadtime(System.currentTimeMillis()/1000);
                   boolean status=ble_sql.add(bleMapper,bleFirmware);
                   if(status){
                       return JsonConfig.getJsonObj(CODE_OK,"",customer.getLang());
                   }else{
                       return JsonConfig.getJsonObj(CODE_REPEAT,"",customer.getLang());
                   }
               }catch (Exception e){
                   println("异常保存蓝牙固件="+e.getMessage());
                   return JsonConfig.getJsonObj(CODE_SQL_ERROR,"",customer.getLang());
               }
            }
        }
    }

    @RequestMapping(value = "userApi/BleFirmware/del", method = RequestMethod.POST, produces = "application/json")
    public JSONObject deleteBleFirmware(HttpServletRequest request, @RequestBody JSONArray jsonArray) {
        Customer customer = getCustomer(request);
        String lang=customer.getLang();
        Ble ble_sql=new Ble();
        List<Integer> id=new ArrayList<Integer>();
        for(Object ids:jsonArray){
            if(ids!=null&& !ids.toString().isEmpty()){
                id.add(Integer.parseInt(ids.toString()));
            }
        }
        if(id.size()>0){
            int status =bleMapper.deleteBatchIds(id);
            if(status!=-1){
                return JsonConfig.getJsonObj(CODE_OK,"",lang);
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,null,lang);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null,lang);
        }
    }
    //  String version;
    //    int id;
    //    String remake;
    //    String uploadtime;
    //    int customization;
    //    String customization_name;
    //    String finishtime;
    //    String author;
    //    String administrator;
    //    int auto;
/*    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,@RequestParam("type") String type,@RequestParam("version") String version,@RequestParam("customization") int customization,
                                       @RequestParam("customization_name") String customization_name,@RequestParam("finishtime") String finishtime,@RequestParam("author") String author,@RequestParam("administrator") String administrator,
                                    @RequestParam("auto") int auto,@RequestParam("remake") String remake) throws IOException {
        InetAddress address = null;
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        try {
            address = InetAddress.getLocalHost();
            println("输出地址="+address.getHostAddress()+"文件路径="+path.getName());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        println("[文件类型] - [{}]"+ file.getContentType());
        println("[文件名称] - [{}]"+ file.getOriginalFilename());
        println("[文件大小] - [{}]"+ file.getSize());

        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

        println("当前时间为: " + ft.format(dNow));
        //保存
        JSONObject jsonObject=new JSONObject();
        if(file==null||type==null||version==null||finishtime==null||author==null||administrator==null){
            jsonObject.put("code",constant.code_error);
            jsonObject.put("msg","参数不完整");
            return jsonObject.toString();
        }else{
            file.transferTo(new File("C:\\Users\\Administrator\\Desktop\\Gateway\\Server_update\\firmware\\" + type+"_"+version+".firmware"));
            if(type.equals("ble")){
                Ble_firmware ble_firmware=new Ble_firmware( "http://"+"192.168.1.10"+":80/download",remake,   ft.format(dNow),  customization,  customization_name,  finishtime,  administrator,  "",  version);

                if(auto==1){
                    UpdateWrapper<Ble_firmware> bleFirmwareUpdateWrapper=new UpdateWrapper<>();
                    bleFirmwareUpdateWrapper.eq("auto",1).set("auto",0);
                    bleMapper.update(null,bleFirmwareUpdateWrapper);
                }
                int d=bleMapper.insert(ble_firmware);
                println("保存蓝牙固件数据的结果码="+d);
            }else if(type.equals("wifi")){
                Wifi_firmware wifi_firmware=new Wifi_firmware( "http://"+"47.241.68.29"+":8080/download",remake,   ft.format(dNow),  customization,  customization_name,  finishtime,  author,  administrator,  "",  version);
                if(auto==1){
                    UpdateWrapper<Wifi_firmware> wifiFirmwareUpdateWrapper=new UpdateWrapper<>();
                    wifiFirmwareUpdateWrapper.eq("auto",1).set("auto",0);
                    wifiMapper.update(null,wifiFirmwareUpdateWrapper);
                }
                int d=wifiMapper.insert(wifi_firmware);
                println("保存wifi数据的结果码="+d);
            }
            jsonObject.put("code",constant.code_ok);
            jsonObject.put("msg","上传成功");
        }

        return jsonObject.toString();
    }

   */

    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   println("customer="+customer);
        return customer;
    }
}
