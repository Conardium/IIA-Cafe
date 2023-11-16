package tareas;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import slot.Slot;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class Translator extends Tarea {

    private Document xmlEntrada;
    private Document xmlSalida;

    private final String Filtro;
    private final String Expresion;

    private Slot slotE;
    private Slot slotS;
    
    public Translator(String Filtro, String Expresion)
    {
        this.Filtro = Filtro;
        this.Expresion = Expresion;
        this.slotS = new Slot("TranslatorSalida");
    }
    
    @Override
    public void realizarTarea() {

        if(slotE != null){
            for (int nXML = 0; nXML < slotE.devolverNConjuntos(); nXML++) {
                getMSJslot();

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

                setMSJslot();
            }
        }
    }

    public void getMSJslot() {

        xmlEntrada = slotE.getMensaje();
    }

    public void setMSJslot() {

        slotS.setMensaje(xmlSalida);
    }

    @Override
    public void enlazarSlotE(Slot slot) {
        this.slotE = slot;
    }

    @Override
    public Slot enlazarSlotS() {
        return slotS;
    }
}
