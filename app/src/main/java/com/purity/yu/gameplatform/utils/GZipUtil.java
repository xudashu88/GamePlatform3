package com.purity.yu.gameplatform.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtil {
    private static GZipUtil instance = null;

    private GZipUtil() {
    }

    public static GZipUtil getInstance() {
        if (null == instance) {
            instance = new GZipUtil();
        }
        return instance;
    }

    public static void zip(File srcFile, File desFile) throws IOException {
        GZIPOutputStream zos = null;
        FileInputStream fis = null;
        try {
//创建压缩输出流,将目标文件传入
            zos = new GZIPOutputStream(new FileOutputStream(desFile));
//创建文件输入流,将源文件传入
            fis = new FileInputStream(srcFile);
            byte[] buffer = new byte[1024];
            int len = -1;
//利用IO流写入写出的形式将源文件写入到目标文件中进行压缩
            while ((len = (fis.read(buffer))) != -1) {
                zos.write(buffer, 0, len);
            }
        } finally {
            close(zos);
            close(fis);
        }
    }

    private static void close(GZIPOutputStream zos) {
        if (zos != null) {
            try {
                zos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(FileInputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void unZip(File srcFile, File desFile) throws IOException {
        GZIPInputStream zis = null;
        FileOutputStream fos = null;
        try {
//创建压缩输入流,传入源文件
            zis = new GZIPInputStream(new FileInputStream(srcFile));
//创建文件输出流,传入目标文件
            fos = new FileOutputStream(desFile);
            byte[] buffer = new byte[1024];
            int len = -1;
//利用IO流写入写出的形式将压缩源文件解压到目标文件中
            while ((len = (zis.read(buffer))) != -1) {
                fos.write(buffer, 0, len);
            }
        } finally {
            close(zis);
            close(fos);
        }
    }

    private static void close(GZIPInputStream zos) {
        if (zos != null) {
            try {
                zos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(FileOutputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {

    }
}
