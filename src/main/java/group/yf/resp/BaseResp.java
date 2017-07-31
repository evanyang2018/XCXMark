package com.zap.miniapp.resp;

public abstract class BaseResp {
    public static final int RESP_SUCCESS = 0;
    public static final int RESP_ERROR = -1;

    private int ret;
    private String info;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
