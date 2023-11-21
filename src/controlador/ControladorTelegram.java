/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import conectores.ConectorTelegram;
import java.util.Scanner;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author Cristian
 */
public class ControladorTelegram {

    //**********Datos Mensajes********//
    static String MensajeEnviar = "Primero que todo, ¿cómo están los maquinas?";
    static String MensajeRecibido = "";

    //********DECLARACION BOT*********//
    ConectorTelegram Bot;

    //Constructor//
    public ControladorTelegram() {
        // Se inicializa el contexto de la API
        ApiContextInitializer.init();

        // Se crea un nuevo Bot API
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            // Se registra el bot
            telegramBotsApi.registerBot(Bot = new ConectorTelegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        
        
        while (true)
        {
            Scanner scanner = new Scanner(System.in);
            String mensaje = scanner.nextLine();
            Bot.enviarMensaje(mensaje);
        }

    }

}
