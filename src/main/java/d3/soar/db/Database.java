package d3.soar.db;
import java.sql.*;
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
    public static void change_email(long user_id, String new_email){
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM change_email(?, ?)");
            ps.setLong(1, user_id);
            ps.setString(2, new_email);
            ps.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static void change_password(long user_id, String new_password){
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM change_password(?, ?)");
            ps.setLong(1, user_id);
            ps.setString(2, new_password);
            ps.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static void delete_account(long user_id){
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM soft_delete_account(?)");
            ps.setLong(1, user_id);
            ps.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static long buy_ticket(long buyerUserId, Long passengerUserId,
                                  String passengerName, String passengerSurname,
                                  String passengerEmail, Date passengerDateOfBirth,
                                  long flightSeatId, String paymentProvider, String externalPaymentRef) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT create_individual_purchase(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setLong(1, buyerUserId);
            if (passengerUserId == null) ps.setNull(2, java.sql.Types.BIGINT);
            else                         ps.setLong(2, passengerUserId);
            ps.setString(3, passengerName);
            ps.setString(4, passengerSurname);
            ps.setString(5, passengerEmail);
            if (passengerDateOfBirth == null) ps.setNull(6, java.sql.Types.DATE);
            else                              ps.setDate(6, passengerDateOfBirth);
            ps.setLong(7, flightSeatId);
            ps.setString(8, paymentProvider);
            ps.setString(9, externalPaymentRef);
            ResultSet result = ps.executeQuery();
            result.next();
            return result.getLong(1);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            return -1;
        }
    }



    ///@brief Creates a premade database
    /// @details Inserts into the database a premade set of airlines, airports, flights and people
    public static void populate_test_data() {
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);

            // Airlines
            long airlineAZ, airlineLH, airlineFR;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO airlines (airline_name) VALUES (?) RETURNING airline_id")) {
                ps.setString(1, "Alitalia Reborn");
                ResultSet rs = ps.executeQuery(); rs.next(); airlineAZ = rs.getLong(1);
                ps.setString(1, "Lufthansa");
                rs = ps.executeQuery(); rs.next(); airlineLH = rs.getLong(1);
                ps.setString(1, "Ryanair");
                rs = ps.executeQuery(); rs.next(); airlineFR = rs.getLong(1);
            }

            // Airports (departure hub FCO has iata_code = NULL)
            long aptFCO, aptLHR, aptCDG, aptAMS, aptBCN, aptMAD;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO airports (iata_code, airport_name, nation) VALUES (?, ?, ?) RETURNING airport_id")) {
                ps.setNull(1, java.sql.Types.CHAR); ps.setString(2, "Rome Fiumicino");          ps.setString(3, "Italy");
                ResultSet rs = ps.executeQuery(); rs.next(); aptFCO = rs.getLong(1);
                ps.setString(1, "LHR"); ps.setString(2, "London Heathrow");         ps.setString(3, "United Kingdom");
                rs = ps.executeQuery(); rs.next(); aptLHR = rs.getLong(1);
                ps.setString(1, "CDG"); ps.setString(2, "Paris Charles de Gaulle"); ps.setString(3, "France");
                rs = ps.executeQuery(); rs.next(); aptCDG = rs.getLong(1);
                ps.setString(1, "AMS"); ps.setString(2, "Amsterdam Schiphol");      ps.setString(3, "Netherlands");
                rs = ps.executeQuery(); rs.next(); aptAMS = rs.getLong(1);
                ps.setString(1, "BCN"); ps.setString(2, "Barcelona El Prat");       ps.setString(3, "Spain");
                rs = ps.executeQuery(); rs.next(); aptBCN = rs.getLong(1);
                ps.setString(1, "MAD"); ps.setString(2, "Madrid Barajas");          ps.setString(3, "Spain");
                rs = ps.executeQuery(); rs.next(); aptMAD = rs.getLong(1);
            }

            // Planes
            long planeAZ1, planeAZ2, planeLH1, planeFR1;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO planes (airline_id, seat_capacity) VALUES (?, 30) RETURNING plane_id")) {
                ps.setLong(1, airlineAZ); ResultSet rs = ps.executeQuery(); rs.next(); planeAZ1 = rs.getLong(1);
                ps.setLong(1, airlineAZ); rs = ps.executeQuery(); rs.next(); planeAZ2 = rs.getLong(1);
                ps.setLong(1, airlineLH); rs = ps.executeQuery(); rs.next(); planeLH1 = rs.getLong(1);
                ps.setLong(1, airlineFR); rs = ps.executeQuery(); rs.next(); planeFR1 = rs.getLong(1);
            }

            // Users – admin first (null actor = bootstrap), then the rest
            long adminId, devId, agencyId, person1, person2, person3;
            try (PreparedStatement ps = conn.prepareStatement("SELECT create_admin_account(NULL, ?, ?, ?)")) {
                ps.setString(1, "admin@soar.test"); ps.setString(2, "hashed_admin_pw"); ps.setString(3, "Super Admin");
                ResultSet rs = ps.executeQuery(); rs.next(); adminId = rs.getLong(1);
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT create_developer_account(NULL, ?, ?, ?)")) {
                ps.setString(1, "dev@soar.test"); ps.setString(2, "hashed_dev_pw"); ps.setString(3, "Dev One");
                ResultSet rs = ps.executeQuery(); rs.next(); devId = rs.getLong(1);
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT create_travel_agency_account(?, ?, ?, ?, ?)")) {
                ps.setString(1, "agency@travelco.test"); ps.setString(2, "hashed_agency_pw");
                ps.setString(3, "TravelCo Srl"); ps.setString(4, "info@travelco.test"); ps.setString(5, "IT12345678901");
                ResultSet rs = ps.executeQuery(); rs.next(); agencyId = rs.getLong(1);
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT create_person_account(?, ?)")) {
                ps.setString(1, "alice@example.test"); ps.setString(2, "hashed_alice_pw");
                ResultSet rs = ps.executeQuery(); rs.next(); person1 = rs.getLong(1);
                ps.setString(1, "bob@example.test");   ps.setString(2, "hashed_bob_pw");
                rs = ps.executeQuery(); rs.next(); person2 = rs.getLong(1);
                ps.setString(1, "carol@example.test"); ps.setString(2, "hashed_carol_pw");
                rs = ps.executeQuery(); rs.next(); person3 = rs.getLong(1);
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT update_person_profile(?, ?, ?, ?)")) {
                ps.setLong(1, person1); ps.setString(2, "Alice"); ps.setString(3, "Rossi");  ps.setDate(4, java.sql.Date.valueOf("1990-04-12")); ps.executeQuery();
                ps.setLong(1, person2); ps.setString(2, "Bob");   ps.setString(3, "Bianchi");ps.setDate(4, java.sql.Date.valueOf("1985-11-03")); ps.executeQuery();
                ps.setLong(1, person3); ps.setString(2, "Carol"); ps.setString(3, "Verdi");  ps.setDate(4, java.sql.Date.valueOf("1998-07-22")); ps.executeQuery();
            }

            // Flight paths
            long pathFCO_LHR, pathFCO_CDG, pathFCO_AMS, pathFCO_BCN, pathFCO_MAD, pathLHR_FCO, pathCDG_AMS;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO flight_paths (departure_airport_id, destination_airport_id, duration_minutes, distance_km) VALUES (?,?,?,?) RETURNING flight_path_id")) {
                ResultSet rs;
                ps.setLong(1,aptFCO);ps.setLong(2,aptLHR);ps.setInt(3,155);ps.setDouble(4,1434); rs=ps.executeQuery();rs.next();pathFCO_LHR=rs.getLong(1);
                ps.setLong(1,aptFCO);ps.setLong(2,aptCDG);ps.setInt(3,130);ps.setDouble(4,1108); rs=ps.executeQuery();rs.next();pathFCO_CDG=rs.getLong(1);
                ps.setLong(1,aptFCO);ps.setLong(2,aptAMS);ps.setInt(3,165);ps.setDouble(4,1577); rs=ps.executeQuery();rs.next();pathFCO_AMS=rs.getLong(1);
                ps.setLong(1,aptFCO);ps.setLong(2,aptBCN);ps.setInt(3,120);ps.setDouble(4,1358); rs=ps.executeQuery();rs.next();pathFCO_BCN=rs.getLong(1);
                ps.setLong(1,aptFCO);ps.setLong(2,aptMAD);ps.setInt(3,145);ps.setDouble(4,1960); rs=ps.executeQuery();rs.next();pathFCO_MAD=rs.getLong(1);
                ps.setLong(1,aptLHR);ps.setLong(2,aptFCO);ps.setInt(3,155);ps.setDouble(4,1434); rs=ps.executeQuery();rs.next();pathLHR_FCO=rs.getLong(1);
                ps.setLong(1,aptCDG);ps.setLong(2,aptAMS);ps.setInt(3, 80);ps.setDouble(4, 430); rs=ps.executeQuery();rs.next();pathCDG_AMS=rs.getLong(1);
            }

            // Flights + seats via create_flight_with_seats
            // Seat layout: 1-10 ECONOMY, 11-20 BUSINESS (business_from_seat=11), 21-30 FIRST (first_from_seat=21)
            java.time.LocalDate today = java.time.LocalDate.now(java.time.ZoneOffset.UTC);
            String flightSql =
                    "SELECT create_flight_with_seats(" +
                            "p_admin_user_id=>?, p_plane_id=>?, p_flight_path_id=>?, p_departure_time=>?," +
                            "p_economy_price=>?, p_business_price=>?, p_first_price=>?," +
                            "p_business_from_seat=>11, p_first_from_seat=>21)";

            // [path, plane, day_offset, hour, min, econ, biz, first]
            Object[][] flights = {
                    // FCO->LHR
                    {pathFCO_LHR, planeAZ1, 0,  7, 30,  89.99, 219.99, 449.99},
                    {pathFCO_LHR, planeAZ1, 0, 15,  0,  99.99, 229.99, 459.99},
                    {pathFCO_LHR, planeLH1, 1,  8,  0,  84.99, 209.99, 429.99},
                    {pathFCO_LHR, planeAZ2, 1, 16, 30,  94.99, 224.99, 444.99},
                    {pathFCO_LHR, planeAZ1, 3,  9,  0,  79.99, 199.99, 399.99},
                    // FCO->CDG
                    {pathFCO_CDG, planeAZ2, 0,  6, 45,  79.99, 199.99, 399.99},
                    {pathFCO_CDG, planeLH1, 0, 14, 15,  89.99, 209.99, 419.99},
                    {pathFCO_CDG, planeAZ1, 1,  7,  0,  74.99, 194.99, 394.99},
                    {pathFCO_CDG, planeFR1, 1, 19,  0,  69.99, 189.99, 389.99},
                    {pathFCO_CDG, planeAZ2, 3, 10, 30,  84.99, 204.99, 409.99},
                    // FCO->AMS
                    {pathFCO_AMS, planeLH1, 0,  8,  0, 109.99, 249.99, 499.99},
                    {pathFCO_AMS, planeAZ2, 0, 17, 45, 119.99, 259.99, 509.99},
                    {pathFCO_AMS, planeFR1, 1,  6, 30,  99.99, 239.99, 479.99},
                    {pathFCO_AMS, planeLH1, 1, 13,  0, 104.99, 244.99, 489.99},
                    {pathFCO_AMS, planeAZ1, 3, 11,  0,  94.99, 234.99, 469.99},
                    // FCO->BCN
                    {pathFCO_BCN, planeFR1, 0,  9, 15,  59.99, 179.99, 359.99},
                    {pathFCO_BCN, planeAZ2, 0, 18, 30,  69.99, 189.99, 369.99},
                    {pathFCO_BCN, planeFR1, 1,  7, 45,  54.99, 174.99, 349.99},
                    {pathFCO_BCN, planeAZ1, 1, 15,  0,  64.99, 184.99, 364.99},
                    {pathFCO_BCN, planeFR1, 3,  8,  0,  49.99, 169.99, 339.99},
                    // FCO->MAD
                    {pathFCO_MAD, planeLH1, 0, 10,  0, 119.99, 269.99, 529.99},
                    {pathFCO_MAD, planeFR1, 0, 20,  0, 129.99, 279.99, 539.99},
                    {pathFCO_MAD, planeAZ2, 1,  9, 30, 109.99, 259.99, 519.99},
                    {pathFCO_MAD, planeLH1, 1, 17,  0, 114.99, 264.99, 524.99},
                    {pathFCO_MAD, planeFR1, 3, 12,  0,  99.99, 249.99, 499.99},
                    // LHR->FCO (return / one-stop support)
                    {pathLHR_FCO, planeAZ1, 1, 12,  0,  89.99, 219.99, 449.99},
                    {pathLHR_FCO, planeAZ2, 2,  8,  0,  84.99, 209.99, 429.99},
                    // CDG->AMS (second leg for one-stop FCO->CDG->AMS)
                    {pathCDG_AMS, planeLH1, 1, 12, 30,  49.99, 139.99, 279.99},
                    {pathCDG_AMS, planeFR1, 3, 14,  0,  44.99, 134.99, 269.99},
            };

            try (PreparedStatement ps = conn.prepareStatement(flightSql)) {
                for (Object[] f : flights) {
                    java.time.ZonedDateTime dep = java.time.ZonedDateTime.of(
                            today.plusDays((int) f[2]),
                            java.time.LocalTime.of((int) f[3], (int) f[4]),
                            java.time.ZoneOffset.UTC);
                    ps.setLong(1, adminId);
                    ps.setLong(2, (long) f[1]);
                    ps.setLong(3, (long) f[0]);
                    ps.setTimestamp(4, Timestamp.from(dep.toInstant()));
                    ps.setBigDecimal(5, new java.math.BigDecimal((double) f[5]).setScale(2, java.math.RoundingMode.HALF_UP));
                    ps.setBigDecimal(6, new java.math.BigDecimal((double) f[6]).setScale(2, java.math.RoundingMode.HALF_UP));
                    ps.setBigDecimal(7, new java.math.BigDecimal((double) f[7]).setScale(2, java.math.RoundingMode.HALF_UP));
                    ps.executeQuery();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("populate_test_data: done.");
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
}
