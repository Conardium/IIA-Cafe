/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tareas;

import org.w3c.dom.Document;
import slot.Slot;

public abstract class Tarea {

    public void realizarTarea(){};

    public void getMSJslot(Document xmlE){};

    public Document setMSJslot(int v){return null;};
    
    public int calcularSalidas(){return 0;};

    //Asociamos el puerto de entrada
    public void enlazarSlotE(Slot slot){};

    //Pasamos slot salida
    public Slot enlazarSlotS(){return null;};
    
}
