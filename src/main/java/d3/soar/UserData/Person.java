package d3.soar.UserData;

///@brief Represents a person in the system, containing their name, surname, email, and password
/// @see User
public class Person extends User {

    private String name;
    private String surname;

    ///@brief Constructs a new person with the specified email, password, name, and surname
    public Person() {
        super();
    }

    ///@brief Constructs a new person with the specified email, password, name, and surname
    /// @param email An email
    /// @param password A password
    /// @param name A name
    /// @param surname A surname
    public Person(String email, String password, String name, String surname) {
        super(email, password);
        this.name = name;
        this.surname = surname;
    }

    ///@brief Gets the name for the person
    public String getName() {
        return name;
    }

    ///@brief Sets the name for the person
    public void setName(String name) {
        this.name = name;
    }

///@brief Gets the surname for the person
    public String getSurname() {
        return surname;
    }

///@brief Sets the surname for the person
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /// @brief Returns a string representation of the Person object
    /// @return A string representation of the Person object
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}