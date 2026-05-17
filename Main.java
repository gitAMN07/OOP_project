import com.university.enums.*;
import com.university.exception.*;
import com.university.model.academic.*;
import com.university.model.research.*;
import com.university.model.user.*;
import com.university.pattern.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printBanner();
        Database db = Database.getInstance();

        Admin admin       = new Admin("admin", "admin123", "Sara", "Kim");
        Manager manager   = new Manager("mgr", "pass", "Aizat", "Bekova", ManagerType.DEPARTMENT);
        Professor prof    = new Professor("prof.jones", "pass", "Alice", "Jones", "CS", 7);
        Student alice     = new Student("alice", "pass", "Alice", "Johnson", 3);
        Student bob       = new Student("bob", "pass", "Bob", "Williams", 4);

        db.saveUser(admin); db.saveUser(manager);
        db.saveUser(prof);  db.saveUser(alice); db.saveUser(bob);

        Course oop  = new Course("OOP in Java", 3, 30, 2);
        Course ml   = new Course("Machine Learning", 4, 25, 3);
        db.saveCourse(oop); db.saveCourse(ml);
        manager.assignTeacherToCourse(prof, oop);
        manager.assignTeacherToCourse(prof, ml);

        try {
            CourseRegistration r1 = alice.registerForCourse(oop);
            CourseRegistration r2 = alice.registerForCourse(ml);
            CourseRegistration r3 = bob.registerForCourse(ml);
            manager.approveRegistration(r1);
            manager.approveRegistration(r2);
            manager.approveRegistration(r3);
        } catch (CreditLimitException | FailLimitException e) {
            System.out.println("Registration error: " + e.getMessage());
        }

        prof.putMark(alice, oop, 25, 27, 35);
        prof.putMark(alice, ml,  15, 12, 10);
        prof.putMark(bob,   ml,  28, 26, 35);

        ResearchPaper paper1 = new ResearchPaper("Deep Learning for NLP",
            List.of("Jones A.", "Doe B."), "IEEE Transactions",
            "10.1109/001", LocalDate.of(2023, 3, 15), 14, true);
        paper1.setCitations(142);
        ResearchPaper paper2 = new ResearchPaper("Federated Learning Privacy",
            List.of("Jones A."), "ACM Surveys",
            "10.1145/002", LocalDate.of(2022, 11, 1), 22, false);
        paper2.setCitations(87);
        prof.addPaper(paper1);
        prof.addPaper(paper2);
        db.savePaper(paper1); db.savePaper(paper2);

        while (true) {
            showMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> demoStudentView(alice);
                case "2" -> demoStudentView(bob);
                case "3" -> demoProfessorView(prof);
                case "4" -> demoManagerView(manager);
                case "5" -> demoAdminView(admin);
                case "6" -> demoResearch(prof);
                case "7" -> demoExceptions(alice, bob, prof, manager);
                case "0" -> { System.out.println("\nGoodbye! 👋"); return; }
                default  -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   RESEARCH-ORIENTED UNIVERSITY SYSTEM        ║");
        System.out.println("║   OOP Java Project — Totally Spies! 🌟       ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
    }

    static void showMainMenu() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║            MAIN MENU             ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Student view (Alice)         ║");
        System.out.println("║  2. Student view (Bob)           ║");
        System.out.println("║  3. Professor view               ║");
        System.out.println("║  4. Manager view                 ║");
        System.out.println("║  5. Admin view                   ║");
        System.out.println("║  6. Research module demo         ║");
        System.out.println("║  7. Exceptions demo              ║");
        System.out.println("║  0. Exit                         ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Choose: ");
    }

    static void demoStudentView(Student s) {
        System.out.println("\n━━━ STUDENT VIEW: " + s.getFullName() + " ━━━");
        s.printAcademicStatus();
        System.out.println();
        s.viewMarks();
        System.out.println();
        s.printTranscript();
    }

    static void demoProfessorView(Professor p) {
        System.out.println("\n━━━ PROFESSOR VIEW: " + p.getFullName() + " ━━━");
        System.out.println(p.getInfo());
        System.out.println();
        p.viewCourses();
        System.out.println();
        System.out.println("─── Papers sorted by citations ───");
        p.printPapers(new com.university.comparator.PaperByCitationsComparator());
        System.out.println("\n─── Papers sorted by date ───");
        p.printPapers(new com.university.comparator.PaperByDateComparator());
        System.out.printf("%nTotal citations: %d | h-index: %d%n",
            p.getTotalCitations(), p.getHIndex());
    }

    static void demoManagerView(Manager m) {
        System.out.println("\n━━━ MANAGER VIEW: " + m.getFullName() + " ━━━");
        m.printAcademicReport();
        System.out.println("\n─── Students sorted by GPA ───");
        m.getStudentsSortedByGPA().forEach(s ->
            System.out.printf("  %-20s GPA: %.2f | Risk: %s%n",
                s.getFullName(), s.getGpa(), s.getRiskLevel()));
    }

    static void demoAdminView(Admin a) {
        System.out.println("\n━━━ ADMIN VIEW: " + a.getFullName() + " ━━━");
        a.viewLogs();
    }

    static void demoResearch(Professor prof) {
        System.out.println("\n━━━ RESEARCH MODULE ━━━");
        ResearchProject project = new ResearchProject(
            "AI in Education", LocalDate.now(), 50000);
        try {
            project.addParticipant(prof);
            System.out.println(project);
        } catch (NonResearcherJoinException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("\n─── All papers (by citations) ───");
        Database.getInstance().getAllPapersSorted(
            new com.university.comparator.PaperByCitationsComparator())
            .forEach(p -> System.out.println("  " + p));
    }

    static void demoExceptions(Student alice, Student bob, Professor prof, Manager manager) {
        System.out.println("\n━━━ EXCEPTIONS DEMO ━━━");

        // LowHIndexException
        System.out.println("\n[1] Assigning low h-index supervisor:");
        try {
            Professor lowProf = new Professor("low", "x", "Low", "Prof", "Bio", 1);
            bob.setSupervisor(lowProf);
        } catch (LowHIndexException e) {
            System.out.println("  CAUGHT LowHIndexException: " + e.getMessage());
        }

        // Successful supervisor
        System.out.println("\n[2] Assigning valid supervisor (h-index=7):");
        try {
            bob.setSupervisor(prof);
        } catch (LowHIndexException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // CreditLimitException
        System.out.println("\n[3] Exceeding credit limit:");
        try {
            Course big1 = new Course("Big Course A", 8, 30, 3);
            Course big2 = new Course("Big Course B", 8, 30, 3);
            Course big3 = new Course("Big Course C", 8, 30, 3);
            alice.registerForCourse(big1);
            alice.registerForCourse(big2);
            alice.registerForCourse(big3);
        } catch (CreditLimitException e) {
            System.out.println("  CAUGHT CreditLimitException: " + e.getMessage());
        } catch (FailLimitException e) {
            System.out.println("  CAUGHT FailLimitException: " + e.getMessage());
        }

        // NonResearcherJoinException
        System.out.println("\n[4] Non-researcher joining project:");
        try {
            ResearchProject p = new ResearchProject("Test", LocalDate.now(), 0);
            p.addParticipant(alice);
        } catch (NonResearcherJoinException e) {
            System.out.println("  CAUGHT NonResearcherJoinException: " + e.getMessage());
        }

        System.out.println("\n  All exceptions caught correctly ✓");
    }
}