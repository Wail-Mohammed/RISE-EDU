package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.UserType;
import app.models.Admin;

public class AdminTester {

	@Test
    void testAdminGetAdminId() {
        Admin admin = new Admin("admin","password1","firstName","lastName","A1100");
        assertEquals("A1100", admin.getAdminId());
    }

	@Test
    void testAdminGetUserType() {
        Admin admin = new Admin("admin","password1","firstName","lastName","A1100");
        assertEquals(UserType.ADMIN, admin.getUserType());
    }

}
