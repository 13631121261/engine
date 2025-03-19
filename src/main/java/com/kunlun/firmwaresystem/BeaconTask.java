package com.kunlun.firmwaresystem;

import com.google.gson.Gson;
import com.kunlun.firmwaresystem.device.Gateway_devices;
import com.kunlun.firmwaresystem.entity.*;
import com.kunlun.firmwaresystem.entity.Record;
import com.kunlun.firmwaresystem.entity.device.Device_offline;
import com.kunlun.firmwaresystem.location_util.backup.Gateway_device;
import com.kunlun.firmwaresystem.mappers.DeviceOfflineMapper;
import com.kunlun.firmwaresystem.mqtt.RabbitMessage;
import com.kunlun.firmwaresystem.sql.*;
import com.kunlun.firmwaresystem.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.*;
import static com.kunlun.firmwaresystem.gatewayJson.Constant.*;
import static com.kunlun.firmwaresystem.gatewayStatusTask.writeLog;
import static com.kunlun.firmwaresystem.util.StringUtil.sendBeaconPush_Bt;
import static java.lang.StrictMath.abs;

@Component
public class BeaconTask {
    @Autowired
    private DeviceOfflineMapper deviceOfflineMapper;
    //信标判断在线离线
    public static List<Record> recordList = new ArrayList<>();
    public static Map<String, Record> recordMap = new HashMap<>();
    int runcount = 0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");//设置日期格式1

    @Scheduled(cron = "*/59 * * * * ?")
    public void execute() throws Exception {
        deleteCache();
        try {
            println("信标定时检测运行  60秒一次-------------------------------------------" + runcount);
            Beacon_Sql beacon_sql = new Beacon_Sql();
            if(beaconsMap==null){
                writeLog("beaconsMap是空的，重新获取一次");

                beaconsMap = beacon_sql.getAllBeacon(beaconMapper);
            }
            long thisTime = System.currentTimeMillis()/1000;
            runcount++;
            if (runcount > 10000) {
                runcount = 0;
            }
          //  println("111");
            Beacon beacon;
            DeviceOffline_Sql deviceOffline_sql = new DeviceOffline_Sql();
            for (String mac : beaconsMap.keySet()) {
                beacon = beaconsMap.get(mac);
              // println(mac + "信标mac=" + beacon.getLastTime());
               // println("222");
                if (beacon.getLastTime() == 0) {
                    //beacon.setOnline(0);
                    StringUtil.sendBeaconPush_onOff(beacon, 0);
                        beacon.setLastTime(-28800);

                    continue;
                }
                //println("333");
                Check_sheet check_sheet=check_sheetMap.get(beacon.getProject_key());
                int linetime=3;
                if(check_sheet!=null){
                    linetime=check_sheet.getLine_time();
                    if(linetime==0){
                        linetime=3;
                    }
                }
              if (thisTime - beacon.getLastTime() < linetime * 60L) {
                    println("信标在线");
                    sendBeaconPush_Bt(beacon,1);
                    StringUtil.sendBeaconPush_onOff(beacon, 1);
                } else {       beacon.setT(true);
                  // println("信标离线"+beacon.getMac());
                  beacon.setRun(0);
                  beacon.setBt(""+0);
                  beacon.setGateway_address("/");
                  beacon.setRssi(0);
                  beacon.setSos(0);
                  //  beacon.setOnline(0);
                   /* if (beacon.getOnline() != 0) {
                        StringUtil.saveRecord(beacon.getMac(), beacon.getLastTime(), beacon.getUser_key(), 2, 0,beacon.getProject_key());
                    }*/
                  // StringUtil.saveRecord(gateway.getAddress(),gateway.getLasttime(),gateway.getUser_key(),1,1,gateway.getProject_key());
                  StringUtil.sendBeaconPush_onOff(beacon, 0);
                  if(!beacon.isT()){
                      Alarm_Sql alarm_sql = new Alarm_Sql();
                      alarm_sql.addAlarm(alarmMapper,new Alarm(Alarm_Type.sos_offline,Alarm_object.beacon,beacon.getMap_key(),0,"", 0,0,"","Beacon",beacon.getMac(),beacon.getProject_key(),beacon.getLastTime()));
                  }
              }
                beacon_sql.update(beaconMapper, beacon);
            }
        }catch (Exception e){
            println("信标定时运行异常报错="+e.getMessage());
            writeLog("信标定时运行异常报错="+e.getMessage());
        }

        try {
       //     println("信标定时检测运行  60秒一次-------------------------------------------" + runcount);
            Bracelet_Sql braceletSql = new Bracelet_Sql();
            if(braceletsMap==null){
                writeLog("braceletsMap，重新获取一次");
                braceletsMap = braceletSql.getAllBracelet(braceletMapper);
            }
            long thisTime = System.currentTimeMillis()/1000;

           //  println("111");
            Bracelet bracelet;

            for (String mac : braceletsMap.keySet()) {
                bracelet = braceletsMap.get(mac);

                // println("222");
                if (bracelet.getLast_time() == 0) {
                    bracelet.setOnline(0);
                    bracelet.setLast_time(-28800);
                    continue;
                }
                //println("333");
                Check_sheet check_sheet=check_sheetMap.get(bracelet.getProject_key());
                int linetime=3;
                if(check_sheet!=null){
                    linetime=check_sheet.getLine_time();
                    if(linetime==0){

                        linetime=3;
                    }
                }


                if (thisTime - bracelet.getLast_time() < linetime * 60L) {
                    bracelet.setOnline(1);
                } else {
                    bracelet.setOnline(0);
                    bracelet.setSpo(0);
                    bracelet.setHeart_rate(0);
                    bracelet.setSteps(0);
                    bracelet.setCalorie(0);
                    bracelet.setSos(0);
                  //  bracelet.k
                    //  beacon.setOnline(0);
                   /* if (beacon.getOnline() != 0) {
                        StringUtil.saveRecord(beacon.getMac(), beacon.getLastTime(), beacon.getUser_key(), 2, 0,beacon.getProject_key());
                    }*/


                }
                braceletSql.update(braceletMapper, bracelet);
            }
        }catch (Exception e){
            println("信标定时运行异常报错="+e.getMessage());
            writeLog("信标定时运行异常报错="+e.getMessage());
        }


    }
    //每天人员报警保存
    private void deleteCache(){
        long time=System.currentTimeMillis()/1000;
        long one_time=time-1296000;
        History_Sql history_sql=new History_Sql();
        history_sql.deleteBy15Day(historyMapper,one_time);
        Logs_Sql logs_sql=new Logs_Sql();
        logs_sql.deleteBy15Day(logsMapper,one_time);
        Alarm_Sql alarm_sql=new Alarm_Sql();
        alarm_sql.deleteBy15Day(alarmMapper,one_time);
        TagLogSql tagLogSql=new TagLogSql();
      //  tagLogSql.deleteBy30s(tagLogMapper,System.currentTimeMillis()/1000-15);

        tagLogSql.keepLatest200Records(tagLogMapper);
    }
}