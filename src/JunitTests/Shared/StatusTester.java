package Shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.Status;

public class StatusTester {

	@Test
    void testStatusSuccess() {
        assertEquals("SUCCESS", Status.SUCCESS.name());
    }

	@Test
    void testStatusFail() {
        assertEquals("FAIL", Status.FAIL.name());
    }

	@Test
    void testStatusNull() {
        assertEquals("NULL", Status.NULL.name());
    }

	@Test
    void testStatusValuesLength() {
        assertEquals(3, Status.values().length);
    }

}
