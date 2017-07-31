package com.zap.miniapp.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class UrlConnUtils {
    private static Logger log = Logger.getLogger(UrlConnUtils.class);

    /**
     * 注意：5秒内请求不到页面.就认为出现错误。
     *
     * @param urlPath
     * @return
     */
    public static String getConnStr(String urlPath) {
        HttpURLConnection urlconn = null;
        StringBuffer sbf = new StringBuffer();
        JSONObject json = new JSONObject();
        try {
            URL url = new URL(urlPath);
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.setConnectTimeout(1500);
            urlconn.setReadTimeout(2500);
            urlconn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sbf.append(line);
            }
            br.close();
            urlconn.disconnect();
            return sbf.toString();
        } catch (Exception e) {
            json.put("code", "500");
            json.put("msg", "连接超时");
            log.info("请求地址出错！！！");
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return json.toJSONString();
    }

    public static String getConnStrLongtime(String urlPath) {
        HttpURLConnection urlconn = null;
        StringBuffer sbf = new StringBuffer();
        JSONObject json = new JSONObject();
        try {
            URL url = new URL(urlPath);
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.setConnectTimeout(30000);
            urlconn.setReadTimeout(30000);
            urlconn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sbf.append(line);
            }
            br.close();
            urlconn.disconnect();
            return sbf.toString();
        } catch (Exception e) {
            json.put("code", "500");
            json.put("msg", "连接超时");
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return json.toJSONString();
    }

    public static String getConnStrForMethod(String urlPath, String method, String encoding) {
        HttpURLConnection urlconn = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(urlPath);
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod(method);

            //urlconn.setConnectTimeout(1500);
            //urlconn.setReadTimeout(2500);
            urlconn.setConnectTimeout(4500);
            urlconn.setReadTimeout(5500);

            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlconn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), encoding));
            String line;
            while ((line = br.readLine()) != null) {
                sbf.append(line);
            }
            br.close();
            urlconn.disconnect();
            urlconn.disconnect();
            return sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return null;
    }

    public static String getConnStrForPOST(String urlPath, String encoding, String param) {
        HttpURLConnection urlconn = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(urlPath);
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("POST");
            urlconn.setDoOutput(true);
            urlconn.setConnectTimeout(15000);
            urlconn.setReadTimeout(20000);
            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlconn.setRequestProperty("Charset", "utf-8");
            urlconn.connect();
            OutputStreamWriter osw = new OutputStreamWriter(urlconn.getOutputStream(), "utf-8");
            osw.write(param);
            osw.flush();
            osw.close();
            System.out.println(urlconn.getResponseCode());
            if (urlconn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), encoding));
                String line;
                while ((line = br.readLine()) != null) {
                    sbf.append(line);
                }
                br.close();
                urlconn.disconnect();
            }
            return sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return null;
    }

    public static String getConnStr(String urlPath, String encoding) {
        HttpURLConnection urlconn = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(urlPath);
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.setConnectTimeout(1500);
            urlconn.setReadTimeout(2500);
            urlconn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), encoding));
            String line;
            while ((line = br.readLine()) != null) {
                sbf.append(line);
            }
            br.close();
            urlconn.disconnect();
            return sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return null;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        String url = "http://xc.izhipeng.com/importMiniappInfo.jspa";
        String encoding = "UTF-8";
        String param = "appName=jj&logo=https://www.zhuige.com/Upload/logo/2017-01-17/587dd21fd78db.png"
                + "&qrcode=https://www.zhuige.com/Upload/qrcode/2017-01-17/587dd222b9b34.jpg&info=hh&classify=hh"
                + "&label=hh&author=hh&updateTime=2015-08-25"
                + "&pic1=https://www.zhuige.com/Upload/screenshot/2017-01-17/587dd2266a588.jpg"
                + "&pic2=https://www.zhuige.com/Upload/screenshot/2017-01-17/587dd2285f2d8.jpg"
                + "&pic3=https://www.zhuige.com/Upload/screenshot/2017-01-17/587dd22ab84b5.jpg";
        String str = UrlConnUtils.getConnStrForPOST(url, encoding, param);
        System.out.println("str = " + str);


    }
}
