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
    
    private ArrayList<Document> xmlBuffer = new ArrayList<>();
    private int nEllamada = 0;
    //hace falta mas metodos porque no todos leen los mensajes de la misma forma dice el profe

    public Document getMensaje() {

        return xmlBuffer.remove(0);
    }
    
    public void setMensaje(Document Mensaje) {
        //Mensajes completos
        /*System.out.println(Mensaje.getFirstChild().getFirstChild().getNodeName()  + " "
                + Mensaje.getFirstChild().getFirstChild().getTextContent());*/

        //Mensajes parciales
        //System.out.println(Mensaje.getFirstChild());

        xmlBuffer.add(Mensaje);
        nEllamada = xmlBuffer.size();
    }
    public int devolverNConjuntos() {
        //System.out.println(xmlSalida.size());
        return nEllamada;
    }
    
    
    
    
}
