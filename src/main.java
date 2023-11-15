
import conectores.*;
import java.util.ArrayList;
import java.util.Scanner;
import puertos.Puerto;
import slot.Slot;
import tareas.*;

/**
 *
 * @author Cristian
 */
public class main {

    //**********Datos BD*********//
    static String sgbd = "derby", ip = "localhost", service_bd = "IIA_Cafe", usuario = "user_web", password = "web";

    /**
     * ***************Tablas BD****************
     */
    static String TablaInicial = "MENSAJEENTRADA", TablaFinal = "MENSAJESALIDA", TablaBebidas = "BEBIDAS";

     //**********Expresiones XML - XPATH********//
    
    static String Expresion = "//drinks//drink";
    static String FiltroDistributor = "//type";
    static String ExpresionTranslator = "SELECT NAME, EXIST\n\t FROM " + TablaBebidas + "\n\tWHERE NAME = '";
    static String FiltroTranslator = "//name";
    
    //**********Iniciales*********//
    static ConectorComandas CInicial = new ConectorComandas();
    static ConectorBarman cBC = new ConectorBarman();
    static ConectorBarman cBH = new ConectorBarman();
    static ConectorCamarero cCam = new ConectorCamarero();

    static Puerto P_Inicial = new Puerto();
    static Puerto P_Final = new Puerto();
    static Puerto P_ES_Cold = new Puerto();
    static Puerto P_ES_Hot = new Puerto();

    //************Slots**********//
    static Slot SInicial = new Slot("Inicial");
    static Slot S2 = new Slot("S2");
    static Slot S3C = new Slot("3C");
    static Slot S3H = new Slot("3H");
    static Slot S4C = new Slot("4C");
    static Slot S4H = new Slot("4H");
    static Slot S5C = new Slot("5C");
    static Slot S5H = new Slot("5H");
    static Slot S6C = new Slot("6C");
    static Slot S6H = new Slot("6H");
    static Slot S7C = new Slot("7C");
    static Slot S7H = new Slot("7H");
    static Slot S8C = new Slot("8C");
    static Slot S8H = new Slot("8H");
    static Slot S9C = new Slot("9C");
    static Slot S9H = new Slot("9H");
    static Slot S10C = new Slot("10C");
    static Slot S10H = new Slot("10H");
    static Slot S11 = new Slot("11");
    static Slot SFinal = new Slot("Final");

    //************Tareas**************//
    static Splitter TSplitter = new Splitter(Expresion);
    static Distributor TDistributor = new Distributor(FiltroDistributor);
    static Replicator TReplicator = new Replicator(2);
    static Translator TTranslatorC = new Translator(FiltroTranslator,ExpresionTranslator);
    static Translator TTranslatorH = new Translator(FiltroTranslator,ExpresionTranslator);
    static Correlator TCorrelatorC = new Correlator();
    static Correlator TCorrelatorH = new Correlator();
    static Content_Enricher TCEnricherC = new Content_Enricher();
    static Content_Enricher TCEnricherH = new Content_Enricher();
    static Merger TMerger = new Merger();
    static Aggregator TAggregator = new Aggregator();

