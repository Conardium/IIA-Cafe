package tareas;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import slot.Slot;

public class Content_Enricher extends Tarea {

    private Slot slotEBody;
    private Slot slotEContex;
    private Slot slotS;

    private Document xmlEntradaContext;
    private Document xmlEntradaBody;
    private Document xmlSalida;
    
    private final String FiltroContexto;
    private final String FiltroBody;

    public Content_Enricher(String FiltroContexo, String FiltroBody) {
        this.FiltroContexto = FiltroContexo;
        this.FiltroBody = FiltroBody;
        this.slotS = new Slot("Content_EnricherSalida");
    }
    

    @Override
    public void realizarTarea() {

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
            Node nodoPadreBuscado = buscarNodoBody(nodoAux,nombreNodo[1]);
            // Importamos el nodo del contexto
            Node nodoHijoBuscado = xmlOut.importNode(nodoAnadir, true);
            // Lo hacemos hijo
            nodoPadreBuscado.appendChild(nodoHijoBuscado);

            //Lo colocamos en la salida
            xmlSalida = xmlOut;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static Node buscarNodoBody(Node nodoActual, String nombre) {
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
    public void getMSJslot(Document xmlE) {

        if ("result".equals(xmlE.getFirstChild().getNodeName())) {
            xmlEntradaContext = xmlE;
        } else {
            xmlEntradaBody = xmlE;
        }
    }

    @Override
    public Document setMSJslot(int v) {

        return xmlSalida;
    }
    
    @Override
    public int calcularSalidas() {
        return 0;
    }

    @Override
    public Slot enlazarSlotS() {
        return slotS;
    }

    public void enlazarSlotEContext(Slot slot) {
        this.slotEContex = slot;
    }
    public void enlazarSlotEBody(Slot slot) {
        this.slotEBody = slot;
    }

}
