/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conectores;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 *
 * @author Cristian
 */
public class ConectorComandas extends Conectores{

    ArrayList<Document> xmlFiles;

    public ConectorComandas() {

        xmlFiles = new ArrayList<>();
        String direccionAux = "./comandas/order";

        for (int i = 1; i <= 9; i++) {
            try {
                File ficheroComandas = new File(direccionAux + i + ".xml");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document MensajeXML = builder.parse(ficheroComandas);
                xmlFiles.add(MensajeXML);
                
            } catch (Exception ex) {
                System.out.println("Error en el inicio de ConectorComandas");
            }

        }
    }
   


}
