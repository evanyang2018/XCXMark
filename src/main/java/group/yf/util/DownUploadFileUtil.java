package com.zap.miniapp.util;

import com.zap.component.storage.StorageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 上传文件    工具类
 *
 * @author liu.na
 */
public class DownUploadFileUtil {
    public static String uploadImage(StorageService storageService, MultipartFile file) {
        String storagePath = null;
        try {
            storagePath = storageService.saveImage(file.getBytes(), file.getOriginalFilename());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return storagePath;     //相对路径
    }

    public static String uploadPotrait(StorageService storageService, MultipartFile file) {
        String storagePath = null;
        try {
            storagePath = storageService.savePortrait(file.getBytes(), file.getOriginalFilename());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return storagePath;     //相对路径
    }

    public static String downLoadImageUrl(String url, StorageService storageService) {
        byte[] btImg = getImageFromNetByUrl(url);
        if (null != btImg && btImg.length > 0) {
            System.out.println("读取到：" + btImg.length + " 字节");
            String fileName = "qw.png";
            String imageUrl = writeImageToDisk(btImg, fileName, storageService);
            ImgUtil mypic = new ImgUtil();
            mypic.compressPic("/storage/xiaocheng/repos/" + imageUrl);
            return imageUrl;
        } else {
            System.out.println("没有从该连接获得内容");
            return null;
        }
    }

    /**
     * 将图片写入到磁盘
     *
     * @param img      图片数据流
     * @param fileName 文件保存时的名称
     */
    public static String writeImageToDisk(byte[] img, String fileName, StorageService storageService) {
        try {
            String imageUrl = storageService.savePortrait(img, fileName);
            System.out.println("图片已经写入到磁盘盘");
            return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}
