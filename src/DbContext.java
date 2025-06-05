import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/proiect_oop2";
    private static final String USER = "root";
    private static final String PASS = "Sutaseaca1";

    private static Connection connection;

    private DbConnection() {}

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASS);
        }
        return connection;
    }
}