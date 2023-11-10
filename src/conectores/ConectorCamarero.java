package conectores;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConectorCamarero extends Conector {

    int id = 0;
    
    public String convertirXMLtoString() {

        Node nPadre = xmlFiles.get(0).getDocumentElement();

        StringBuilder mensaje = new StringBuilder();

        // Nodo Padre
        System.out.println("\n*************************PUERTO FINAL**************************");
        mostrarNodos(nPadre.getChildNodes(), mensaje);
        String mensajeFinal = "<" + nPadre.getNodeName() + ">"
                + mensaje.toString()
                + "</" + nPadre.getNodeName() + ">";
        //Cogemos los Nodos del padre recursivamente
        System.out.println(mensajeFinal);
        System.out.println("\n*************************------------**************************");

        return mensajeFinal;
    }

    public void mostrarNodos(NodeList nHijos, StringBuilder mensaje) {

        for (int i = 0; i < nHijos.getLength(); i++) {
            Node nAux = nHijos.item(i);

            boolean esTexto = nAux.getNodeName().startsWith("#");

            if (!esTexto) {
                mensaje.append(" ").append("<").append(nAux.getNodeName()).append(">");
            }
            if (esTexto) {
                mensaje.append(" ").append(nAux.getTextContent());
            }
            if (!esTexto) {
                if (nAux.getChildNodes() != null) {//Si tiene hijos los mostramos 
                    mostrarNodos(nAux.getChildNodes(), mensaje);
                }
                mensaje.append(" ").append("</").append(nAux.getNodeName()).append(">");
            }

        }

    }

    public boolean CargarBD(String NombreTabla) {

        try {
            String Mensaje = convertirXMLtoString();
            
            PreparedStatement ps = getConexion().prepareStatement("INSERT INTO" + NombreTabla + " VALUES "
                    + "(?,?)");
            ps.setInt(1, id);
            ps.setString(2, URL);
            ps.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            System.out.println("Error en Conector Camarero");
            return false;
        }
    }

    @Override
    public Document leerMensaje(String Table) {

        CargarBD(Table);
        return xmlFiles.remove(0);
    }

}
