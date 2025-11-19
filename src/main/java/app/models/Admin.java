package main.java.app.models;

import main.java.app.Shared.UserType;

public class Admin extends User {

    private String adminId;

    public Admin(String username, String password, String firstName, String lastName, String adminId) {
        super(username, password, firstName, lastName, UserType.ADMIN);
        this.adminId = adminId;
    }

    public String getAdminId() {
        return adminId;
    }
}
