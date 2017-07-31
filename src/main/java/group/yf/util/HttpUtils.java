package com.zap.miniapp.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    public String postRequest(String requestURL, String[][] postParams) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.connection.timeout", new Integer(500));
        client.getParams().setParameter("http.protocol.content-charset", "GBK");
        PostMethod post = new PostMethod(requestURL);
        NameValuePair[] data = new NameValuePair[postParams.length];
        try {
            for (int i = 0; i < postParams.length; i++) {
                data[i] = new NameValuePair(postParams[i][0], postParams[i][1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        post.setRequestBody(data);
        try {
            client.executeMethod(post);
            return post.getResponseBodyAsString();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return null;
    }

    public String getRequest(String requestURL) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.connection.timeout", new Integer(500));
        client.getParams().setParameter("http.protocol.content-charset", "utf-8");
        GetMethod get = new GetMethod(requestURL);
        try {
            client.executeMethod(get);
            return get.getResponseBodyAsString();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            get.releaseConnection();
        }
        return null;
    }

    /**
     * 2101114
     * http请求证金发送短信接口
     *
     * @param address
     * @param encode
     * @return
     */
    public static String getHttp(String address, String... encode) {
        try {
            URL url = new URL(address);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setConnectTimeout(3 * 1000);
            httpUrl.setReadTimeout(30 * 1000);
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(), encode.length == 0 ? "utf-8" : encode[0]));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            in.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String httpPost(String url, Map<String, String> paramMap) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.connection.timeout", new Integer(500));
        client.getParams().setParameter("http.protocol.content-charset", "utf-8");
        PostMethod post = new PostMethod(url);
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        Iterator<String> iterator = paramMap.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            String value = paramMap.get(name);
            try {
                NameValuePair nameValuePair = new NameValuePair(name, value);
                paramList.add(nameValuePair);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        post.setRequestBody((NameValuePair[]) paramList.toArray(new NameValuePair[0]));

        try {
            client.executeMethod(post);
            return post.getResponseBodyAsString();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return null;
    }

    public static String getHttpPost(String address, NameValuePair[] data) {
        try {
            HttpClient c = new HttpClient();
            c.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                    "utf-8");
            PostMethod post = new PostMethod(address);
            post.setRequestBody(data);
            c.executeMethod(post);
            String result = post.getResponseBodyAsString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取用户真实IP
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = "";
        String ips = request.getHeader("X-Forwarded-For");
        if (ips == null || ips.equals("")) {
            ip = request.getHeader("X-Real-IP");
            if (ip == null || ip.equals("")) {
                ip = request.getRemoteAddr();
            }
        } else {
            ip = ips.split(",")[0];
        }
        return ip;
    }

    public static class UTF8PostMethod extends PostMethod {
        public UTF8PostMethod(String url) {
            super(url);
        }

        @Override
        public String getRequestCharSet() {
            //return super.getRequestCharSet();
            return "UTF-8";
        }
    }
}
