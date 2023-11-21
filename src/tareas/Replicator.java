package tareas;

import org.w3c.dom.Document;
import slot.Slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Replicator extends Tarea {

    private Document xmlEntrada, xmlSalida;

    //Numero de veces que replicará
    private final int numSalidas;

    private Slot slotE;
    private ArrayList<Slot> ListaSlotS;

    public Replicator(int numSalidas) {

        this.numSalidas = numSalidas;
        this.ListaSlotS = new ArrayList<>();
        for (int i = 0; i < numSalidas; i++) {
            ListaSlotS.add(new Slot("ReplicatorSalida_" + i));
        }
    }

    @Override
    public void realizarTarea() {

        if(slotE != null) {
            for (int nXML = 0; nXML < slotE.devolverNConjuntos(); nXML++) {

                getMSJslot();
                xmlSalida = xmlEntrada;
                setMSJslot();
            }

        }

    }
    @Override
    protected void getMSJslot() {

        xmlEntrada = slotE.getMensaje();
    }
    @Override
    protected void setMSJslot() {
        for (int i = 0; i < numSalidas; i++) {
            ListaSlotS.get(i).setMensaje(xmlSalida);
        }
    }


    @Override
    public void enlazarSlotE(Slot slot) {
        this.slotE = slot;
    }

    public Slot enlazarSlotS(int n) {
        return ListaSlotS.get(n-1);
    }

}
