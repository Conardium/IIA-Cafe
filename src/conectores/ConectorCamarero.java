package conectores;

import java.sql.PreparedStatement;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import puertos.PuertoEoS;

public class ConectorCamarero extends Conector {

    private int id = 0;
    private PuertoEoS puerto = new PuertoEoS(1);

    public String convertirXMLtoString() {

        Node nPadre = xmlFiles.get(0).getDocumentElement();

        StringBuilder mensaje = new StringBuilder();

        // Nodo Padre
        //System.out.println("\n*************************PUERTO FINAL**************************");
        mostrarNodos(nPadre.getChildNodes(), mensaje, 1);
        String mensajeFinal = "<" + nPadre.getNodeName() + ">"
                + mensaje.toString()
                + "\n</" + nPadre.getNodeName() + ">";
        //Cogemos los Nodos del padre recursivamente
        //System.out.println(mensajeFinal);
        //System.out.println("\n*************************------------**************************");

        return mensajeFinal;
    }

    public void mostrarNodos(NodeList nHijos, StringBuilder mensaje, int paso) {

        String salto = "\n";

        for (int i = 0; i < paso; i++) {

            salto = salto + "\t";
        }

        for (int i = 0; i < nHijos.getLength(); i++) {
            Node nAux = nHijos.item(i);

            boolean esTexto = nAux.getNodeName().startsWith("#");

            if (!esTexto) {
                mensaje.append(" ").append(salto).append("<").append(nAux.getNodeName()).append(">");
                if (nAux.getNodeName().contains("id")) {
                    id = Integer.parseInt(nAux.getTextContent());
                }
            }
            if (esTexto) {
                mensaje.append(" ").append(nAux.getTextContent());
            }
            if (!esTexto) {
                if (nAux.getChildNodes() != null) {//Si tiene hijos los mostramos 
                    mostrarNodos(nAux.getChildNodes(), mensaje, paso + 1);
                }
                if (nAux.getChildNodes().getLength() > 1) {
                    mensaje.append(" ").append(salto).append("</").append(nAux.getNodeName()).append(">");
                } else {
                    mensaje.append(" ").append("</").append(nAux.getNodeName()).append(">");
                }
            }

        }

    }

    public PuertoEoS getPuerto(){
        return puerto;
    }

    @Override
    public void leerPuerto() {

        xmlFiles.add(puerto.getPuerto());
    }

    public boolean CargarBD(String NombreTabla, String sgbd, String ip, String service_bd, String usuario,
            String password) {
        try {
            Conexion(sgbd, ip, service_bd, usuario, password);

            String Mensaje = convertirXMLtoString();

            PreparedStatement ps = getConexion().prepareStatement("INSERT INTO " + NombreTabla + " VALUES "
                    + "(?,?)");
            ps.setInt(1, id);
            ps.setString(2, Mensaje);
            ps.executeUpdate();

            desconexion();

            return true;
        } catch (Exception ex) {
            System.out.println("Error en Conector Camarero");
            return false;
        }
    }

    public Document anadirMensajeBD(String Table, String sgbd, String ip, String service_bd, String usuario,
                                    String password) {

        CargarBD(Table, sgbd, ip, service_bd, usuario,
                password);
        return xmlFiles.remove(0);
    }

    //Usamos este metodo solo para las pruebas
    public boolean borrarBD(String NombreTabla, String sgbd, String ip, String service_bd, String usuario,
            String password) {
        try {
            Conexion(sgbd, ip, service_bd, usuario, password);

            PreparedStatement ps = getConexion().prepareStatement("DELETE FROM " + NombreTabla);
            ps.executeUpdate();

            desconexion();

            return true;
        } catch (Exception ex) {
            System.out.println("Error en Conector Camarero - No se han podido borrar las tablas de la BD");
            return false;
        }
    }

}
