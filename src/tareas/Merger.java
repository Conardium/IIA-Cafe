package tareas;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class Merger implements ITarea {

    private Document xmlEntrada;
    ArrayList<Document> xmlSalida = new ArrayList<>();

    @Override
    public void realizarTarea() {

        xmlSalida.add(xmlEntrada);
    }



    @Override
    public void getMSJslot(Document xmlE) {

        xmlEntrada = xmlE;
    }

    @Override
    public Document setMSJslot(int v) {

        return xmlSalida.remove(0);//
    }


}
