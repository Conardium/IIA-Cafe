package tareas;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.ArrayList;
import org.w3c.dom.Document;

public class Splitter implements ITarea {

    private Document xmlEntrada;
    private ArrayList<Document> xmlSalida = new ArrayList<>();
    private int nEllamada = 0;
    private String Expresion;

    public Splitter(String Expresion) {
        this.Expresion = Expresion;
    }

    @Override
    public void realizarTarea() {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            XPath xPath = XPathFactory.newInstance().newXPath();

            //Todos los nodos por los que partimos
            NodeList NodeList = (NodeList) xPath.compile(Expresion).evaluate(xmlEntrada, XPathConstants.NODESET);

            //Para pillar el nodo por el cual eliminaremos
            String[] nNodos = Expresion.split("//");

            //Empieza en 1 porque en XML se busca por la XPATH a partir de 1
            for (int i = 1; i < NodeList.getLength() + 1; i++) {

                //Crear un documento XML
                Document xmlOut = dBuilder.newDocument();

                Node NodoPadre = xmlEntrada.getFirstChild();
                Node NodoPadreImportado = xmlOut.importNode(NodoPadre, true);

                //Eliminamos todos menos el hijo que queramos
                ajustarNodos(NodoPadreImportado, i, nNodos[1]);

                //Mi contexto: Número de Trozos
                Node size = xmlOut.createElement("size");
                size.appendChild(xmlOut.createTextNode(String.valueOf(NodeList.getLength())));
                NodoPadreImportado.insertBefore(size, NodoPadreImportado.getFirstChild().getNextSibling());

                //añadimos el padre ya transformado
                xmlOut.appendChild(NodoPadreImportado);

                xmlSalida.add(xmlOut);
                nEllamada = xmlSalida.size();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ajustarNodos(Node nodoPadre, int posicion, String expresion) {

        if (nodoPadre.getNodeType() == Node.ELEMENT_NODE && nodoPadre.getNodeName().equalsIgnoreCase(expresion)) {
            // Si el nodo actual coincide con la expresión, eliminar todos los hijos excepto el de la posición especificada
            NodeList hijos = nodoPadre.getChildNodes();
            int contador = 0;

            for (int i = 0; i < hijos.getLength(); i++) {
                Node hijo = hijos.item(i);
                if (hijo.getNodeType() == Node.ELEMENT_NODE) {
                    // Verificar si es el hijo en la posición específica
                    if (++contador != posicion) {
                        nodoPadre.removeChild(hijo);
                        i--; // Ajustar el índice después de eliminar un nodo
                    }
                }
            }
        } else {
            // Llamada recursiva para los nodos hijos
            NodeList hijos = nodoPadre.getChildNodes();
            for (int i = 0; i < hijos.getLength(); i++) {
                Node hijo = hijos.item(i);
                ajustarNodos(hijo, posicion, expresion);
            }
        }
    }

    @Override
    public void getMSJslot(Document xmlE) {
        xmlEntrada = xmlE;
    }

    @Override
    public Document setMSJslot(int v) {
        return xmlSalida.remove(0);
    }

    public int devolverNConjuntos() {
        return nEllamada;
    }

    @Override
    public int calcularSalidas() {
        return 0;
    }

}
