
import conectores.ConectorComandas;
import puertos.Puerto;
import slot.Slot;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Cristian
 */
public class main {
    
    
    //**********Iniciales*********//
    static ConectorComandas CInicial = new ConectorComandas();
    static Puerto PInicial = new Puerto();
    
    //************Slots**********//
    static Slot SInicial = new Slot();
    
    
    
    
    //main//
    public static void main(String[] args) {
        
        for (int i = 1; i <= 9; i++) {
            
            //Escribimos los Mensajes en el puerto Inicial 1 a 1
            PInicial.setPuerto(CInicial.leerMensaje());
            SInicial.setMensaje(PInicial.getPuerto());
        }
        

    }
    
    
}
