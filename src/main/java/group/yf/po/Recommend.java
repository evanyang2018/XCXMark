package com.zap.miniapp.po;

import java.io.Serializable;

/**
 * 应用推荐
 *
 * @author Yang Fei
 */
public class Recommend implements Serializable {

    private static final long serialVersionUID = 1L;
    private int rId;
    private int appId;

    public int getrId() {
        return rId;
    }

    public void setrId(int rId) {
        this.rId = rId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }
}
