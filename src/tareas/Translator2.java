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

public class Translator2 extends Tarea {

    private Document xmlEntrada, xmlSalida;

    private final String Filtro;

    private Slot slotE, slotS;

    public Translator2(String Filtro)
    {
        this.Filtro = Filtro;
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

                    //Crear un documento XML
                    Document xmlOut = dBuilder.newDocument();

                    //Cogemos todos los nodos de entrada
                    Node nodoAux = xmlOut.importNode(xmlEntrada.getFirstChild(), true);
                    xmlOut.appendChild(nodoAux);

                    Node nodoEncontrado = buscarNodoBody(xmlOut.getFirstChild(),Filtro);

                    nodoEncontrado.getParentNode().removeChild(nodoEncontrado);

                    //Guardo en xmlSalida
                    xmlSalida = xmlOut;


                }catch (Exception e){
                    e.printStackTrace();
                }

                setMSJslot();
            }
        }
    }

    private Node buscarNodoBody(Node nodoActual, String nombre) {
        // Verificar si el nodo actual es el que estamos buscando
        if (nodoActual != null && nodoActual.getNodeName().equalsIgnoreCase(nombre)) {
            return nodoActual;
        }

        // Obtener la lista de hijos del nodo actual
        Node hijo = nodoActual.getFirstChild();

        // Recorrer todos los hijos y realizar la búsqueda recursiva
        while (hijo != null) {
            Node nodoEncontrado = buscarNodoBody(hijo, nombre);
            if (nodoEncontrado != null) {
                return nodoEncontrado; // Devolver el nodo si se encuentra
            }
            hijo = hijo.getNextSibling(); // Pasar al siguiente hijo
        }

        return null; // Devolver null si no se encuentra el nodo
    }

    @Override
    protected void getMSJslot() {

        xmlEntrada = slotE.getMensaje();
    }
    @Override
    protected void setMSJslot() {

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
