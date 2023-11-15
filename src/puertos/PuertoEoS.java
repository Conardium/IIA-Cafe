package puertos;

import org.w3c.dom.Document;
import slot.Slot;

public class PuertoEoS {

    private Slot slot;
    private int tipoPuerto;//1 entrada, 2 salida

    public PuertoEoS (int tipoPuerto) {
        this.tipoPuerto = tipoPuerto;
        if(tipoPuerto == 2)
            slot = new Slot("Puerto-Salida");
    }

    public Document getPuerto() {

            return slot.getMensaje();
    }

    public void setPuerto(Document xmlFile) {

            slot.setMensaje(xmlFile);
    }

    public void enlazarSlotE(Slot slot){

        this.slot = slot;
    }

    public Slot enlazarSlotS(){

        return slot;
    }
}
