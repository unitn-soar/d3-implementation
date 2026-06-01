package d3.soar.classes;
public class Person extends User {

    private String name;
    private String surname;

    // Default constructor
    public Person() {
        super();
    }

    // Constructor with all fields
    public Person(String email, String password, String name, String surname) {
        super(email, password);
        this.name = name;
        this.surname = surname;
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

}