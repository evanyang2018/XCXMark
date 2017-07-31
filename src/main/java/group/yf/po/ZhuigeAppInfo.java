/**
 *
 */
package com.zap.miniapp.po;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yang Fei
 */
public class ZhuigeAppInfo {

    private String appName;
    private String authorName;
    private String categoryName;
    private String logoUrl;
    private String qrcodeUrl;
    private String appDescription;
    private Date updateTime;
    private String tags;
    private List<String> thumbnailUrlList = new ArrayList<String>();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getThumbnailUrlList() {
        return thumbnailUrlList;
    }

    public void setThumbnailUrlList(List<String> thumbnailUrlList) {
        this.thumbnailUrlList = thumbnailUrlList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                if (Modifier.isPublic(fields[i].getModifiers())) {
                    continue;
                }
                fields[i].setAccessible(true);
                sb.append(fields[i].getName());
                sb.append("=");
                String value = "";
                if (fields[i].getGenericType().toString().equals("int")) {
                    value = String.valueOf(fields[i].getInt(this));
                } else if (fields[i].getGenericType().toString().equals("byte")) {
                    value = String.valueOf(fields[i].getByte(this));
                } else if (fields[i].getGenericType().toString().equals("short")) {
                    value = String.valueOf(fields[i].getShort(this));
                } else if (fields[i].getGenericType().toString().equals("long")) {
                    value = String.valueOf(fields[i].getLong(this));
                } else if (fields[i].getGenericType().toString().equals("float")) {
                    value = String.valueOf(fields[i].getFloat(this));
                } else if (fields[i].getGenericType().toString().equals("double")) {
                    value = String.valueOf(fields[i].getDouble(this));
                } else if (fields[i].getGenericType().toString().equals("class java.lang.String")) {
                    value = (String) fields[i].get(this);
                } else if (fields[i].getGenericType().toString().equals("class java.util.Date")
                        && (fields[i].get(this) != null)) {
                    value = ((Date) fields[i].get(this)).toString();
                }
                sb.append(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i < fields.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}
