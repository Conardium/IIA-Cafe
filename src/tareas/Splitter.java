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
import java.util.ArrayList;
import org.w3c.dom.Document;

public class Splitter implements ITarea{

    Document xmlEntrada;
    ArrayList<Document> xmlSalida = new ArrayList<>();

    @Override
    public void getMSJslot(Document xmlE){

        xmlEntrada = xmlE;
    }

    @Override
    public Document setMSJslot(){

        return xmlSalida.remove(0);
    }

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
            NodeList hotNodeList = (NodeList) xPath.compile("//cafe_order//drinks//drink[type='hot']").evaluate(xmlEntrada, XPathConstants.NODESET);
            
            //Hot
            for (int i = 0; i < hotNodeList.getLength(); i++) {

                //Crear un documento XML
                Document xmlOut = dBuilder.newDocument();

                Node nodoHot = xmlOut.importNode(hotNodeList.item(i), true);

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
            NodeList coldNodeList = (NodeList) xPath.compile("//cafe_order//drinks//drink[type='cold']").evaluate(xmlEntrada, XPathConstants.NODESET);
            
            //Cold
            for (int i = 0; i < coldNodeList.getLength(); i++) {

                //Crear un documento XML
                Document xmlOut = dBuilder.newDocument();

                Node nodoCold = xmlOut.importNode(coldNodeList.item(i), true);

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

    public int devolverNConjuntos()
    {
        //System.out.println(xmlSalida.size());
        return xmlSalida.size();
    }

}
