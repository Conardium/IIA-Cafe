
import conectores.ConectorComandas;
import puertos.Puerto;
import slot.Slot;
import tareas.Splitter;

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
    static Slot S2 = new Slot();
    
    //************Tareas**************//
    static Splitter TSplitter = new Splitter();
    
    
    //main//
    public static void main(String[] args) {
        
        for (int i = 1; i <= 9; i++) {
            
            //Escribimos los Mensajes en el puerto Inicial del 1 al 9
            PInicial.setPuerto(CInicial.leerMensaje()); //FUNCIONANDO
            SInicial.setMensaje(PInicial.getPuerto()); //FUNCIONANDO
            //Actua el Splitter
            TSplitter.getMSJslot(SInicial.getMensaje()); //FUNCIONANDO
            TSplitter.realizarTarea();
            for (int j = 0 ; j < TSplitter.devolverNConjuntos(); j++)
            {
                S2.setMensaje(TSplitter.setMSJslot());
            }
        }

        // ===== PARA COMPROBAR SI LA INFORMACION DEL DOCUMENTO LLEGA BIEN ======
        /*System.out.println(Mensaje.getFirstChild().getFirstChild().getNodeName()  + " "
                + Mensaje.getFirstChild().getFirstChild().getTextContent());*/
        

    }
    
    
}
