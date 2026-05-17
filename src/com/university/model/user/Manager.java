package src.com.university.model.user;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import src.com.university.enums.ManagerType;
import src.com.university.enums.Role;
import src.com.university.model.academic.*;
import src.com.university.pattern.Database;

public class Manager extends Employee {
    private ManagerType managerType;

    public Manager(String login, String password, String firstName,
                   String lastName, ManagerType type) {
        super(login, password, Role.MANAGER, firstName, lastName, "Management");
        this.managerType = type;
    }

    public void approveRegistration(CourseRegistration reg) {
        reg.approve(this);
        System.out.println("[MANAGER] Approved: " + reg.getStudent().getFullName()
                + " → " + reg.getCourse().getCourseName());
    }

    public void rejectRegistration(CourseRegistration reg, String reason) {
        reg.reject(this, reason);
        System.out.println("[MANAGER] Rejected: " + reg.getStudent().getFullName()
                + " reason: " + reason);
    }

    public void assignTeacherToCourse(Teacher teacher, Course course) {
        course.addInstructor(teacher);
        teacher.addCourse(course);
        System.out.println("[MANAGER] Assigned " + teacher.getFullName() + " to " + course.getCourseName());
    }

    public void addCourseForRegistration(Course course) {
        Database.getInstance().saveCourse(course);
        System.out.println("[MANAGER] Course added for registration: " + course.getCourseName());
    }

    public List<Student> getStudentsSortedByGPA() {
        return Database.getInstance().getAllStudents().stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsSortedAlphabetically() {
        return Database.getInstance().getAllStudents().stream()
                .sorted(Comparator.comparing(Student::getFullName))
                .collect(Collectors.toList());
    }

    public void printAcademicReport() {
        List<Student> students = Database.getInstance().getAllStudents();
        if (students.isEmpty()) { System.out.println("No students in system."); return; }

        double avgGpa  = students.stream().mapToDouble(Student::getGpa).average().orElse(0);
        long critical  = students.stream().filter(s -> s.getRiskLevel() == src.com.university.enums.RiskLevel.CRITICAL).count();
        long high      = students.stream().filter(s -> s.getRiskLevel() == src.com.university.enums.RiskLevel.HIGH).count();
        long atRisk    = critical + high;

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║           ACADEMIC REPORT                ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Total students : %-22d║%n", students.size());
        System.out.printf( "║  Average GPA    : %-22.2f║%n", avgGpa);
        System.out.printf( "║  At risk        : %-22d║%n", atRisk);
        System.out.printf( "║  Critical risk  : %-22d║%n", critical);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  TOP STUDENTS BY GPA                     ║");
        System.out.println("╠══════════════════════════════════════════╣");
        students.stream()
            .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
            .limit(5)
         .forEach(s -> System.out.printf("║  %-20s GPA: %.2f   %-4s║%n",
            s.getFullName(), s.getGpa(),
            s.getRiskLevel() == src.com.university.enums.RiskLevel.LOW ? "✓" : "⚠"));
        System.out.println("╚══════════════════════════════════════════╝");
        }

    public void publishNews(String title, String body) {
        News news = new News(title, body, this);
        Database.getInstance().saveNews(news);
        src.com.university.pattern.NewsPublisher.getInstance().publish(news);
        System.out.println("[MANAGER] News published: " + title);
    }

    public ManagerType getManagerType() { return managerType; }

    @Override
    public String getInfo() {
        return "Manager: " + getFullName() + " | type: " + managerType + " | login: " + getLogin();
    }
}
