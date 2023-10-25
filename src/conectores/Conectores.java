/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conectores;

import java.util.ArrayList;
import org.w3c.dom.Document;

/**
 *
 * @author Cristian
 */
public abstract class  Conectores {
    
    ArrayList<Document> xmlFiles;
    
    public Document leerMensaje() {
        
        return xmlFiles.remove(0); 
    }
    
    public void escribirMensaje(Document Mensaje) {
        
        xmlFiles.add(Mensaje);
    }
    
}
