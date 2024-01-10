package com.kunlun.firmwaresystem.entity.device;

public class DeviceCode {
    int id;
    String name;
    String customer_key;
    String customer_name;
    String project_key;
    String user_key;
    String describes;
    int sum;
    int stock;
    String unit;
    int unit_sum;
    String supplier_name;
    int supplier_id;
    //类目名称
    String type_name;
    //类目id
    int type_id;
    long create_time;
    long update_time;
    double price;
    String model;
    String brand;
    String file;
    String code;
    String code_sn;
    double count_monery;



    public DeviceCode(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getUnit_sum() {
        return unit_sum;
    }

    public void setUnit_sum(int unit_sum) {
        this.unit_sum = unit_sum;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
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

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFile() {
        return file;
    }

    public String getModel() {
        return model;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode_sn(String code_sn) {
        this.code_sn = code_sn;
    }

    public void setCount_monery(double count_monery) {
        this.count_monery = count_monery;
    }

    public double getCount_monery() {
        return count_monery;
    }

    public String getCode_sn() {
        return code_sn;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "DeviceCode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", customer_key='" + customer_key + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", project_key='" + project_key + '\'' +
                ", user_key='" + user_key + '\'' +
                ", describes='" + describes + '\'' +
                ", sum=" + sum +
                ", stock=" + stock +
                ", unit='" + unit + '\'' +
                ", unit_sum=" + unit_sum +
                ", supplier_name='" + supplier_name + '\'' +
                ", supplier_id=" + supplier_id +
                ", type_name='" + type_name + '\'' +
                ", type_id=" + type_id +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", price=" + price +
                ", model='" + model + '\'' +
                ", file='" + file + '\'' +
                ", code='" + code + '\'' +
                ", code_sn='" + code_sn + '\'' +
                ", count_monery=" + count_monery +
                ", brand='" + brand + '\'' +
                '}';
    }
}
