package conectores;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

public class ConectorBarman extends Conector {

    ArrayList<Document> xmlSQL = new ArrayList();
    int total;

    public ConectorBarman() {

    }

    public void busquedaBD() {
        try {
            Document xmlAux = leerMensaje();
            
            String bebidaNombre = xmlAux.getFirstChild().getTextContent();
            
            /***********LO QUE DEVOLVERÁ LA BD**********
                <result>
                <name>Nombre bebida</name>
                <exist>0 o 1</exist>
                </result>
             */
            
            //Crear un documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlOut = dBuilder.newDocument();

            //Crear un elemento Padre
            Node NodoPadre = xmlOut.createElement("result");
            xmlOut.appendChild(NodoPadre);

            //El nombre
            Node name = xmlOut.createElement("" +
                    "");
            name.appendChild(xmlOut.createTextNode(bebidaNombre));
            NodoPadre.appendChild(name);

            //Si hay
            Node exits = xmlOut.createElement("exist");
            exits.appendChild(xmlOut.createTextNode("1"));
            NodoPadre.appendChild(exits);

            xmlSQL.add(xmlAux);
            total = xmlSQL.size();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Document devolverSQL() {
        return xmlSQL.remove(0);
    }

    public int getTotal() {
        return total;
    }
    
}
