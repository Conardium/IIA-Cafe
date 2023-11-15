package tareas;

import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;

public class Distributor implements ITarea {

    //Mapa para añadir los tipos de datos.
    private Map<String, ArrayList<Document>> mapaListas;
    //Archivo que se encargará de distribuir
    private Document xmlEntrada;
    //Cantidad de salidas
    private int nSalidas = 0;


    private String Filtro;

    public Distributor(String Filtro) {
        
        this.Filtro = Filtro;
        mapaListas = new HashMap<>();
    }

    @Override
    public void realizarTarea() {

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            //Recojemos el primer filtro de la lista que indica cual es el parametro a buscar
            Node NodoPadre = (Node) xPath.compile(Filtro).evaluate(xmlEntrada, XPathConstants.NODE);

            String Filtro = NodoPadre.getTextContent();
            // Obtener la lista correspondiente al filtro
            ArrayList<Document> listaFiltro = mapaListas.computeIfAbsent(Filtro, k -> new ArrayList<>());

            // Agregar el mensaje a la lista
            listaFiltro.add(xmlEntrada);

            //Guardamos el numero de salidas
            nSalidas = mapaListas.size();

        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getMSJslot(Document xmlE) {
        xmlEntrada = xmlE;
    }

    @Override
    public Document setMSJslot(int posicion) {
        if (posicion < 0 || posicion >= mapaListas.size()) {
            // La posición no es válida
            return null;
        }

        int contador = 0;
        for (ArrayList<Document> lista : mapaListas.values()) {
            if (contador == posicion) {
                return lista.remove(0);
            }
            contador++;
        }

        return null; // No debería llegar aquí, pero se incluye por precaución
    }

    public int devolverN(int posicion) {
        if (posicion < 0 || posicion >= mapaListas.size()) {
            // La posición no es válida
            return 0; 
        }

        int contador = 0;
        for (ArrayList<Document> lista : mapaListas.values()) {
            if (contador == posicion) {
                return lista.size();
            }
            contador++;
        }

        return 0; // No debería llegar aquí, pero se incluye por precaución
    }

    @Override
    public int calcularSalidas() {
        return nSalidas;
    }


}
