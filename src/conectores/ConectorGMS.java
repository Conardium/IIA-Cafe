package conectores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import puertos.PuertoEoS;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConectorGMS extends Conector {

    private int id = 0;
    private String Mensaje = "";
    private int nEllamada = 0;
    private PuertoEoS puerto = new PuertoEoS(2);

    public boolean CargarBD(String NombreTabla, String sgbd, String ip, String service_bd, String usuario,
                            String password) {
        try {
            Conexion(sgbd, ip, service_bd, usuario, password);

            String consulta = "SELECT * FROM " + NombreTabla;
            PreparedStatement ps = getConexion().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
                Mensaje = rs.getString(2);
                nEllamada++;
                TransformarStringXML();
            }
            //Nos desconectamos
            desconexion();

            return true;
        } catch (SQLException ex) {
            System.out.println("Error en el Conector GMS");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public void TransformarStringXML() {

        try {
            ///Crear un documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlOut = dBuilder.parse(new org.xml.sax.InputSource(new java.io.StringReader(Mensaje)));

            StringToXML(xmlOut);

            xmlFiles.add(xmlOut);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PuertoEoS getPuerto(){
        return puerto;
    }

    @Override
    public void escribirPuerto() {

        puerto.setPuerto(xmlFiles.remove(0));
    }

    public int numMensajes() {
        if (xmlFiles.isEmpty()) {
            nEllamada = 0;
        }
        return nEllamada;
    }

    private static void StringToXML(Node node) {
        NodeList childNodes = node.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                StringToXML(childNode);
            }
        }

        if (node instanceof Element) {
            ((Element) node).normalize();
        }
    }
}
