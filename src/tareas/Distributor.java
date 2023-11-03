package tareas;

import org.w3c.dom.Document;
import java.util.ArrayList;

public class Distributor implements ITarea {

    Document xmlEntrada; //Archivo que se encargará de distribuir a la sección hot o cold

    ArrayList<Document> xmlCold = new ArrayList<>();
    ArrayList<Document> xmlHot = new ArrayList<>();
    int nEhot = 0;
    int nEcold = 0;

    @Override
    public void realizarTarea() {

        //Si es cold
        if(xmlEntrada.getFirstChild().getLastChild().getLastChild().getTextContent().compareTo("cold") == 0)
        {
            xmlCold.add(xmlEntrada);
            nEcold++;
        }
        //Si es hot
        else if(xmlEntrada.getFirstChild().getLastChild().getLastChild().getTextContent().compareTo("hot") == 0)
        {
            xmlHot.add(xmlEntrada);
            nEhot++;
        }
    }

    @Override
    public void getMSJslot(Document xmlE) {

        xmlEntrada = xmlE;
    }

    @Override
    public Document setMSJslot(int v) {

        if(v == 1)
        {
            return setMSJslotCold();
        }
        else if(v == 2)
        {
            return setMSJslotHot();
        }
        else return null;
    }

    private Document setMSJslotCold()
    {
        return xmlCold.remove(0);
    }

    private Document setMSJslotHot()
    {
        return xmlHot.remove(0);
    }

    public int devolverNCold() {

        return nEcold;
    }

    public int devolverNHot() {

        return nEhot;
    }

}