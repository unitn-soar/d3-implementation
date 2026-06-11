package d3.soar.main;

import d3.soar.db.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class Main {
    void main(String[] args) {
        System.out.println("Welcome to Soar! Your flight app");
        System.out.println("Type 'help' to get the command list");
        //Database.populate_test_data();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            switch (Command.fromString(scanner.nextLine())) {
                case HELP -> System.out.print(Command.help());
                case INVALID -> System.out.println("Invalid command, type 'help' to see the command list");
                case REGISTER -> {
                    System.out.print("email: ");
                    String email = scanner.nextLine().strip();
                    System.out.print("password: ");
                    String password = scanner.nextLine().strip();
                    Database.register_user(email, BCrypt.hashpw(password, BCrypt.gensalt()));
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
            }
        }
    }
}