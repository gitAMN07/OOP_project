package com.university.model.user;

import com.university.enums.ManagerType;
import com.university.enums.Role;
import com.university.model.academic.*;
import com.university.pattern.Database;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        double avgGpa = students.stream().mapToDouble(Student::getGpa).average().orElse(0);
        long atRisk = students.stream().filter(s -> s.getGpa() < 2.0).count();
        System.out.println("=== ACADEMIC REPORT ===");
        System.out.printf("Total students: %d%n", students.size());
        System.out.printf("Average GPA: %.2f%n", avgGpa);
        System.out.printf("Students at risk (GPA < 2.0): %d%n", atRisk);
        System.out.println("Top 3 by GPA:");
        students.stream().sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .limit(3).forEach(s -> System.out.printf("  %s — GPA: %.2f%n", s.getFullName(), s.getGpa()));
    }

    public ManagerType getManagerType() { return managerType; }

    @Override
    public String getInfo() {
        return "Manager: " + getFullName() + " | type: " + managerType + " | login: " + getLogin();
    }
}
