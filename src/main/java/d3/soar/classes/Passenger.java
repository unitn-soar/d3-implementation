package d3.soar.classes;

public class Passenger {
    private String name;
    private String surname;
    private String email;

    // Default constructor
    public Passenger() {
    }

    // Constructor with all fields
    public Passenger(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for surname
    public String getSurname() {
        return surname;
    }

    // Setter for surname
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void actAs(Person person) {
        this.name = person.getName();
        this.surname = person.getSurname();
        this.email = person.getEmail();
    }
}
