package tareas;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import org.w3c.dom.Document;

public class Content_Enricher implements ITarea {

    Document xmlEntradaContext;
    Document xmlEntradaBody;
    Document xmlSalida;

    @Override
    public void realizarTarea() {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            XPath xPath = XPathFactory.newInstance().newXPath();

            // Evaluar la expresión XPath para obtener el contenido de <exist> del contexto
            XPathExpression expr = xPath.compile("//result//exist");
            String existValue = (String) expr.evaluate(xmlEntradaContext, XPathConstants.STRING);

            //Creamos nuevo documento XML
            Document xmlOut = dBuilder.newDocument();

            //Cogemos todos los nodos del Body
            XPathExpression expression = xPath.compile("//cafe_order");
            Node nodoAux = (Node) expression.evaluate(xmlEntradaBody, XPathConstants.NODE);
            nodoAux = xmlOut.importNode(nodoAux, true);
            xmlOut.appendChild(nodoAux);
            
            // Crear un nodo para <exist> en el documento creado, dentro del nodo drink
            Node existencia = xmlOut.createElement("exist");
            existencia.appendChild(xmlOut.createTextNode(existValue));
            xmlOut.getFirstChild().getLastChild().appendChild(existencia);

            //Lo colocamos en la salida
            xmlSalida = xmlOut;

            xmlEntradaBody = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getMSJslot(Document xmlE) {

        if ("result".equals(xmlE.getFirstChild().getNodeName())) {
            xmlEntradaContext = xmlE;
        } else {
            xmlEntradaBody = xmlE;
        }
    }

    @Override
    public Document setMSJslot(int v) {

        return xmlSalida;
    }

}
