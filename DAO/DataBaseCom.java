
package ifsp.edu.source.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aluno
 */
public class DataBaseCom {

    private static Connection connection = null;
    private static Statement statement = null;
    
    public DataBaseCom() {
    	conectar();
    	criarTabelas();
    }

    public static Connection getConnection() {
          conectar();
          return connection;
    }
    
    public static Statement getStatement(){
    	conectar();
        return statement;
    }

    public static Connection conectar() {
        try {
            if(connection==null) {
            	connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            	statement = connection.createStatement();
            	statement.setQueryTimeout(30);  // set timeout to 30 sec.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean criarTabelas() {
        try {

            statement.executeUpdate("create table if not exists usuario(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, senha TEXT, id_conta INTEGER, salario REAL, email TEXT, cpf TEXT, data_nascimento TEXT)");
            statement.executeUpdate("create table if not exists conta(id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT, status TEXT, valor REAL, numero_conta TEXT, situacao TEXT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS caixa_eletronico (" +
                    "id SERIAL PRIMARY KEY, " +
                    "nota_2 INT DEFAULT 200, " +
                    "nota_5 INT DEFAULT 100, " +
                    "nota_10 INT DEFAULT 50, " +
                    "nota_20 INT DEFAULT 20, " +
                    "nota_50 INT DEFAULT 10, " +
                    "nota_100 INT DEFAULT 5, " +
                    "nota_200 INT DEFAULT 2)");

            statement.executeUpdate("INSERT INTO caixa_eletronico (id, nota_2, nota_5, nota_10, nota_20, nota_50, nota_100, nota_200) " +
                    "VALUES (1, 200, 100, 50, 20, 10, 5, 2) " +
                    "ON CONFLICT (id) DO NOTHING;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS movimento (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_conta_remetente INTEGER, " +
                    "id_conta_destinatario INTEGER, " +
                    "tipo_movimento TEXT CHECK( tipo_movimento IN ('SAQUE', 'TRANSFERENCIA', 'DEPOSITO', 'PIX') ), " +
                    "valor REAL, " +
                    "data TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (id_conta_remetente) REFERENCES conta(id), " +
                    "FOREIGN KEY (id_conta_destinatario) REFERENCES conta(id))");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public static void close(){
        try {
            getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
