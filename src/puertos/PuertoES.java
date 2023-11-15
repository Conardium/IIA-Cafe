package puertos;

import org.w3c.dom.Document;
import slot.Slot;

public class PuertoES {

    private Document xmlFile;

    private Slot slotE;
    private Slot slotS;

    public PuertoES () {
        slotS = new Slot("Puerto-E.Salida");
    }

    public Document getPuertoE() {
        return slotE.getMensaje();
    }

    public void setPuertoE(Document xmlFile) {
        slotE.setMensaje(xmlFile);
    }

    public Document getPuertoS() {
        return slotS.getMensaje();
    }

    public void setPuertoS(Document xmlFile) {

        slotS.setMensaje(xmlFile);
    }

    //Asociamos el puerto de entrada
    public void enlazarSlotE(Slot slot){
        this.slotE = slot;
    }

    //Pasamos slot salida
    public Slot enlazarSlotS(){
        return slotS;
    }

}
