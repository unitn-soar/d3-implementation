package d3.soar.main;

import d3.soar.db.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class Main {
    void main(String[] args) {
        System.out.println("Welcome to Soar! Your flight app");
        System.out.println("Type 'help' to get the command list");
        Database db = new Database();

        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("> ");
            switch (Command.fromString(scanner.next())) {
                case HELP -> System.out.print(Command.help());
                case INVALID -> System.out.println("Invalid command, type 'help' to see the command list");
                case REGISTER -> {
                    System.out.print("email: ");
                    String email = scanner.next().strip();
                    System.out.print("password: ");
                    String password = scanner.next().strip();
                    db.register_user(email, BCrypt.hashpw(password, BCrypt.gensalt()));
                }
            }
        }
    }
}