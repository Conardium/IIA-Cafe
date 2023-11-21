package controlador;

import conectores.ConectorBarman;
import conectores.ConectorCamarero;
import conectores.ConectorComandas;
import tareas.*;
import java.util.Scanner;


public class ControladorCafe {

    //**********Datos BD*********//
    static String sgbd = "derby", ip = "localhost", service_bd = "IIA_Cafe", usuario = "user_web", password = "web";

    /**
     * ***************Tablas BD****************
     */
    static String TablaBebidas = "BEBIDAS";

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
    static Distributor TDistributor = new Distributor(FiltroDistributor,2);
    static Replicator TReplicatorC = new Replicator(2);
    static Replicator TReplicatorH = new Replicator(2);
    static Translator TTranslatorC = new Translator(FiltroTranslator,ExpresionTranslator);
    static Translator TTranslatorH = new Translator(FiltroTranslator,ExpresionTranslator);
    static Correlator TCorrelatorC = new Correlator(FiltroCorrelator);
    static Correlator TCorrelatorH = new Correlator(FiltroCorrelator);
    static Content_Enricher TCEnricherC = new Content_Enricher(FiltroContext,FiltroBody);
    static Content_Enricher TCEnricherH = new Content_Enricher(FiltroContext,FiltroBody);
    static Merger TMerger = new Merger();
    static Aggregator TAggregator = new Aggregator(ExpresionAggregator,FiltroAgregator);

    //Constructor//
    public ControladorCafe() {

        //Enlazamos los slots;
        TSplitter.enlazarSlotE(CInicial.getPuerto().enlazarSlotS());
        TDistributor.enlazarSlotE(TSplitter.enlazarSlotS());
        TReplicatorC.enlazarSlotE(TDistributor.enlazarSlotS(1));
        TReplicatorH.enlazarSlotE(TDistributor.enlazarSlotS(2));
        TTranslatorC.enlazarSlotE(TReplicatorC.enlazarSlotS(1));
        TTranslatorH.enlazarSlotE(TReplicatorH.enlazarSlotS(1));
        cBC.getPuerto().enlazarSlotE(TTranslatorC.enlazarSlotS());
        cBH.getPuerto().enlazarSlotE(TTranslatorH.enlazarSlotS());
        TCorrelatorC.enlazarSlotE(cBC.getPuerto().enlazarSlotS(),1);
        TCorrelatorH.enlazarSlotE(cBH.getPuerto().enlazarSlotS(),1);
        TCorrelatorC.enlazarSlotE(TReplicatorC.enlazarSlotS(2),2);
        TCorrelatorH.enlazarSlotE(TReplicatorH.enlazarSlotS(2),2);
        TCEnricherC.enlazarSlotEContext(TCorrelatorC.enlazarSlotS(1));
        TCEnricherC.enlazarSlotEBody(TCorrelatorC.enlazarSlotS(2));
        TCEnricherH.enlazarSlotEContext(TCorrelatorH.enlazarSlotS(1));
        TCEnricherH.enlazarSlotEBody(TCorrelatorH.enlazarSlotS(2));
        TMerger.enlazarSlotE(TCEnricherC.enlazarSlotS());
        TMerger.enlazarSlotE(TCEnricherH.enlazarSlotS());
        TAggregator.enlazarSlotE(TMerger.enlazarSlotS());
        cCam.getPuerto().enlazarSlotE(TAggregator.enlazarSlotS());

        //Cargamos Datos
        if (CInicial.CargarFicheros() == true){
            for (int i = 1; i <= CInicial.numMensajes(); i++) {
                
                //=====> Escribimos los Mensajes en el puerto Inicial del 1 al 9
                CInicial.escribirPuerto();

                //***************************************************//
                //********************SPLITTER***********************//
                //***************************************************//
                //=====> Actua el Splitter
                TSplitter.realizarTarea();

                //***************************************************//
                //******************DISTRIBUTOR**********************//
                //***************************************************//
                //=====> Actua el Distributor
                TDistributor.realizarTarea();

                //***************************************************//
                //******************REPLICATOR***********************//
                //***************************************************//
                //=====> Actua el Replicator para los cold
                TReplicatorC.realizarTarea(); //Multiplica la cantidad
                //=====> Actua el Replicator para los hot
                TReplicatorH.realizarTarea(); //Multiplica la cantidad

                //***************************************************//
                //******************TRANSLATOR***********************//
                //***************************************************//
                //=====> Actua el Translator cold
                TTranslatorC.realizarTarea();
                //=====> Actua el Translator hot
                TTranslatorH.realizarTarea();

                //***************************************************//
                //*****************PUERTO CAMARERO*******************//
                //***************************************************//

                //========> Conector escribe mensajes cold, el puerto los pasa al slot
                cBC.busquedaBD(TablaBebidas, sgbd, ip, service_bd, usuario, password);

                //========> Conector escribe mensajes hot, el puerto los pasa al slot
                cBH.busquedaBD(TablaBebidas, sgbd, ip, service_bd, usuario, password);

                //***************************************************//
                //******************CORRELATOR***********************//
                //***************************************************//
                //=====> Actua el Correlator cold
                TCorrelatorC.realizarTarea();

                //=====> Actua el Correlator hot
                TCorrelatorH.realizarTarea();


                //***************************************************//
                //***************CONTEXT_ENRICHER********************//
                //***************************************************//
                //=====> Actua el Content Enricher cold
                TCEnricherC.realizarTarea();

                //=====> Actua el Content Enricher hot
                TCEnricherH.realizarTarea();

                //***************************************************//
                //**********************MERGER***********************//
                //***************************************************//
                //=====> Actua el Merger
                TMerger.realizarTarea();

                //***************************************************//
                //********************AGGREGATOR*********************//
                //***************************************************//
                TAggregator.realizarTarea();

                // El leer mensaje, escribirá el mensaje en la carpeta
                cCam.escribirFicheros();

                System.out.println("Presiona Enter para continuar...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            }

            //Borramos la carpeta
            System.out.println("Vamos a Borrar los mensajes finales de la carpeta");
            System.out.println("Presiona Enter para continuar...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            cCam.borrarFicheros();
        }
    }

}