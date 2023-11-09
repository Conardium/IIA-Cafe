package conectores;

import org.w3c.dom.Document;
import puertos.Puerto;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import org.w3c.dom.Element;

public class ConectorCamarero extends Conector {

    Puerto puerto = new Puerto();
    String xmlFileName = String.valueOf(puerto.getPuerto());

    String docfinal;

    public void convertirXMLtoString() {
        try {
            File archivoXML = new File(xmlFileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;

            builder = factory.newDocumentBuilder();

            Document document = null;
            document = builder.parse(archivoXML);


            Element rootElement = document.getDocumentElement();
            NodeList childNodes = rootElement.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String tagName = element.getTagName();
                    String textContent = element.getTextContent();
                    docfinal = docfinal + textContent;
                }
            }
            CargarBD();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean CargarBD(){


    return true;
    }
}