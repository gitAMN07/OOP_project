import com.university.comparator.*;
import com.university.enums.*;
import com.university.exception.*;
import com.university.feature.*;
import com.university.model.academic.*;
import com.university.model.research.*;
import com.university.model.user.*;
import com.university.pattern.*;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Research-Oriented University System");

        Database db = Database.getInstance();

        System.out.println("--- [1] Creating users ---");
        Admin admin = new Admin("admin", "admin123", "System", "Admin");
        Manager manager = new Manager("manager1", "pass", "Aizat", "Bekova", ManagerType.DEPARTMENT);
        Professor prof = new Professor("prof.smith", "pass", "John", "Smith", "Computer Science", 7);
        Tutor tutor = new Tutor("tutor.ali", "pass", "Ali", "Nurlan", "Mathematics", false);

        Student alice = new Student("alice", "pass123", "Alice", "Johnson", 3);
        Student bob   = new Student("bob",   "pass123", "Bob",   "Williams", 4);
        Student kate  = new Student("kate",  "pass123", "Kate",  "Brown",    2);

        db.saveUser(admin);
        db.saveUser(manager);
        db.saveUser(prof);
        db.saveUser(tutor);
        db.saveUser(alice);
        db.saveUser(bob);
        db.saveUser(kate);

        System.out.println("\n--- [2] Authentication ---");
        try {
            AuthService.login("admin", "wrong_password");
            AuthService.login("admin", "admin123");
            AuthService.logout();
            AuthService.login("alice", "pass123");
        } catch (UserNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println("\n--- [3] Courses & Registration ---");
        Course oop  = new Course("OOP in Java", 3, 30, 2);
        Course ml   = new Course("Machine Learning", 4, 25, 3);
        Course math = new Course("Advanced Math", 3, 20, 1);
        db.saveCourse(oop);
        db.saveCourse(ml);
        db.saveCourse(math);

        manager.assignTeacherToCourse(prof, oop);
        manager.assignTeacherToCourse(prof, ml);
        manager.assignTeacherToCourse(tutor, math);

        try {
            CourseRegistration reg1 = alice.registerForCourse(oop);
            CourseRegistration reg2 = alice.registerForCourse(ml);
            CourseRegistration reg3 = bob.registerForCourse(ml);
            db.saveRegistration(reg1);
            db.saveRegistration(reg2);
            db.saveRegistration(reg3);

            manager.approveRegistration(reg1);
            manager.approveRegistration(reg2);
            manager.approveRegistration(reg3);

        } catch (CreditLimitException | FailLimitException e) {
            System.out.println("Registration ERROR: " + e.getMessage());
        }

        System.out.println("\n--- [4] Putting marks ---");
        prof.putMark(alice, oop, 85, 90, 88);
        prof.putMark(alice, ml,  40, 35, 30);
        prof.putMark(bob,   ml,  70, 75, 80);

        alice.viewMarks();
        alice.printTranscript();
        System.out.println("\nAlice info: " + alice.getInfo());

        System.out.println("\n--- [5] Teacher rating ---");
        alice.rateTeacher(prof, 5, "Great professor, explains well!");
        bob.rateTeacher(prof, 4, "Good but sometimes too fast.");
        System.out.printf("Prof. Smith average rating: %.1f/5%n", prof.getAverageRating());

        System.out.println("\n--- [6] Research module ---");
        ResearchPaper paper1 = new ResearchPaper(
                "Deep Learning for NLP",
                List.of("Smith J.", "Doe A."),
                "IEEE Transactions on Neural Networks",
                "10.1109/TNN.2022.001",
                LocalDate.of(2022, 3, 15), 14, true);
        paper1.setCitations(142);

        ResearchPaper paper2 = new ResearchPaper(
                "Quantum Computing Algorithms",
                List.of("Smith J.", "Brown K."),
                "Nature Computing",
                "10.1038/NC.2023.042",
                LocalDate.of(2023, 7, 1), 22, false);
        paper2.setCitations(87);

        ResearchPaper paper3 = new ResearchPaper(
                "Federated Learning Privacy",
                List.of("Smith J."),
                "ACM Computing Surveys",
                "10.1145/CS.2021.118",
                LocalDate.of(2021, 11, 20), 31, true);
        paper3.setCitations(210);

        prof.addPaper(paper1);
        prof.addPaper(paper2);
        prof.addPaper(paper3);
        db.savePaper(paper1);
        db.savePaper(paper2);
        db.savePaper(paper3);

        System.out.println("\n-- Papers sorted by citations --");
        prof.printPapers(new PaperByCitationsComparator());

        System.out.println("\n-- Papers sorted by date (newest first) --");
        prof.printPapers(new PaperByDateComparator());

        System.out.println("\n-- Papers sorted by length --");
        prof.printPapers(new PaperByLengthComparator());

        System.out.println("\n--- [7] Research project & custom exceptions ---");
        ResearchProject project = new ResearchProject(
                "AI in Education", LocalDate.of(2024, 1, 1), 50000.0);

        try {
            project.addParticipant(prof);
            project.addParticipant(alice);
        } catch (NonResearcherJoinException e) {
            System.out.println("CAUGHT: " + e.getMessage());
        }

        alice.enableResearcher(1);
        try {
            project.addParticipant(alice);
        } catch (NonResearcherJoinException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        project.addPaper(paper1);
        System.out.println(project);

        System.out.println("\n--- [8] Supervisor assignment ---");
        Professor lowHProf = new Professor("low.prof", "pass", "Low", "HIndex",
                "Biology", 1);
        db.saveUser(lowHProf);

        try {
            bob.setSupervisor(lowHProf);
        } catch (LowHIndexException e) {
            System.out.println("CAUGHT: " + e.getMessage());
        }

        try {
            bob.setSupervisor(prof);
        } catch (LowHIndexException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println("\n--- [9] Credit limit exception ---");
        Course bigCourse1 = new Course("Heavy Course A", 8, 30, 2);
        Course bigCourse2 = new Course("Heavy Course B", 8, 30, 2);
        Course bigCourse3 = new Course("Heavy Course C", 8, 30, 2);
        db.saveCourse(bigCourse1);
        db.saveCourse(bigCourse2);
        db.saveCourse(bigCourse3);
        try {
            kate.registerForCourse(bigCourse1);
            kate.registerForCourse(bigCourse2);
            kate.registerForCourse(bigCourse3);
        } catch (CreditLimitException | FailLimitException e) {
            System.out.println("CAUGHT: " + e.getMessage());
        }

        System.out.println("\n--- [10] News & Observer pattern ---");
        NewsPublisher publisher = NewsPublisher.getInstance();
        publisher.subscribeAll();
        manager.publishNews("Exam Schedule Released",
                "Final exams will be held from Dec 20 to Dec 30.");

        System.out.println("\n--- [11] Academic Risk Detector ---");
        AcademicRiskDetector detector = new AcademicRiskDetector();
        detector.runFullAnalysis();

        System.out.println("\n--- [12] Citation Graph ---");
        CitationGraph graph = new CitationGraph();
        graph.addCitationLink(paper3, paper1);
        graph.addCitationLink(paper3, paper2);
        graph.addCitationLink(paper1, paper2);
        ResearchPaper most = graph.getMostInfluentialPaper();
        if (most != null) System.out.println("Most influential: " + most.getTitle());
        graph.printTopCitedResearchers();

        System.out.println("\n--- [13] Admin operations ---");
        AuthService.logout();
        try { AuthService.login("admin", "admin123"); }
        catch (UserNotFoundException e) { System.out.println(e.getMessage()); }
        admin.viewLogs();

        System.out.println("\n--- [14] Academic report ---");
        manager.printAcademicReport();
        System.out.println("\nStudents sorted by GPA:");
        manager.getStudentsSortedByGPA().forEach(s ->
                System.out.printf("  %s — GPA: %.2f%n", s.getFullName(), s.getGpa()));

        System.out.println("\n--- [15] Saving data ---");
        db.save();

        System.out.println("Demo completed successfully!");
    }
}
