
package tareas;

import slot.Slot;

public abstract class Tarea {

    public void realizarTarea(){};

    protected void getMSJslot(){};

    protected void setMSJslot(){};
    
    //Asociamos el puerto de entrada
    public void enlazarSlotE(Slot slot){};

    //Pasamos slot salida
    public Slot enlazarSlotS(){return null;};
    
}
