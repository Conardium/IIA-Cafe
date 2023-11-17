package conectores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import puertos.PuertoES;
import puertos.PuertoEoS;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

public class ConectorBarman extends Conector {

    private ArrayList<Document> xmlSQL = new ArrayList();
    private int total = 0;
    private PuertoES puerto = new PuertoES();

    public void busquedaBD(String Table, String sgbd, String ip, String service_bd, String usuario,
                           String password) {

        try {
            //Nos conectamos
            Conexion( sgbd, ip,  service_bd,  usuario, password);

            Document xmlAux = xmlFiles.remove(0);
            String bebidaNombre = "";
            int existe = 0;

            bebidaNombre = xmlAux.getFirstChild().getTextContent();
            
            String[] partes = bebidaNombre.split("=");

            bebidaNombre = partes[1].trim();

            bebidaNombre = bebidaNombre.substring(0, bebidaNombre.length() - 1);
            

            String consulta = "SELECT NAME,EXIST FROM " + Table +
                    " WHERE NAME = " + bebidaNombre ;
            PreparedStatement ps = getConexion().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bebidaNombre = rs.getString(1);
                existe = rs.getInt(2);
            }
            //Nos desconectamos
            desconexion();

            /**
             * *********LO QUE DEVOLVERÁ LA BD**********
             * <result>
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

    }
    //Colocamos en el puerto el xml
    public void leerPuerto() {
        xmlFiles.add(puerto.getPuertoE());
    }

    public PuertoES getPuerto(){
        return puerto;
    }

    //Recogemos el mensaje del puerto
    public void escribirPuerto() {
        puerto.setPuertoS(devolverSQL());
    }

    public Document devolverSQL() {
        return xmlSQL.remove(0);
    }

    public int getTotal() {

        if (xmlSQL.isEmpty()) {
            total = 0;
        }
        return total;
    }

}
