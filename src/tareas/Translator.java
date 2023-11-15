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

    private Document xmlEntrada;
    private Document xmlSalida;
    private String Filtro;
    private String Expresion;
    
    public Translator(String Filtro, String Expresion)
    {
        this.Filtro = Filtro;
        this.Expresion = Expresion;
    }
    
    @Override
    public void realizarTarea() {
        try{

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            XPath xPath = XPathFactory.newInstance().newXPath();
            
            //************************************************************************//
            //**********UN TRADUCTOR DEBE SER CONFIGURADO PARA CADA PROYECTO**********//
            //************************************************************************//
            
            //Pillamos el nombre
            XPathExpression expressionXpath2 = xPath.compile(Filtro);
            String nameOrder = (String) expressionXpath2.evaluate(xmlEntrada, XPathConstants.STRING);

            //Crear un documento XML
            Document xmlOut = dBuilder.newDocument();

            //El nodo con la sentenciasql
            Node sentence = xmlOut.createElement("sentence");

            //"EXIST" SERÁ UN INTEGER (0 o 1) PARA INDICAR SI HAY EXISTENCIAS
            sentence.appendChild(xmlOut.createTextNode(Expresion + nameOrder + "';"));
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
    
    @Override
    public int calcularSalidas() {
        return 0;
    }
}
