package d3.soar.UserData;

///@brief Represents a developer in the system, containing their name, email, and password
public class Developer extends User {
    private String name;

    // Default constructor
    ///@brief Constructs a new developer
    public Developer() {
        super();
    }

///@brief Constructs a new developer with the specified email, password, and name
    public Developer(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    ///@brief Gets the name for the developer
    public String getName() {
        return name;
    }

///@brief Sets the name for the developer
    public void setName(String name) {
        this.name = name;
    }

    /// @brief Returns a string representation of the Developer object
    /// @return A string representation of the Developer object
    @Override
    public String toString() {
        return "Developer{" +
                "name='" + name + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}
