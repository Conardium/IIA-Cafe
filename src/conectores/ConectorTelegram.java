package conectores;

import java.util.ArrayList;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import puertos.PuertoEoS;


public class ConectorTelegram extends TelegramLongPollingBot {

    private long chatId = 0;
    private int id = 0;
    private ArrayList<Document> xmlFiles = new ArrayList();
    private final PuertoEoS puerto = new PuertoEoS(1);

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

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
        // Se obtiene el id de chat del usuario
        System.out.println(update.getMessage().getText());
        this.chatId = update.getMessage().getChatId();
    }

    public void enviarMensajes() {
        while (puerto.enlazarSlotS().devolverNConjuntos() != 0) {
            leerPuerto();
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(convertirXMLtoString());

            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Enviaste: " + convertirXMLtoString());
            }
            
            xmlFiles.remove(0);
        }
    }

    public PuertoEoS getPuerto() {
        return puerto;
    }

    private void leerPuerto() {

        xmlFiles.add(puerto.getPuerto());
    }

    public String convertirXMLtoString() {

        Node nPadre = xmlFiles.get(0).getDocumentElement();

        StringBuilder mensaje = new StringBuilder();

        mostrarNodos(nPadre.getChildNodes(), mensaje, 1);
        String mensajeFinal = "<" + nPadre.getNodeName() + ">"
                + mensaje.toString()
                + "\n</" + nPadre.getNodeName() + ">";
        
        System.out.println("\n*******************PUERTO SALIDA***********************");
        System.out.println("El Mensaje llega al Puerto Final");
        System.out.println(mensajeFinal);
        System.out.println("\n********************-----------*************************");
        
        return mensajeFinal;
    }

    public void mostrarNodos(NodeList nHijos, StringBuilder mensaje, int paso) {

        String salto = "\n";

        for (int i = 0; i < paso; i++) {

            salto = salto + "\t";
        }

        for (int i = 0; i < nHijos.getLength(); i++) {
            Node nAux = nHijos.item(i);

            boolean esTexto = nAux.getNodeName().startsWith("#");

            if (!esTexto) {
                mensaje.append(" ").append(salto).append("<").append(nAux.getNodeName()).append(">");
                if (nAux.getNodeName().contains("id")) {
                    id = Integer.parseInt(nAux.getTextContent());
                }
            }
            if (esTexto) {
                mensaje.append(" ").append(nAux.getTextContent());
            }
            if (!esTexto) {
                if (nAux.getChildNodes() != null) {//Si tiene hijos los mostramos
                    mostrarNodos(nAux.getChildNodes(), mensaje, paso + 1);
                }
                if (nAux.getChildNodes().getLength() > 1) {
                    mensaje.append(" ").append(salto).append("</").append(nAux.getNodeName()).append(">");
                } else {
                    mensaje.append(" ").append("</").append(nAux.getNodeName()).append(">");
                }
            }

        }

    }
}
