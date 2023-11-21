
import controlador.*;
import java.util.Scanner;

public class main {

    static ControladorCafe CAFE;
    static ControladorProblema1 Problema_1;
    static ControladorTelegram BotTelegram;

    //main//
    public static void main(String[] args) {

        int eleccion = 1;

        System.out.println("---MENU---");
        System.out.println("1-CAFE");
        System.out.println("2-Problema 1");
        System.out.println("3-CAFE + BOT Telegram");
        System.out.println("Introduce elección: ");
        Scanner scanner = new Scanner(System.in);

        try {
            eleccion = scanner.nextInt();

            if (eleccion == 1) {
                //SIEMPRE BORRAMOS LOS MENSAJES FINALES, EN CADA MAIN ESTÁ AL FINAL//
                //EL COMANDO QUE LOS BORRA, LO HACEMOS ASÍ PARA HACER PRUEBAS//
                CAFE = new ControladorCafe();
            } else if (eleccion == 2) {
                //SIEMPRE BORRAMOS LOS MENSAJES FINALES, EN CADA MAIN ESTÁ AL FINAL//
                //EL COMANDO QUE LOS BORRA, LO HACEMOS ASÍ PARA HACER PRUEBAS//
                Problema_1 = new ControladorProblema1();
            } else if (eleccion == 3) {
                //SI NO TIENES ACCESO AL BOT (DESDE TELEGRAM), ESTE METODO NO PUEDE FUNCIONAR//
                BotTelegram = new ControladorTelegram();
            } else {
                //SIEMPRE BORRAMOS LOS MENSAJES FINALES, EN CADA MAIN ESTÁ AL FINAL//
                //EL COMANDO QUE LOS BORRA, LO HACEMOS ASÍ PARA HACER PRUEBAS//
                CAFE = new ControladorCafe();
            }
        } catch (Exception ex) {
            //SIEMPRE BORRAMOS LOS MENSAJES FINALES, EN CADA MAIN ESTÁ AL FINAL//
            //EL COMANDO QUE LOS BORRA, LO HACEMOS ASÍ PARA HACER PRUEBAS//
            CAFE = new ControladorCafe();
        }

    }

}
