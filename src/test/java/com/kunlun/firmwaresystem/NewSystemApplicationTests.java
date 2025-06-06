package com.kunlun.firmwaresystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

@SpringBootTest
class NewSystemApplicationTests {

    @Test
    void contextLoads() {
        int  d=3;
        int b=4;

//        double[] a={9,8,7,6,9,8,7,6,8,7,9,8,7,8,7,8,9,7,4,5,8,8};
        double[] a={-42, -70, -43, -48, -43, -46, -42, -48, -43, -46, -42, -71, -46, -46, -42, -43, -43, -48, -44, -48, -47, -46, -42, -48, -43, -41, -46, -43, -45, -45, -41, -42, -45, -42, -41, -41, -46, -46, -45, -41, -45, -41, -41, -46, -45, -41, -41, -46, -46, -45, -46, -49, -41, -46, -46, -45, -46, -41, -46, -45, -41, -46, -45, -41, -41, -46, -41, -41, -76, -51, -72, -45, -46, -41, -41, -45, -45, -41, -47, -44, -45, -45, -47, -48, -42, -41, -51, -41, -48, -41, -44, -41, -51, -54, -43, -71, -48, -45, -42, -45, -50, -42, -48, -48, -72, -45, -42, -42, -48, -45, -42, -49, -42, -45, -48, -51, -48, -71, -45, -48, -45, -72, -45, -45, -51, -45, -45, -45, -42, -42, -49, -45, -45, -51, -45, -48, -50, -42, -45, -42, -42, -45, -42, -46, -49, -43, -42, -45, -42, -45, -50, -54, -42, -45, -42, -51, -46, -46, -42, -48, -45, -41, -49, -45, -45, -42, -49, -49, -45, -41, -41, -45, -45, -41, -45, -45, -49, -42, -51, -41, -40, -41, -41, -41, -45, -45, -39, -51, -48, -45, -44, -41, -48, -41, -48, -49, -52, -45, -44, -54, -51, -51, -51, -45, -45, -42, -45, -42, -42, -41, -49, -48, -42, -49, -49, -44, -42, -45, -42, -45, -41, -49, -49, -46, -41, -48, -42, -45, -45, -41, -45, -51, -45, -41, -48, -41, -48, -41, -46, -45, -46, -41, -49, -41, -51, -41, -49, -49, -41, -45, -40, -45, -45, -41, -49, -41, -41, -49, -41, -51, -51, -51, -45, -41, -41, -49, -45, -49, -38, -48, -41, -48, -40, -41, -47, -45, -40, -47, -47, -48, -45, -45, -45, -48, -45, -42, -44, -45, -43, -45, -40, -45, -40, -44, -40, -39, -49, -44, -51, -44, -48, -44, -40, -44, -42, -40, -43, -40, -41, -48, -51, -40, -51, -49, -44, -49, -40, -44, -40, -44, -45, -40, -42, -49, -45, -40, -41, -41, -45, -47, -41, -51, -49, -45, -41, -48, -41, -41, -45, -40, -47, -44, -49, -41, -41, -45, -47, -41, -49, -49, -45, -41, -48, -46, -41, -48, -45, -70, -45, -48, -42, -45, -46, -40, -50, -41, -45, -42, -49, -45, -46, -42, -56, -49, -44, -51, -49, -45, -45, -41, -51, -45, -42, -48, -44, -41, -51, -40, -41, -48, -43, -44, -41, -44, -41, -50, -48, -43, -49, -42, -45, -46, -48, -48, -46, -49, -49, -48, -46, -50, -52, -49, -43, -42, -41, -44, -49, -44, -49, -41, -45, -42, -45, -46, -42, -41, -49, -41, -41, -45, -52, -44, -52, -43, -52, -49, -49, -48, -43, -43, -46, -44, -49, -42, -50, -45, -45, -49, -49, -42, -45, -42, -50, -48, -46, -43, -46, -46, -42, -50, -42, -46, -43, -43, -46, -49, -46, -42, -73, -41, -41, -41, -49, -44, -42, -41, -49, -50, -41, -45, -49, -44, -41, -41, -41, -47, -39, -49, -45, -48, -43, -43, -40, -49, -44, -45, -41, -41, -41, -41, -45, -41, -45, -51, -55, -45, -40, -49, -42, -49, -45, -49, -50, -46, -45, -44, -42, -53, -46, -39, -74, -48, -41, -51, -49, -45, -54, -44, -44, -41, -44, -48, -42, -47, -42, -49, -45, -41, -42, -42, -44, -45, -41, -50, -50, -41, -41, -41, -49, -41, -43, -41, -43, -45, -40, -50, -43, -48, -41, -44, -54, -48, -46, -51, -42, -41, -40, -44, -45, -49, -49, -43, -42, -49, -46, -42, -49, -51, -42, -49, -42, -49, -41, -41, -44, -44, -44, -44, -49, -49, -49, -44, -40, -45, -49, -44, -49, -41, -49, -46, -41, -41, -41, -44, -41, -44, -45, -45, -42, -49, -45, -41, -48, -42, -45, -42, -42, -42, -51, -49, -41, -41, -49, -45, -42, -49, -49, -49, -45, -50, -45, -41, -45, -42, -44, -41, -49, -45, -41, -41, -44, -43, -49, -49, -41, -41, -44, -45, -51, -45, -42, -45, -45, -42, -49, -46, -45, -45, -42, -46, -45, -42, -42, -49, -42, -41, -73, -50, -41, -50, -44, -46, -44, -44, -51, -51, -46, -46, -41, -49, -44, -42, -44, -45, -49, -45, -41, -45, -47, -45, -42, -52, -42, -45, -45, -44, -41, -49, -41, -46, -50, -45, -41, -45, -41, -50, -42, -45, -73, -45, -50, -45, -50, -49, -41, -49, -42, -42, -49, -49, -45, -49, -49, -42, -45, -50, -42, -44, -41, -43, -49, -49, -41, -47, -42, -43, -46, -48, -51, -42, -42, -45, -41, -42, -48, -41, -43, -44, -43, -48, -46, -50, -42, -42, -45, -42, -45, -46, -49, -44, -42, -49, -50, -44, -42, -44, -42, -43, -49, -45, -51, -42, -41, -48, -41, -45, -46, -41, -44, -41, -52, -70, -50, -50, -41, -45, -48, -48, -48, -41, -41, -41, -45, -45, -41, -41, -45, -45, -44, -41, -45, -40, -41, -41, -45, -45, -48, -49, -46, -42, -42, -44, -41, -41, -48, -44, -42, -49, -44, -41, -50, -45, -48, -51, -41, -45, -41, -48, -44, -46, -40, -47, -55, -44, -49, -45, -45, -46, -43, -51, -45, -46, -45, -42, -42, -44, -45, -42, -44, -48, -49, -49, -46, -42, -42, -48, -52, -44, -49, -42, -73, -42, -46, -49, -42, -47, -48, -45, -45, -42, -48, -42, -48, -42, -44, -49, -43, -50, -42, -45, -44, -42, -44, -42, -42, -42, -51, -42, -45, -51, -48, -44, -41, -41, -39, -54, -45, -52, -53, -41, -41, -38, -44, -41, -42, -41, -44, -49, -41, -52, -39, -44, -44, -41, -40, -44, -45, -43, -57, -44, -42, -45, -52, -49, -45, -45, -43, -43, -49, -45, -49, -48, -48, -42, -43, -45, -45, -42, -42, -48, -48, -44, -46, -48, -43, -43, -51, -41, -49, -42, -43, -43, -45, -44, -43, -52, -49, -45, -46, -43, -46, -50, -43, -42, -45, -49, -49, -56, -45, -46, -42, -51, -57, -44, -45, -43, -48, -44, -42, -50, -49, -45, -42, -43, -45, -48, -42, -45, -42, -48, -43, -44, -45, -48, -44, -45, -45, -48, -48, -49, -44, -42, -49, -45, -42, -42, -48, -46, -42, -51, -46, -45, -45, -42, -45, -41, -49, -51, -45, -41, -43, -45, -43, -41, -43, -41, -77, -41, -41, -49, -41, -57, -42, -45, -42, -41, -45, -45, -45, -53, -45, -41, -49, -49, -44, -50, -49, -50, -45, -45, -42, -49, -42, -41, -51, -54, -41, -45, -50, -42, -50, -44, -46, -40, -42, -46, -42, -46, -56, -44, -50, -41, -41, -50, -39, -48, -42, -42, -42, -49, -49, -45, -42, -50, -49, -42, -45, -50, -45, -45, -42, -43, -51, -50, -49, -42, -48, -49, -44, -42, -41, -47, -41, -74, -44, -45, -41, -42, -45, -41, -41, -49, -41, -49, -45, -47, -48, -41, -41, -52, -49, -48, -41, -44, -46, -41, -47, -43, -47, -47, -42, -52, -47, -47, -41, -48, -45, -51, -42, -53, -41, -73, -51, -45, -46, -49, -45, -49, -45, -46, -42, -42, -49, -47, -46, -50, -45, -45, -42, -45, -40, -41, -39, -42, -40, -56, -42, -48, -42, -41, -45, -54, -45, -42, -41, -44, -48, -49, -49, -46, -49, -42, -46, -49, -41, -45, -41, -52, -50, -45, -42, -44, -48, -45, -39, -49, -41, -45, -48, -48, -49, -51, -41, -48, -41, -47, -43, -46, -43, -45, -48, -43, -43, -48, -47, -43, -43, -43, -43, -42, -42, -40, -45, -41, -48, -50, -55, -48, -42, -42, -49, -52, -41, -42, -46, -46, -41, -56, -44, -42, -46, -46, -45, -41, -47, -42, -42, -47, -44, -43, -42, -42, -44, -51, -44, -44, -44, -42, -43, -51, -41, -49, -44, -49, -41, -54, -48, -50, -43, -43, -42, -46, -43, -43, -47, -44, -44, -42, -42, -47, -48, -47, -42, -45, -49, -45, -45, -45, -42, -51, -44, -42, -40, -46, -49, -46, -46, -42, -46, -49, -42, -42, -49, -41, -49, -44, -45, -50, -49, -45, -44, -41, -49, -44, -49, -44, -49, -44, -41, -49, -40, -41, -49, -44, -41, -49, -49, -44, -44, -41, -44, -49, -45, -44, -41, -44, -41, -44, -41, -44, -49, -49, -41, -41, -41, -42, -41, -41, -44, -41, -41, -42, -41, -44, -50, -54, -44, -44, -41, -41, -50, -44, -41, -41, -44, -49, -42, -53, -57, -44, -45, -45, -43, -46, -43, -52, -43, -43, -56, -52, -42, -46, -52, -42, -56, -45, -43, -51, -45, -45, -43, -43, -45, -53, -45, -52, -43, -52, -45, -43, -56, -45, -45, -51, -42, -52, -45, -49, -45, -45, -45, -45, -42, -57, -44, -44, -42, -42, -56, -41, -45, -42, -42, -51, -43, -53, -41, -43, -43, -44, -41, -44, -52, -41, -48, -42, -43, -41, -48, -42, -48, -48, -42, -44, -41, -45, -53, -50, -44, -46};
        ArrayList<Double> cd=new ArrayList<Double>();

        double cc=calculateVariance(a);

        for(double s:a){
            cd.add(s);
        }
        test(cd);
    }
    public double calculateVariance(double[] data) {
        double mean = Arrays.stream(data).average().orElse(0);
        double sumOfSquares = Arrays.stream(data).map(x -> (x - mean) * (x - mean)).sum();
        return sumOfSquares / data.length;
    }
    public double calculateMean(double[] data) {
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        return sum / data.length;
    }

    public double calculateVariance1(double[] data) {
        double mean = calculateMean(data);
        double sumOfSquares = 0;
        for (int i = 0; i < data.length; i++) {
            double diff = data[i] - mean;
            sumOfSquares += diff * diff;
        }
        return sumOfSquares / data.length;
    }

    public static void test( List<Double> numbers) {





        double meanq = numbers.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
        double variance = numbers.stream().mapToDouble(n -> Math.pow(n - meanq, 2)).average().getAsDouble();
        double stdDev = Math.sqrt(variance);

        println("Mean: " + meanq);
        println("Variance: " + variance);
        println("Standard Deviation: " + stdDev);
        int i=0;
        String d[]=new String[5];
        d[i++]="kk";
        println(d[0]);


    }


}
