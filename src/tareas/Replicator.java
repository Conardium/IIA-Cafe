package tareas;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Replicator implements ITarea {

    //Mapa para añadir los tipos de datos.
    private Map<Integer, ArrayList<Document>> mapaListas;
    //Archivo que se encargará de replicar
    private Document xmlEntrada;
    //Numero de veces que replicará
    private final int numSalidas;

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

    @Override
    public Document setMSJslot(int posicion) {
        int contador = 0;
        for (ArrayList<Document> lista : mapaListas.values()) {
            if (contador == posicion) {
                return lista.remove(0);
            }
            contador++;
        }
        return null;
    }

    @Override
    public int calcularSalidas() {
        return 0;
    }
}
