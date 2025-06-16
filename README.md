# PlayWithPdf

## What's This About?
This project is designed to provide a simple interface for manipulating PDF files using JAR files, which include different functionalities such as compressing PDF files with varying levels of compression and converting PDF files to DOCX documents.

## Project Setup
Environment Preparation: Ensure that the run.bat script is placed in the root directory of the project (e.g., target folder or any specific directory defined in the script).
### Run the Script:
1. Open the command prompt.
2. Navigate to the directory where the run.bat file is located (e.g., cd target).
3. Run the script by entering the text: run.bat.
### Select an Operation:
When prompted, choose the number that corresponds to the operation you want to perform:
1 for Compress PDF
2 for Compress PDF with Options
3 for Export PDF to DOCX
### Additional Option 2:
If you choose operation 2, you can specify a compression level:
HIGH: The PDF file is compressed to the maximum extent.
MEDIUM: The PDF file is moderately compressed.
LOW: The PDF file is minimally compressed.
### Output: For each operation, the result will be displayed on the screen. Afterward, the terminal will keep the output window open to let you review the results.
### Completion: Press any key to continue, and the terminal window will close.
## Troubleshooting
Ensure that your environment is configured correctly to avoid any permission errors.
Check if you are using valid paths for the JAR files. The paths provided in the script (target\PlayWithPDF-1.0.jar) and related files should work correctly if those are accessible from the terminal.
## Useful Links
This script assumes that the .jar file necessary for performing the desired operations is available. If the .jar files are missing or improperly configured, it's essential to provide proper paths or adjust the scripts accordingly.

Note: The compression levels are truncated to prevent malicious input, and any specified compression level not matching HIGH, MEDIUM, or LOW will result in an invalid operation message.

## How This Project Works
This project utilizes Java to interact with the Adobe PDF Services and other PDF manipulation utilities via jar files. By setting up a straightforward interface with the .bat file, users can easily perform the necessary PDF manipulations. The script ensures that all files required for the operations are available and properly referenced, aiding in a smooth process of using the project functionalities.