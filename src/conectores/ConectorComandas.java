
package conectores;

import java.io.File;
import org.w3c.dom.Document;

import puertos.PuertoEoS;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConectorComandas extends Conector {

    private int nEllamada = 0;
    private PuertoEoS puerto = new PuertoEoS(2);

    private final String directorioActual = System.getProperty("user.dir") + "\\src\\comandas";

    public boolean CargarFicheros() {
        
        File xmlDirectorio = new File(directorioActual);
        File[] xmls = xmlDirectorio.listFiles();
        
        try {
            for (int i = 1; i <= xmls.length; i++) {

                File archivoXML = new File(directorioActual + "\\order" + i + ".xml");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document MensajeXML = builder.parse(archivoXML);

                xmlFiles.add(MensajeXML);

                nEllamada++;
            }
        } catch (Exception ex) {
            System.out.println("Error en el Conector Comandas");
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }

    public PuertoEoS getPuerto() {
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

}
