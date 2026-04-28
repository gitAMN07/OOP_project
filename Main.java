import com.university.enums.*;
import com.university.exception.*;
import com.university.model.academic.*;
import com.university.model.research.*;
import com.university.model.user.*;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== University System Demo ===\n");

        Admin admin = new Admin("admin", "admin123", "Sara", "Kim");
        Manager manager = new Manager("manager1", "pass", "Aizat", "Bekova", ManagerType.DEPARTMENT);
        Professor prof = new Professor("prof.jones", "pass", "Alice", "Jones", "CS", 5);
        Student student1 = new Student("john.doe", "pass", "John", "Doe", 2);
        Student student2 = new Student("jane.doe", "pass", "Jane", "Doe", 4);

        System.out.println("Users created:");
        System.out.println("  " + admin.getInfo());
        System.out.println("  " + manager.getInfo());
        System.out.println("  " + prof.getInfo());
        System.out.println("  " + student1.getInfo());
        System.out.println("  " + student2.getInfo());

        System.out.println("\n--- Courses ---");
        Course oop = new Course("OOP in Java", 3, 30, 2);
        Course math = new Course("Discrete Math", 3, 25, 1);
        manager.assignTeacherToCourse(prof, oop);
        System.out.println("Course: " + oop);

        System.out.println("\n--- Registration ---");
        try {
            CourseRegistration reg = student1.registerForCourse(oop);
            manager.approveRegistration(reg);
        } catch (CreditLimitException | FailLimitException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Marks ---");
        prof.putMark(student1, oop, 80, 85, 90);
        student1.viewMarks();
        student1.printTranscript();

        System.out.println("\n--- Research ---");
        ResearchPaper paper = new ResearchPaper(
                "Deep Learning Survey",
                List.of("Jones A.", "Smith B."),
                "IEEE Transactions",
                "10.1109/001",
                LocalDate.of(2023, 5, 10),
                12, true
        );
        paper.setCitations(95);
        prof.addPaper(paper);
        System.out.println("Paper added: " + paper);

        ResearchProject project = new ResearchProject("AI in Education", LocalDate.now(), 10000);
        try {
            project.addParticipant(prof);
            project.addParticipant(student1);
        } catch (NonResearcherJoinException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n--- Supervisor assignment ---");
        try {
            student2.setSupervisor(prof);
            System.out.println("Supervisor assigned to " + student2.getFullName());
        } catch (LowHIndexException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n--- Credit limit test ---");
        Course c1 = new Course("Course A", 8, 30, 2);
        Course c2 = new Course("Course B", 8, 30, 2);
        Course c3 = new Course("Course C", 8, 30, 2);
        try {
            student1.registerForCourse(c1);
            student1.registerForCourse(c2);
            student1.registerForCourse(c3);
        } catch (CreditLimitException | FailLimitException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Done ===");
    }
}