package com.zap.miniapp.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yang Fei
 */
public class Miniapp implements Serializable {
    private static final long serialVersionUID = 1L;
    private int appId;
    private String appName;
    private String logo;
    private String qrcode;
    private String info;
    private int firstClassifyId;
    private int secondClassifyId;
    private Date createTime;
    private Date updateTime;
    private int favorNum; //关注度
    private String pic1;
    private String pic2;
    private String pic3;
    private String pic4;
    private String pic5;
    private String label;
    private String company;
    private String author;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getFirstClassifyId() {
        return firstClassifyId;
    }

    public void setFirstClassifyId(int firstClassifyId) {
        this.firstClassifyId = firstClassifyId;
    }

    public int getSecondClassifyId() {
        return secondClassifyId;
    }

    public void setSecondClassifyId(int secondClassifyId) {
        this.secondClassifyId = secondClassifyId;
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

    public int getFavorNum() {
        return favorNum;
    }

    public void setFavorNum(int favorNum) {
        this.favorNum = favorNum;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }

    public String getPic5() {
        return pic5;
    }

    public void setPic5(String pic5) {
        this.pic5 = pic5;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
