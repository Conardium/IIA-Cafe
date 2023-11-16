package tareas;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Replicator extends Tarea {

    //Archivo que se encargará de replicar
    private Document xmlEntrada;
    //Numero de veces que replicará
    private final int numSalidas;
    private Slot slotE;
    private ArrayList<Slot> ListaSlotS;

    public Replicator(int numSalidas) {
        this.numSalidas = numSalidas;
    }

    @Override
    public void realizarTarea() {

        mapaListas = new HashMap<>();

        for (int i = 0; i < numSalidas; i++) {
            mapaListas.put(i, new ArrayList<>());
            ArrayList<Document> Replicas = mapaListas.get(i);
            Replicas.add(xmlEntrada);
        }

    }

    @Override
    public void getMSJslot(Document xmlE) {
        xmlEntrada = xmlE;
    }

    public void setMSJslot() {
        for (int i = 0; i < numSalidas; i++) {
            ListaSlotS.get(i).setMensaje(xmlSalida);
        }
    }

    @Override
    public int calcularSalidas() {
        return 0;
    }
}
