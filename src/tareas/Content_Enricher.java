package tareas;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.ArrayList;
import org.w3c.dom.Document;

public class Content_Enricher {

/*
    ArrayList<Document> xmlEntrada1 = new ArrayList<Document>();
    ArrayList<Document> xmlEntrada2 = new ArrayList<Document>();
    ArrayList<Document> xmlSalida = new ArrayList<Document>();

    @Override
    public void realizarTarea(){

        // Crear un objeto XPath
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        XPath xPath = XPathFactory.newInstance().newXPath();

        // Evaluar la expresión XPath para obtener el contenido de <exist> del documento 2
        XPathExpression expr = xPath.compile("//result//exist");
        String existValue = (String) expr.evaluate(xmlEntrada2, XPathConstants.STRING);

        // Crear un nodo para <exist> en el documento 1
        Element existElement = xmlEntrada1.createElement("exist");
        existElement.appendChild(xmlEntrada1.createTextNode(existValue));

        // Encontrar el lugar adecuado para agregar <exist> en el documento 1
        Node root = xmlEntrada1.getDocumentElement();
        Node idNode = findNodeByXPath(xmlEntrada1, "/cafe_order/id");
        if (idNode != null) {
            root.insertBefore(existElement, idNode.getNextSibling());
        } else {
            // Si no se encuentra el lugar adecuado, simplemente se agrega al final del documento
            root.appendChild(existElement);
        }

        // El documento 1 ahora contiene la combinación deseada
        xmlSalida = xmlEntrada1;

    }
    @Override
    public void getMSJslot(Document xmlE1){
    //Preguntar su nodo padre, si es cafe_order lo metes a 1 y si es otro lo metes al otro
        this.xmlEntrada1=xmlE1;

        realizarTarea();

    }
    @Override
    public Document setMSJslot(int v){
        return xmlSalida.remove(0);
    }
*/
}
