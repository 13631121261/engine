package com.kunlun.firmwaresystem.entity;

public class Wordcard_a extends Tag {
    int id;
    int sos;
    int run;
    double n;
    int isbind;
    String idcard;
    double x,y;
    String customer_key;


    public Wordcard_a(String mac,
                      String user_key, String type, String customer_key, int id) {

        this.mac = mac;
        this.user_key = user_key;
        this.type = type;
        this.customer_key=customer_key;
        this.id=id;
    }

    public Wordcard_a(String mac,
                      String user_key, String type, String customer_key) {

        this.mac = mac;
        this.user_key = user_key;
        this.type = type;
        this.customer_key=customer_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSos() {
        return sos;
    }

    public void setSos(int sos) {
        this.sos = sos;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public int getIsbind() {
        return isbind;
    }

    public void setIsbind(int isbind) {
        this.isbind = isbind;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public Wordcard_a(){

    }
}
