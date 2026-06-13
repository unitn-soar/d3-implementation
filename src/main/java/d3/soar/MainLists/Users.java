package d3.soar.MainLists;

import java.util.List;

import d3.soar.UserData.User;

/* @brief Represents a list of users in the system, containing methods to add, remove, and read users from a file*/
public class Users {
    public List<User> users;

    /* @brief Constructs a new users object with the specified list of users*/
    public Users() {
    }

    /* @brief Constructs a new users object with the specified list of users*/
    public Users(List<User> users) {
        this.users = users;
    }

    /* @brief Adds a user to the users list*/
    public void addUser(User user) {
        this.users.add(user);
    }

/* @brief Removes a user from the users list*/
    public void removeUser(User user) {
        this.users.remove(user);
    }

/* @brief Reads users from a file and populates the users list*/
    public void readUsersFromFile(String filePath) {
        // Implement file reading logic to populate the users list
    }

    /* @brief Gets the list of users for the users object*/
    public void setUsers(Users users) {
        this.users = users.users;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user.toString()).append("\n");
        }
        return sb.toString();
    }
}
