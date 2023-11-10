package tareas;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class Translator implements ITarea {

    Document xmlEntrada;
    Document xmlSalida;

    @Override
    public void realizarTarea() {
        try{

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            XPath xPath = XPathFactory.newInstance().newXPath();

            //Pillamos el nombre
            XPathExpression expressionXpath2 = xPath.compile("//cafe_order//drink//name");
            String nameOrder = (String) expressionXpath2.evaluate(xmlEntrada, XPathConstants.STRING);

            //************************************************************************

            //Crear un documento XML
            Document xmlOut = dBuilder.newDocument();

            //El nodo con la sentenciasql
            Node sentence = xmlOut.createElement("sentence");

            //"EXIST" SERÁ UN INTEGER (0 o 1) PARA INDICAR SI HAY EXISTENCIAS
            sentence.appendChild(xmlOut.createTextNode("SELECT NAME, EXIST\n\tFROM \"Guidance4\".Bebidas\n\t" +
                    "WHERE NAME = '" + nameOrder + "';"));
            xmlOut.appendChild(sentence);

            //EJEMPLO DEL CONTENIDO DEL NUEVO DOCUMENTO XML
            /*<sentence>"SELECT name, exist FROM Bebidas \n" + "WHERE name =" + nameOrder + ";"</sentence>*/

            //Guardo en xmlSalida
            xmlSalida = xmlOut;

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void getMSJslot(Document xmlE) {
        xmlEntrada = xmlE;
    }

    @Override
    public Document setMSJslot(int v) {
        return xmlSalida;
    }
}
