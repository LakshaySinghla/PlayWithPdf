/*
 * Copyright 2025 Singhla
 * All Rights Reserved.
 */

package com.singhla.combinePDF;

import com.adobe.pdfservices.operation.PDFServices;
import com.adobe.pdfservices.operation.PDFServicesMediaType;
import com.adobe.pdfservices.operation.PDFServicesResponse;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.auth.ServicePrincipalCredentials;
import com.adobe.pdfservices.operation.exception.SDKException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.Asset;
import com.adobe.pdfservices.operation.io.StreamAsset;
import com.adobe.pdfservices.operation.pdfjobs.jobs.CombinePDFJob;
import com.adobe.pdfservices.operation.pdfjobs.params.combinepdf.CombinePDFParams;
import com.adobe.pdfservices.operation.pdfjobs.result.CombinePDFResult;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.singhla.common.FileUtil.createOutputFilePath;

/**
 * This sample illustrates how to combine multiple PDF files into a single PDF file.
 * <p>
 * Note that the SDK supports combining upto 20 files in one operation.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CombinePDF {

    public static void main(String[] args) {

        // Validate arguments
        if (args.length < 1) {
            System.err.println("Usage: java -cp PlayWithPDF-1.0.jar com.singhla.combinePDF.CombinePDF <input_pdf_paths>");
            System.exit(1);
        }

        List<String> inputPdfPaths = Arrays.asList(args);
        List<String> fileNames = new ArrayList<>();
        List<Path> filePaths = new ArrayList<>();


        // Validate input file
        for (String inputPath : inputPdfPaths) {
            File inputFile = new File(inputPath);
            if (!inputFile.exists() || !inputFile.isFile()) {
                System.err.println("Error: Input file does not exist or is not a valid file: " + inputPath);
                System.exit(1);
            }
            fileNames.add(inputFile.getName());
            filePaths.add(inputFile.toPath().toAbsolutePath());
        }

        try {
            List<InputStream> inputStreams = new ArrayList<>();
            for (Path filePath : filePaths) {
                inputStreams.add(Files.newInputStream(filePath));
            }

            // Load credentials from environment variables
            String clientId = System.getenv("PDF_SERVICES_CLIENT_ID");
            String clientSecret = System.getenv("PDF_SERVICES_CLIENT_SECRET");

            if (clientId == null || clientSecret == null) {
                System.err.println("Error: Environment variables PDF_SERVICES_CLIENT_ID and/or PDF_SERVICES_CLIENT_SECRET are not set.");
                System.exit(1);
            }

            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(clientId, clientSecret);

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            List<StreamAsset> streamAssets = inputStreams.stream()
                    .map(inputStream -> new StreamAsset(inputStream, PDFServicesMediaType.PDF.getMediaType()))
                    .collect(Collectors.toList());
            List<Asset> assets = pdfServices.uploadAssets(streamAssets);

            // Create parameters for the job
            CombinePDFParams.Builder combinePdfParamsBuilder = CombinePDFParams.combinePDFParamsBuilder();
            assets.forEach(combinePdfParamsBuilder::addAsset);
            CombinePDFParams combinePDFParams = combinePdfParamsBuilder.build();

            // Creates a new job instance
            CombinePDFJob combinePDFJob = new CombinePDFJob(combinePDFParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(combinePDFJob);
            PDFServicesResponse<CombinePDFResult> pdfServicesResponse = pdfServices.getJobResult(location, CombinePDFResult.class);

            // Get content from the resulting asset(s)
            Asset resultAsset = pdfServicesResponse.getResult().getAsset();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);

            // Creates an output stream and copy stream asset's content to it
            String outputFilePath = createOutputFilePath(fileNames.get(0), filePaths.get(0).getParent().toString(), "combine-"+filePaths.size(), "pdf");
            System.out.println("Saving asset at " + outputFilePath);

            OutputStream outputStream = Files.newOutputStream(new File(outputFilePath).toPath());
            IOUtils.copy(streamAsset.getInputStream(), outputStream);
            outputStream.close();
        } catch (IOException | ServiceApiException | SDKException | ServiceUsageException e) {
            System.out.println("Exception encountered while executing operation " + e.getMessage());
            e.printStackTrace();
        }
    }
}
