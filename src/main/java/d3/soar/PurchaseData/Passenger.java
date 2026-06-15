package d3.soar.PurchaseData;

import d3.soar.UserData.Person;

///@brief Represents a passenger in the system, containing their name, surname, and email
///@details During a Purchase, the Passenger acts either as a registered user and as an unregistered one
/// @see Person
public class Passenger {
    private String name;
    private String surname;
    private String email;

    ///@brief Constructs a new passenger with default values
    public Passenger() {
    }

    ///@brief Constructs a new passenger with the specified fields
    public Passenger(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    ///@brief Gets the name for the passenger
    public String getName() {
        return name;
    }

    ///@brief Sets the name for the passenger
    public void setName(String name) {
        this.name = name;
    }

///@brief Gets the surname for the passenger
    public String getSurname() {
        return surname;
    }

    ///@brief Sets the surname for the passenger
    public void setSurname(String surname) {
        this.surname = surname;
    }

///@brief Gets the email for the passenger
    public String getEmail() {
        return email;
    }

    ///@brief Sets the email for the passenger
    public void setEmail(String email) {
        this.email = email;
    }

    ///@brief Sets the attributes of the passenger based on a person object
    ///@param person An existing person that the Passanger class impersonates
    public void actAs(Person person) {
        this.name = person.getName();
        this.surname = person.getSurname();
        this.email = person.getEmail();
    }

    /// @brief Returns a string representation of the Passenger object
    /// @return A string representation of the Passenger object
    @Override
    public String toString() {
        return "Passenger{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
