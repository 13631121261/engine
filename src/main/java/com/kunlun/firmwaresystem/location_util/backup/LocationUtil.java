
package com.kunlun.firmwaresystem.location_util.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class LocationUtil {
    //信号在1米时的值暂定为-51；
   // public static double rssi_At_1m =47;
    //信号在1米时的值暂定为-35；
  //  public static double rssi_At_1m =35;
    /**
     * @param dataList 一个设备对应的基站信号值
     * @param address 设备的mac*/
    public Location calculate(List<Gateway_device> dataList, String address,int type) {
        Map<String, List<Gateway_device>> dataMap = new HashMap<>();
        //把这个设备的被对应的网关扫描的信息取出
        List<Gateway_device> gateway_beacon;
        Gateway_device data;
      //  println("信标="+dataList.get(0).dAddress);
   //    println("dataList长度" + dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            data = dataList.get(i);
            String gmac = data.getgAddress();
            gateway_beacon = dataMap.get(gmac);
            if (gateway_beacon == null) {
                gateway_beacon = new ArrayList<Gateway_device>();
                gateway_beacon.add(data);
                dataMap.put(gmac, gateway_beacon);
            } else {
                gateway_beacon.add(data);
            }
        }
             // println("dataMap"+dataMap.size());
      /*  if(dataMap.size()<=2){
            if(dataMap.size()==2){

            }
        //
            return null;
        }*/

      //   println("dataMap长度" + dataMap.size());
        //整理好全部code对应的位置
        Point points = onePoint(dataMap,type);
      //  println(points);
        Location location = new Location(points, address,points.getMap_key());
        location.setDataMap(points.getList());
        return location;
    }

//
    private Point onePoint(Map<String, List<Gateway_device>> beaconMap,int type) {
        ArrayList<Gateway_device> list = new ArrayList<>();
        for (String key : beaconMap.keySet()) {
            int rssi = 0;
            List<Gateway_device> dataList = beaconMap.get(key);
            if (dataList.isEmpty()) {
                    println("出现了定位周期内，此网关没有扫描到设备的情况，按理说不可能");
            } else {

                rssi=-100;
                for (Gateway_device data : dataList) {
                  if(data.getRssi()>rssi){
                      //println(" 每个 "+data.getRssi());
                      rssi=data.getRssi();
                  }
                }

                if (rssi>=-100) {
                    Gateway_device data = dataList.get(0);
                    //  println(dataList.get(0).getName()+ "信号=" + rssi);
                    data.setRssi(rssi);
                    list.add(data);
                }
            }
        }

        if(type==0){
            for(int i=list.size()-1;i>=0;i--){
             //   println("循环次数="+i);
                if(i>=1){
                    if(list.get(i).getRssi()<list.get(i-1).getRssi()){
                        println("删除i="+i);
                        list.remove(i);
                    }else{
                        list.remove(i-1);
                        println("删除i="+(i-1));
                    }
                }


            }
        }else if (type==1){
            int size = list.size();
            for (int i = 0; i < size - 1; i++) {
                // println("循环次数="+i);
                int n = 0;
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getRssi() < list.get(n).getRssi()) {
                        n = j;
                    }
                }
                list.remove(n);
            }
        }

        println("定位长度="+list.size());
        //
        if(list.size()==1){
            println("只有一个网关"+list.get(0).getdAddress());
            Point p = new Point(list.get(0).getX(), list.get(0).getY(), list.get(0).getgAddress(),list.get(0).getMap_key());
            p.setList(list);
         //   println("只有一个P"+p.getX());
            return p;
        }
        if(list.size()==2){
            Point p = new Point((list.get(0).getX()+list.get(1).getX())/2, (list.get(0).getY()+list.get(1).getY())/2, list.get(0).getgAddress(),list.get(0).getMap_key());
            p.setList(list);
            return p;
        }
