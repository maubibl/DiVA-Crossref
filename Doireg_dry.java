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

public class Doireg_dry {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}