package d3.soar.UserData;

/* @brief Represents an admin in the system, containing their admin name, email, and password*/
public class Admin extends User {

    private String adminName;

    /* @brief Constructs a new admin*/
    public Admin() {
        super();
    }

    /* @brief Constructs a new admin with the specified email, password, and admin name*/
    public Admin(String email, String password, String adminName) {
        super(email, password);
        this.adminName = adminName;
    }

    /* @brief Gets the admin name for the admin*/
    public String getAdminName() {
        return adminName;
    }

/* @brief Sets the admin name for the admin*/
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    /* @brief Returns a string representation of the admin object*/
    public String toString() {
        return "Admin{" +
                "adminName='" + adminName + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }   
}