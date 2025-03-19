package com.kunlun.firmwaresystem.util;


import com.alibaba.fastjson.JSONObject;
import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report_data_info;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;
import com.kunlun.firmwaresystem.sql.Gateway_sql;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayStatusTask.writeLog;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.Push;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.sendtoMap;

public class StringUtil {

    private static int id = 0;
    private static final char HexCharArr[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final String HexStr = "0123456789ABCDEF";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //int 转字节数组
    public static byte[] intTo4ByteArray(int i) {
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }
    //long 转字节数组
    public static byte[] intTo4ByteArray(long i) {
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }
    public static String byteArrToHex(byte[] btArr) {
        char strArr[] = new char[btArr.length * 2];
        int i = 0;
        for (byte bt : btArr) {
            strArr[i++] = HexCharArr[bt >>> 4 & 0xf];
            strArr[i++] = HexCharArr[bt & 0xf];
        }
        return new String(strArr);
    }

    public static byte[] hexToByteArr(String hexStr) {
        hexStr = hexStr.replaceAll(" ", "");
        char[] charArr = hexStr.toCharArray();
        byte btArr[] = new byte[charArr.length / 2];
        int index = 0;
        for (int i = 0; i < charArr.length; i++) {
            int highBit = HexStr.indexOf(charArr[i]);
            int lowBit = HexStr.indexOf(charArr[++i]);
            btArr[index] = (byte) (highBit << 4 | lowBit);
            index++;
        }
        return btArr;
    }
    //int 转字节数组
    public static long ByteToLong(byte[] data) {
        long a= (data[0]&0xFF)*16777216;
        long b=(data[1]&0xff)*65536;
        long c=(data[2]&0xff)*256;
        long d=(data[3]&0xff);
        long e=a+b+c+d;
        return e*1000;
    }
    //int 转字节数组
    public static long eByteToLong(byte[] data) {
        long a= (data[0]&0xFF)*281474976710656l;
        long b=(data[1]&0xff)*1099511627776l;
        long c=(data[2]&0xff)*4294967296l;
        long d=(data[3]&0xff)*16777216;
        long e=(data[4]&0xff)*65536;
        long f=(data[5]&0xff)*256;
        long g=(data[6]&0xff);
        long h=a+b+c+d+e+f+g;
        return h;
    }
    //小端转大端
    public static String LtoB(byte[] l) {
        byte[] b = new byte[l.length];
        for (int i = 0; i < l.length; i++) {
            b[i] = l[l.length - i];
        }
        return byteArrToHex(b);
    }
    //字节数组转int
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < bytes.length; i++) {
            int shift = (bytes.length - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }


    //int 转字节数组
    public static byte[] intTo2ByteArray(int i) {
        byte[] result = new byte[2];
        // 由高位到低位
        result[0] = (byte) ((i >> 8) & 0xFF);
        result[1] = (byte) (i & 0xFF);
        return result;
    }

















  /* public static void  createExcelTwo( String path, Map<String, List<Integer>>map_list) {
       try {
        //创建Excel文件薄
        HSSFWorkbook workbook=new HSSFWorkbook();

        //创建工作表sheeet
        HSSFSheet sheet = workbook.createSheet();
        //创建第一行
       HSSFRow row = sheet.createRow(0);

       HSSFCell cell;
       int i=0;
        try {
            for (String key : map_list.keySet()) {
                cell = row.createCell(i);
                cell.setCellValue(key.split("==")[1]);
                i++;
            }
        }catch (Exception e){
            println("异常在这里="+e.toString());
        }
       i=0;
       for (String key:map_list.keySet()) {
           List<Integer> list=map_list.get(key);
           if(i==0){
               for(int j=1;j<=list.size();j++){
                   row = sheet.createRow(j);
                   cell = row.createCell(i);
                   cell.setCellValue(list.get(j-1));
               }
           }else{
               for(int j=1;j<=list.size();j++){
                   row = sheet.getRow(j);
                   if(row==null){
                       row=sheet.createRow(j);
                   }
                   cell = row.createCell(i);
                   cell.setCellValue(list.get(j-1));
               }
           }
           i++;
       }
        File file = new File("D:\\测试数据\\"+ path);

            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
            println("----保存文档成功");
        }catch (Exception e){
            println("----"+e.toString());
        }


    }

*/

    //室内定位，计算位置公式


    //保存一个文件，全部的信号值，全部设备全部信号一个列。
    private static Scan_report_data_info getLast(ArrayList<Scan_report_data_info> list) {
        ArrayList<Scan_report_data_info> lists = new ArrayList<>();
        long stime;
        try {
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);


            Date time1 = sdf.parse(list.get(0).getTime());
            Date time2 = sdf.parse(list.get(list.size() - 1).getTime());
            stime = time2.getTime() - time1.getTime();
        } catch (Exception e) {
            println("rrrrr" + e.getMessage());
            return null;

        }

        int index;
        int length = list.size();
        for (int b = 0; b < length; b++) {
            index = 0;
            for (int j = 1; j < list.size(); j++) {
                if (list.get(index).getRssi() <= list.get(j).getRssi()) {
                    index = j;
                }
            }
            try {
                lists.add(list.get(index));
                println("信号排序=" + list.get(index).getRssi());
                list.remove(index);
            } catch (Exception e) {
                println("++++" + e.getMessage());
                return null;

            }
        }

        for (int i = 0; i < 20; i++) {
            lists.remove(0);
            lists.remove(lists.size() - 1);
        }
        Double count;
        count = 0.0;

        for (int i = 0; i < lists.size(); i++) {
            println("循环输出=" + i + " 信号=" + lists.get(i).getRssi());
            count = count + lists.get(i).getRssi();
        }
        count = count / lists.size();
        Scan_report_data_info beacon = new Scan_report_data_info("平均信号", count.intValue(), "" + stime / 1000);
        return beacon;
    }

