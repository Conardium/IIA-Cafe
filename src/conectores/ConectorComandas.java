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
public class ConectorComandas extends Conector {


    public ConectorComandas() {

        xmlFiles = new ArrayList<>();
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
    }
   


}
