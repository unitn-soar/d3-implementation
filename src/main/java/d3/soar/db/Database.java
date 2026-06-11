package d3.soar.db;
import java.sql.*;
public class Database {
    private static Connection connection;

    private static Connection getConnection() throws SQLException{
        if (connection == null) {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/soar",
                    "postgres", ""
            );
        }
        return connection;
    }
    public void register_user(String email, String password_hash){
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT create_person_account(?, ?)");
            ps.setString(1, email);
            ps.setString(2, password_hash);
            ps.executeQuery();
        } catch (Exception e){
            System.out.println("Error: " + e.toString());
        }
    }
}
