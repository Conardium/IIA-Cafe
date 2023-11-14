package tareas;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class Replicator implements ITarea{

    Document xmlEntrada;
    int numSalidas = 2;
    ArrayList<Document> xmlSalida = new ArrayList<>();

    @Override
    public void realizarTarea() {
        for (int i = 0; i < numSalidas; i++)
            xmlSalida.add(xmlEntrada);
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
}
