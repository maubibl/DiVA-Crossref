import sys
import subprocess
import requests
from lxml import etree
from datetime import datetime

# Load the XML and XSLT files
xml = etree.parse('export.xml')
xslt = etree.parse('DiVA-CrossRef.xslt')

# Get the current date and time in the desired format: YYYYMMDDHHMMSS000
current_timestamp = datetime.now().strftime('%Y%m%d%H%M%S') + '000'

# Pass the timestamp as a parameter to the XSLT transformation
transform = etree.XSLT(xslt)
result = transform(xml, currentDateTime=etree.XSLT.strparam(current_timestamp))

# Save the transformed XML to a file
transformed_filename = 'doireg.xml'

with open(transformed_filename, 'wb') as f:
    f.write(etree.tostring(result, pretty_print=True, xml_declaration=True, encoding='UTF-8'))

# Print success message
print(f"Transformation completed successfully! File saved as {transformed_filename}")

# Upload the file to CrossRef using crossref-upload-tool.jar
username = "your username"  # Replace with your CrossRef username
password = "your password"  # Replace with your CrossRef password
jar_path = "crossref-upload-tool.jar"  # Path to the JAR file

try:
    subprocess.run(
        [
            "java", "-jar", jar_path,
            "--user", username, password,
            "--metadata", transformed_filename
        ],
        check=True
    )
    print("File uploaded to CrossRef successfully!")
except subprocess.CalledProcessError as e:
    print(f"Error during upload: {e}")