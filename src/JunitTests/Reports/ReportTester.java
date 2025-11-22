package Reports;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Report.Report;

public class ReportTester {
	
	@Test
    void generateGettersAndSave() {
        Report report = new Report("Enrollment Summary");
        String id = report.getReportID();
        assertNotNull(id);
        report.generate("Example Report");
        assertEquals("Enrollment Summary", report.getReportType());
        assertEquals("Example Report", report.getReportData());
        assertTrue(report.saveReport());
        assertNotNull(report.getGeneratedDate());
    }

}
