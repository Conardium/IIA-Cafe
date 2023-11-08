
package conectores;

import java.util.ArrayList;
import org.w3c.dom.Document;

/**
 *
 * @author Cristian
 */
public abstract class Conector {



    private Connection conn = null;

    String sgbd;
    String ip;
    String service_bd;
    String usuario;
    String password;

    public void Conector(String sgbd, String ip, String service_bd, String usuario,
                    String password) throws ClassNotFoundException, SQLException {

        this.sgbd = sgbd;
        this.ip = ip;
        this.service_bd = service_bd;
        this.usuario = usuario;
        this.password = password;

            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@" + ip + ":1521:" + service_bd, usuario , password);

    }

    Connection getConexion() {

        return conn;
    }

    public void desconexion() throws SQLException {
        conn.close();
    }


    ArrayList<Document> xmlFiles;
    
    public Document leerMensaje() {
        
        return xmlFiles.remove(0);
    }

    public void escribirMensaje(Document Mensaje) {
        
       xmlFiles.add(Mensaje);
    }

}
