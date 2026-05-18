import com.university.comparator.*;
import com.university.enums.*;
import com.university.exception.*;
import com.university.model.academic.*;
import com.university.model.research.*;
import com.university.model.user.*;
import com.university.pattern.*;

import com.university.comparator.PaperByCitationsComparator;
import com.university.comparator.PaperByDateComparator;
import com.university.comparator.PaperByLengthComparator;
import com.university.enums.ManagerType;
import com.university.exception.CreditLimitException;
import com.university.exception.FailLimitException;
import com.university.exception.UserNotFoundException;
import com.university.model.academic.Course;
import com.university.model.academic.CourseRegistration;
import com.university.model.research.ResearchPaper;
import com.university.model.user.Admin;
import com.university.model.user.Employee;
import com.university.model.user.Manager;
import com.university.model.user.Professor;
import com.university.model.user.Student;
import com.university.model.user.User;
import com.university.pattern.AuthService;
import com.university.pattern.Database;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final Scanner sc = new Scanner(System.in);
    static Database db = Database.getInstance();

    public static void main(String[] args) {
        seedData();
        printBanner();

        while (true) {
            User user = showLoginScreen();
            if (user == null) continue;

            if      (user instanceof Admin)   adminMenu((Admin) user);
            else if (user instanceof Manager) managerMenu((Manager) user);
            else if (user instanceof Professor) teacherMenu((Professor) user);
            else if (user instanceof Student) studentMenu((Student) user);
        }
    }

    // ─────────────────────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────────────────────
    static User showLoginScreen() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║            UNIVERSITY LOGIN          ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  Demo accounts:                      ║");
        System.out.println("║  admin    / admin123                 ║");
        System.out.println("║  manager1 / pass                     ║");
        System.out.println("║  prof1    / pass                     ║");
        System.out.println("║  alice    / pass                     ║");
        System.out.println("║  bob      / pass                     ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Login: ");
        String login = sc.nextLine().trim();
        System.out.print("Password: ");
        String pass  = sc.nextLine().trim();

        try {
            var session = AuthService.login(login, pass);
            if (session == null) return null;
            return session.getUser();
        } catch (UserNotFoundException e) {
            System.out.println("User not found: " + login);
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────
    // STUDENT MENU
    // ─────────────────────────────────────────────────────────
    static void studentMenu(Student s) {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.printf( "║  STUDENT: %-28s║%n", s.getFullName());
            System.out.printf( "║  GPA: %.2f  Credits: %d/21  Fails: %d/3  ║%n",
                s.getGpa(), s.getCredits(), s.getFailCount());
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. View available courses           ║");
            System.out.println("║  2. Register for a course            ║");
            System.out.println("║  3. View my marks                    ║");
            System.out.println("║  4. View transcript                  ║");
            System.out.println("║  5. Rate a teacher                   ║");
            System.out.println("║  6. View news                        ║");
            System.out.println("║  0. Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose: ");
            switch (sc.nextLine().trim()) {
                case "1" -> viewCourses();
                case "2" -> studentRegister(s);
                case "3" -> s.viewMarks();
                case "4" -> s.printTranscript();
                case "5" -> studentRateTeacher(s);
                case "6" -> viewNews();
                case "0" -> { AuthService.logout(); return; }
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    static void viewCourses() {
        List<Course> courses = db.getAllCourses();
        if (courses.isEmpty()) { System.out.println("No courses available."); return; }
        System.out.println("\n  # | Course               | Cr | Year | Enrolled");
        System.out.println("  --|----------------------|----|------|--------");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.printf("  %d | %-20s | %2d | %4d | %d/%d%n",
                i + 1, c.getCourseName(), c.getCredits(),
                c.getTargetYear(), c.getEnrolledStudents().size(), c.getMaxStudents());
        }
    }

    static void studentRegister(Student s) {
        viewCourses();
        List<Course> courses = db.getAllCourses();
        if (courses.isEmpty()) return;
        System.out.print("Enter course number: ");
        try {
            int n = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (n < 0 || n >= courses.size()) { System.out.println("Invalid number."); return; }
            Course c = courses.get(n);
            CourseRegistration reg = s.registerForCourse(c);
            db.saveRegistration(reg);
            System.out.println("Request sent! Waiting for manager approval.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
        } catch (CreditLimitException | FailLimitException e) {
            System.out.println("Cannot register: " + e.getMessage());
        }
    }

    static void studentRateTeacher(Student s) {
        List<User> teachers = db.getAllUsers().stream()
            .filter(u -> u instanceof Professor).toList();
        if (teachers.isEmpty()) { System.out.println("No teachers found."); return; }
        System.out.println("\n  Teachers:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1,
                ((Professor) teachers.get(i)).getFullName());
        }
        System.out.print("Choose teacher number: ");
        try {
            int n = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (n < 0 || n >= teachers.size()) { System.out.println("Invalid."); return; }
            Professor t = (Professor) teachers.get(n);
            System.out.print("Score (1-5): ");
            int score = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Comment: ");
            String comment = sc.nextLine().trim();
            s.rateTeacher(t, score, comment);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
        }
    }

    // ─────────────────────────────────────────────────────────
    // TEACHER MENU
    // ─────────────────────────────────────────────────────────
    static void teacherMenu(Professor p) {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.printf( "║  PROFESSOR: %-26s║%n", p.getFullName());
            System.out.printf( "║  h-index: %d  Rating: %.1f/5          ║%n",
                p.getHIndex(), p.getAverageRating());
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. View my courses                  ║");
            System.out.println("║  2. View students in a course        ║");
            System.out.println("║  3. Put mark                         ║");
            System.out.println("║  4. View my research papers          ║");
            System.out.println("║  5. Send message to employee         ║");
            System.out.println("║  0. Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose: ");
            switch (sc.nextLine().trim()) {
                case "1" -> p.viewCourses();
                case "2" -> teacherViewStudents(p);
                case "3" -> teacherPutMark(p);
                case "4" -> {
                    System.out.println("Sort: 1-Citations  2-Date  3-Length");
                    System.out.print("Choose: ");
                    switch (sc.nextLine().trim()) {
                        case "1" -> p.printPapers(new PaperByCitationsComparator());
                        case "2" -> p.printPapers(new PaperByDateComparator());
                        case "3" -> p.printPapers(new PaperByLengthComparator());
                        default  -> p.printPapers(new PaperByCitationsComparator());
                    }
                }
                case "5" -> sendMessage(p);
                case "0" -> { AuthService.logout(); return; }
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    static void teacherViewStudents(Professor p) {
        List<Course> courses = p.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses assigned."); return; }
        for (int i = 0; i < courses.size(); i++)
            System.out.printf("  %d. %s%n", i + 1, courses.get(i).getCourseName());
        System.out.print("Choose course number: ");
        try {
            int n = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (n < 0 || n >= courses.size()) { System.out.println("Invalid."); return; }
            p.viewStudents(courses.get(n));
        } catch (NumberFormatException e) { System.out.println("Enter a number."); }
    }

    static void teacherPutMark(Professor p) {
        List<Course> courses = p.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses."); return; }
        System.out.println("  Courses:");
        for (int i = 0; i < courses.size(); i++)
            System.out.printf("  %d. %s%n", i + 1, courses.get(i).getCourseName());
        System.out.print("Choose course number: ");
        try {
            int cn = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (cn < 0 || cn >= courses.size()) { System.out.println("Invalid."); return; }
            Course course = courses.get(cn);
            List<Student> students = course.getEnrolledStudents();
            if (students.isEmpty()) { System.out.println("No students enrolled."); return; }
            System.out.println("  Students:");
            for (int i = 0; i < students.size(); i++)
                System.out.printf("  %d. %s%n", i + 1, students.get(i).getFullName());
            System.out.print("Choose student number: ");
            int sn = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (sn < 0 || sn >= students.size()) { System.out.println("Invalid."); return; }
            Student student = students.get(sn);
            System.out.print("ATT1 (0-30): "); double a1 = Double.parseDouble(sc.nextLine().trim());
            System.out.print("ATT2 (0-30): "); double a2 = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Final (0-40): "); double fn = Double.parseDouble(sc.nextLine().trim());
            p.putMark(student, course, a1, a2, fn);
        } catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
    }

    static void sendMessage(Employee from) {
        List<User> employees = db.getAllUsers().stream()
            .filter(u -> u instanceof Employee && !u.getUserId().equals(from.getUserId())).toList();
        if (employees.isEmpty()) { System.out.println("No other employees."); return; }
        System.out.println("  Send to:");
        for (int i = 0; i < employees.size(); i++)
            System.out.printf("  %d. %s (%s)%n", i + 1,
                ((Employee) employees.get(i)).getFullName(), employees.get(i).getRole());
        System.out.print("Choose number: ");
        try {
            int n = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (n < 0 || n >= employees.size()) { System.out.println("Invalid."); return; }
            Employee to = (Employee) employees.get(n);
            System.out.print("Subject: "); String subj = sc.nextLine().trim();
            System.out.print("Message: "); String body = sc.nextLine().trim();
            from.sendMessage(to, subj, body);
        } catch (NumberFormatException e) { System.out.println("Enter a number."); }
    }

    // ─────────────────────────────────────────────────────────
    // MANAGER MENU
    // ─────────────────────────────────────────────────────────
    static void managerMenu(Manager m) {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.printf( "║  MANAGER: %-28s║%n", m.getFullName());
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. View pending registrations       ║");
            System.out.println("║  2. Approve / reject registration    ║");
            System.out.println("║  3. Assign teacher to course         ║");
            System.out.println("║  4. Academic report                  ║");
            System.out.println("║  5. Students sorted by GPA           ║");
            System.out.println("║  6. Publish news                     ║");
            System.out.println("║  0. Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose: ");
            switch (sc.nextLine().trim()) {
                case "1" -> viewPendingRegistrations();
                case "2" -> managerApprove(m);
                case "3" -> managerAssignTeacher(m);
                case "4" -> m.printAcademicReport();
                case "5" -> m.getStudentsSortedByGPA().forEach(s ->
                    System.out.printf("  %-20s GPA: %.2f | Risk: %s%n",
                        s.getFullName(), s.getGpa(), s.getRiskLevel()));
                case "6" -> {
                    System.out.print("Title: "); String title = sc.nextLine().trim();
                    System.out.print("Body: ");  String body  = sc.nextLine().trim();
                    m.publishNews(title, body);
                }
                case "0" -> { AuthService.logout(); return; }
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    static void viewPendingRegistrations() {
        List<CourseRegistration> pending = db.getPendingRegistrations();
        if (pending.isEmpty()) { System.out.println("No pending registrations."); return; }
        System.out.println("\n  # | Student              | Course               | Date");
        System.out.println("  --|----------------------|----------------------|----------");
        for (int i = 0; i < pending.size(); i++) {
            CourseRegistration r = pending.get(i);
            System.out.printf("  %d | %-20s | %-20s | %s%n",
                i + 1, r.getStudent().getFullName(),
                r.getCourse().getCourseName(), r.getRequestDate());
        }
    }

    static void managerApprove(Manager m) {
        List<CourseRegistration> pending = db.getPendingRegistrations();
        if (pending.isEmpty()) { System.out.println("No pending registrations."); return; }
        viewPendingRegistrations();
        System.out.print("Choose number: ");
        try {
            int n = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (n < 0 || n >= pending.size()) { System.out.println("Invalid."); return; }
            CourseRegistration reg = pending.get(n);
            System.out.print("Approve? (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y")) {
                m.approveRegistration(reg);
            } else {
                System.out.print("Reason for rejection: ");
                String reason = sc.nextLine().trim();
                m.rejectRegistration(reg, reason);
            }
        } catch (NumberFormatException e) { System.out.println("Enter a number."); }
    }

    static void managerAssignTeacher(Manager m) {
        List<User> teachers = db.getAllUsers().stream()
            .filter(u -> u instanceof Professor).toList();
        List<Course> courses = db.getAllCourses();
        if (teachers.isEmpty() || courses.isEmpty()) {
            System.out.println("No teachers or courses available."); return;
        }
        System.out.println("  Teachers:");
        for (int i = 0; i < teachers.size(); i++)
            System.out.printf("  %d. %s%n", i + 1,
                ((Professor) teachers.get(i)).getFullName());
        System.out.print("Choose teacher: ");
        try {
            int tn = Integer.parseInt(sc.nextLine().trim()) - 1;
            System.out.println("  Courses:");
            for (int i = 0; i < courses.size(); i++)
                System.out.printf("  %d. %s%n", i + 1, courses.get(i).getCourseName());
            System.out.print("Choose course: ");
            int cn = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (tn < 0 || tn >= teachers.size() || cn < 0 || cn >= courses.size()) {
                System.out.println("Invalid choice."); return;
            }
            m.assignTeacherToCourse((Professor) teachers.get(tn), courses.get(cn));
        } catch (NumberFormatException e) { System.out.println("Enter a number."); }
    }

    // ─────────────────────────────────────────────────────────
    // ADMIN MENU
    // ─────────────────────────────────────────────────────────
    static void adminMenu(Admin a) {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.printf( "║  ADMIN: %-30s║%n", a.getFullName());
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. View all users                   ║");
            System.out.println("║  2. Add new student                  ║");
            System.out.println("║  3. Ban a user                       ║");
            System.out.println("║  4. View system logs                 ║");
            System.out.println("║  5. Add new course                   ║");
            System.out.println("║  0. Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choose: ");
            switch (sc.nextLine().trim()) {
                case "1" -> db.getAllUsers().forEach(u ->
                    System.out.printf("  [%s] %s%n", u.getRole(), u.getLogin()));
                case "2" -> adminAddStudent(a);
                case "3" -> adminBanUser(a);
                case "4" -> a.viewLogs();
                case "5" -> adminAddCourse();
                case "0" -> { AuthService.logout(); return; }
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    static void adminAddStudent(Admin a) {
        System.out.print("Login: ");    String login = sc.nextLine().trim();
        System.out.print("Password: "); String pass  = sc.nextLine().trim();
        System.out.print("First name: "); String fn  = sc.nextLine().trim();
        System.out.print("Last name: ");  String ln  = sc.nextLine().trim();
        System.out.print("Year of study (1-4): ");
        try {
            int year = Integer.parseInt(sc.nextLine().trim());
            Student s = new Student(login, pass, fn, ln, year);
            a.addUser(s);
            System.out.println("Student created: " + s.getFullName());
        } catch (NumberFormatException e) { System.out.println("Invalid year."); }
    }

    static void adminBanUser(Admin a) {
        System.out.println("  Users:");
        List<User> users = db.getAllUsers();
        for (int i = 0; i < users.size(); i++)
            System.out.printf("  %d. [%s] %s%n", i + 1,
                users.get(i).getRole(), users.get(i).getLogin());
        System.out.print("Choose number: ");
        try {
            int n = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (n < 0 || n >= users.size()) { System.out.println("Invalid."); return; }
            a.banUser(users.get(n).getUserId());
        } catch (NumberFormatException e) { System.out.println("Enter a number."); }
    }

    static void adminAddCourse() {
        System.out.print("Course name: ");  String name   = sc.nextLine().trim();
        System.out.print("Credits: ");
        try {
            int cr  = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Max students: ");
            int max = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Target year: ");
            int yr  = Integer.parseInt(sc.nextLine().trim());
            Course c = new Course(name, cr, max, yr);
            db.saveCourse(c);
            System.out.println("Course added: " + name);
        } catch (NumberFormatException e) { System.out.println("Invalid number."); }
    }

    // ─────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────
    static void viewNews() {
        List<com.university.model.academic.News> news = db.getAllNews();
        if (news.isEmpty()) { System.out.println("No news."); return; }
        news.forEach(n -> System.out.println("\n  " + n));
    }

    static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   RESEARCH-ORIENTED UNIVERSITY SYSTEM        ║");
        System.out.println("║   OOP Java Project  --  Totally Spies!       ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    // ─────────────────────────────────────────────────────────
    // SEED DATA
    // ─────────────────────────────────────────────────────────
    static void seedData() {
        Admin admin     = new Admin("admin", "admin123", "Sara", "Kim");
        Manager manager = new Manager("manager1", "pass", "Aizat", "Bekova", ManagerType.DEPARTMENT);
        Professor prof  = new Professor("prof1", "pass", "Alice", "Jones", "CS", 7);
        Student alice   = new Student("alice", "pass", "Alice", "Johnson", 3);
        Student bob     = new Student("bob",   "pass", "Bob",   "Williams", 4);

        db.saveUser(admin);
        db.saveUser(manager);
        db.saveUser(prof);
        db.saveUser(alice);
        db.saveUser(bob);

        Course oop = new Course("OOP in Java",       3, 30, 2);
        Course ml  = new Course("Machine Learning",  4, 25, 3);
        Course math= new Course("Discrete Math",     3, 20, 1);
        db.saveCourse(oop);
        db.saveCourse(ml);
        db.saveCourse(math);

        manager.assignTeacherToCourse(prof, oop);
        manager.assignTeacherToCourse(prof, ml);

        // pre-approve alice into OOP, bob into ML
        try {
            CourseRegistration r1 = alice.registerForCourse(oop);
            CourseRegistration r2 = bob.registerForCourse(ml);
            db.saveRegistration(r1);
            db.saveRegistration(r2);
            manager.approveRegistration(r1);
            manager.approveRegistration(r2);
        } catch (Exception ignored) {}

        prof.putMark(alice, oop, 25, 27, 35);
        prof.putMark(bob,   ml,  28, 26, 35);

        ResearchPaper p1 = new ResearchPaper("Deep Learning for NLP",
            List.of("Jones A."), "IEEE Trans", "10.1/001",
            LocalDate.of(2023,3,15), 14, true);
        p1.setCitations(142);
        ResearchPaper p2 = new ResearchPaper("Federated Learning Privacy",
            List.of("Jones A."), "ACM Surveys", "10.2/002",
            LocalDate.of(2022,11,1), 22, false);
        p2.setCitations(87);
        prof.addPaper(p1);
        prof.addPaper(p2);
        db.savePaper(p1);
        db.savePaper(p2);
    }
}