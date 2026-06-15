package d3.soar.db;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class Database {
    private static Connection connection;

    ///@brief Creates a connection with a Database
    private static Connection getConnection() throws SQLException{
        if (connection == null) {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/soar",
                    "postgres", ""
            );
        }
        return connection;
    }

    ///@brief Creates a new user and stores it in the database
    public static void register_user(String email, String password_hash){
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT create_person_account(?, ?)");
            ps.setString(1, email);
            ps.setString(2, password_hash);
            ps.executeQuery();
        } catch (Exception e){
            System.out.println("Error: " + e.toString());
        }
    }

    ///@brief Returns a list of all existing airports
    /// @returns All existing airports and relative informations as a string
    public static String airport_list(){
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM get_airports()");
            ResultSet result = ps.executeQuery();
            StringBuilder out = new StringBuilder();
            while(result.next()){
                String app = "Airport id: " + result.getLong("airport_id") + " - Name: " + result.getString("airport_name") + " - Nation: " + result.getString("nation") + "\n";
                out.append(app);
            }
            return out.toString();
        } catch (Exception e){
            System.out.println("Error: " + e.toString());
            return "";
        }
    }

    ///@brief Finds direct flights to a given destination and a date
    /// @param destination The destination airport
    /// @param date The date of the flight
    /// @return A string containing informations about all available flights
    public static String search_direct_flights(int destination, Date date) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT * FROM search_direct_flights(p_destination_airport_id => ?, p_flight_date => ?)"
            );
            ps.setLong(1, destination);
            if (date == null) {
                ps.setNull(2, java.sql.Types.DATE);
            } else {
                ps.setDate(2, date);
            }
            ResultSet result = ps.executeQuery();
            StringBuilder out = new StringBuilder();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            while (result.next()) {
                out.append(result.getLong("flight_id")).append(" | ")
                        .append(result.getString("departure_airport")).append(" -> ")
                        .append(result.getString("destination_airport")).append(" | ")
                        .append(result.getString("airline_name")).append(" | ")
                        .append(result.getTimestamp("departure_time").toLocalDateTime().format(fmt)).append(" -> ")
                        .append(result.getTimestamp("arrival_time").toLocalDateTime().format(fmt)).append(" | ")
                        .append(result.getInt("duration_minutes")).append(" min | ")
                        .append(result.getBigDecimal("lowest_price")).append("€ | ")
                        .append(result.getLong("available_seats")).append(" seats\n");
            }
            return out.toString();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            return "";
        }
    }
    public static long check_credentials(String email, String password_hash){
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM check_credentials(?, ?)");
            ps.setString(1, email);
            ps.setString(2, password_hash);
            ResultSet result = ps.executeQuery();
            result.next();
            long user_id = result.getLong("check_credentials");
            if (result.wasNull()){
                return -1;
            }else{
                return user_id;
            }

        } catch (Exception e){
            System.out.println("Error: " + e.toString());
            return -1;
        }
    }


    ///@brief Creates a premade database
    /// @details Inserts into the database a premade set of airlines, airports, flights and people
    public static void populate_test_data() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            // Airlines
            stmt.executeUpdate("INSERT INTO airlines (airline_name) VALUES ('Ryanair'), ('Lufthansa')");

            // Airports
            stmt.executeUpdate("INSERT INTO airports (iata_code, airport_name, nation) VALUES " +
                    "('MXP', 'Milan Malpensa', 'Italy')," +
                    "('FCO', 'Rome Fiumicino', 'Italy')," +
                    "('CDG', 'Paris Charles de Gaulle', 'France')," +
                    "('LHR', 'London Heathrow', 'UK')," +
                    "('BCN', 'Barcelona El Prat', 'Spain')");

            // Planes
            stmt.executeUpdate("INSERT INTO planes (airline_id, seat_capacity) VALUES " +
                    "(1, 180), (1, 200), (2, 150), (2, 220), (2, 100)");

            // Flight paths
            stmt.executeUpdate("INSERT INTO flight_paths (departure_airport_id, destination_airport_id, duration_minutes, distance_km) VALUES " +
                    "(1, 2, 75, 480)," +
                    "(1, 3, 100, 640)," +
                    "(1, 4, 120, 980)," +
                    "(2, 5, 130, 1360)," +
                    "(3, 4, 75, 340)");

            // Flights
            stmt.executeUpdate("INSERT INTO flights (plane_id, flight_path_id, departure_time, status) VALUES " +
                    "(1, 1, NOW() + INTERVAL '1 day', 'SCHEDULED')," +
                    "(2, 2, NOW() + INTERVAL '2 days', 'SCHEDULED')," +
                    "(3, 3, NOW() + INTERVAL '3 days', 'SCHEDULED')," +
                    "(4, 4, NOW() + INTERVAL '4 days', 'SCHEDULED')," +
                    "(5, 5, NOW() + INTERVAL '5 days', 'SCHEDULED')");

            // Flight seats (5 per flight)
            for (int flightId = 1; flightId <= 5; flightId++) {
                for (int seat = 1; seat <= 5; seat++) {
                    stmt.executeUpdate("INSERT INTO flight_seats (flight_id, seat_number, seat_class, base_price) VALUES " +
                            "(" + flightId + ", " + seat + ", 'ECONOMY', " + (50 + seat * 10) + ")");
                }
            }

            // Users
            stmt.executeQuery("SELECT create_person_account('alice@example.com', 'hash1', 'Alice', 'Rossi', '1990-01-01')");
            stmt.executeQuery("SELECT create_person_account('bob@example.com', 'hash2', 'Bob', 'Bianchi', '1985-06-15')");
            stmt.executeQuery("SELECT create_person_account('carol@example.com', 'hash3', 'Carol', 'Verdi', '2000-03-22')");
            stmt.executeQuery("SELECT create_travel_agency_account('agency@example.com', 'hash4', 'Best Travels', 'info@besttravels.com', 'IT12345')");
            stmt.executeQuery("SELECT create_admin_account(NULL, 'admin@example.com', 'hash5', 'Super Admin')");
        } catch (Exception e) {
            System.out.println("Error populating test data: " + e.getMessage());
        }
    }
}
