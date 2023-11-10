/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conectores;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import tareas.Translator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Cristian
 */
public class ConectorComandas extends Conector {

    int id=0;
    String Mensaje = "";

    /*public ConectorComandas() {

        String direccionAux = System.getProperty("user.dir") + "\\src\\comandas\\order";
        System.out.println(direccionAux);

        for (int i = 1; i <= 9; i++) {
            try {
                File ficheroComandas = new File(direccionAux + i + ".xml");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document MensajeXML = builder.parse(ficheroComandas);
                xmlFiles.add(MensajeXML);

            } catch (Exception ex) {
                System.out.println("Error en el inicio de ConectorComandas");
            }

        }
    }*/

    public boolean CargarBD(String NombreTabla, String sgbd, String ip, String service_bd, String usuario,
                            String password) {
        try {
            Conexion(sgbd, ip,  service_bd,  usuario, password);

            String consulta = "SELECT * FROM " + NombreTabla;
            PreparedStatement ps = getConexion().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
                Mensaje = rs.getString(2);
                TransformarStringXML();
            }
            //Nos desconectamos
            desconexion();

            return true;
        } catch (Exception ex) {
            System.out.println("Error en el Conector Comandas");
            return false;
        }
    }

    public void TransformarStringXML(){

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


    public Document leerMensaje() {

        return xmlFiles.remove(0);
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
