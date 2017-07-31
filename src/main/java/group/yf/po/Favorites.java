/**
 *
 */
package com.zap.miniapp.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yang Fei
 */
public class Favorites implements Serializable {

    private static final long serialVersionUID = 1L;
    private int userId;
    private int appId;
    private Date createTime;
    private Date updateTime;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
