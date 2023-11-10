
package conectores;

import org.w3c.dom.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Cristian
 */
public abstract class Conector {

    private Connection conn = null;
    String URL = "";
    
    public void Conector(String sgbd, String ip, String service_bd, String usuario,
                    String password) throws ClassNotFoundException, SQLException {
        
        if (sgbd.equals("oracle"))//Oracle
        {
            URL = "jdbc:oracle:thin:@" + ip + ":1521:" + service_bd;
        } else if (sgbd.equals("mariadb"))//MariaDB
        {
            URL = "jdbc:mariadb://" + ip + ":3306/" + service_bd;
        } else
        {
            URL = "JavaDB";
        }

        conn = DriverManager.getConnection(URL,usuario,password);
        System.out.println("Conexión conseguida");
    }
    
    Connection getConexion() {

        return conn;
    }

    public void desconexion() throws SQLException {
        conn.close();
    }


    ArrayList<Document> xmlFiles = new ArrayList();
    
    public Document leerMensaje(String Table) {
        
        return xmlFiles.remove(0);
    }

    public void escribirMensaje(Document Mensaje) {
        
       xmlFiles.add(Mensaje);
    }

}
