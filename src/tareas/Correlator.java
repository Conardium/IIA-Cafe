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
    private Map<String, ArrayList<Document>> mapaListasE;

    private ArrayList<Document> xmlBodyS = new ArrayList<>();
    private ArrayList<Document> xmlContextS = new ArrayList<>();

    private int nEllamada = 0;

    private Slot slotEContext, slotEBody, slotSContext, slotSBody;

    private final String Filtro;

    public Correlator(String Filtro) {

        this.Filtro = Filtro;
        this.mapaListasE = new HashMap<>();
        this.slotSBody = new Slot("SlotCorrelatorSalidaBody");
        this.slotSContext = new Slot("SlotCorrelatorSalidaContext");
    }

    @Override
    public void realizarTarea() {

        //Se puede hacer más génerico
        //ya que el correlator puede tener muchas entradas y muchas salidas.
        //pero nosotros asumimos que tiene 2 entradas y 2 salidas, por que
        //en clase no hemos visto otro tipo.
        
        // Crear iteradores separados para las listas
        Iterator<ArrayList<Document>> IteratorE = mapaListasE.values().iterator();
        // Obtener la lista correspondiente al filtro
        ArrayList<Document> listaContext = IteratorE.next();
        ArrayList<Document> listaBody = IteratorE.next();

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression Expresion = xPath.compile(Filtro);

            for (int i = 0; i < listaContext.size(); i++) {
                int Body = -1, Context = -1;
                // Evaluar la expresión XPath para obtener el contenido de <name> del contexto
                String nombreContext = (String) Expresion.evaluate(listaContext.get(i), XPathConstants.STRING);
                for (int j = 0; j < listaBody.size(); j++) {

                    // Evaluar la expresión XPath para obtener el contenido de <name> del body
                    String nombreBody = (String) Expresion.evaluate(listaBody.get(j), XPathConstants.STRING);
                    //Si son iguales los guardo y me salgo de los bucles
                    if (nombreContext.equalsIgnoreCase(nombreBody)) {

                        Context = i;
                        Body = j;

                        j = listaBody.size();
                    }
                }
                //Colocamos en su respectiva posición cada xml
                if (Context == i) {

                    xmlContextS.add(listaContext.get(Context));
                    xmlBodyS.add(listaBody.get(Body));
                    nEllamada = xmlContextS.size();
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public void getMSJslot(Document xmlE) {

        // Obtener la lista correspondiente al filtro
        ArrayList<Document> listaFiltro = mapaListasE.computeIfAbsent(xmlE.getFirstChild().getNodeName(), k -> new ArrayList<>());
        listaFiltro.add(xmlE);
    }

    @Override
    public Document setMSJslot(int v) {
        
        // Crear iteradores separados para las listas
        Iterator<ArrayList<Document>> IteratorE = mapaListasE.values().iterator();
        // Obtener la lista correspondiente al filtro
        ArrayList<Document> listaContext = IteratorE.next();
        ArrayList<Document> listaBody = IteratorE.next();
        
        if (v == 1) {
            listaContext.remove(xmlContextS.get(0));
            return xmlContextS.remove(0);
        } else if (v == 2) {
            listaBody.remove(xmlBodyS.get(0));
            return xmlBodyS.remove(0);
        } else {
            return null;
        }
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
