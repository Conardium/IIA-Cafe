
package conectores;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author Cristian
 */
public class ConectorTelegram extends TelegramLongPollingBot {

    long chatId;
    
    @Override
    public String getBotUsername() {
        // Devuelve el nombre de usuario que diste a tu bot en BotFather
        return "IIA_GPT_bot";
    }

    @Override
    public String getBotToken() {
        // Devuelve el token que te dio BotFather
        return "6483356412:AAHQO2DV52oGwk3GgA9g_17tRYZuxY8CUTo";
    }

    @Override
    public void onUpdateReceived(final Update update) {
        // Esta función se invocará cuando nuestro bot reciba un mensaje

        // Se obtiene el mensaje escrito por el usuario
        final String messageTextReceived = update.getMessage().getText();

        // Se obtiene el id de chat del usuario
        this.chatId = update.getMessage().getChatId();

        // Se crea un objeto mensaje
        SendMessage message = new SendMessage().setChatId(chatId).setText(messageTextReceived);

        try {
            // Se envía el mensaje
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Recibiste: " + messageTextReceived);
        }
    }
    
      public void enviarMensaje(String mensaje) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(mensaje);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Enviaste: " + mensaje);
        }
    }
}

