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

    @Override
    public void realizarTarea() {

        if(!mapaSlotsE.isEmpty()){
            for (Slot slotS : mapaSlotsE.values()) {
                for (int nXML = 0; nXML < slotS.devolverNConjuntos(); nXML++) {
                    getMSJslot();
                    xmlSalida.add(xmlEntrada);
                    setMSJslot();
                }
            }
        }


    }
    @Override
    protected void getMSJslot() {
        boolean encontrado = false;
        for (Slot slotS : mapaSlotsE.values() ) {
            if(slotS.devolverNConjuntos() != 0 && !encontrado){
                xmlEntrada = slotS.getMensaje();
                encontrado = true;
            }
        }
    }

    @Override
    protected void setMSJslot() {
        slotS.setMensaje(xmlSalida.remove(0));
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
