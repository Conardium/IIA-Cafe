package conectores;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import puertos.PuertoES;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ConectorAlumnos extends Conector {
    private ArrayList<Document> xmlSQL = new ArrayList();
    private int total = 0;
    private PuertoES puerto = new PuertoES();

    public void busquedaBD(String Table, String sgbd, String ip, String service_bd, String usuario,
                           String password) {

        try {
            //Nos conectamos
            Conexion(sgbd, ip, service_bd, usuario, password);

            Document xmlAux = xmlFiles.remove(0);
            String dniAlumno = "";
            String emailAlumno = "";
            String telefonoAlumno = "";

            dniAlumno = xmlAux.getFirstChild().getTextContent();

            String[] partes = dniAlumno.split("=");

            dniAlumno = partes[1].trim();

            dniAlumno = dniAlumno.substring(0, dniAlumno.length() - 1);

            String consulta = "SELECT DNI,EMAIL,TELEFONO FROM " + Table
                    + " WHERE NAME = " + dniAlumno;
            PreparedStatement ps = getConexion().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dniAlumno = rs.getString(1);
                emailAlumno = rs.getString(2);
                telefonoAlumno = rs.getString(3);
            }
            //Nos desconectamos
            desconexion();

             /**
             * *********LO QUE DEVOLVERÁ LA BD
             **********
             * <result>
             * <dni>dni</dni>
             * <email> email </email>
              * <telefono> telefono </telefono>
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
            Node name = xmlOut.createElement("dni");
            name.appendChild(xmlOut.createTextNode(dniAlumno));
            NodoPadre.appendChild(name);

            //Si hay
            Node email = xmlOut.createElement("email");
            email.appendChild(xmlOut.createTextNode(emailAlumno));
            NodoPadre.appendChild(email);

            //Si no hay
            if(telefonoAlumno == null)
            {
                telefonoAlumno = "NULL";
            }
            Node telefono = xmlOut.createElement("telefono");
            telefono.appendChild(xmlOut.createTextNode(telefonoAlumno));
            NodoPadre.appendChild(telefono);

            xmlSQL.add(xmlOut);
            total = xmlSQL.size();

        } catch (Exception e) {
            e.printStackTrace();
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
