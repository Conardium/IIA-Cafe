package tareas;

import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import slot.Slot;

public class Distributor extends Tarea {

    //Archivo que se encargará de distribuir
    private Document xmlEntrada;
    //Cantidad de salidas
    private int nSalidas = 0;

    private Slot slotE;
    private Map<String, Slot> mapaSalida;

    private final String Filtro;

    public Distributor(String Filtro) {
        
        this.Filtro = Filtro;
        mapaSalida = new HashMap<>();
    }

    @Override
    public void realizarTarea() {

        if(slotE != null) {
            for (int nXML = 0; nXML < slotE.devolverNConjuntos(); nXML++) {

                getMSJslot();

                try {
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    //Recogemos el primer filtro de la lista que indica cual es el parametro a buscar
                    Node NodoPadre = (Node) xPath.compile(Filtro).evaluate(xmlEntrada, XPathConstants.NODE);

                    String Filtro = NodoPadre.getTextContent();
                    // Obtener la lista correspondiente al filtro
                    Slot slotS = mapaSalida.computeIfAbsent(Filtro, k -> new Slot("DistributorSalida_" + Filtro));

                    xmlSalida = xmlEntrada;

                    setMSJslot(slotS);

                } catch (XPathExpressionException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    public void getMSJslot() {
        xmlEntrada = slotE.getMensaje();

    }

    public void setMSJslot(Slot slotS) {
        slotS.setMensaje(xmlSalida);
    }

    public int devolverN(int posicion) {
        if (posicion < 0 || posicion >= mapaListas.size()) {
            // La posición no es válida
            return 0; 
        }

        int contador = 0;
        for (ArrayList<Document> lista : mapaListas.values()) {
            if (contador == posicion) {
                return lista.size();
            }
            contador++;
        }

        return 0; // No debería llegar aquí, pero se incluye por precaución
    }

    @Override
    public int calcularSalidas() {
        return nSalidas;
    }

    @Override
    public void enlazarSlotE(Slot slot) {
        this.slotE = slot;
    }

    public Slot enlazarSlotS(int n) {
        int contador = 0;
        for (Slot slotS : mapaSalida.values()) {
            if(contador == n){
                return slotS;
            }
            contador++;
        }
        return null;
    }

}
