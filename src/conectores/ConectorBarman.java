package conectores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import puertos.PuertoES;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

public class ConectorBarman extends Conector {

    private ArrayList<Document> xmlSQL = new ArrayList();
    private int total = 0;
    private PuertoES puerto = new PuertoES();

    public void busquedaBD(String Table, String sgbd, String ip, String service_bd, String usuario,
            String password) {
        while (puerto.nMensajes() != 0) {

            leerPuerto();

            try {
                //Nos conectamos
                Conexion(sgbd, ip, service_bd, usuario, password);

                Document xmlAux = xmlFiles.remove(0);
                String expression = xmlAux.getFirstChild().getTextContent();
                int existe = 0;

                String bebidaNombre = xmlAux.getFirstChild().getTextContent();
                
                String[] partes = bebidaNombre.split("=");
                bebidaNombre = partes[1].trim();

                bebidaNombre = bebidaNombre.substring(0, bebidaNombre.length() - 1);
                bebidaNombre = bebidaNombre.substring(1, bebidaNombre.length());
                
                System.out.println(bebidaNombre);
                
                PreparedStatement ps = getConexion().prepareStatement(expression);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    expression = rs.getString(1);
                    existe = rs.getInt(2);
                }
                //Nos desconectamos
                desconexion();

                /**
                 * *********LO QUE DEVOLVERÁ LA BD ********* <result>
                 * <name>Nombre bebida</name>
                 * <exist>0 o 1</exist>
                 * </result>
                 */
                ///Crear un documento XML
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document xmlOut = dBuilder.newDocument();

                //Crear un elemento Padre
                Node NodoPadre = xmlOut.createElement("result");
                xmlOut.appendChild(NodoPadre);

                //El nombre
                Node name = xmlOut.createElement("name");
                name.appendChild(xmlOut.createTextNode(bebidaNombre));
                NodoPadre.appendChild(name);

                //Si hay
                Node exits = xmlOut.createElement("exist");
                exits.appendChild(xmlOut.createTextNode(Integer.toString(existe)));
                NodoPadre.appendChild(exits);

                xmlSQL.add(xmlOut);
                total = xmlSQL.size();

            } catch (Exception e) {
                e.printStackTrace();
            }

            escribirPuerto();
        }

    }

    public PuertoES getPuerto() {
        return puerto;
    }

    @Override
    public void leerPuerto() {
        xmlFiles.add(puerto.getPuertoE());
    }

    @Override
    public void escribirPuerto() {
        puerto.setPuertoS(devolverSQL());
    }

    private Document devolverSQL() {
        return xmlSQL.remove(0);
    }

}
