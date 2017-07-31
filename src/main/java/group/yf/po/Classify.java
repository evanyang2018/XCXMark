/**
 *
 */
package com.zap.miniapp.po;

import java.io.Serializable;

/**
 * @author Yang Fei
 */
public class Classify implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String logo;
    private int level;
    private int parentId;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
