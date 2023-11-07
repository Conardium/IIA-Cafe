package tareas;

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

            XPath xPath = XPathFactory.newInstance().newXPath();
            
            // Evaluar la expresión XPath para obtener el contenido de <exist> del contexto
            XPathExpression expr = xPath.compile("//result//exist");
            String existValue = (String) expr.evaluate(xmlEntradaContext, XPathConstants.STRING);
            
            // Crear un nodo para <exist> en el documento 1, dentro del nodo drink
            Node existencia = xmlEntradaBody.createElement("exist");
            existencia.appendChild(xmlEntradaBody.createTextNode(existValue));
            xmlEntradaBody.getFirstChild().getLastChild().appendChild(existencia);
            
            
            //Lo colocamos en la salida
            xmlSalida = xmlEntradaBody;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getMSJslot(Document xmlE) {
        
        if("result".equals(xmlE.getFirstChild().getNodeName()))
        {
            xmlEntradaContext = xmlE;
        }
        else
        {
            xmlEntradaBody = xmlE;
        }
    }

    @Override
    public Document setMSJslot(int v) {
        
        return xmlSalida;
    }
}
