/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package slot;

import java.util.ArrayList;
import org.w3c.dom.Document;

/**
 *
 * @author Cristian
 */
public class Slot {
    
    private ArrayList<Document> xmlBuffer;
    //hace falta mas metodos porque no todos leen los mensajes de la misma forma dice el profe

    public Document getMensaje() {
        
        return xmlBuffer.remove(0);
    }
    
    public void setMensaje(Document Mensaje) {
        
        xmlBuffer.add(Mensaje);
    }
    
    
    
    
}
