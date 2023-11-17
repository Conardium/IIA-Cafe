/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import conectores.ConectorBarman;
import conectores.ConectorCamarero;
import conectores.ConectorComandas;
import puertos.PuertoEoS;
import slot.Slot;
import tareas.*;
import java.util.Scanner;

/**
 *
 * @author Cristian
 */
public class Controlador {

    //**********Datos BD*********//
    static String sgbd = "derby", ip = "localhost", service_bd = "IIA_Cafe", usuario = "user_web", password = "web";

    /**
     * ***************Tablas BD****************
     */
    static String TablaInicial = "MENSAJEENTRADA", TablaFinal = "MENSAJESALIDA", TablaBebidas = "BEBIDAS";

    //**********Expresiones XML - XPATH********//

    static String ExpresionSplitter = "//drinks//drink"; //Padre + hijo de donde hacer Split
    static String FiltroDistributor = "//type"; //Diferentes valores por donde se distribuye
    static String ExpresionTranslator = "SELECT NAME, EXIST\n\t FROM " + TablaBebidas + "\n\tWHERE NAME = '";//En lo que se convierte el XML
    static String FiltroTranslator = "//name";//Para buscar en la base datos
    static String FiltroCorrelator = "//name";//Por donde hacer la correlacion
    static String FiltroContext = "//exist";//El nodo contexto
    static String FiltroBody = "//drink";//El nodo body al que añadirlo
    static String ExpresionAggregator = "//drinks//drink";//Padre + hijo de donde se hace Agregacion
    static String FiltroAgregator = "//order_id/text()"; //Para saber que trozos son iguales

    //**********Iniciales*********//
    static ConectorComandas CInicial = new ConectorComandas();
    static ConectorBarman cBC = new ConectorBarman();
    static ConectorBarman cBH = new ConectorBarman();
    static ConectorCamarero cCam = new ConectorCamarero();


    //************Tareas**************//
    static Splitter TSplitter = new Splitter(ExpresionSplitter);
    static Distributor TDistributor = new Distributor(FiltroDistributor);
    static Replicator TReplicator = new Replicator(2);
    static Translator TTranslatorC = new Translator(FiltroTranslator,ExpresionTranslator);
    static Translator TTranslatorH = new Translator(FiltroTranslator,ExpresionTranslator);
    static Correlator TCorrelatorC = new Correlator(FiltroCorrelator);
    static Correlator TCorrelatorH = new Correlator(FiltroCorrelator);
    static Content_Enricher TCEnricherC = new Content_Enricher(FiltroContext,FiltroBody);
    static Content_Enricher TCEnricherH = new Content_Enricher(FiltroContext,FiltroBody);
    static Merger TMerger = new Merger();
    static Aggregator TAggregator = new Aggregator(ExpresionAggregator,FiltroAgregator);

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
                TDistributor.realizarTarea();

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
                TTranslatorH.realizarTarea();

                //***************************************************//
                //***************************************************//
                //***************************************************//
                //========> Puerto lee mensajes cold, el conector los lee y los transforma


                //========> Conector escribe mensajes cold, el puerto los pasa al slot
                for (int j = 0; j < cBC.getTotal(); j++) {
                    P_ES_Cold.setPuerto(cBC.devolverSQL());
                    S7C.setMensaje(P_ES_Cold.getPuerto());
                }

                //========> Puerto lee mensajes hot, el conector los lee y los transforma
                cBH.busquedaBD(TablaBebidas, sgbd, ip, service_bd, usuario, password);

                //========> Conector escribe mensajes hot, el puerto los pasa al slot
                while (cBH.getPuerto().nMensajes() != 0) {
                    cBH.leerPuerto();
                    cBH.busquedaBD(TablaBebidas, sgbd, ip, service_bd, usuario, password);
                    cBH.escribirPuerto();
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
                TCEnricherH.realizarTarea();

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
                TAggregator.realizarTarea();

                //=====> El conector Camarero recoge los datos del puerto Final
                cCam.escribirMensaje();
                // El leer mensaje, escribirá el mensaje en la BD y lo mostrará por salida
                cCam.anadirMensajeBD(TablaFinal, sgbd, ip, service_bd, usuario, password);

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
    }

}