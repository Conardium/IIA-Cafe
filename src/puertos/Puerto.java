/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puertos;

import org.w3c.dom.Document;

/**
 *
 * @author Cristian
 */
public class Puerto {
    
    Document xmlFile;

    public Document getPuerto() {

        //System.out.println(xmlFile.getFirstChild().getFirstChild().getTextContent());
        return xmlFile;
    }

    public void setPuerto(Document xmlFile) {
        this.xmlFile = xmlFile;
    }
    
    
    
}
