@echo off
setlocal enabledelayedexpansion

:: Set Adobe PDF Services credentials (replace with actual credentials or set externally)
set "PDF_SERVICES_CLIENT_ID="
set "PDF_SERVICES_CLIENT_SECRET="

:MAIN_LOOP
cls
echo ===========================
echo      PDF Utility Tool     
echo ===========================

:: Set JAR path
set "JAR_PATH=target\PlayWithPDF-1.0.jar"

:: Check if the JAR exists
if not exist "%JAR_PATH%" (
    echo Error: JAR file not found at %JAR_PATH%
    pause
    goto MAIN_LOOP
)

:: Prompt for operation
echo.
echo Choose operation:
echo 1 - Combine multiple PDFs
echo 2 - Compress PDF with options (HIGH/MEDIUM/LOW)
echo 3 - Convert PDF to DOCX
set /p "OPERATION=Enter choice (1/2/3): "

:: Validate operation
if "%OPERATION%" NEQ "1" if "%OPERATION%" NEQ "2" if "%OPERATION%" NEQ "3" (
    echo Invalid choice. Try again.
    pause
    goto MAIN_LOOP
)

:: Handle Combine PDFs (Multiple Inputs)
if "%OPERATION%"=="1" (
    echo Enter absolute paths of PDF files to combine, one per line.
    echo Type DONE when finished.
    set "INPUT_FILES="
    :READ_INPUT_LOOP
    set /p "FILE_PATH=Path: "
    if /I "!FILE_PATH!"=="DONE" goto COMBINE_PDF_RUN
    if not exist "!FILE_PATH!" (
        echo [Warning] File not found: !FILE_PATH!
        goto READ_INPUT_LOOP
    )
    set "INPUT_FILES=!INPUT_FILES! "!FILE_PATH!""
    goto READ_INPUT_LOOP

    :COMBINE_PDF_RUN
    if "!INPUT_FILES!"=="" (
        echo No valid input files provided.
        pause
        goto MAIN_LOOP
    )
    echo Running: CombinePDF with files: !INPUT_FILES!
    java -cp "%JAR_PATH%" com.singhla.combinePDF.CombinePDF !INPUT_FILES!
    if errorlevel 1 (
        echo [ERROR] CombinePDF failed with exit code %ERRORLEVEL%
    )
    goto POST_OPERATION
)

:: Optional compression level for option 2
if "%OPERATION%"=="2" (
    set /p "LEVEL=Enter compression level (HIGH/MEDIUM/LOW) (Optional): "
)

:: Prompt for single PDF input for options 2 and 3
set /p "INPUT_PDF=Enter absolute path to PDF file: "
if not exist "!INPUT_PDF!" (
    echo Error: File not found at "!INPUT_PDF!"
    pause
    goto MAIN_LOOP
)

if "%OPERATION%"=="2" (
    echo Running: CompressPDFWithOptions with level "!LEVEL!"
    java -cp "%JAR_PATH%" com.singhla.CompressPDF.CompressPDFWithOptions "!INPUT_PDF!" "!LEVEL!"
    if errorlevel 1 (
        echo [ERROR] CompressPDFWithOptions failed with exit code %ERRORLEVEL%
    )
)

if "%OPERATION%"=="3" (
    echo Running: ExportPDFToDOCX
    java -cp "%JAR_PATH%" com.singhla.exportPdf.ExportPDFToDOCX "!INPUT_PDF!"
    if errorlevel 1 (
        echo [ERROR] ExportPDFToDOCX failed with exit code %ERRORLEVEL%
    )
)

:POST_OPERATION
echo.
echo Operation completed. Press any key to continue or close the window to exit.
pause
goto MAIN_LOOP

endlocal