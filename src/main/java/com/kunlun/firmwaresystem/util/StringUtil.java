package com.kunlun.firmwaresystem.util;


import com.kunlun.firmwaresystem.device.Gateway;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.entity.device.Devicep;
import com.kunlun.firmwaresystem.gatewayJson.type_scan_report.Scan_report_data_info;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;
import com.kunlun.firmwaresystem.sql.Gateway_sql;
import com.kunlun.firmwaresystem.sql.Moffline_Sql;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import static com.kunlun.firmwaresystem.DeviceTask.writeLog;
import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.Push;
import static com.kunlun.firmwaresystem.mqtt.DirectExchangeRabbitMQConfig.sendtoMap;

public class StringUtil {

    private static int id = 0;
    private static final char HexCharArr[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final String HexStr = "0123456789ABCDEF";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    public static byte[] intTo4ByteArray(int i) {
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
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
            System.out.println("异常在这里="+e.toString());
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
            System.out.println("----保存文档成功");
        }catch (Exception e){
            System.out.println("----"+e.toString());
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
            System.out.println("rrrrr" + e.getMessage());
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
                System.out.println("信号排序=" + list.get(index).getRssi());
                list.remove(index);
            } catch (Exception e) {
                System.out.println("++++" + e.getMessage());
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
            System.out.println("循环输出=" + i + " 信号=" + lists.get(i).getRssi());
            count = count + lists.get(i).getRssi();
        }
        count = count / lists.size();
        Scan_report_data_info beacon = new Scan_report_data_info("平均信号", count.intValue(), "" + stime / 1000);
        return beacon;


    }

