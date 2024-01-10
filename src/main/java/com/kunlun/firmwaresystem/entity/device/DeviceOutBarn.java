package com.kunlun.firmwaresystem.entity.device;

public class DeviceOutBarn {
    int id;
    String notes;
    int out_sum;
    String unit;
    long create_time;
    String customer_key;
    String customer_name;
    String user_key;
    String code_sn;
    String code_name;
    String project_key;
    String person_name;
    String  idcard;
    BarnType barnType;
    long batch;
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



    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
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

    public int getOut_sum() {
        return out_sum;
    }

    public void setOut_sum(int out_sum) {
        this.out_sum = out_sum;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public BarnType getBarnType() {
        return barnType;
    }

    public void setBarnType(BarnType barnType) {
        this.barnType = barnType;
    }

    public long getBatch() {
        return batch;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getType_id() {
        return type_id;
    }

    @Override
    public String toString() {
        return "DeviceOutBarn{" +
                "id=" + id +
                ", notes='" + notes + '\'' +
                ", out_sum=" + out_sum +
                ", unit='" + unit + '\'' +
                ", create_time=" + create_time +
                ", customer_key='" + customer_key + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", user_key='" + user_key + '\'' +
                ", code_sn='" + code_sn + '\'' +
                ", code_name='" + code_name + '\'' +
                ", project_key='" + project_key + '\'' +
                ", person_name='" + person_name + '\'' +
                ", idcard=" + idcard +
                ", barnType=" + barnType +
                ", batch=" + batch +
                '}';
    }
}