//只有三个网关结合定位，就算一个点
        if (list.size() == 3) {
           // println("就三个基站");
            Point point=getPoint(list);
            if (point != null) {
                point.setList(list);
            }
            return  point;
        } else {
            //取信号强度最强的四个，每三个定位一次，再取中间点。
            int size = list.size();
            for (int i = 0; i < size - 3; i++) {
                // println("循环次数="+i);
                int n = 0;
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getRssi() < list.get(n).getRssi()) {
                        n = j;
                    }
                }
                list.remove(n);
            }
            for (int i = 0; i < list.size(); i++) {
                println("剩余="+list.get(i).getgAddress()+"  信号="+list.get(i).getRssi());
            }
            ArrayList<Gateway_device> list1 = new ArrayList<>();
            list1.add(list.get(0));
            list1.add(list.get(1));
            list1.add(list.get(2));
            Point p1 = getPoint(list1);
            if (p1 != null) {
                p1.setList(list1);
            }

            return p1;
        }

        //  return getPoint(list);
    }

    private Point getPoint(List<Gateway_device> list) {

        if (list.get(0).getX() <= 0.0 && list.get(1).getX() <= 0.0 && list.get(2).getX() <= 0.0) {

            return null;
        }
        double[] dis = new double[3];
        Point[] points = new Point[3];
        for (int i = 0; i < list.size(); i++) {
            int rssi = list.get(i).getRssi();

            double d = (abs(rssi) -abs( list.get(i).getA_rssi())) / (10 * list.get(i).getN());
            println("距离="+d+"   "+ list.get(i).getA_rssi());

          //  println("高度="+list.get(i).getZ());
            d = pow(10, d);
           //
            if(d>1){
               // if(list.get(i).getZ()>0){
                    d = sqrt((d * d) - 1);
                //}
            }else{
                Point point = new Point();
                point.setMap_key(list.get(i).getMap_key());
                point.x = list.get(i).getX();
                point.y = list.get(i).getY();
                return point;
            }

         /*  if (d > 1&&list.get(i).getZ()>0) {
                d = sqrt((d * d) - list.get(i).getZ());
            } else {

            }*/
         /*  if(d<1){
                Point point = new Point();
                point.setMap_key(list.get(i).getMap_key());
                point.x = list.get(i).getX();
                point.y = list.get(i).getY();
                return point;
            }*/

            //    println("直接距离=" + d);
           /* if (d <= 1.2) {
                Point point = new Point();
                point.x = list.get(i).getX();
                point.y = list.get(i).getY();
                return point;
            }*/
            // d = sqrt((d * d) - (1.2 * 1.2));
            dis[i] = d;

            String result = String.format("%.2f", d);
            println("rssi="+rssi  +"    距离="+result);
            list.get(i).setD(Double.parseDouble(result));
            println(list.get(i).getgAddress() + "    坐标=" + list.get(i).getX() + "   " + list.get(i).getY() + "   rssi=" + list.get(i).getRssi() + "   距离=" + d);
            if (d > 100) {
                //  println("距离严重错误");
                return null;
            }
           // println("list.get(i).getMap_key()="+list.get(i).getMap_key());
            Point p = new Point(list.get(i).getX(), list.get(i).getY(), list.get(i).getgAddress(), list.get(i).getMap_key());

            p.setA(list.get(i).getA_rssi());
            p.setN(list.get(i).getN());
            points[i] = p;

            //println("P="+p.toString());
        }
        //println(list.get(i).getiMac()+"   rssi="+list.get(i).gettRssi() +    "    距离="+d);
        //println(list.get(i).getiMac()+"   rssi="+list.get(i).gettRssi() +    "    距离="+d);
        Point point = threePoints(list.get(0).getUsed(),dis, points,list.get(0).getMap_key());
        return point;
    }


    //三点定位法
