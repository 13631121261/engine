package com.kunlun.firmwaresystem.util;

import java.util.Scanner;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class Test {
    public static void main(String[] arg0) {
String dd="I:\\Code\\IdeaProject\\engine\\wififirmware\\ss.bin";
String ss="path=" +dd.replaceAll(":","12471");
        println(ss);
            int i=0;
            String d[]=new String[5];
                    i++;
            d[i++]="kk";
            println(d[1]);

       /* Scanner sr = new Scanner(System.in);

        int n = sr.nextInt();
        //初始值为n依次倒叙判断
        for (int i = n; i >= 2; i--) {
            println("输出i=" + i);
            for (int j = 2; j < i; j++) {
                println("输出j=" + j);
                //余数为零不是质数直接跳过
                if (i % j == 0) {
                    break;
                }
                //继续判断直到	j 等于当前的数(i)-1
                if (j == i - 1) {
                    println(i);
                    //因为是求不大于n的最大质数，
                    //所有找到不大于n的第一个质数就可以用return结束程序
                    return;
                }
            }
        }*/
        String msg="AAFE00EC010203040506070809120304050607080000";
        String url="AAFE10EC01010203040506070809120304050607080000";
     String tlm_no="AAFE2000123456781122334455667788";
     String tlm_on="AAFE200112345678112233445566778811112222";
        String eid="AAFE30EC1234567811223344";
    String a=msg.substring(8,28);
        String b=msg.substring(28,40);
        double nn=Math.pow(2, -1)+Math.pow(2, -2)+Math.pow(2, -3)+Math.pow(2, -4)+Math.pow(2, -5)+Math.pow(2, -6)+Math.pow(2, -7)+Math.pow(2, -8);

    byte[] data=msg.getBytes();
      println(a);
        println(b);

      /*  31
                0.25*/
        double aa=0.0976566;
        char[] bit="00000000".toCharArray();
        if(aa-0.5>=0){
            bit[0]='1';
            aa=aa-0.5;
        }
        if(aa-0.25>=0){
            bit[1]='1';
            aa=aa-0.25;
        }
        if(aa-0.125>=0){
            bit[2]='1';
            aa=aa-0.125;
        }
        if(aa-0.0625>=0){
            bit[3]='1';
            aa=aa-0.0625;
        }
        if(aa-0.03125>=0){
            bit[4]='1';
            aa=aa-0.03125;
        }
        if(aa-0.015625>=0){
            bit[5]='1';
            aa=aa-0.015625;
        }
        if(aa-0.0078125>=0){
            bit[6]='1';
            aa=aa-0.0078125;
        }
        if(aa-0.00390625>=0){
            bit[7]='1';
            aa=aa-0.00390625;
        }
      /*  if(aa-0.03125>=0){
            bit[4]='1';
            aa=aa-0.03125;
        }*/
        println(new String(bit));

       /* aa=aa-0.5;
        aa=aa-0.25;
        aa=aa-0.125;
        aa=aa-0.0625;
        aa=aa-0.03125;
        aa=aa-0.015625;
        aa=aa-0.0078125;
        aa=aa-0.00390625;*/

     /*   030701E4BB8AE5A4A9E68891E683B3E78EA9
        030702E4BDA0E5A5BDE59097EFBC9FE5ADA9
        030703E4BDA0E5A5BDE59097EFBC9FE5ADA9
        030704E4BDA0E5A5BDE59097EFBC9FE5ADA9
        030705E4BDA0E5A5BDE59097EFBC9FE5ADA9
        030706E4BDA0E5A5BDE59097EFBC9FE5ADA9
        030707E4BDA0E5A5BDE59097EFBC9FE5ADA9*/
        byte v=(byte)0xC8;
      //  v=(byte)(v|(byte)0x01);
        println(""+v);



        byte[] data1 = { (byte) 0xFF, (byte) 0x8D };
        int result1 = parseSigned12BitBE(data1, 0);
        System.out.println(result1); // 输出: -1793

        // 示例 2：输入 0x07, 0xFF，输出 +2047
        byte[] data2 = { 0x07, (byte) 0xFF };
        int result2 = parseSigned12BitBE(data2, 0);
        System.out.println(result2); // 输出: 2047

        // 示例 3：输入 0x08, 0x00，输出 -2048
        byte[] data3 = { 0x08, 0x00 };
        int result3 = parseSigned12BitBE(data3, 0);
        System.out.println(result3); // 输出: -2048
        for(int j=0;j<3;j++){
            System.out.println(j);
        }
    }
    //zeng929620555

    //0.09765625
    //0x1F19


        /**
         * 将两个字节解析为 12 位有符号整数（大端序）
         * @param byte1 高位字节（必须为无符号值 0~255）
         * @param byte2 低位字节（必须为无符号值 0~255）
         * @return 带符号的整数值
         */
        public static int parseSigned12BitBE(int byte1, int byte2) {
            // 合并为大端序 16 位整数
            int combined = (byte1 << 8) | byte2;

            // 提取高 12 位（右移 4 位，丢弃低 4 位）
            int value = combined >> 4;

            // 符号扩展：若最高位为 1（负数），填充高 4 位为 1
            if ((value & 0x800) != 0) { // 0x800 = 0b1000_0000_0000
                value |= 0xF000;        // 0xF000 = 0b1111_0000_0000_0000
            }

            // 转换为 16 位有符号整数（Java 中需强制转换）
            return (short) value;
        }

        /**
         * 从字节数组中解析数据
         * @param bytes 字节数组（至少包含两个字节）
         * @param offset 起始偏移量
         * @return 带符号的整数值
         */
        public static int parseSigned12BitBE(byte[] bytes, int offset) {
            // 将字节转换为无符号整数（0~255）
            int byte1 = bytes[offset] & 0xFF;     // 高位字节
            int byte2 = bytes[offset + 1] & 0xFF; // 低位字节
            return parseSigned12BitBE(byte1, byte2);
        }



}
