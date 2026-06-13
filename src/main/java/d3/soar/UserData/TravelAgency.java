package d3.soar.UserData;

/* @brief Represents a travel agency in the system, containing their company name, email, and password*/
public class TravelAgency extends User {

    private String companyName;

/* @brief Constructs a new travel agency*/
    public TravelAgency() {
        super();
    }

/* @brief Constructs a new travel agency with the specified email, password, and company name*/
    public TravelAgency(String email, String password, String companyName) {
        super(email, password);
        this.companyName = companyName;
    }

/* @brief Gets the company name for the travel agency*/
    public String getCompanyName() {
        return companyName;
    }

/* @brief Sets the company name for the travel agency*/
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    /* @brief Returns a string representation of the travel agency object*/
    public String toString() {
        return "TravelAgency{" +
                "companyName='" + companyName + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}