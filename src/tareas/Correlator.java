
package tareas;

import java.util.ArrayList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

/**
 *
 * @author Cristian
 */
public class Correlator implements ITarea {

    ArrayList<Document> xmlBodyE = new ArrayList<>();
    ArrayList<Document> xmlBodyS = new ArrayList<>();

    ArrayList<Document> xmlContextE = new ArrayList<>();
    ArrayList<Document> xmlContextS = new ArrayList<>();

    private int nEllamada = 0;

    @Override
    public void realizarTarea() {

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression exprC = xPath.compile("//result//name");
            XPathExpression exprB = xPath.compile("//cafe_order//drink//name");

            for (int i = 0; i < xmlContextE.size(); i++) {
                int Body = -1, Context = -1;
                // Evaluar la expresión XPath para obtener el contenido de <name> del contexto
                String nombreContext = (String) exprC.evaluate(xmlContextE.get(i), XPathConstants.STRING);
                for (int j = 0; j < xmlBodyE.size(); j++) {

                    // Evaluar la expresión XPath para obtener el contenido de <name> del body
                    String nombreBody = (String) exprB.evaluate(xmlBodyE.get(j), XPathConstants.STRING);
                    //Si son iguales los guardo y me salgo de los bucles
                    if (nombreContext.equalsIgnoreCase(nombreBody)) {

                        Context = i;
                        Body = j;

                        j = xmlBodyE.size();
                    }
                }
                //Colocamos en su respectiva posición cada xml
                if (Context == i) {

                    xmlContextS.add(xmlContextE.get(Context));
                    xmlBodyS.add(xmlBodyE.get(Body));
                    nEllamada = xmlContextS.size();
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public void getMSJslot(Document xmlE) {

        if ("result".equals(xmlE.getFirstChild().getNodeName())) {
            xmlContextE.add(xmlE);
        } else {
            xmlBodyE.add(xmlE);
        }
    }

    @Override
    public Document setMSJslot(int v) {

        if (v == 1) {
            xmlContextE.remove(xmlContextS.get(0));
            return xmlContextS.remove(0);
        } else if (v == 2) {
            xmlBodyE.remove(xmlBodyS.get(0));
            return xmlBodyS.remove(0);
        } else {
            return null;
        }
    }

    public int devolverNConjuntos() {
        if (xmlContextS.size() == 0) {
            nEllamada = 0;
        }
        return nEllamada;
    }
    
    @Override
    public int calcularSalidas() {
        return 0;
    }

}
