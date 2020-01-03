package cn.easybuy.util;

import java.io.File;
import java.util.UUID;

public class FileUpLoadUtil {
    /**
     * 文件上传路径
     */
//    public static final String path = "/usr/local/smbms/upload";
    public static final String PATH = "D:"+ File.separator+"upload";

    /**
     * 单个文件上传大小5MB
     */
    public static final int FILE_SIZE = 5000000;

    public static String getFileName(String suffix){
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        return fileName + "." + suffix;
    }
}
