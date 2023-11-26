
package slot;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Slot {

    private String idSlot;
    private ArrayList<Document> xmlBuffer = new ArrayList<>();
    private int nEllamada = 0;

    public Slot(String idSlot) {
        
        this.idSlot = idSlot;
    }

    public Document getMensaje() {

        return xmlBuffer.remove(0);
    }

    public void setMensaje(Document Mensaje) {

        Node nPadre = Mensaje.getDocumentElement();
        //Nodo Padre
        System.out.println("\n*************************SLOT**************************");
        System.out.println("El Mensaje llega al Slot: " + idSlot);
        System.out.println("<" + nPadre.getNodeName() + ">");
        //Cogemos los Nodos del padre recursivamente
        mostrarNodos(nPadre.getChildNodes(), 1);
        System.out.println("</" + nPadre.getNodeName() + ">");
        System.out.println("\n*************************----**************************");

        xmlBuffer.add(Mensaje);
        nEllamada = xmlBuffer.size();
    }

    public int devolverNConjuntos() {

        if (xmlBuffer.size() == 0) {
            nEllamada = 0;
        }
        return nEllamada;
    }

    private void mostrarNodos(NodeList nHijos, int paso) {

        String tabulaciones = "\t";

        //Para que ponga las tabulaciones
        for (int i = 1; i < paso; i++) {

            tabulaciones = tabulaciones + "\t";
        }

        for (int i = 0; i < nHijos.getLength(); i++) {
            Node nAux = nHijos.item(i);

            boolean esTexto = nAux.getNodeName().startsWith("#");
            
            String Mostrar;
            
            if (!esTexto) {
                System.out.println(tabulaciones + "<" + nAux.getNodeName() + ">");
            }
            if (esTexto) {
                System.out.println(tabulaciones + nAux.getTextContent());
            }
            if (!esTexto) {
                if (nAux.getChildNodes() != null) {//Si tiene hijos los mostramos FUNCIONA
                    mostrarNodos(nAux.getChildNodes(), paso + 1);
                }
                System.out.println(tabulaciones + "</" + nAux.getNodeName() + ">");
            }

        }
    }

}
