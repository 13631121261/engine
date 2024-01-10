package com.kunlun.firmwaresystem.entity.device;

public class DeviceBarn {
    int id;
    String notes;
    int add_sum;
    int surplus;
    String unit;
    long create_time,update_time;
    String customer_key;
    String customer_name;
    double price;
    String user_key;
    String code_sn;
    String code_name;
    String project_key;
    BarnType barnType;
    int type_id;
    String model;
    String brand;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getAdd_sum() {
        return add_sum;
    }

    public void setAdd_sum(int addSum) {
        this.add_sum = addSum;
    }

    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProject_key() {
        return project_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getCode_sn() {
        return code_sn;
    }

    public void setCode_sn(String code_sn) {
        this.code_sn = code_sn;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setBarnType(BarnType barnType) {

        this.barnType = barnType;
    }

    public BarnType getBarnType() {
        return barnType;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getType_id() {
        return type_id;
    }
}
