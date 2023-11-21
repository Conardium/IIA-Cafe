

import controlador.*;


public class main {

    static ControladorCafe CAFE;
    static ControladorProblema1 Problema_1;
    static ControladorTelegram BotTelegram;

    //main//
    public static void main(String[] args) {

        CAFE = new ControladorCafe();
        Problema_1 = new ControladorProblema1();
        //BotTelegram = new ControladorTelegram();

    }

}
