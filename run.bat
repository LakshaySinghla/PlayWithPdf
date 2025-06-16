@echo off
setlocal enabledelayedexpansion

:: Set Adobe PDF Services credentials (replace with actual credentials or set externally)
set "PDF_SERVICES_CLIENT_ID="
set "PDF_SERVICES_CLIENT_SECRET="


:: Prompt for PDF path
set /p "INPUT_PDF=Enter absolute path to PDF file: "
if not exist "!INPUT_PDF!" (
    echo Error: File not found at "!INPUT_PDF!"
    exit /b 1
)

:: Prompt for operation
echo.
echo Choose operation:
echo 1 - Compress PDF
echo 2 - Compress PDF with options (HIGH/MEDIUM/LOW)
echo 3 - Convert PDF to DOCX
set /p "OPERATION=Enter choice (1/2/3): "

:: Validate operation
if "%OPERATION%" NEQ "1" if "%OPERATION%" NEQ "2" if "%OPERATION%" NEQ "3" (
    echo Invalid choice. Exiting.
    exit /b 1
)

:: Optional compression level for option 2
if "%OPERATION%"=="2" (
    set /p "LEVEL=Enter compression level (HIGH/MEDIUM/LOW): "
    set "LEVEL=!LEVEL:~0,5!"  :: truncate to prevent malicious inputs
    set "LEVEL=!LEVEL: =!"    :: remove any spaces
)

:: Set JAR path
set "JAR_PATH=target\PlayWithPDF-1.0.jar"

:: Check if the JAR exists
if not exist "%JAR_PATH%" (
    echo Error: JAR file not found at %JAR_PATH%
    exit /b 1
)

:: Run based on choice
if "%OPERATION%"=="1" (
    echo Running: CompressPDF
    java -cp "%JAR_PATH%" com.singhla.CompressPDF.CompressPDF "!INPUT_PDF!"
)

if "%OPERATION%"=="2" (
    echo Running: CompressPDFWithOptions with level "!LEVEL!"
    java -cp "%JAR_PATH%" com.singhla.CompressPDF.CompressPDFWithOptions "!INPUT_PDF!" "!LEVEL!"
)

if "%OPERATION%"=="3" (
    echo Running: ExportPDFToDOCX
    java -cp "%JAR_PATH%" com.singhla.exportPdf.ExportPDFToDOCX "!INPUT_PDF!"
)

pause
endlocal
