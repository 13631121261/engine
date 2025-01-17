package com.kunlun.firmwaresystem.entity;

public class Firmware_task {
    String url;
    String version;
    int count;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "firmware_task{" +
                "url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", count=" + count +
                '}';
    }
}
