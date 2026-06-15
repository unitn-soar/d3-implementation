package d3.soar.UserData;

///@brief Represents a travel agency in the system, containing their company name, email, and password
/// @see User
public class TravelAgency extends User {

    private String companyName;

///@brief Constructs a new travel agency
    public TravelAgency() {
        super();
    }

///@brief Constructs a new travel agency with the specified email, password, and company name
/// @param email An email
/// @param password A password
/// @param companyName The name of the company
    public TravelAgency(String email, String password, String companyName) {
        super(email, password);
        this.companyName = companyName;
    }

///@brief Gets the company name for the travel agency
    public String getCompanyName() {
        return companyName;
    }

///@brief Sets the company name for the travel agency
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /// @brief Returns a string representation of the Travel Agency object
    /// @return A string representation of the Travel Agency object
    @Override
    public String toString() {
        return "TravelAgency{" +
                "companyName='" + companyName + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}