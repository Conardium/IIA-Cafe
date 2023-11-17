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
    private ArrayList<Slot> slotS;
    private final int nSalidas;

    public Distributor(String Filtro, int nSalidas) {

        this.Filtro = Filtro;
        this.nSalidas = nSalidas;
        this.mapaSalida = new HashMap<>();
        this.slotS = new ArrayList();
        
        for (int i = 0; i < nSalidas; i++) {
            slotS.add(new Slot("DistributorSalida_" + i));
        }
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
                    Slot slotFicheros = mapaSalida.computeIfAbsent(Filtro, k -> new Slot("DistributorSalida_" + Filtro));

                    slotFicheros.setMensaje(xmlEntrada);

                } catch (XPathExpressionException ex) {
                    ex.printStackTrace();
                }
 
            }
            setMSJslot();
        }

    }
    @Override
    public void getMSJslot() {
        xmlEntrada = slotE.getMensaje();

    }

    public void setMSJslot() {
        int contador = 0;
        for (Slot sAux : mapaSalida.values()) {
            int i = 0;
            while(i < sAux.devolverNConjuntos()) {
                slotS.get(contador).setMensaje(sAux.getMensaje());
            }
           contador++;
        }
    }

    @Override
    public void enlazarSlotE(Slot slot) {
        this.slotE = slot;
    }

    public Slot enlazarSlotS(int n) {
        return slotS.get(n - 1);
    }

}
