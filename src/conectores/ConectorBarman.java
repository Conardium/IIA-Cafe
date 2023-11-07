package conectores;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;

public class ConectorBarman extends Conector {
/*
    ArrayList<Document> xmlSQL = new ArrayList();

    public ConectorBarman() {

    }

    public void busquedaBD{
        try{
            Document xmlAux = leerMensaje();

            /***********LO QUE DEVOLVERÁ LA BD**********/
            /*
                <result>
                <name>Nombre bebida</name>
                <exist>0 o 1</exist>
                </result>
            */
/*
            //Crear un documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlOut = dBuilder.newDocument();

            //Crear un elemento Padre
            Node NodoPadre = xmlOut.createElement("result");
            xmlOut.appendChild(NodoPadre);

            //El nombre
            Node name = xmlOut.createElement("name");
            name.appendChild(xmlOut.createTextNode("Nombre"));
            NodoPadre.appendChild(id);

            //Si hay
            Node exits = xmlOut.createElement("exist");
            exits.appendChild(xmlOut.createTextNode("1"));
            NodoPadre.appendChild(id);



            xmlSQL.add(xmlAux);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public Document devolverSQL()
    {
        return xmlSQL.remove(0);
    }
*/
}
