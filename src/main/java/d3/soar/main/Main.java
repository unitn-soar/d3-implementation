package d3.soar.main;

import java.util.Scanner;

import d3.soar.sessions.Session;
import org.mindrot.jbcrypt.BCrypt;

import d3.soar.db.Database;

import javax.xml.crypto.Data;

public class Main {
    void main(String[] args) {
        System.out.println("Welcome to Soar! Your flight app");
        System.out.println("Type 'help' to get the command list");
        Database.populate_test_data();

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
                    Session.setEmail(email);
                    System.out.print("password: ");
                    String password = scanner.nextLine().strip();
                    System.out.print("Do you confirm (y/n)? ");
                    String choice = scanner.nextLine().trim();

                    if (choice.equals("y")) {
                        Database.register_user(email, BCrypt.hashpw(password, Session.getSalt())); // A joke
                    }
                }
                case PURCHASE_HISTORY -> {
                    if (Session.getUser_id() != -1) {
                        System.out.println(Database.get_purchase_history(Session.getUser_id()));
                    }else{
                        System.out.println("You must be logged-in to see your purchase history!");
                    }
                }
                case SELF_CHECK_IN -> {
                    if (Session.getUser_id() != -1) {
                        System.out.println(Database.get_pending_checkins(Session.getUser_id()));
                        System.out.println("Ticket id: ");
                        long ticket_id = Long.parseLong(scanner.nextLine().trim());
                        Database.self_check_in(Session.getUser_id(), ticket_id);
                        System.out.println("Ticket checked-in successfully!");
                    }else{
                        System.out.println("You must be logged-in to self check-in!");
                    }
                }
                case DELETE_ACCOUNT -> {
                    if (Session.getUser_id() != -1){
                        System.out.print("Are you sure you want to delete your account (y/n)? ");
                        String choice = scanner.nextLine().trim();

                        if (choice.equals("y")) {
                            Database.delete_account(Session.getUser_id());
                            System.out.println("Account deleted successfully");
                            Session.setUser_id(-1);
                        }
                    }else{
                        System.out.println("You must be logged-in to delete your account!");
                    }
                }
                case CHANGE_PASSWORD -> {
                    if (Session.getUser_id() != -1){
                        System.out.print("New password: ");
                        String new_password = scanner.nextLine().trim();
                        Database.change_password(Session.getUser_id(), BCrypt.hashpw(new_password, Session.getSalt()));
                        System.out.println("Password changed successfully!");
                    }else{
                        System.out.println("You must be logged-in to change your password!");
                    }
                }
                case EDIT_ACCOUNT -> {
                    if (Session.getUser_id() != -1){
                        System.out.print("New email: ");
                        String new_email = scanner.nextLine().trim();
                        Database.change_password(Session.getUser_id(), new_email);
                        System.out.println("Email changed successfully!");
                    }else{
                        System.out.println("You must be logged-in to change your email!");
                    }
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

                    if (Session.getUser_id() != 1){
                        System.out.print("Do you want to buy a flight (y/n)?");
                        String choice2 = scanner.nextLine().trim();
                        if(choice2.equals("y")){
                            System.out.print("Ticket to buy (id): ");
                            int flight = Integer.parseInt(scanner.nextLine().trim());

                            System.out.print("Name: ");
                            String name = scanner.nextLine().trim();

                            System.out.print("Surname: ");
                            String surname = scanner.nextLine().trim();

                            System.out.print("Date of birth (YYYY-MM-DD): ");
                            date = java.sql.Date.valueOf(scanner.nextLine().trim());



                            Database.buy_ticket(Session.getUser_id(), Session.getUser_id(), name, surname,Session.getEmail(), date, 1, "Provider", "");
                            System.out.println("Ticket successfully bought!");
                        }
                    }else{
                        System.out.println("You need to be logged-in to buy a ticket");
                    }
                }
                case LOGIN -> {
                    System.out.print("email: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("password: ");
                    String password = scanner.nextLine().trim();
                    long user_id = Database.check_credentials(email, BCrypt.hashpw(password, Session.getSalt()));
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