    //给第三方推送设备离线
    public static void sendBeaconPush_onOff(Beacon beacon, int status) {
          //  println("--------------12");
        if (beacon.getUser_key() == null) {
            return;
        }
        println("进行在线或者离线检测"+beacon.getMac()+"======="+status);
      //  if (beacon.getOnline() != status) {
            Check_sheet check_sheet = check_sheetMap.get(beacon.getProject_key());
            if (check_sheet !=null) {
                beacon.setOnline(status);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("device_type", "beacon");
                if(status==1){
                    jsonObject.put("push_type", "online");
                }else{
                    jsonObject.put("push_type", "offline");
                }
                jsonObject.put("last_time",beacon.getLastTime());
              //  jsonObject.put("push_type", "online");
                jsonObject.put("bt", beacon.getBt());
                jsonObject.put("address", beacon.getMac());
                jsonObject.put("project_key", beacon.getProject_key());
                id++;
                jsonObject.put("id", id);
                jsonObject.put("time", sdf.format(new Date()));
                    println("原始信标" + jsonObject);
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setMsg(jsonObject.toString());
                directExchangeProducer.send(rabbitMessage.toString(), Push);
           // }
        }
    }
    public static void sendBeaconPush_Bt(Beacon beacon, int status) {
        //  println("--------------12");
        if (beacon.getUser_key() == null) {
            return;
        }
      //  println("进行在线或者离线检测"+beacon.getMac()+"======="+status);
    //    if (beacon.getOnline() != status) {

                beacon.setOnline(status);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("device_type", "beacon");
                jsonObject.put("push_type", "bt");
                jsonObject.put("last_time",beacon.getLastTime());
                jsonObject.put("bt", beacon.getBt());
                jsonObject.put("address", beacon.getMac());
                jsonObject.put("project_key", beacon.getProject_key());
                id++;
                jsonObject.put("id", id);
                jsonObject.put("time", sdf.format(new Date()));
                println("原始信标" + jsonObject);
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setMsg(jsonObject.toString());
                directExchangeProducer.send(rabbitMessage.toString(), Push);

        //}
    }

