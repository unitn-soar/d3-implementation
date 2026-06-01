package d3.soar;

import java.util.Date;

public class Session {
    private User user; 
    private int token;
    private Date expiry;

    // Default constructor
    public Session() {
    }

    // Constructor with all fields
    public Session(User user, int token, Date expiry) {
        this.user = user;
        this.token = token;
        this.expiry = expiry;
    }

    // Getter for user
    public User getUser() {
        return this.user;
    }

    // Setter for user
    public void setUser(User user) {
        this.user = user;
    }

    // Getter for token
    public int getToken() {
        return this.token;
    }

    // Setter for token
    public void setToken(int token) {
        this.token = token;
    }

    // Getter for expiry    
    public Date getExpiry() {
        return this.expiry;
    }

    // Setter for expiry
    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }  

    // Method to check if the session is still valid
    public boolean isValid() {
        return this.expiry != null && this.expiry.after(new Date());
    }
    
    // Method to terminate the session
    public void terminate() {
        this.expiry = new Date(); // Set expiry to current time to invalidate the session
    }

}
