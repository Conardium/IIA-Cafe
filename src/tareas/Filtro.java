package tareas;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import slot.Slot;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class Filtro extends Tarea{
    private Document xmlEntrada, xmlSalida;

    private final String Filtro;

    private Slot slotE;
    private final Slot slotS;

    public Filtro(String Filtro)
    {
        this.Filtro = Filtro;
        this.slotS = new Slot("FiltroSalida");
    }

    @Override
    public void realizarTarea() {

        if(slotE != null){
            for (int nXML = 0; nXML < slotE.devolverNConjuntos(); nXML++) {
                getMSJslot();

                try{

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                    XPath xPath = XPathFactory.newInstance().newXPath();

                    //Pillamos el nombre
                    XPathExpression expressionXpath2 = xPath.compile(Filtro);
                    Node nodoSMS = (Node) expressionXpath2.evaluate(xmlEntrada, XPathConstants.NODE);

                    if(!nodoSMS.getTextContent().equals("NULL"))
                    {
                        //Crear un documento XML
                        Document xmlOut = dBuilder.newDocument();

                        //Guardo en xmlSalida
                        xmlSalida = xmlOut;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                setMSJslot();
            }
        }
    }
    @Override
    protected void getMSJslot() {

        xmlEntrada = slotE.getMensaje();
    }
    @Override
    protected void setMSJslot() {

        slotS.setMensaje(xmlSalida);
    }

    @Override
    public void enlazarSlotE(Slot slot) {
        this.slotE = slot;
    }

    @Override
    public Slot enlazarSlotS() {
        return slotS;
    }
}
