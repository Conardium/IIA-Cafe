package tareas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import slot.Slot;

/**
 *
 * @author Cristian
 */
public class Correlator extends Tarea {

    //Mapa para añadir los tipos de datos de entrada.
    private ArrayList<Document> xmlBodyE = new ArrayList<>();
    private ArrayList<Document> xmlContextE = new ArrayList<>();

    private ArrayList<Document> xmlBodyS = new ArrayList<>();
    private ArrayList<Document> xmlContextS = new ArrayList<>();

    private int nEllamada = 0;

    private Slot slotEContext, slotEBody, slotSContext, slotSBody;

    private final String Filtro;

    public Correlator(String Filtro) {

        this.Filtro = Filtro;
        this.slotSBody = new Slot("SlotCorrelatorSalidaBody");
        this.slotSContext = new Slot("SlotCorrelatorSalidaContext");
    }

    @Override
    public void realizarTarea() {

        //Se puede hacer más génerico
        //ya que el correlator puede tener muchas entradas y muchas salidas.
        //pero nosotros asumimos que tiene 2 entradas y 2 salidas, por que
        //en clase no hemos visto otro tipo.
        if(slotEContext != null && slotEBody != null) {
            for (int nXML = 0; nXML < slotEContext.devolverNConjuntos(); nXML++) {
                getMSJslot();
                try {
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    XPathExpression Expresion = xPath.compile(Filtro);

                    for (int i = 0; i < xmlContextE.size(); i++) {
                        int Body = -1, Context = -1;
                        // Evaluar la expresión XPath para obtener el contenido de <name> del contexto
                        String nombreContext = (String) Expresion.evaluate(xmlContextE.get(i), XPathConstants.STRING);
                        for (int j = 0; j < xmlBodyE.size(); j++) {

                            // Evaluar la expresión XPath para obtener el contenido de <name> del body
                            String nombreBody = (String) Expresion.evaluate(xmlBodyE.get(j), XPathConstants.STRING);
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

                setMSJslot();
            }
        }

    }
    @Override
    public void getMSJslot(){
        xmlBodyE.add(slotEBody.getMensaje());
        xmlContextE.add(slotEContext.getMensaje());
    }
    @Override
    public void setMSJslot(){
        xmlBodyS.add(slotSBody.getMensaje());
        xmlContextS.add(slotSContext.getMensaje());
    }


    public void enlazarSlotE(Slot slot, int i) {
        if(i == 1)
            this.slotEContext = slot;
        else
            this.slotEBody = slot;
    }

    public Slot enlazarSlotS(int i) {
        if(i == 1)
            return slotSContext;
        else
            return slotSBody;
    }
}
