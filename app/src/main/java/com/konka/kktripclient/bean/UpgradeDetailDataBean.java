package com.konka.kktripclient.bean;

/**
 * Created by HP on 2017-5-31.
 */

public class UpgradeDetailDataBean {

    private int versionid;
    private String name;
    private String version_name;
    private int version_code;
    private String version_url;
    private String description;
    private int flag;

    public UpgradeDetailDataBean(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }

    public int getVersionid() {
        return versionid;
    }

    public void setVersionid(int versionid) {
        this.versionid = versionid;
    }

}
