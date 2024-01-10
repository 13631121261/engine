package com.kunlun.firmwaresystem.gatewayJson.type_response;

public class WifiVersionDetail extends ResponseHead {
    boolean result;
    String version;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
