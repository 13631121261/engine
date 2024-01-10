package com.kunlun.firmwaresystem.util;

import java.util.Scanner;

public class Test {
    public static void main(String[] arg0) {
       /* Scanner sr = new Scanner(System.in);

        int n = sr.nextInt();
        //初始值为n依次倒叙判断
        for (int i = n; i >= 2; i--) {
            System.out.println("输出i=" + i);
            for (int j = 2; j < i; j++) {
                System.out.println("输出j=" + j);
                //余数为零不是质数直接跳过
                if (i % j == 0) {
                    break;
                }
                //继续判断直到	j 等于当前的数(i)-1
                if (j == i - 1) {
                    System.out.println(i);
                    //因为是求不大于n的最大质数，
                    //所有找到不大于n的第一个质数就可以用return结束程序
                    return;
                }
            }
        }*/
       String a="-a-b-c";
       String b[]=a.split("-");
       for(String c:b){
           System.out.println(c);
       }
    }


}
