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

    private Document xmlEntrada, xmlSalida;

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
    @Override
    public void getMSJslot() {
        xmlEntrada = slotE.getMensaje();

    }

    public void setMSJslot(Slot slotS) {
        slotS.setMensaje(xmlSalida);
    }

    @Override
    public void enlazarSlotE(Slot slot) {
        this.slotE = slot;
    }

    public Slot enlazarSlotS(int n) {
        int contador = 1;
        for (Slot slotS : mapaSalida.values()) {
            if(contador == n){
                return slotS;
            }
            contador++;
        }
        return null;
    }

}
