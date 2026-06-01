package d3.soar.classes;

import java.util.List;

public class Users {
    public List<User> users;

    // Default constructor
    public Users() {
    }

    // Constructor with all fields
    public Users(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public void readUsersFromFile(String filePath) {
        // Implement file reading logic to populate the users list
    }
}
