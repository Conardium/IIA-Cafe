package controlador;

import conectores.*;
import tareas.*;

import java.util.Scanner;

public class ControladorProblema1 {
    //**********Datos BD*********//
    static String sgbd = "derby", ip = "localhost", service_bd = "IIA_Cafe", usuario = "user_web", password = "web";

    /**
     * ***************Tablas BD****************
     */
    static String TablaInicial = "CALIFICACIONES", TablaFinalEmail = "EMAIL", TablaFinalTelefono = "SMS", TablaAlumnos= "ALUMNOS";

    //**********Expresiones XML - XPATH********//

    static String ExpresionSplitter = "//alumnos//alumno"; //Padre + hijo de donde hacer Split
    static String ExpresionTranslator = "SELECT NAME, EXIST\n\t FROM " + TablaAlumnos + "\n\tWHERE NAME = '";//En lo que se convierte el XML
    static String FiltroTranslator = "//dni";//Para buscar en la base datos
    static String FiltroTraslatorSMS = "EMAIL";
    static String FiltroTraslatorEMAIL = "SMS";
    static String FiltroCorrelator = "//dni";//Por donde hacer la correlacion
    static String FiltroContext = "//email | //telefono";//El nodo contexto
    static String FiltroBody = "//alumno";//El nodo body al que añadirlo
    static String FiltroSMS = "//telefono";

    //**********Iniciales*********//
    static ConectorGMS CInicial = new ConectorGMS();
    static ConectorMensajes cSMS = new ConectorMensajes();
    static ConectorMensajes cEMAIL = new ConectorMensajes();
    static ConectorAlumnos cAlum = new ConectorAlumnos();

    //************Tareas**************//
    static Splitter TSplitter = new Splitter(ExpresionSplitter);
    static Replicator TReplicator1 = new Replicator(2);
    static Replicator TReplicator2 = new Replicator(2);
    static Translator TTranslatorBD = new Translator(FiltroTranslator,ExpresionTranslator);
    static Translator2 TTranslatorSMS = new Translator2(FiltroTraslatorSMS);
    static Translator2 TTranslatorEMAIL = new Translator2(FiltroTraslatorEMAIL);
    static Correlator TCorrelator = new Correlator(FiltroCorrelator);
    static Content_Enricher TCEnricher = new Content_Enricher(FiltroContext,FiltroBody);
    static Filtro TFiltro = new Filtro(FiltroSMS);

    //Constructor//
    public ControladorProblema1() {

        //Enlazamos los slots;
        TSplitter.enlazarSlotE(CInicial.getPuerto().enlazarSlotS());
        TReplicator1.enlazarSlotE(TSplitter.enlazarSlotS());
        TTranslatorBD.enlazarSlotE(TReplicator1.enlazarSlotS(1));
        cAlum.getPuerto().enlazarSlotE(TTranslatorBD.enlazarSlotS());
        TCorrelator.enlazarSlotE(TReplicator1.enlazarSlotS(1),1);
        TCorrelator.enlazarSlotE(cAlum.getPuerto().enlazarSlotS(),2);
        TCEnricher.enlazarSlotEContext(TCorrelator.enlazarSlotS(1));
        TCEnricher.enlazarSlotEBody(TCorrelator.enlazarSlotS(2));
        TReplicator2.enlazarSlotE(TCEnricher.enlazarSlotS());
        TFiltro.enlazarSlotE(TReplicator2.enlazarSlotS(1));
        TTranslatorEMAIL.enlazarSlotE(TReplicator2.enlazarSlotS(2));
        TTranslatorSMS.enlazarSlotE(TFiltro.enlazarSlotS());
        cEMAIL.getPuerto().enlazarSlotE(TTranslatorEMAIL.enlazarSlotS());
        cSMS.getPuerto().enlazarSlotE(TTranslatorSMS.enlazarSlotS());

        //Cargamos Datos
        if (CInicial.CargarBD(TablaInicial, sgbd, ip, service_bd, usuario, password)) {
            for (int i = 1; i <= CInicial.numMensajes(); i++) {

                //=====> Escribimos los Mensajes en el puerto Inicial del 1 al 9
                CInicial.escribirPuerto();

                //***************************************************//
                //********************SPLITTER***********************//
                //***************************************************//
                TSplitter.realizarTarea();

                //***************************************************//
                //******************REPLICATOR 1*********************//
                //***************************************************//
                TReplicator1.realizarTarea();

                //***************************************************//
                //*****************TRADUCTOR_BD**********************//
                //***************************************************//
                TTranslatorBD.realizarTarea();

                //***************************************************//
                //******************BD ALUMNOS***********************//
                //***************************************************//
                while(cAlum.getPuerto().nMensajes() != 0) {
                    cAlum.leerPuerto();
                    cAlum.busquedaBD(TablaAlumnos, sgbd, ip, service_bd, usuario, password);
                    cAlum.escribirPuerto();
                }

                //***************************************************//
                //********************CORRELATOR*********************//
                //***************************************************//
                //=====> Actua el Correlator
                TCorrelator.realizarTarea();

                //***************************************************//
                //***************CONTEXT_ENRICHER********************//
                //***************************************************//
                TCEnricher.realizarTarea();

                //***************************************************//
                //********************REPLICATOR_2*******************//
                //***************************************************//
                TReplicator2.realizarTarea();

                //***************************************************//
                //********************FILTRO*********************//
                //***************************************************//
                TFiltro.realizarTarea();

                //***************************************************//
                //*******************TRADUCTORES*********************//
                //***************************************************//
                TTranslatorSMS.realizarTarea();
                TTranslatorEMAIL.realizarTarea();

                //=====> El conector SMS y EMAIL recoge los datos del puerto Final
                cSMS.leerPuerto();
                cEMAIL.leerPuerto();

                // El leer mensaje, escribirá el mensaje en la BD y lo mostrará por salida
                cSMS.anadirMensajeBD(TablaFinalTelefono, sgbd, ip, service_bd, usuario, password);
                cEMAIL.anadirMensajeBD(TablaFinalEmail, sgbd, ip, service_bd, usuario, password);

                System.out.println("Presiona Enter para continuar...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            }

            //Borramos la BD
            System.out.println("Vamos a Borrar los mensajes finales de la BD");
            System.out.println("Presiona Enter para continuar...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            cSMS.borrarBD(TablaFinalTelefono, sgbd, ip, service_bd, usuario, password);
            cEMAIL.borrarBD(TablaFinalEmail, sgbd, ip, service_bd, usuario, password);
        }
    }
}
