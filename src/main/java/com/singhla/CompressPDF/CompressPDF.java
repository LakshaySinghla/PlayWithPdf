/*
 * Copyright 2025 Singhla
 * All Rights Reserved.
 */

package com.singhla.CompressPDF;

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
import com.adobe.pdfservices.operation.pdfjobs.jobs.CompressPDFJob;
import com.adobe.pdfservices.operation.pdfjobs.result.CompressPDFResult;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.singhla.common.FileUtil.createOutputFilePath;

/**
 * This sample illustrates how to compress PDF by reducing the size of the PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CompressPDF {

    public static void main(String[] args) {

        // Validate arguments
        if (args.length < 1) {
            System.err.println("Usage: java -cp PlayWithPDF-1.0.jar com.singhla.CompressPDF.CompressPDF <input_pdf_path>");
            System.exit(1);
        }

        String inputPdfPath = args[0];

        // Validate input file
        File inputFile = new File(inputPdfPath);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.err.println("Error: Input file does not exist or is not a valid file: " + inputPdfPath);
            System.exit(1);
        }

        String fileName = inputFile.getName();
        Path filePath = inputFile.toPath().toAbsolutePath();

        try (InputStream inputStream = Files.newInputStream(filePath)) {
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
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());

            // Creates a new job instance
            CompressPDFJob compressPDFJob = new CompressPDFJob(asset);

            // Submit the job and gets the job result
            String location = pdfServices.submit(compressPDFJob);
            PDFServicesResponse<CompressPDFResult> pdfServicesResponse = pdfServices.getJobResult(location, CompressPDFResult.class);

            // Get content from the resulting asset(s)
            Asset resultAsset = pdfServicesResponse.getResult().getAsset();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);

            // Creating an output stream and copying stream asset content to it
            String outputFilePath = createOutputFilePath(fileName, filePath.getParent().toString(), "compress", "pdf");
            System.out.println("Saving asset at " + outputFilePath);

            OutputStream outputStream = Files.newOutputStream(new File(outputFilePath).toPath());
            IOUtils.copy(streamAsset.getInputStream(), outputStream);
            outputStream.close();
        } catch (ServiceApiException | IOException | SDKException | ServiceUsageException ex) {
            System.out.println("Exception encountered while executing operation " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
