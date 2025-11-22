package app.Report;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Report {
    // Private attributes
    private String reportID;
    private String reportType;
    private Date generatedDate;
    private String reportData;

    // Constructor 
    public Report(String reportType) {
        this.reportType = reportType;
        this.generatedDate = new Date(); // sets current date
        this.reportID = generateReportID();
        this.reportData = "";
    }

    // Generates a unique report ID
    private String generateReportID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "RPT-" + sdf.format(new Date());
    }

    // Generates report data
    public void generate(String data) {
        this.reportData = data;
        this.generatedDate = new Date(); // update generated date
    }

    // Displays the report
    public void displayReport() {
        System.out.println("Report ID: " + reportID);
        System.out.println("Report Type: " + reportType);
        System.out.println("Generated Date: " + generatedDate);
        System.out.println("Report Data: " + reportData);
    }

    // Saves the report (stub implementation)
    public boolean saveReport() {
        // In a real scenario, you could save to a file or database
        System.out.println("Report saved successfully.");
        return true;
    }

    // Getters 
    public String getReportID() { 
    	return reportID; 
    	}
    
    public String getReportType() { 
    	return reportType; 
    	}
    
    public Date getGeneratedDate() { 
    	return generatedDate; 
    	}
    
    public String getReportData() { 
    	return reportData; 
    	}
}

