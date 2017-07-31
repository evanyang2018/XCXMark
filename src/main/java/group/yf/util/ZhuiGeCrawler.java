/**
 *
 */
package com.zap.miniapp.util;

import com.zap.miniapp.po.Miniapp;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author raxlee
 */
public class ZhuiGeCrawler {

    public static final Logger logger = Logger.getLogger(ZhuiGeCrawler.class);

    public static final String ENTRY_URL = "https://www.zhuige.com/app/index/page/0";
    public static final String PAGE_URL = "https://www.zhuige.com/app/index/page/";

    public static int totalPageCount = 35;// 29;
    public static int currentPageId = -1;

    public static class AppInfo {

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

    public static void main(String[] args) {
        for (int i = 0; i < totalPageCount; i++) {
            Set destUrlSet = getDestUrlSet();
            Iterator iterator = destUrlSet.iterator();
            while (iterator.hasNext()) {
                String url = (String) iterator.next();
                AppInfo appInfo = parseAppInfo(url);
                System.out.println("appInfo=" + appInfo.toString());
                // 在下面添加处理appinfo的代码
                insertToMiniapp(appInfo);
            }
        }
    }

    public static void insertToMiniapp(AppInfo appInfo) {

        Miniapp miniapp = new Miniapp();
        miniapp.setAppName(appInfo.getAppName());
        miniapp.setLogo(appInfo.getLogoUrl());
        miniapp.setQrcode(appInfo.getQrcodeUrl());
        miniapp.setInfo(appInfo.getAppDescription());
        miniapp.setFirstClassifyId(1);

        String url = "http://xc.izhipeng.com/importMiniappInfo.jspa";
        StringBuffer sbf = new StringBuffer();
        sbf.append("appName=" + appInfo.getAppName());
        sbf.append("&logo=" + appInfo.getLogoUrl());
        if (!appInfo.getQrcodeUrl().endsWith("zhuige.jpg")) {
            sbf.append("&qrcode=" + appInfo.getQrcodeUrl());
        }

        sbf.append("&info=" + appInfo.getAppDescription());
        sbf.append("&classify=" + appInfo.getCategoryName());
        sbf.append("&label=" + appInfo.getTags());
        sbf.append("&author=" + appInfo.getAuthorName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sbf.append("&updateTime=" + sdf.format(appInfo.getUpdateTime()));
        List<String> thumbnailList = new ArrayList<String>();
        thumbnailList = appInfo.getThumbnailUrlList();
        for (int i = 0; i < thumbnailList.size(); i++) {
            sbf.append("&pic" + (i + 1) + "=" + (String) thumbnailList.get(i));
        }
        System.out.println(sbf.toString());
        System.out.println(UrlConnUtils.getConnStrForPOST(url, "utf-8", sbf.toString()));
        ;
    }

    public static AppInfo parseAppInfo(String url) {
        AppInfo appInfo = new AppInfo();
        Parser parser = null;
        try {
            parser = new Parser(url);
            parser.setEncoding("utf-8");
            NodeFilter infoDivFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    // TODO Auto-generated method stub
                    if ((node instanceof Div) && node.getText().contains("prd-")) { // 带有prd前缀的div
                        return true;
                    }
                    return false;
                }
            };

            NodeList list = parser.parse(infoDivFilter);
            SimpleNodeIterator iterator = list.elements();
            while (iterator.hasMoreNodes()) {
                Node node = iterator.nextNode();
                Div divTag = (Div) node;
                logger.debug("div text=" + divTag.getText());
                if (divTag.getAttribute("class").equals("prd-icon")) {
                    // 解析logo
                    NodeList nodeList = divTag.searchFor(ImageTag.class, true);
                    if (nodeList.size() != 0) {
                        ImageTag imageTag = (ImageTag) nodeList.elementAt(0);
                        String logoUrl = imageTag.getImageURL();
                        logger.debug("logo url=" + logoUrl);
                        appInfo.setLogoUrl(logoUrl);
                    }

                } else if (divTag.getAttribute("class").equals("prd-qrCode app_type-hide_min")) {
                    // 解析二维码
                    NodeList nodeList = divTag.searchFor(ImageTag.class, true);
                    if (nodeList.size() != 0) {
                        ImageTag imageTag = (ImageTag) nodeList.elementAt(0);
                        String qrcodeUrl = imageTag.getImageURL();
                        logger.debug("qrcodeUrl=" + qrcodeUrl);
                        appInfo.setQrcodeUrl(qrcodeUrl);
                    }

                } else if (divTag.getAttribute("class").equals("prd-name")) {
                    // 解析应用名称
                    NodeList nodeList = divTag.searchFor(HeadingTag.class, true);
                    if (nodeList.size() != 0) {
                        HeadingTag headingTag = (HeadingTag) nodeList.elementAt(0);
                        String appName = headingTag.getStringText();
                        logger.debug("appName = " + appName);
                        appInfo.setAppName(appName);
                    }
                    // 解析更新日期
                    nodeList = divTag.getChildren();
                    ArrayList<String> textList = new ArrayList<String>();
                    for (int i = 0; i < nodeList.size(); i++) {
                        String text = nodeList.elementAt(i).toPlainTextString().trim();
                        if (text.indexOf("更新日期：") != -1) {
                            String updateTime = text.substring(5);
                            logger.debug("updateTime = " + updateTime);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                appInfo.setUpdateTime(sdf.parse(updateTime));
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                } else if (divTag.getAttribute("class").equals("prd-type_company")) {
                    NodeList nodeList = divTag.searchFor(LinkTag.class, true);
                    for (int i = 0; i < nodeList.size(); i++) {
                        LinkTag linkTag = (LinkTag) nodeList.elementAt(i);
                        if (linkTag.getAttribute("class").equals("prd-type")) {
                            // 解析类型
                            String category = linkTag.getLinkText();
                            logger.debug("category = " + category);
                            appInfo.setCategoryName(category);
                        } else if (linkTag.getAttribute("class").equals("prd-company")) {
                            // 解析作者
                            String author = linkTag.getLinkText();
                            logger.debug("author = " + author);
                            appInfo.setAuthorName(author);
                        }
                    }
                } else if (divTag.getAttribute("class").equals("prd-summary")) {
                    NodeList nodeList = divTag.searchFor(ParagraphTag.class, true);
                    for (int i = 0; i < nodeList.size(); i++) {
                        ParagraphTag summaryTag = (ParagraphTag) nodeList.elementAt(i);
                        if (i == 0) {
                            // 解析应用说明
                            String appDescription = summaryTag.getStringText();
                            System.out.println("appDescription = " + appDescription);
                            appInfo.setAppDescription(appDescription);
                        }
                        if (summaryTag.getAttribute("class") != null
                                && summaryTag.getAttribute("class").equals("prd-tag")) {
                            // 解析标签
                            NodeList linkNodeList = summaryTag.searchFor(LinkTag.class, true);
                            StringBuilder tagBuilder = new StringBuilder();
                            for (int j = 0; j < linkNodeList.size(); j++) {
                                LinkTag linkTag = (LinkTag) linkNodeList.elementAt(j);
                                if (linkTag.getLink().indexOf("/app/taglist/tag/") != -1) {
                                    String tagText = linkTag.getLinkText();
                                    logger.debug("tag = " + tagText);
                                    tagBuilder.append(tagText + "/");

                                }
                            }
                            String tags = tagBuilder.toString();
                            if (tags.length() > 0) {
                                appInfo.setTags(tags.substring(0, tags.length() - 1));
                            } else {
                                appInfo.setTags("");
                            }
                        }
                    }
                } else if (divTag.getAttribute("class").equals("row prd-images")) {
                    // 解析预览图
                    NodeList nodeList = divTag.searchFor(ImageTag.class, true);
                    for (int i = 0; i < nodeList.size(); i++) {
                        ImageTag imageTag = (ImageTag) nodeList.elementAt(i);
                        String thumbnailUrl = imageTag.getImageURL();
                        System.out.println("thumbnailUrl = " + thumbnailUrl);
                        appInfo.getThumbnailUrlList().add(thumbnailUrl);
                    }
                }
            }
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appInfo;
    }

    public static Set<String> getDestUrlSet() {
        Set<String> destUrlSet = new LinkedHashSet<String>();
        Parser parser = null;
        try {
            String pageUrl = getNextUrl();
            parser = new Parser(pageUrl);
            parser.setEncoding("utf-8");
            NodeFilter appRowFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    // TODO Auto-generated method stub
                    if (node.getText().contains("/app/detail/id/"))
                        return true;
                    return false;
                }

            };
            NodeList list = parser.parse(appRowFilter);
            SimpleNodeIterator iterator = list.elements();
            while (iterator.hasMoreNodes()) {
                Node node = iterator.nextNode();
                try {
                    if (node instanceof LinkTag) {
                        LinkTag linkTag = (LinkTag) node;
                        String href = linkTag.getLink();
                        destUrlSet.add(href);
                        logger.debug("add link to set = " + href);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return destUrlSet;
    }

    public static String getNextUrl() {
        if (currentPageId + 1 < totalPageCount) {
            currentPageId++;
            return PAGE_URL + currentPageId;
        }
        return null;
    }
}
