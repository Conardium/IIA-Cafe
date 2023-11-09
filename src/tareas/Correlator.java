/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

        for (int a = 0; a < xmlContextE.size(); a++) {
            int Body = -1, Context = -1;
            try {
                XPath xPath = XPathFactory.newInstance().newXPath();
                for (int i = 0; i < xmlContextE.size(); i++) {

                    // Evaluar la expresión XPath para obtener el contenido de <name> del contexto
                    XPathExpression exprC = xPath.compile("//result//name");
                    String nombreContext = (String) exprC.evaluate(xmlContextE.get(i), XPathConstants.STRING);

                    for (int j = 0; j < xmlBodyE.size(); j++) {

                        // Evaluar la expresión XPath para obtener el contenido de <name> del body
                        XPathExpression exprB = xPath.compile("//cafe_order//drink//name");
                        String nombreBody = (String) exprB.evaluate(xmlBodyE.get(i), XPathConstants.STRING);

                        //Si son iguales los guardo y me salgo de los bucles
                        if (nombreContext.equalsIgnoreCase(nombreBody)) {

                            Context = i;
                            Body = j;

                            i = xmlContextE.size();
                            j = xmlBodyE.size();
                        }
                    }
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

            //Colocamos en su respectiva posición cada xml
            xmlContextS.add(xmlContextE.remove(Context));
            xmlBodyS.add(xmlBodyE.remove(Body));
            nEllamada = xmlContextS.size();
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
            return xmlContextS.remove(0);
        } else if (v == 2) {
            return xmlBodyS.remove(0);
        } else {
            return null;
        }
    }

    public int devolverNConjuntos() {
        //System.out.println(xmlSalida.size());
        return nEllamada;
    }

}
