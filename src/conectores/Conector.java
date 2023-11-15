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
    private String URL = "";

    public void Conexion(String sgbd, String ip, String service_bd, String usuario,
            String password) throws SQLException {

        if (sgbd.equals("oracle"))//Oracle
        {
            URL = "jdbc:oracle:thin:@" + ip + ":1521:" + service_bd;
        } else if (sgbd.equals("mariadb"))//MariaDB
        {
            URL = "jdbc:mariadb://" + ip + ":3306/" + service_bd;
        } else if (sgbd.equals("derby"))//Derby
        {
            URL = "jdbc:derby://" + ip + ":1527/" + service_bd;
            //jdbc:derby://localhost:1527/IIA_Cafe
        }

        conn = DriverManager.getConnection(URL, usuario, password);
    }

    Connection getConexion() {

        return conn;
    }

    public void desconexion() throws SQLException {
        conn.close();
    }

    ArrayList<Document> xmlFiles = new ArrayList();

}
