package com.kunlun.firmwaresystem.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunlun.firmwaresystem.device.PageBeacon;
import com.kunlun.firmwaresystem.device.PageWordcarda;
import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.entity.Customer;
import com.kunlun.firmwaresystem.entity.Wordcard_a;
import com.kunlun.firmwaresystem.interceptor.ParamsNotNull;
import com.kunlun.firmwaresystem.mappers.GatewayMapper;
import com.kunlun.firmwaresystem.mappers.Gateway_configMapper;
import com.kunlun.firmwaresystem.sql.Beacon_Sql;
import com.kunlun.firmwaresystem.sql.WordCarda_Sql;
import com.kunlun.firmwaresystem.util.JsonConfig;
import com.kunlun.firmwaresystem.util.RedisUtils;
import com.kunlun.firmwaresystem.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.util.JsonConfig.*;

@RestController
public class WorkCardControl {
    private static final Logger log = LoggerFactory.getLogger(WorkCardControl.class);
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private Gateway_configMapper gatewayConfigMapper;
    @Resource
    private GatewayMapper gatewayMapper;

    @RequestMapping(value = "userApi/workcard/index", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getAll(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        WordCarda_Sql wordCardaSql=new WordCarda_Sql();
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
      //  println("1111111");
      PageWordcarda pageWordcarda= wordCardaSql.selectPageWordcard(wordCardaMapper,page,limit,customer.getProject_key(),quickSearch);
       if(!pageWordcarda.getWordcard_as().isEmpty()){
            for(Wordcard_a wordcardA:pageWordcarda.getWordcard_as()){
                Wordcard_a wordcardA1=wordcard_aMap.get(wordcardA.getMac());
                wordcardA.setMap_key(wordcardA1.getMap_key());
                wordcardA.setOnline(wordcardA1.getOnline());
                wordcardA.setLastTime(wordcardA1.getLastTime());
                wordcardA.setSos(wordcardA1.getSos());
                wordcardA.setRun(wordcardA1.getRun());
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", pageWordcarda.getTotal());
        jsonObject.put("data", pageWordcarda.getWordcard_as());
         return jsonObject;
    }
/*
    @RequestMapping(value = "userApi/workcard/getByMac", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getBeaconByMac(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        Beacon_Sql beacon_sql=new Beacon_Sql();
        String quickSearch=request.getParameter("mac");
        if (StringUtils.isBlank(quickSearch)) {
            quickSearch="";
        }
        List<Beacon> list=beacon_sql.getAllBeaconbyMac(beaconMapper,customer.getUserkey(),customer.getProject_key(),quickSearch);
        if(!list.isEmpty()){
            for(Beacon beacon:list){
                Beacon beacon1=beaconsMap.get(beacon.getMac());
                beacon.setMap_key(beacon1.getMap_key());
                beacon.setOnline(beacon1.getOnline());
                if(beacon1.getOnline()==0){
                    beacon.setSos(-1);
                    beacon.setRun(-1);
                    beacon.setBt(""+0);
                }else{
                    beacon.setSos(beacon1.getSos());
                    beacon.setRun(beacon1.getRun());
                    beacon.setBt(beacon1.getBt());
                }

                beacon.setLastTime(beacon1.getLastTime());

            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count",list.size());
        jsonObject.put("data", list);
        return jsonObject;
    }*/
   /* @RequestMapping(value = "userApi/workcard/index1", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getAllBeacon1(HttpServletRequest request) {
        Customer customer = getCustomer(request);
        String lang=customer.getLang();
        String type=request.getParameter("type");
        Beacon_Sql beacon_sql=new Beacon_Sql();
        println("类型="+type);
        List<Beacon> beaconList=beacon_sql.getunAllBeacon(beaconMapper,customer.getUserkey(),customer.getProject_key(),type);
        JSONObject jsonObject = new JSONObject();
        if(lang!=null&&lang.equals("en")){
            beaconList.add(0,new Beacon("UnBind"));
        }
        else{
            beaconList.add(0,new Beacon("不绑定标签"));
        }

        jsonObject.put("code", 1);
        jsonObject.put("msg", "ok");
        jsonObject.put("count", beaconList.size());
        jsonObject.put("data",beaconList);
        return jsonObject;
    }
*/


    @RequestMapping(value = "userApi/workcard/del", method = RequestMethod.POST, produces = "application/json")
    public JSONObject deleteWorkcard(HttpServletRequest request, @RequestBody JSONArray jsonArray) {
        Customer customer = getCustomer(request);
        String lang=customer.getLang();
        WordCarda_Sql wordCardaSql=new WordCarda_Sql();
        List<Integer> id=new ArrayList<Integer>();
        for(Object ids:jsonArray){
            if(ids!=null&&ids.toString().length()>0){
                id.add(Integer.parseInt(ids.toString()));
            }
        }
        if(id.size()>0){
            int status = wordCardaMapper.deleteBatchIds( id);
            wordcard_aMap=wordCardaSql.getAllWordCarda(wordCardaMapper);

            if(status!=-1){
                return JsonConfig.getJsonObj(CODE_OK,"",lang);
            }else{
                return JsonConfig.getJsonObj(CODE_SQL_ERROR,null,lang);
            }
        }else{
            return JsonConfig.getJsonObj(CODE_PARAMETER_NULL,null,lang);
        }
    }
    @RequestMapping(value = "userApi/workcard/add", method = RequestMethod.POST, produces = "application/json")
    public JSONObject addWorkcard(HttpServletRequest request, @RequestBody JSONObject json) {
        println(json.toString());

       try
       {
           Customer customer = getCustomer(request);

        String lang=customer.getLang();
        WordCarda_Sql wordCardaSql=new WordCarda_Sql();
        Wordcard_a wordcardA=new Gson().fromJson(json.toString(),new TypeToken<Wordcard_a>(){}.getType());
        wordcardA.setUser_key(customer.getUserkey());
           wordcardA.setN(2.67);
        wordcardA.setProject_key(customer.getProject_key());
        wordcardA.setCreate_time(System.currentTimeMillis()/1000);
        wordcardA.setCustomer_key(customer.getCustomerkey());
        if(wordcardA.getMac()!=null){
            wordcardA.setMac(wordcardA.getMac().replaceAll(" ","").toLowerCase());
        }

        boolean status=wordCardaSql.addWordCarda(wordCardaMapper,wordcardA);
        if(status){
            wordcard_aMap.put(wordcardA.getMac(),wordcardA);
            return JsonConfig.getJsonObj(CODE_OK,null,lang);
        }
          else{
            return JsonConfig.getJsonObj(CODE_REPEAT,null,lang);
            }
       }catch (Exception e){
           println(e.getMessage());
           return null;
       }
    }
    /* @RequestMapping(value = "/userApi/uploadBeacon", method = RequestMethod.POST)
    public JSONObject uploadBeacon(HttpServletRequest request, @RequestParam("data") MultipartFile file, HttpServletResponse response)
             throws IOException {
         println("ssssssssssss" + file.getName());
         Customer customer = getCustomer(request);
         File outfile = new File(file.getName()+System.currentTimeMillis() + ".xlsx");
         outfile.createNewFile();
         println("666" + file.getName());
         if (file == null) {
          return  JsonConfig.getJsonObj(CODE_13,null,customer.getLang());
         }
         ArrayList<HashMap<String, String>> data = null;
         try {
             data = SystemUtil.readExcel(file, new String[]{"mac", "type"});
         } catch (Exception e) {
             println("特别特别" + e);
         }
         if (data == null) {
                 return  JsonConfig.getJsonObj(CODE_PARAMETER_TYPE_ERROR,null,customer.getLang());
         } else {
             Beacon_Sql beacon_sql = new Beacon_Sql();
             for (Map<String, String> map : data) {
                 println("循环");
                 Beacon beacon = new Beacon();
                 String types = map.get("type");
                 String mac=map.get("mac");
                 if(types==null||types.equals("")||mac==null||mac.equals("")){
                     map.put("result", "Failed, incomplete data");
                     continue;
                 }

                 beacon.setType(types);
                 beacon.setProject_key(customer.getProject_key());
                 beacon.setUser_key(customer.getUserkey());
                 beacon.setMac(map.get("mac"));
                 boolean status = beacon_sql.addBeacon(beaconMapper, beacon);
                 if (status) {
                     beaconsMap.put(beacon.getMac(),beacon);
                     map.put("result", "Import was successful");
                 } else {
                     map.put("result", "Import failed, data duplication or other anomalies. Please contact the administrator");
                 }
             }
             SystemUtil.writeExcel(outfile, new String[]{"mac","type","result"}, data);
             String file_name=outfile.getAbsolutePath();
             println("文件保存地址="+file_name);
             redisUtil.set("file_name",file_name);
             return JsonConfig.getJsonObj(CODE_OK,file_name,customer.getLang());

         }
     }*/

    /*@RequestMapping(value = "/userApi/downResult", method = RequestMethod.GET)
    @ResponseBody
    public void checkRecord(HttpServletResponse response, @ParamsNotNull @RequestParam(value = "file_name") String file_name) throws UnsupportedEncodingException {



        file_name=file_name.replaceAll("5678","//").replaceAll("8765",":");
        //String filePath = "E:\\蓝牙网关\\固件版本" ;
        File file = new File(file_name);
        if (file.exists()) { //判断文件父目录是否存在q
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=result.xls" );
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;
            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            println("----------file download---" + file.getPath());
            try {
                bis.close();
                fis.close();
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
                println("删除文件异常");
            }
        }
    }*/
    private Customer getCustomer(HttpServletRequest request) {
        String  token=request.getHeader("batoken");
        Customer customer = (Customer) redisUtil.get(token);
        //   println("customer="+customer);
        return customer;
    }


}
