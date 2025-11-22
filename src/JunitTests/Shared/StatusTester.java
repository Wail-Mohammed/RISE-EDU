package Shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.Status;

public class StatusTester {

	 @Test
	    void statusEnumValues() {
	        assertEquals("SUCCESS", Status.SUCCESS.name());
	        assertEquals("FAIL", Status.FAIL.name());
	        assertEquals("NULL", Status.NULL.name());
	        assertEquals(3, Status.values().length);
	    }

}
