package d3.soar.classes.UserData;

public class TravelAgency extends User {

    private String companyName;

    // Default constructor
    public TravelAgency() {
        super();
    }

    // Constructor with all fields
    public TravelAgency(String email, String password, String companyName) {
        super(email, password);
        this.companyName = companyName;
    }

    // Getter for companyName
    public String getCompanyName() {
        return companyName;
    }

    // Setter for companyName
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}