//dis:半径
//points：圆心
    private Point threePoints(int used,double[] dis, Point[] ps,String map_key) {
    //    println("threePoints="+map_key);
        //计算三个圆心的相互距离
       /* float ab,ac,bc;
         ab=(float)sqrt(abs(ps[0].x-ps[1].x)*abs(ps[0].x-ps[1].x)+abs(ps[0].y+ps[1].y)*abs(ps[0].y+ps[1].y));
        ac=(float)sqrt(abs(ps[0].x-ps[2].x)*abs(ps[0].x-ps[2].x)+abs(ps[0].y+ps[2].y)*abs(ps[0].y+ps[2].y));
        bc=(float)sqrt(abs(ps[1].x-ps[2].x)*abs(ps[1].x-ps[2].x)+abs(ps[1].y+ps[2].y)*abs(ps[1].y+ps[2].y));


        //计算三个圆半径的相互距离

        float abr,acr,bcr;
        abr=(float)( dis[0]+dis[1]);
        acr=(float)( dis[0]+dis[2]);
        bcr=(float)( dis[1]+dis[2]);*/

/*
        Point p=new Point(0,0);

        for (int i = 0; i < 3; ++i) {
            //检查距离是否有问题
            if (dis[i] < 0)
                return p;

            for (int j = i + 1; j < 3; ++j) {
                //圆心距离
                float p2p = (float)sqrt((ps[i].x - ps[j].x)*(ps[i].x - ps[j].x) +
                        (ps[i].y - ps[j].y)*(ps[i].y - ps[j].y));
                //判断两圆是否相交
                if (dis[i] + dis[j] <= p2p) {
                    //不相交，按比例求
                    p.x += ps[i].x + (ps[j].x - ps[i].x)*dis[i] / (dis[i] + dis[j]);
                    p.y += ps[i].y + (ps[j].y - ps[i].y)*dis[i] / (dis[i] + dis[j]);
                }
                else
                    //相离
                    {
                    //相交则套用公式（上面推导出的）
                    double dr = p2p / 2 + (dis[i] * dis[i] - dis[j] * dis[j]) / (2 * p2p);
                    p.x += ps[i].x + (ps[j].x - ps[i].x)*dr / p2p;
                    p.y += ps[i].y + (ps[j].y - ps[i].y)*dr / p2p;
                }
            }
        }

        //三个圆两两求点，最终得到三个点，求其均值
        p.x /= 3;
        p.y /= 3;*/
        // List<double[]> xyList=new ArrayList<>();
        //   for (int i = 0; i < dis.length; i++) {
        Round dev1 = new Round(ps[0].x, ps[0].y, dis[0], ps[0].mac);
        Round dev2 = new Round(ps[1].x, ps[1].y, dis[1], ps[1].mac);
        Round dev3 = new Round(ps[2].x, ps[2].y, dis[2], ps[2].mac);
        double[] xy = threepoint(used,dev1, dev2, dev3);
        //  xyList.add(xy);
        // }

        Point p = new Point(xy[0], xy[1], "",map_key);

        return p;
    }


    private double[] threepoint(int used,Round dev1, Round dev2, Round dev3) {
        double[] p1 = new double[2];// 有效交叉点1
        double[] p2 = new double[2];// 有效交叉点2
        double[] p3 = new double[2];// 有效交叉点3
        double[] zx = new double[2];//计算三点质心
        List<double[]> jds1 = jd(used,dev1.x, dev1.y, dev1.r, dev2.x, dev2.y, dev2.r, dev1.mac, dev2.mac);// r1,r2交点
        List<double[]> jds2 = jd(used,dev1.x, dev1.y, dev1.r, dev3.x, dev3.y, dev3.r, dev1.mac, dev3.mac);// r1,r3交点
        List<double[]> jds3 = jd(used,dev2.x, dev2.y, dev2.r, dev3.x, dev3.y, dev3.r, dev2.mac, dev3.mac);// r2,r3交点
        double t = 1000000;
        for (double[] jd1 : jds1) {
            for (double[] jd2 : jds2) {
                for (double[] jd3 : jds3) {
                    double jd1_2_dis = sqrt(pow(jd1[0] - jd2[0], 2) + pow(jd1[1] - jd2[1], 2));
                    double jd1_3_dis = sqrt(pow(jd1[0] - jd3[0], 2) + pow(jd1[1] - jd3[1], 2));
                    double jd2_3_dis = sqrt(pow(jd2[0] - jd3[0], 2) + pow(jd2[1] - jd3[1], 2));
                    if (jd1_2_dis + jd1_3_dis + jd2_3_dis < t) {
                        p1 = jd1;
                        p2 = jd2;
                        p3 = jd3;
                        t = jd1_2_dis + jd1_3_dis + jd2_3_dis;
                    }
                }
            }
        }
      //  println("三个点计算的位置1=" + p1[0] + "/" + p1[1]);
       // println("三个点计算的位置2=" + p2[0] + "/" + p2[1]);
       // println("三个点计算的位置3=" + p3[0] + "/" + p3[1]);
        zx[0] = (p1[0] + p2[0] + p3[0]) / 3;//质心
        zx[1] = (p1[1] + p2[1] + p3[1]) / 3;
        return zx;
    }


    private List<double[]> jd(int used,double x1, double y1, double r1, double x2, double y2, double r2, String mac1, String mac2) {
       //   println("网关1="+mac1+"   网关2="+mac2);
        List<double[]> points = new ArrayList<>();//交点坐标
        double[] coor = new double[2];
        double[] coor1 = new double[2];
        double buf = 0.2;
        double d = sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));// 两圆心距离
         //  println(x1+"  "+y1+"  "+r1);
      //  println("R2="+r2);
        //半径倍数
        double cd;
        if (r1 > r2) {
            cd = r1 / r2;

        } else {
            cd = r2 / r1;
        }
        if (d > r1 + r2) {   //相离
         //  println("相离"+cd);
           if(cd==1){
             //  println("1.0处理="+cd);
            r2 += buf;
            r1 += buf;
           }
      else{
               //println("否则="+cd);
               if (r1 > r2) {
                   r1 += buf * cd;
                   r2 += buf;
               } else {
                   r1 += buf;
                   r2 += buf * cd;
               }
           }

          //  println("半径="+r2);
            return jd(used,x1, y1, r1, x2, y2, r2, mac1, mac2);
        } else if (d < abs(r1 - r2)) {
            if (r1 > r2)//r1包含r2
            {
                r1 -= buf * cd;
                r2 += buf;
            } else//r2包含r1
            {
                r2 -= buf * cd;
                r1 += buf;
            }
            return jd(used,x1, y1, r1, x2, y2, r2, mac1, mac2);
        } else if (y1 == y2 && x1 != x2 && d < (r1 + r2))// 两圆向交
        {//  println("两圆向交111");
            if(used==1){
                if (abs(r2 + r1) - d > 0.5) {
                    r1 -= buf;
                    r2 -= buf;
                    return jd(used,x1, y1, r1, x2, y2, r2, mac1, mac2);
                }else{
                    double a = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2)) / (2 * x2 - 2 * x1);
                    if (d == abs(r1 - r2) || d == r1 + r2) //圆心距==半径差或者==半径和
                    {
                        // 只有一个交点时
                        coor[0] = a;
                        coor[1] = y1;
                        points.add(coor);
                    } else {
                        // 两个交点
                        double t = r1 * r1 - (a - x1) * (a - x1);
                        coor[0] = a;
                        coor[1] = y1 + sqrt(t);
                        points.add(coor);
                        coor1[0] = a;
                        coor1[1] = y1 - sqrt(t);
                        points.add(coor1);
                    }
                }
            }else{
                double a = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2)) / (2 * x2 - 2 * x1);

                if (d == abs(r1 - r2) || d == r1 + r2) //圆心距==半径差或者==半径和
                {
                    // 只有一个交点时
                    coor[0] = a;
                    coor[1] = y1;
                    points.add(coor);
                } else {
                    // 两个交点
                    double t = r1 * r1 - (a - x1) * (a - x1);
                    coor[0] = a;
                    coor[1] = y1 + sqrt(t);
                    points.add(coor);
                    coor1[0] = a;
                    coor1[1] = y1 - sqrt(t);
                    points.add(coor1);
                }
            }


            //}
        } else if (y1 != y2 && d < (r1 + r2))// 两圆向交
        {

            if(used==1){
                if (abs(r2 + r1) - d > 0.5) {
                    r1 -= buf;
                    r2 -= buf;
                    return jd(used,x1, y1, r1, x2, y2, r2, mac1, mac2);
                } else { double k, disp;
                    k = (2 * x1 - 2 * x2) / (2 * y2 - 2 * y1);
                    disp = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2) - (y1 * y1 - y2 * y2)) / (2 * y2 - 2 * y1);// 直线偏移量
                    double a, b, c;
                    a = (k * k + 1);
                    b = (2 * (disp - y1) * k - 2 * x1);
                    c = (disp - y1) * (disp - y1) - r1 * r1 + x1 * x1;
                    double disc;
                    disc = b * b - 4 * a * c;// 一元二次方程判别式
                    if (d == abs(r1 - r2) || d == r1 + r2)//圆心距==半径差或者==半径和
                    {
                        //一个交点
                        coor[0] = (-b) / (2 * a);
                        coor[1] = k * coor[0] + disp;
                        points.add(coor);
                    } else {
                        //2个交点
                        coor[0] = ((-b) + sqrt(disc)) / (2 * a);
                        coor[1] = k * coor[0] + disp;
                        points.add(coor);
                        coor1[0] = ((-b) - sqrt(disc)) / (2 * a);
                        coor1[1] = k * coor1[0] + disp;
                        points.add(coor1);
                        //   println("交点1=" + coor[0] + "**" + coor[1]);
                        // println("交点2=" + coor1[0] + "**" + coor1[1]);
                    }
                    }
            }
            else{
                double k, disp;
                k = (2 * x1 - 2 * x2) / (2 * y2 - 2 * y1);
                disp = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2) - (y1 * y1 - y2 * y2)) / (2 * y2 - 2 * y1);// 直线偏移量
                double a, b, c;
                a = (k * k + 1);
                b = (2 * (disp - y1) * k - 2 * x1);
                c = (disp - y1) * (disp - y1) - r1 * r1 + x1 * x1;
                double disc;
                disc = b * b - 4 * a * c;// 一元二次方程判别式
                if (d == abs(r1 - r2) || d == r1 + r2)//圆心距==半径差或者==半径和
                {
                    //一个交点
                    coor[0] = (-b) / (2 * a);
                    coor[1] = k * coor[0] + disp;
                    points.add(coor);
                } else {
                    //2个交点
                    coor[0] = ((-b) + sqrt(disc)) / (2 * a);
                    coor[1] = k * coor[0] + disp;
                    points.add(coor);
                    coor1[0] = ((-b) - sqrt(disc)) / (2 * a);
                    coor1[1] = k * coor1[0] + disp;
                    points.add(coor1);
                    //   println("交点1=" + coor[0] + "**" + coor[1]);
                    // println("交点2=" + coor1[0] + "**" + coor1[1]);
                }
            }
           /* if (abs(r2 + r1) - d > 0.3) {
                r1 -= buf;
                r2 -= buf;
                return jd(x1, y1, r1, x2, y2, r2, mac1, mac2);
            } else {*/
               //  println("两圆向交222");
              /*  double k, disp;
                k = (2 * x1 - 2 * x2) / (2 * y2 - 2 * y1);
                disp = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2) - (y1 * y1 - y2 * y2)) / (2 * y2 - 2 * y1);// 直线偏移量
                double a, b, c;
                a = (k * k + 1);
                b = (2 * (disp - y1) * k - 2 * x1);
                c = (disp - y1) * (disp - y1) - r1 * r1 + x1 * x1;
                double disc;
                disc = b * b - 4 * a * c;// 一元二次方程判别式
                if (d == abs(r1 - r2) || d == r1 + r2)//圆心距==半径差或者==半径和
                {
                    //一个交点
                    coor[0] = (-b) / (2 * a);
                    coor[1] = k * coor[0] + disp;
                    points.add(coor);
                } else {
                    //2个交点
                    coor[0] = ((-b) + sqrt(disc)) / (2 * a);
                    coor[1] = k * coor[0] + disp;
                    points.add(coor);
                    coor1[0] = ((-b) - sqrt(disc)) / (2 * a);
                    coor1[1] = k * coor1[0] + disp;
                    points.add(coor1);
                    //   println("交点1=" + coor[0] + "**" + coor[1]);
                    // println("交点2=" + coor1[0] + "**" + coor1[1]);
                }
            //}*/
        }
        return points;
    }
}
