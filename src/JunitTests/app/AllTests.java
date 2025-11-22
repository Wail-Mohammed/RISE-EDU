package app;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

// Import all your tester classes
import Server.SystemManagerTester;
import Server.DataManagerTester;
import Shared.MessageTester;
import Shared.MessageTypeTester;
import Shared.StatusTester;
import Shared.UserTypeTester;
import Client.ClientTester;
import Reports.ReportTester;
import models.UserTester;
import models.CourseTester;
import models.UniversityTester;
import models.StudentTester;
import models.ScheduleTester;
import models.AdminTester;

@Suite
@SelectClasses({
    SystemManagerTester.class,
    DataManagerTester.class,
    MessageTester.class,
    MessageTypeTester.class,
    StatusTester.class,
    UserTypeTester.class,
    ClientTester.class,
    ReportTester.class,
    UserTester.class,
    CourseTester.class,
    UniversityTester.class,
    StudentTester.class,
    ScheduleTester.class,
    AdminTester.class
})
public class AllTests {}