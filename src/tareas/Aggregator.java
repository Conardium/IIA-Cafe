package tareas;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.*;
import slot.Slot;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

/**
 *
 * @author Cristian
 */
public class Aggregator extends Tarea {

    private ArrayList<Document> xmlEntrada = new ArrayList<>();
    private ArrayList<Document> xmlUnir = new ArrayList<>();
    private Document xmlSalida;

    private String Expresion;
    private String Filtro;
    private Slot slotE;
    private Slot slotS;

    public Aggregator(String Expresion, String Filtro) {

        this.Expresion = Expresion;
        this.Filtro = Filtro;
        this.slotS = new Slot("AggregatorSalida");
    }

    @Override
    public void realizarTarea() {

        boolean encontrado = false;
        int contador = 0;
        int contadorXMLs = 1;
        String id_Unir = "0";
        XPath xPath = XPathFactory.newInstance().newXPath();

        //Intentamos recoger todos los XML que sean del mismo id hasta su tamaño
        while (contador < xmlEntrada.size() && encontrado == false) {
            try {
                //Mientras el contador sea menor a todos los que hay
                xmlUnir = new ArrayList<>();//Reinicio la lista
                contador = 0;
                contadorXMLs = 1;

                //Meto el primero en la lista y pillo su id y el numero de trozos
                xmlUnir.add(xmlEntrada.get(contador));

                // Evaluar la expresión XPath para obtener el id (o algo que lo identifique)
                XPathExpression expr = xPath.compile(Filtro);
                double id = (double) expr.evaluate(xmlEntrada.get(contador), XPathConstants.NUMBER);
                // Evaluar la expresión XPath para obtener el numero de elementos
                expr = xPath.compile("//size/text()");
                double size = (double) expr.evaluate(xmlEntrada.get(contador), XPathConstants.NUMBER);

                //Para la cabecera
                id_Unir = String.valueOf(id);

                //A partir del siguiente empezamos a buscar
                int j = contador + 1;
                //Si el contadorXML es igual al numero pues salimos, o si nos quedamos sin entrada
                while (j < xmlEntrada.size() && contadorXMLs < size) {
                    //Si es del mismo id lo agregamos
                    if (Integer.parseInt(xmlEntrada.get(j).getFirstChild().getFirstChild().getTextContent()) == id) {
                        xmlUnir.add(xmlEntrada.get(j));
                        contadorXMLs++;
                    }
                    j++;
                }
                //Encontro un conjunto bueno
                if (contadorXMLs == size) {

                    encontrado = true;
                }
                //Sigo buscando
                contador++;
            } catch (XPathExpressionException ex) {
                Logger.getLogger(Aggregator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (encontrado) {
            try {

                //Pillamos el primer nodo y lo hacemos padre, luego, simplemente cogemos los nodos drink: "//drinks//drink" pasado por parametro
                //y lo añadimos al nodo drinks (buscamos recursivamente) 
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                //Crear un documento XML
                Document xmlOut = dBuilder.newDocument();
                
                //Cogemos todos los nodos del primer elemento
                Node nodoAux = xmlOut.importNode(xmlUnir.get(0).getFirstChild(), true);
                xmlOut.appendChild(nodoAux);
                
                //Buscamos el nodo padre del hijo que troceamos
                String [] NodoBuscar = Expresion.split("//");
                String NombreBuscar = NodoBuscar[1];
                Node Padre = buscarNodoBody(nodoAux,NombreBuscar);
                
                //Buscamos el nodo size para borrarlo
                String SizeBuscar = "size";
                Node size = buscarNodoBody(nodoAux,SizeBuscar);
                size.getParentNode().removeChild(size);
                
                xmlEntrada.remove(xmlUnir.get(0));
                
                

                //Pillamos todos los Nodos
                for (int i = 1; i < contadorXMLs; i++) {
                    //Pillamos la orden
                    XPathExpression expression = xPath.compile(Expresion);
                    Node nodoAux2 = (Node) expression.evaluate(xmlUnir.get(i), XPathConstants.NODE);
                    Node Hijo = xmlOut.importNode(nodoAux2, true);
                    Padre.appendChild(Hijo);
                    
                    //Borra del entrada
                    xmlEntrada.remove(xmlUnir.get(i));
                }

                xmlSalida = xmlOut;

            } catch (Exception e) {
                e.printStackTrace();
            }
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

        xmlEntrada.add(xmlE);
    }

    @Override
    public Document setMSJslot(int v) {

        return xmlSalida;
    }

    public int devolverNConjuntos() {
        return xmlEntrada.size();
    }

    @Override
    public int calcularSalidas() {
        return 0;
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
