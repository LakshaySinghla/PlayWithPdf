package com.singhla;

import com.singhla.CompressPDF.CompressPDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompressPDF.class);

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/CompressPDF"));
        return ("output/CompressPDF/compress" + timeStamp + ".pdf");
    }
}