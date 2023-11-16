package tareas;

import org.w3c.dom.Document;
import slot.Slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Merger extends Tarea {

    private Map<Integer, Slot> mapaSlotsE;
    private Slot slotS;

    private Document xmlEntrada;
    private ArrayList<Document> xmlSalida = new ArrayList<>();
    private int contador;

    public Merger() {
        this.slotS = new Slot("MergerSalida");
        mapaSlotsE = new HashMap<>();
        this.contador = 0;
    }

    //FALTA
    @Override
    public void realizarTarea() {

        if(!mapaSlotsE.isEmpty()){
            //MAL
            for (int nXML = 0; nXML < mapaSlotsE.size(); nXML++) {
                xmlSalida.add(xmlEntrada);
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

    @Override
    public int calcularSalidas() {
        return 0;
    }

    @Override
    public Slot enlazarSlotS() {
        return slotS;
    }
    public void enlazarSlotE(Slot slotE) {

            mapaSlotsE.put(contador, slotE);
            contador++;
    }


}
