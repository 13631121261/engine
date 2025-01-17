package com.kunlun.firmwaresystem.location_util.location;

import java.util.ArrayList;
import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class Main {
    public  static void main(String[] arg){
        LocationUtil util = new LocationUtil();
        List<Gateway_device> dataList=new ArrayList<>();
        for(int i=0;i<10;i++){
            //基站地址，定位标签地址，基站扫描到标签的信号，基站坐标X，基站坐标Y,N值,标签在距离基站1米位置的信号强度
            dataList.add(new Gateway_device("111","F0C814010101",-50-(i*3),0,0,2.67,-51));
        }
        for(int i=0;i<10;i++){
            dataList.add(new Gateway_device("222","F0C814010101",-56-(i*3),6,3,2.67,-51));
        }
        for(int i=0;i<10;i++){
            dataList.add(new Gateway_device("333","F0C814010101",-54-(i*3),3,6,2.67,-51));
        }
        for(int i=0;i<10;i++){
            dataList.add(new Gateway_device("444","F0C814010101",-60-(i*3),1,1,2.67,-51));
        }
        //传入数组，数组包涵一个定位标签对应的多个基站，，，如果是手机，可以理解为一个手机扫描到的多个标签的信号
        //或者是一个标签被多个网关扫描到的信号
        //传入该定位标签的mac地址
        Location location = util.calculate(dataList, "F0C814010101");
        println("X="+location.x+"   Y="+location.y+"  mac="+location.mac);
    }
}
