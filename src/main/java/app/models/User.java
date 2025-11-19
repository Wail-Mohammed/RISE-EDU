package main.java.app.models;

import java.io.Serializable;
import main.java.app.Shared.UserType;

public abstract class User implements Serializable {

    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected UserType userType;

    public User(String username, String password, String firstName, String lastName, UserType userType) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserType getUserType() {
        return userType;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    // Added for DataManager
    public String getPassword() {
        return this.password;
    }
}
