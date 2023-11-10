/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conectores;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 *
 * @author Cristian
 */
public class ConectorComandas extends Conector {

    int id=0;
    String Mensaje = "";

    /*public ConectorComandas() {

        String direccionAux = System.getProperty("user.dir") + "\\src\\comandas\\order";
        System.out.println(direccionAux);

        for (int i = 1; i <= 9; i++) {
            try {
                File ficheroComandas = new File(direccionAux + i + ".xml");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document MensajeXML = builder.parse(ficheroComandas);
                xmlFiles.add(MensajeXML);

            } catch (Exception ex) {
                System.out.println("Error en el inicio de ConectorComandas");
            }

        }
    }

    public void TransformarStringXML(){

        try {
        ///Crear un documento XML
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document xmlOut = dBuilder.parse(new org.xml.sax.InputSource(new java.io.StringReader(Mensaje)));

        StringToXML(xmlOut);

        xmlFiles.add(xmlOut);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Document leerMensaje() {

        return xmlFiles.remove(0);
    }


    private static void StringToXML(Node node) {
        NodeList childNodes = node.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                StringToXML(childNode);
            }
        }

        if (node instanceof Element) {
            ((Element) node).normalize();
        }
    }

}
