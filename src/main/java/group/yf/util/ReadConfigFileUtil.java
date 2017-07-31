package com.zap.miniapp.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author liu.na
 *         读取文件util
 */
public class ReadConfigFileUtil {
    private Logger logger = Logger.getLogger(super.getClass());

    /**
     * 读取conf配置文件  返回字符串形式；
     *
     * @param filePath
     * @param jsonName
     * @return
     */
    public String readConfFile(String filePath, String jsonName) {
        FileInputStream fis = null;
        try {
            File file = null;
            file = new File(filePath + ".tpl");
            if (!file.exists()) {
                file = new File(filePath);
            }
            fis = new FileInputStream(file);
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            String config = new String(buf, "utf-8");
            logger.info(config + "config>>>>>>>>>>");
            JSONObject json = JSONObject.parseObject(config);
            String jsonConfig = json.getString(jsonName);
            return jsonConfig;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 读取vm配置文件信息
     *
     * @param fileName
     * @param jsonName
     * @param object
     * @return
     */
    public JSONObject readVMFile(String fileName) {
        FileInputStream fis = null;
        try {
            File file = null;
            file = new File(fileName + ".tpl");
            if (!file.exists()) {
                file = new File(fileName);
            }
            fis = new FileInputStream(file);
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            String config = "{" + new String(buf, "utf-8") + "}";
            System.out.println("config::" + config);
            JSONObject json = JSONObject.parseObject(config);
            //JSONArray jsonArray = json.getJSONArray(jsonName);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
