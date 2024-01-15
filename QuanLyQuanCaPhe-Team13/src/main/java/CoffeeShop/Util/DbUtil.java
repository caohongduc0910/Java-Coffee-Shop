package CoffeeShop.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    private static Connection conn;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=CoffeeShop";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "sa";

    public Connection getConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
