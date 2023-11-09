/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tareas;

import org.w3c.dom.Document;

import java.util.ArrayList;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

/**
 *
 * @author Cristian
 */
public class Aggregator implements ITarea{

    ArrayList<Document> xmlEntrada = new ArrayList<>();
    ArrayList<Document> xmlUnir = new ArrayList<>();
    Document xmlSalida;

    @Override
    public void realizarTarea() {

        boolean encontrado = false;
        int contador = 0;
        int contadorXMLs = 1;
        String id_Unir = "0";

        //Intentamos recoger todos los XML que sean del mismo id hasta su tamaño
        while(contador < xmlEntrada.size() && encontrado == false){//Mientras el contador sea menor a todos los que hay
            xmlUnir = new ArrayList<>();//Reinicio la lista

            //meto el primero en la lista y pillo su id y el numero de trozos
            xmlUnir.add(xmlEntrada.get(contador));
            int id = Integer.parseInt(xmlEntrada.get(contador).getFirstChild().getFirstChild().getTextContent());
            int num = Integer.parseInt(xmlEntrada.get(contador)
                    .getFirstChild().getChildNodes().item(1).getTextContent());
            id_Unir = String.valueOf(id);//Para la cabecera

            //A partir del siguiente empezamos a buscar
            int j = contador+1;
            //Si el contadorXML es igual al numero pues salimos, o si nos quedamos sin entrada
            while (j < xmlEntrada.size() && contadorXMLs < num) {
                //Si es del mismo id lo agregamos
                if (Integer.parseInt(xmlEntrada.get(j).getFirstChild().getFirstChild().getTextContent()) == id) {
                    xmlUnir.add(xmlEntrada.get(j));
                    contadorXMLs++;
                }
                j++;
            }
            //Encontro un conjunto bueno
            if(contadorXMLs == num){
                encontrado = true;
            }
            //Sigo buscando
            contador++;
        }

        if (encontrado) {
            try {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                XPath xPath = XPathFactory.newInstance().newXPath();

                //Crear un documento XML
                Document xmlOut = dBuilder.newDocument();

                //Crear un elemento Padre
                Node NodoPadre = xmlOut.createElement("cafe_order");
                xmlOut.appendChild(NodoPadre);
                //Mi contexto: el ID
                Node id = xmlOut.createElement("order_id");
                id.appendChild(xmlOut.createTextNode(id_Unir));
                NodoPadre.appendChild(id);
                //Mi contexto: drinks
                Node drinks = xmlOut.createElement("drinks");
                NodoPadre.appendChild(drinks);

                //Pillamos todos los Nodos
                for (int i = 0; i < contadorXMLs; i++) {//FALLA
                    Node nodoAux = (Node) xPath.compile("//cafe_order//drinks//drink").evaluate(xmlUnir.get(0), XPathConstants.NODE);
                    Node nodo = xmlOut.importNode(nodoAux.getChildNodes().item(i), true);
                    drinks.appendChild(nodo);
                    //Borra del entrada
                    xmlEntrada.remove(xmlUnir.get(i));
                }

                xmlSalida = xmlOut;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getMSJslot(Document xmlE) {

        xmlEntrada.add(xmlE);
    }

    @Override
    public Document setMSJslot(int v) {

        return xmlSalida;
    }
    
    public int devolverNConjuntos()
    {
        return xmlEntrada.size();
    }
}
