package conectores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import puertos.PuertoEoS;

public class ConectorCamarero extends Conector {

    private int id = 0;
    private final PuertoEoS puerto = new PuertoEoS(1);
    private final String directorioActual = System.getProperty("user.dir") + "\\src\\comandas_output";

    public String convertirXMLtoString() {

        Node nPadre = xmlFiles.get(0).getDocumentElement();

        StringBuilder mensaje = new StringBuilder();

        mostrarNodos(nPadre.getChildNodes(), mensaje, 1);
        String mensajeFinal = "<" + nPadre.getNodeName() + ">"
                + mensaje.toString()
                + "\n</" + nPadre.getNodeName() + ">";

        System.out.println("\n*******************PUERTO SALIDA***********************");
        System.out.println("El Mensaje llega al Puerto Final");
        System.out.println(mensajeFinal);
        System.out.println("\n********************-----------*************************");

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

    public PuertoEoS getPuerto() {
        return puerto;
    }

    @Override
    public void leerPuerto() {

        xmlFiles.add(puerto.getPuerto());
    }

    public boolean escribirFicheros() {

        try {
            while (puerto.enlazarSlotS().devolverNConjuntos() != 0) {

                leerPuerto();
                this.id++;

                System.out.println(id);
                String nombreArchivo = "order" + this.id + "_output.xml";

                // Crea un nuevo objeto File con la ruta completa
                File nuevoArchivo = new File(directorioActual, nombreArchivo);

                String mensaje = convertirXMLtoString();

                try ( BufferedWriter writer = new BufferedWriter(new FileWriter(nuevoArchivo))) {
                    writer.write(mensaje);
                } catch (IOException e) {
                    System.out.println("Error al escribir en el archivo: " + e.getMessage());
                }

                xmlFiles.remove(0);
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Error en Conector Camarero");
        }
        return false;

    }

    //Usamos este metodo solo para las pruebas
    public boolean borrarFicheros() {
        try {

            File xmlDirectorio = new File(directorioActual);
            File[] xmls = xmlDirectorio.listFiles();

            for (int i = 1; i <= xmls.length + 1; i++) {
                File archivoABorrar = new File(directorioActual + "\\order" + i + "_output.xml");
                archivoABorrar.delete();
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Error en Conector Camarero - No se han podido borrar los ficheros XML output");
        }
        return false;
    }

}
