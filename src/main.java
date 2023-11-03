
import conectores.ConectorComandas;
import puertos.Puerto;
import slot.Slot;
import tareas.Distributor;
import tareas.Replicator;
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
    static Slot S3C = new Slot();
    static Slot S3H = new Slot();
    static Slot S4C = new Slot();
//    static Slot S4H = new Slot();
    static Slot S5C = new Slot();
    
    //************Tareas**************//
    static Splitter TSplitter = new Splitter();
    static Distributor TDistributor = new Distributor();
    static Replicator TReplicator = new Replicator();
    
    
    //main//
    public static void main(String[] args) {
        
        for (int i = 1; i <= 9; i++) {
            
            //Escribimos los Mensajes en el puerto Inicial del 1 al 9
            PInicial.setPuerto(CInicial.leerMensaje()); //FUNCIONANDO
            SInicial.setMensaje(PInicial.getPuerto()); //FUNCIONANDO

            //Actua el Splitter
            TSplitter.getMSJslot(SInicial.getMensaje()); //FUNCIONANDO
            TSplitter.realizarTarea(); //FUNCIONANDO
            for (int j = 0 ; j < TSplitter.devolverNConjuntos(); j++)
            {
                S2.setMensaje(TSplitter.setMSJslot(0));//FUNCIONANDO
            }
            //Actua el Distributor
            for (int j = 0; j < S2.devolverNConjuntos(); j++)
            {
                TDistributor.getMSJslot(S2.getMensaje());
                TDistributor.realizarTarea();
            }
            //para los cold
            for (int j = 0; j < TDistributor.devolverNCold(); j++)
            {
                S3C.setMensaje(TDistributor.setMSJslot(1));
            }
            //para los hot
            /*for (int j = 0; j < TDistributor.devolverNHot(); j++)
            {
                S3H.setMensaje(TDistributor.setMSJslot(2));
            }*/
            //Actua el Replicator para los cold
            for (int j = 0; j < S3C.devolverNConjuntos(); j++)
            {
                TReplicator.getMSJslot(S3C.getMensaje());
                TReplicator.realizarTarea(); //Multiplica la cantidad
                S4C.setMensaje(TReplicator.setMSJslot(0));
                S5C.setMensaje(TReplicator.setMSJslot(0));
            }
            //Actua el Replicator para los hot
            /*for (int j = 0; j < S3H.devolverNConjuntos(); j++)
            {
                TReplicator.getMSJslot(S3H.getMensaje());
                TReplicator.realizarTarea(); //Multiplica la
                S4H.setMensaje(TReplicator.setMSJslot(0));
                S5H.setMensaje(TReplicator.setMSJslot(0));
            }*/
            //Actua el Translator
        }

        // ===== PARA COMPROBAR SI LA INFORMACION DEL DOCUMENTO LLEGA BIEN ======
        /*System.out.println(Mensaje.getFirstChild().getFirstChild().getNodeName()  + " "
                + Mensaje.getFirstChild().getFirstChild().getTextContent());*/
        

    }
    
    
}
