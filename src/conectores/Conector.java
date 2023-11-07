
package conectores;

import java.util.ArrayList;
import org.w3c.dom.Document;

/**
 *
 * @author Cristian
 */
public abstract class Conector {
    
    ArrayList<Document> xmlFiles;
    
    public Document leerMensaje() {
        
        return xmlFiles.remove(0);
    }

    public void escribirMensaje(Document Mensaje) {
        
       xmlFiles.add(Mensaje);
    }

}
