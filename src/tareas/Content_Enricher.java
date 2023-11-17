package tareas;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import slot.Slot;

public class Content_Enricher extends Tarea {

    private Slot slotEContext, slotEBody, slotS;

    private Document xmlEntradaContext, xmlEntradaBody, xmlSalida;
    
    private final String FiltroContexto, FiltroBody;

    public Content_Enricher(String FiltroContexo, String FiltroBody) {
        this.FiltroContexto = FiltroContexo;
        this.FiltroBody = FiltroBody;
        this.slotS = new Slot("Content_EnricherSalida");
    }
    

    @Override
    public void realizarTarea() {
        if (slotEContext != null && slotEBody != null) {
            for (int nXML = 0; nXML < slotEContext.devolverNConjuntos(); nXML++) {

                getMSJslot();

                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    XPath xPath = XPathFactory.newInstance().newXPath();

                    // Evaluar la expresión XPath para obtener el nodo del contexto
                    XPathExpression expr = xPath.compile(FiltroContexto);
                    Node nodoAnadir = (Node) expr.evaluate(xmlEntradaContext, XPathConstants.NODE);

                    //Creamos nuevo documento XML
                    Document xmlOut = dBuilder.newDocument();

                    //Cogemos todos los nodos del Body
                    Node nodoAux = xmlOut.importNode(xmlEntradaBody.getFirstChild(), true);
                    xmlOut.appendChild(nodoAux);

                    String nombreNodo[] = FiltroBody.split("//");

                    // Busqueda del nodo del que será hijo
                    Node nodoPadreBuscado = buscarNodoBody(nodoAux, nombreNodo[1]);
                    // Importamos el nodo del contexto
                    Node nodoHijoBuscado = xmlOut.importNode(nodoAnadir, true);
                    // Lo hacemos hijo
                    nodoPadreBuscado.appendChild(nodoHijoBuscado);

                    //Lo colocamos en la salida
                    xmlSalida = xmlOut;

                } catch (Exception e) {
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
    public void getMSJslot() {

        xmlEntradaBody = slotEBody.getMensaje();
        xmlEntradaContext = slotEContext.getMensaje();
    }
    @Override
    public void setMSJslot() {

        slotS.setMensaje(xmlSalida);
    }

    @Override
    public Slot enlazarSlotS() {
        return slotS;
    }

    public void enlazarSlotEContext(Slot slot) {
        this.slotEContext = slot;
    }
    public void enlazarSlotEBody(Slot slot) {
        this.slotEBody = slot;
    }

}
