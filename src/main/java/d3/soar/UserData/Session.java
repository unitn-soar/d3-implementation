package d3.soar.UserData;

import java.util.Date;

///@brief Represents a session in the system, containing the user, token, and expiry date
/// @see User
public class Session {
    private User user; 
    private int token;
    private Date expiry;

///@brief Constructs a new session
    public Session() {
    }

///@brief Constructs a new session with the specified user, token, and expiry date
/// @param user The active User
/// @param token A unique token
/// @param expiry An expiry date
    public Session(User user, int token, Date expiry) {
        this.user = user;
        this.token = token;
        this.expiry = expiry;
    }

///@brief Gets the user for the session
    public User getUser() {
        return this.user;
    }

///@brief Sets the user for the session
    public void setUser(User user) {
        this.user = user;
    }

///@brief Gets the token for the session
    public int getToken() {
        return this.token;
    }

    ///@brief Sets the token for the session
    public void setToken(int token) {
        this.token = token;
    }

///@brief Gets the expiry date for the session
    public Date getExpiry() {
        return this.expiry;
    }

///@brief Sets the expiry date for the session
    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }  

///@brief Checks if the session is still valid
    public boolean isValid() {
        return this.expiry != null && this.expiry.after(new Date());
    }
    
///@brief Terminates the session by setting its expiry to the current time, effectively invalidating it
    public void terminate() {
        this.expiry = new Date(); 
    }

    /// @brief Returns a string representation of the Session object
    /// @return A string representation of the Session object
    @Override
    public String toString() {
        return "Session{" +
                "user=" + user +
                ", token=" + token +
                ", expiry=" + expiry +
                '}';
    }
}
