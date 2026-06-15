package d3.soar.main;

import java.util.Scanner;

import d3.soar.sessions.Session;
import org.mindrot.jbcrypt.BCrypt;

import d3.soar.db.Database;

public class Main {
    void main(String[] args) {
        System.out.println("Welcome to Soar! Your flight app");
        System.out.println("Type 'help' to get the command list");
        //Database.populate_test_data();

        main_loop:
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            switch (Command.fromString(scanner.nextLine())) {
                case HELP -> System.out.print(Command.help());
                case EXIT -> {
                    break main_loop;
                }
                case INVALID -> System.out.println("Invalid command, type 'help' to see the command list");
                case REGISTER -> {
                    System.out.print("email: ");
                    String email = scanner.nextLine().strip();
                    System.out.print("password: ");
                    String password = scanner.nextLine().strip();
                    Database.register_user(email, BCrypt.hashpw(password, "$2a$12$R9h/cIPz0gi.URNNX3kh2O")); // A joke
                }
                
                case SEARCH_FLIGHT -> {
                    System.out.println("Here are all possible destinations:\n" + Database.airport_list());
                    System.out.print("Destination (id/-1 for any): ");
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Do you want to specify a date (y/n)? ");
                    String choice = scanner.nextLine().trim();
                    java.sql.Date date = null;
                    if (choice.equals("y")) {
                        System.out.print("Date (YYYY-MM-DD): ");
                        date = java.sql.Date.valueOf(scanner.nextLine().trim());
                    }else{
                        date = java.sql.Date.valueOf(java.time.LocalDate.now());
                    }
                    System.out.println("Flights:\n" + Database.search_direct_flights(id, date));
                }
                case LOGIN -> {
                    System.out.print("email: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("password: ");
                    String password = scanner.nextLine().trim();
                    long user_id = Database.check_credentials(email, BCrypt.hashpw(password, "$2a$12$R9h/cIPz0gi.URNNX3kh2O"));
                    if (user_id == -1){
                        System.out.println("Invalid credentials");
                    }else{
                        Session.setUser_id(user_id);
                        System.out.println("Successfully logged-in");
                    }
                }
                case LOGOUT -> {
                    Session.logout();
                }
            }
        }
    }
}