    //main//
    public static void main(String[] args) {
        
        
        //Cargamos Datos
        if (CInicial.CargarBD(TablaInicial, sgbd, ip, service_bd, usuario, password)) {
            for (int i = 1; i <= CInicial.numMensajes(); i++) {

                //=====> Escribimos los Mensajes en el puerto Inicial del 1 al 9
                P_Inicial.setPuerto(CInicial.leerMensaje()); //FUNCIONANDO
                SInicial.setMensaje(P_Inicial.getPuerto()); //FUNCIONANDO

                //***************************************************//
                //********************SPLITTER***********************//
                //***************************************************//
                //=====> Actua el Splitter
                TSplitter.getMSJslot(SInicial.getMensaje()); //FUNCIONANDO
                TSplitter.realizarTarea(); //FUNCIONANDO
                for (int j = 0; j < TSplitter.devolverNConjuntos(); j++) {
                    S2.setMensaje(TSplitter.setMSJslot(0));//FUNCIONANDO
                }

                //***************************************************//
                //***************************************************//
                //***************************************************//
                
                //***************************************************//
                //******************DISTRIBUTOR**********************//
                //***************************************************//
                //=====> Actua el Distributor
                for (int j = 0; j < S2.devolverNConjuntos(); j++) {
                    TDistributor.getMSJslot(S2.getMensaje());
                    TDistributor.realizarTarea();
                }
                //=====> coloca en el slot el Distributor para los cold
                while (TDistributor.devolverN(0) > 0) {
                    S3C.setMensaje(TDistributor.setMSJslot(0));
                }
                //=====> coloca en el slot el Distributor para los hot
                while (TDistributor.devolverN(1) > 0) {
                    S3H.setMensaje(TDistributor.setMSJslot(1));
                }

                //***************************************************//
                //***************************************************//
                //***************************************************//
                
                //***************************************************//
                //******************REPLICATOR***********************//
                //***************************************************//
                //=====> Actua el Replicator para los cold
                for (int j = 0; j < S3C.devolverNConjuntos(); j++) {
                    TReplicator.getMSJslot(S3C.getMensaje());
                    TReplicator.realizarTarea(); //Multiplica la cantidad
                    S4C.setMensaje(TReplicator.setMSJslot(0));
                    S5C.setMensaje(TReplicator.setMSJslot(1));
                }
                //=====> Actua el Replicator para los hot
                for (int j = 0; j < S3H.devolverNConjuntos(); j++) {
                    TReplicator.getMSJslot(S3H.getMensaje());
                    TReplicator.realizarTarea(); //Multiplica la cantidad
                    S4H.setMensaje(TReplicator.setMSJslot(0));
                    S5H.setMensaje(TReplicator.setMSJslot(1));
                }

                //***************************************************//
                //***************************************************//
                //***************************************************//
                
                //***************************************************//
                //******************TRANSLATOR***********************//
                //***************************************************//
                //=====> Actua el Translator cold
                for (int j = 0; j < S4C.devolverNConjuntos(); j++) {
                    TTranslatorC.getMSJslot(S4C.getMensaje());
                    TTranslatorC.realizarTarea();
                    S6C.setMensaje(TTranslatorC.setMSJslot(0));
                }

                //=====> Actua el Translator hot
                for (int j = 0; j < S4H.devolverNConjuntos(); j++) {
                    TTranslatorH.getMSJslot(S4H.getMensaje());
                    TTranslatorH.realizarTarea();
                    S6H.setMensaje(TTranslatorH.setMSJslot(0));
                }

                //***************************************************//
                //***************************************************//
                //***************************************************//
                //========> Puerto lee mensajes cold, el conector los lee y los transforma
                for (int j = 0; j < S6C.devolverNConjuntos(); j++) {
                    P_ES_Cold.setPuerto(S6C.getMensaje());
                    cBC.escribirMensaje(P_ES_Cold.getPuerto());
                    cBC.busquedaBD(TablaBebidas, sgbd, ip, service_bd, usuario, password);
                }
                //========> Conector escribe mensajes cold, el puerto los pasa al slot 
                for (int j = 0; j < cBC.getTotal(); j++) {
                    P_ES_Cold.setPuerto(cBC.devolverSQL());
                    S7C.setMensaje(P_ES_Cold.getPuerto());
                }

                //========> Puerto lee mensajes hot, el conector los lee y los transforma
                for (int j = 0; j < S6H.devolverNConjuntos(); j++) {
                    P_ES_Hot.setPuerto(S6H.getMensaje());
                    cBH.escribirMensaje(P_ES_Hot.getPuerto());
                    cBH.busquedaBD(TablaBebidas, sgbd, ip, service_bd, usuario, password);
                }
                //========> Conector escribe mensajes hot, el puerto los pasa al slot 
                for (int j = 0; j < cBH.getTotal(); j++) {
                    P_ES_Hot.setPuerto(cBH.devolverSQL());
                    S7H.setMensaje(P_ES_Hot.getPuerto());
                }

                //***************************************************//
                //******************CORRELATOR***********************//
                //***************************************************//
                //=====> Actua el Correlator cold
                for (int j = 0; j < S5C.devolverNConjuntos(); j++) { //puede ser tambien S7C.devolverNConjuntos()
                    TCorrelatorC.getMSJslot(S5C.getMensaje());
                    TCorrelatorC.getMSJslot(S7C.getMensaje());
                }
                TCorrelatorC.realizarTarea();

                for (int j = 0; j < TCorrelatorC.devolverNConjuntos(); j++) {
                    S8C.setMensaje(TCorrelatorC.setMSJslot(1));
                    S9C.setMensaje(TCorrelatorC.setMSJslot(2));
                }
                //=====> Actua el Correlator hot
                for (int j = 0; j < S5H.devolverNConjuntos(); j++) { //puede ser tambien S7C.devolverNConjuntos()
                    TCorrelatorH.getMSJslot(S5H.getMensaje());
                    TCorrelatorH.getMSJslot(S7H.getMensaje());
                }
                TCorrelatorH.realizarTarea();
                for (int j = 0; j < TCorrelatorH.devolverNConjuntos(); j++) {
                    S8H.setMensaje(TCorrelatorH.setMSJslot(1));
                    S9H.setMensaje(TCorrelatorH.setMSJslot(2));
                }
                //***************************************************//
                //***************************************************//
                //***************************************************//

                //***************************************************//
                //***************CONTEXT_ENRICHER********************//
                //***************************************************//
                //=====> Actua el Content Enricher cold
                for (int j = 0; j < S8C.devolverNConjuntos(); j++) {

                    TCEnricherC.getMSJslot(S8C.getMensaje());
                    TCEnricherC.getMSJslot(S9C.getMensaje());
                    TCEnricherC.realizarTarea();
                    S10C.setMensaje(TCEnricherC.setMSJslot(0));
                }

                //=====> Actua el Content Enricher hot
                for (int j = 0; j < S8H.devolverNConjuntos(); j++) {
                    TCEnricherH.getMSJslot(S8H.getMensaje());
                    TCEnricherH.getMSJslot(S9H.getMensaje());
                    TCEnricherH.realizarTarea();
                    S10H.setMensaje(TCEnricherH.setMSJslot(0));
                }

                //***************************************************//
                //***************************************************//
                //***************************************************//
                //***************************************************//
                //**********************MERGER***********************//
                //***************************************************//
                //=====> Actua el Merger para los cold
                for (int j = 0; j < S10C.devolverNConjuntos(); j++) {
                    TMerger.getMSJslot(S10C.getMensaje());
                    TMerger.realizarTarea();
                    S11.setMensaje(TMerger.setMSJslot(0));
                }

                //=====> Actua el Merger para los hot
                for (int j = 0; j < S10H.devolverNConjuntos(); j++) {
                    TMerger.getMSJslot(S10H.getMensaje());
                    TMerger.realizarTarea();
                    S11.setMensaje(TMerger.setMSJslot(0));
                }

                //***************************************************//
                //***************************************************//
                //***************************************************//
                
                //***************************************************//
                //********************AGGREGATOR*********************//
                //***************************************************//
                //=====> Actua el Aggregator
                for (int j = 0; j < S11.devolverNConjuntos(); j++) {

                    TAggregator.getMSJslot(S11.getMensaje());
                }
                //Mientras queden documentos en su entrada que siga
                //Esto no hace falta realmente, porque todo esta dentro del for
                //Por lo tanto en una simple llamada a realizarTarea se quedará sin XMLEntrada
                while (TAggregator.devolverNConjuntos() != 0) {
                    TAggregator.realizarTarea();
                    SFinal.setMensaje(TAggregator.setMSJslot(0));
                }

                //***************************************************//
                //***************************************************//
                //***************************************************//
                //=====> Escribimos los Mensajes en el puerto Final
                P_Final.setPuerto(SFinal.getMensaje());

                //=====> El conector Camarero recoge los datos del puerto Final
                cCam.escribirMensaje(P_Final.getPuerto());
                // El leer mensaje, escribirá el mensaje en la BD y lo mostrará por salida
                cCam.leerMensaje(TablaFinal, sgbd, ip, service_bd, usuario, password);

                System.out.println("Presiona Enter para continuar...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            }
            //Borramos la BD
            System.out.println("Vamos a Borrar los mensajes finales de la BD");
            System.out.println("Presiona Enter para continuar...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            cCam.borrarBD(TablaFinal, sgbd, ip, service_bd, usuario, password);
        }

        // ===== PARA COMPROBAR SI LA INFORMACION DEL DOCUMENTO LLEGA BIEN ======
        /*System.out.println(Mensaje.getFirstChild().getFirstChild().getNodeName()  + " "
                + Mensaje.getFirstChild().getFirstChild().getTextContent());*/
    }

}