    //给第三方推送设备离线
    public static void sendBeaconPush(Beacon beacon, int status) {
        if (beacon.getUser_key() == null) {
            writeLog("此信标异常，没有关联账户" + beacon.getMac());
            return;
        }
        if (beacon.getOnline() != status) {
            Check_sheet check_sheet = check_sheetMap.get(beacon.getUser_key());
            if (check_sheet == null) {
                writeLog("此配置文件异常，没有关联账户" + beacon.getUser_key());
                return;
            } else {
                beacon.setOnline(status);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "beacon");
                jsonObject.put("data", beacon);
                id++;
                jsonObject.put("id", id);
                jsonObject.put("time", sdf.format(new Date()));
               // System.out.println("原始" + sdf.format(new Date()) + "Push" + "id=" + id);
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setMsg(jsonObject.toString());
                rabbitMessage.setUdp(check_sheet.getUdp());
                directExchangeProducer.send(rabbitMessage.toString(), Push);


            }

        }
    }


    //给第三方推送设备离线
    public static void sendGatewayPush(Gateway gateway, int status) {
        if (gateway.getUser_key() == null) {
            writeLog("此网关异常，没有关联账户" + gateway.getAddress());
            return;
        }
        if (gateway.getOnline() != status) {
            Check_sheet check_sheet = check_sheetMap.get(gateway.getUser_key());
            if (check_sheet == null) {
                writeLog("网关此配置文件异常，没有关联账户" + gateway.getUser_key());
                return;
            } else {

                gateway.setOnline(status);
                gateway.setOnline_txt("在线");
                Gateway_sql gateway_sql = new Gateway_sql();
                System.out.println("上线后更新到数据库");
                gateway_sql.updateGateway(gatewayMapper, gateway);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "gateway");
                jsonObject.put("data", gateway);
                id++;
                jsonObject.put("id", id);
                jsonObject.put("time", sdf.format(new Date()));
                System.out.println("原始" + sdf.format(new Date()) + "Push" + "id=" + id);
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setMsg(jsonObject.toString());
                rabbitMessage.setUdp(check_sheet.getUdp());
                directExchangeProducer.send(rabbitMessage.toString(), Push);
            }

        }
    }

    //给第三方推送设备离线
    public static void sendDevicePush(Devicep devicep, int status) {
        // System.out.println("预计推送"+devicep.getBind_mac()+"status="+devicep.toString());
        if (devicep.getUserkey() == null) {
            System.out.println("空格" + devicep.getUserkey());
            writeLog("此资产异常，没有关联账户" + devicep.getSn());
            return;
        }
        if (devicep.getOnline() != status) {
            //  System.out.println("预备开始");
            Check_sheet check_sheet = check_sheetMap.get(devicep.getUserkey());
            //System.out.println(check_sheet);
            if (check_sheet == null) {
                //  System.out.println("算了");
                writeLog("此资产异常配置文件异常，没有关联账户" + devicep.getUserkey());
                return;
            } else {
                devicep.setOnline(status);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "device");
                jsonObject.put("data", devicep);
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setMsg(jsonObject.toString());
                rabbitMessage.setUdp(check_sheet.getUdp());
                id++;
                jsonObject.put("id", id);
                jsonObject.put("time", sdf.format(new Date()));
                directExchangeProducer.send(rabbitMessage.toString(), Push);
               // System.out.println("原始" + sdf.format(new Date()) + "Push" + "id=" + id);

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("device", devicep);
                jsonObject1.put("type", "device_line");
                id++;
                jsonObject1.put("id", id);
                jsonObject1.put("time", sdf.format(new Date()));
                System.out.println("原始" + sdf.format(new Date()) + "发给网页了" + "id=" + id);
                RabbitMessage rabbitMessage1 = new RabbitMessage(devicep.getProject_key(), jsonObject1.toString());
                directExchangeProducer.send(rabbitMessage1.toString(), "sendtoHtml");
            }
        }
    }

    //人员围栏报警
    public static void sendFenceSosPerson(Person person) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("data", person);
                jsonObject1.put("type", "fence_person");
                RabbitMessage rabbitMessage1 = new RabbitMessage(person.getProject_key(), jsonObject1.toString());
                directExchangeProducer.send(rabbitMessage1.toString(), "sendtoHtml");
    }
    //资产围栏报警
    public static void sendFenceSosDevice(Devicep devicep) {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("data", devicep);
        jsonObject1.put("type", "fence_devicep");
        RabbitMessage rabbitMessage1 = new RabbitMessage(devicep.getProject_key(), jsonObject1.toString());
        directExchangeProducer.send(rabbitMessage1.toString(), "sendtoHtml");
    }
    //给第三方推送设备离线
    public static void sendDeviceSoSPush(Devicep devicep, int status) {
        if (devicep.getUserkey() == null) {
            writeLog("此资产异常，没有关联账户" + devicep.getSn());
            return;
        }
        if (devicep.getSos() != status) {
            Check_sheet check_sheet = check_sheetMap.get(devicep.getUserkey());
            if (check_sheet == null) {
                writeLog("此资产异常配置文件异常，没有关联账户" + devicep.getUserkey());
                return;
            } else {
                devicep.setSos(status);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "device_sos");
                jsonObject.put("data", devicep);
                id++;
                jsonObject.put("id", id);
                jsonObject.put("time", sdf.format(new Date()));
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setMsg(jsonObject.toString());
                rabbitMessage.setUdp(check_sheet.getUdp());

                directExchangeProducer.send(rabbitMessage.toString(), Push);
              //  System.out.println("原始" + sdf.format(new Date()) + "Push" + "id=" + id);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("device", devicep);
                jsonObject1.put("type", "device_sos");
                id++;
                jsonObject1.put("id", id);
                jsonObject1.put("time", sdf.format(new Date()));

                System.out.println("原始" + sdf.format(new Date()) + "发给网页了" + "id=" + id);
                RabbitMessage rabbitMessage1 = new RabbitMessage(devicep.getProject_key(), jsonObject1.toString());
                directExchangeProducer.send(rabbitMessage1.toString(), "sendtoHtml");
            }

        }
    }
    //给地图推送位置

    public static void sendTagPush(ArrayList<Object> devicep, String map_key ) {

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("device", devicep);

        id++;
        jsonObject1.put("id", id);
        jsonObject1.put("time", sdf.format(new Date()));
        //System.out.println("原始" + map_key);
        //System.out.println("原始" + map_key);
        RabbitMessage rabbitMessage1 = new RabbitMessage(map_key, jsonObject1.toString());
       directExchangeProducer.send(rabbitMessage1.toString(), sendtoMap);
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