    //给第三方推送设备离线
    public static void sendGatewayPush(Gateway gateway, int status) {
        if (gateway.getUser_key() == null) {
            return;
        }
        if (gateway.getOnline() != status) {
               // Check_sheet check_sheet = check_sheetMap.get(gateway.getProject_key());
                //if (check_sheet != null){
                    gateway.setOnline(status);
                    Gateway_sql gateway_sql = new Gateway_sql();
                    // println("上线后更新到数据库");
                    gateway_sql.updateGateway(gatewayMapper, gateway);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("device_type", "gateway");
                    if(status==1){
                        jsonObject.put("push_type", "online");
                    }else{
                        jsonObject.put("push_type", "offline");
                    }
                    jsonObject.put("last_time",gateway.getLasttime());
                    //  jsonObject.put("push_type", "online");
                    jsonObject.put("map_key",gateway.getMap_key());
                    jsonObject.put("x",gateway.getX());
                    jsonObject.put("y",gateway.getY());
                    jsonObject.put("address", gateway.getAddress());
                    jsonObject.put("project_key", gateway.getProject_key());
                    id++;
                    jsonObject.put("id", id);
                    jsonObject.put("time", sdf.format(new Date()));
                    println("原始网关推送" + sdf.format(new Date()) + "Push" + "id=" + id);
                    RabbitMessage rabbitMessage = new RabbitMessage();
                    rabbitMessage.setMsg(jsonObject.toString());
                    directExchangeProducer.send(rabbitMessage.toString(), Push);
            //}
        }
    }
    //给第三方推送设备离线
    public static void sendGatewayHeartbeatPush(Gateway gateway) {
        if (gateway.getUser_key() == null) {
            return;
        }

           // Check_sheet check_sheet = check_sheetMap.get(gateway.getProject_key());
                 println("输出");
          //  if (check_sheet != null){
                gateway.setOnline(1);
                Gateway_sql gateway_sql = new Gateway_sql();
                // println("上线后更新到数据库");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("device_type", "gateway");
                jsonObject.put("push_type", "HeartBeat");
                jsonObject.put("last_time",gateway.getLasttime());
                jsonObject.put("map_key",gateway.getMap_key());
                jsonObject.put("x",gateway.getX());
                jsonObject.put("y",gateway.getY());
                jsonObject.put("address", gateway.getAddress());
                jsonObject.put("project_key", gateway.getProject_key());
                id++;
                jsonObject.put("id", id);
                jsonObject.put("time", sdf.format(new Date()));
                println("原始网关推送" + sdf.format(new Date()) + "Push" + "id=" + id);
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setMsg(jsonObject.toString());
              //  println("转发原始="+rabbitMessage.getMsg());
                directExchangeProducer.send(rabbitMessage.toString(), Push);

       // }
    }
    //给第三方推送位置信息
    public static void sendLocationPush(Beacon beacon) {
         println("--------------12");
        if (beacon.getUser_key() == null) {
            return;
        }

        Check_sheet check_sheet = check_sheetMap.get(beacon.getProject_key());
        if (check_sheet !=null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_type", "beacon");

            jsonObject.put("push_type", "location");
            jsonObject.put("gateway_address",beacon.getGateway_address());
            jsonObject.put("x", beacon.getX());
            jsonObject.put("y",beacon.getY());
            jsonObject.put("map_key",beacon.getMap_key());
            jsonObject.put("last_time",beacon.getLastTime());
            jsonObject.put("address", beacon.getMac());
            jsonObject.put("project_key", beacon.getProject_key());

            id++;
            jsonObject.put("id", id);
            jsonObject.put("time", sdf.format(new Date()));
              println("位置推送666" + jsonObject);
            RabbitMessage rabbitMessage = new RabbitMessage();
            rabbitMessage.setMsg(jsonObject.toString());
            directExchangeProducer.send(rabbitMessage.toString(), Push);
        }

    }
    //给第三方推送位置信息
    public static void sendLocationPush_OFcat1(FWordcard fWordcard) {
        //  println("--------------12");
        if (fWordcard.getUser_key() == null) {
            return;
        }

        Check_sheet check_sheet = check_sheetMap.get(fWordcard.getProject_key());
        if (check_sheet !=null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_type", "beacon");

            jsonObject.put("push_type", "location");
            jsonObject.put("x", fWordcard.getX());
            jsonObject.put("y",fWordcard.getY());
            jsonObject.put("map_key",fWordcard.getMap_key());
            jsonObject.put("last_time",fWordcard.getLastTime());
            jsonObject.put("address", fWordcard.getMac());
            jsonObject.put("project_key", fWordcard.getProject_key());
            id++;
            jsonObject.put("id", id);
            jsonObject.put("time", sdf.format(new Date()));
            //  println("原始信标" + sdf.format(new Date()) + "Push" + "id=" + id);
            RabbitMessage rabbitMessage = new RabbitMessage();
            rabbitMessage.setMsg(jsonObject.toString());
            directExchangeProducer.send(rabbitMessage.toString(), Push);
        }

    }




    //给地图推送位置
    public static void sendTagPush(ArrayList<Object> devicep, String map_key ) {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("device", devicep);
        id++;
        jsonObject1.put("id", id);
        jsonObject1.put("time", sdf.format(new Date()));
        println("原始" + devicep);
        //println("原始" + map_key);
        RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(),map_key);
       directExchangeProducer.send(rabbitMessage1.toString(), sendtoMap);
    }
    //针对客户的项目配置，转发原始数据
    public static void sendRelayPush(ArrayList<Object> devicep, String map_key ) {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("tag", devicep);
        id++;
        jsonObject1.put("id", id);
        jsonObject1.put("time", sdf.format(new Date()));
        //println("原始" + map_key);
        //println("原始" + map_key);
        RabbitMessage rabbitMessage1 = new RabbitMessage("", jsonObject1.toString(),map_key);
        //directExchangeProducer.send(rabbitMessage1.toString(), sendtoMap);
    }


    public static String unzip(byte[] data) throws IOException,
            DataFormatException {

        Inflater inf = new Inflater();
            inf.setInput(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while(!inf.finished())
        {
            int count = inf.inflate(buffer);
            baos.write(buffer, 0, count);
        }
            baos.close();
            data=baos.toByteArray();

           String json = new String(data, 0, data.length);
            return json;

}


   /*     //保存设备的离线以及在线记录
    public static void saveRecord(String mac,long lasttime,String userkey,int type,int status,String project_key){
        Moffline moffline=new Moffline(mac,type,status,lasttime,userkey,project_key);
        Moffline_Sql mofflineSql=new Moffline_Sql();
        mofflineSql.addMoffline(mofflineMapper,moffline);

    }*/
}
