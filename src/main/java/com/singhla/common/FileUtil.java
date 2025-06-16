package com.singhla.common;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtil {

    // Generates a string containing a directory structure and file name for the output file
    public static String createOutputFilePath(String fileName, String fileParentPath, String operation, String ext) throws IOException {
        fileName = fileName.split("\\.")[0];
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        return fileParentPath + "\\" + fileName + "-" + operation + "-" +timeStamp + "." + ext;
    }
}
