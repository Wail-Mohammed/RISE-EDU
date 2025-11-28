package Reports;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Report.Report;

public class ReportTester {
	
	@Test
    void testReportGetReportID() {
        Report report = new Report("Enrollment Summary", "RISE-EDU");
        String id = report.getReportID();
        assertNotNull(id);
    }

	@Test
    void testReportGetReportType() {
        Report report = new Report("Enrollment Summary", "RISE-EDU");
        assertEquals("Enrollment Summary", report.getReportType());
    }

	@Test
    void testReportGenerate() {
        Report report = new Report("Enrollment Summary", "RISE-EDU");
        report.generate("Example Report");
        assertEquals("Example Report", report.getReportData());
    }

	@Test
    void testReportGetReportData() {
        Report report = new Report("Enrollment Summary", "RISE-EDU");
        report.generate("Example Report");
        assertEquals("Example Report", report.getReportData());
    }

	@Test
    void testReportSaveReport() {
        Report report = new Report("Enrollment Summary", "RISE-EDU");
        report.generate("Example Report");
        assertTrue(report.saveReport());
    }

	@Test
    void testReportGetGeneratedDate() {
        Report report = new Report("Enrollment Summary", "RISE-EDU");
        report.generate("Example Report");
        assertNotNull(report.getGeneratedDate());
    }

}
