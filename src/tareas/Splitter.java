/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tareas;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.ArrayList;

public class Splitter implements ITarea{

    Document xmlEntrada;
    ArrayList<Document> xmlSalida;

    @Override
    public void realizarTarea() {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            XPath xPath = XPathFactory.newInstance().newXPath();

            //Pillamos la orden
            XPathExpression expression = xPath.compile("//cafe_order//order_id");
            String numOrder = (String) expression.evaluate(xmlEntrada, XPathConstants.STRING);

            //Pillamos todos los nodo hot
            NodeList hotNodeList = (NodeList) xPath.compile("//cafe_order//drinks//drink[@type='hot']//drinks").evaluate(xmlEntrada, XPathConstants.NODESET);
            
            //Hot
            for (int i = 0; i < hotNodeList.getLength(); i++) {
                Node nodoHot = hotNodeList.item(i);

                //Crear un documento XML
                Document xmlOut = dBuilder.newDocument();

                //Creo una nuevo Nodo que va a ser mi cabecera
                Node NodoPadre = xmlOut.createElement("cafe_order");
                
                //Mi contexto: el ID
                Node id = xmlOut.createElement("order_id");
                id.appendChild(xmlOut.createTextNode(numOrder));
                NodoPadre.appendChild(id);

                //Pongo el hijo hot
                NodoPadre.appendChild(nodoHot);

                //Guardo el xmlSalida
                xmlSalida.add(xmlOut);
            }
            
            //Pillamos todos los nodo cold
            NodeList coldNodeList = (NodeList) xPath.compile("//cafe_order//drinks//drink[@type='cold']//drinks").evaluate(xmlEntrada, XPathConstants.NODESET);
            
            //Cold
            for (int i = 0; i < coldNodeList.getLength(); i++) {
                Node nodoCold = coldNodeList.item(i);

                //Crear un documento XML
                Document xmlOut = dBuilder.newDocument();

                //Creo un Nuevo nodo que va a ser mi cabecera
                Node NodoPadre = xmlOut.createElement("cafe_order");
                
                //Mi contexto: el ID
                Node id = xmlOut.createElement("order_id");
                id.appendChild(xmlOut.createTextNode(numOrder));
                NodoPadre.appendChild(id);

                //Pongo el hijo hot
                NodoPadre.appendChild(nodoCold);

                //Guardo el xmlSalida
                xmlSalida.add(xmlOut);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
