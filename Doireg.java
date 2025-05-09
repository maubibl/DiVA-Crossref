import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

public class Doireg {
    public static void main(String[] args) {
        try {
            // Paths to input and output files
            String xmlInputPath = "export.xml";
            String xsltPath = "DiVA-CrossRef.xslt";
            String transformedOutputPath = "doiregjava.xml";

            // Clean the input XML to remove any content outside the root element
            System.out.println("Cleaning input XML...");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlInputPath));

            // Write the cleaned XML to a temporary file
            File cleanedXmlFile = new File("cleaned_export.xml");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer identityTransformer = transformerFactory.newTransformer();
            identityTransformer.transform(new DOMSource(document), new StreamResult(cleanedXmlFile));

            // Generate the current timestamp in the format YYYYMMDDHHMMSS000
            String currentTimestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "000";

            // Transform the cleaned XML using the XSLT
            System.out.println("Transforming XML...");
            Transformer xsltTransformer = transformerFactory.newTransformer(new StreamSource(new File(xsltPath)));

            // Pass the current timestamp as a parameter to the XSLT
            xsltTransformer.setParameter("currentDateTime", currentTimestamp);

            // Perform the transformation
            xsltTransformer.transform(
                new StreamSource(cleanedXmlFile),
                new StreamResult(new File(transformedOutputPath))
            );

            System.out.println("Transformation completed successfully! File saved as " + transformedOutputPath);

            // Upload the transformed file to CrossRef using crossref-upload-tool.jar
            String username = "your username"; // Replace with your CrossRef username
            String password = "your password"; // Replace with your CrossRef password
            String jarPath = "crossref-upload-tool.jar"; // Path to the JAR file

            System.out.println("Uploading file to CrossRef...");
            ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", jarPath,
                "--user", username, password,
                "--metadata", transformedOutputPath
            );

            // Redirect output and error streams
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("File uploaded to CrossRef successfully!");
            } else {
                System.err.println("Error during upload. Exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}