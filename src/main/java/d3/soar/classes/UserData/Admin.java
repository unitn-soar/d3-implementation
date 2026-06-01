package d3.soar.classes.UserData;

public class Admin extends User {

    private String adminName;

    // Default constructor
    public Admin() {
        super();
    }

    // Constructor with all fields
    public Admin(String email, String password, String adminName) {
        super(email, password);
        this.adminName = adminName;
    }

    // Getter for adminName
    public String getAdminName() {
        return adminName;
    }

    // Setter for adminName
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}