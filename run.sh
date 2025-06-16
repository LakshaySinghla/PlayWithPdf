#!/bin/bash

# Set Adobe PDF Services credentials
export "PDF_SERVICES_CLIENT_ID="
export "PDF_SERVICES_CLIENT_SECRET="

# Prompt for PDF path
read -p "Enter absolute path to PDF file: " INPUT_PDF
if [ ! -f "$INPUT_PDF" ]; then
    echo "Error: File not found at $INPUT_PDF"
    exit 1
fi

# Validate the file path is good
if [ -z "$INPUT_PDF" ]; then
    echo "Error: File path is empty"
    exit 1
fi

# Set JAR path
JAR_PATH=target/PlayWithPDF-1.0.jar

# Check if the JAR file exists
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JAR file not found at $JAR_PATH"
    exit 1
fi

# Run the specified script
case $1 in
    1)
        java -cp "$JAR_PATH" com.singhla.CompressPDF.CompressPDF "$INPUT_PDF"
        ;;
    2)
        LEVEL="HIGH"
        java -cp "$JAR_PATH" com.singhla.CompressPDF.CompressPDFWithOptions "$INPUT_PDF" "$LEVEL"
        ;;
    3)
        java -cp "$JAR_PATH" com.singhla.exportPdf.ExportPDFToDOCX "$INPUT_PDF"
        ;;
esac

# Pause to prevent immediate termination
read -p "Press any key to continue... " key
exit 0