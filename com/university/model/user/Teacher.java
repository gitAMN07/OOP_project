package com.university.model.user;

import com.university.enums.Role;
import com.university.enums.TeacherTitle;
import com.university.model.academic.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Teacher extends Employee {
    private TeacherTitle title;
    private List<Course> courses;
    private List<TeacherRating> ratings;

    public Teacher(String login, String password, String firstName,
                   String lastName, String department, TeacherTitle title) {
        super(login, password, Role.TEACHER, firstName, lastName, department);
        this.title = title;
        this.courses = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public void putMark(Student student, Course course, double att1, double att2, double finalExam) {
        Mark mark = new Mark(course, student);
        mark.setAttestation1(att1);
        mark.setAttestation2(att2);
        mark.setFinalExam(finalExam);
        student.getTranscript().addMark(mark);
        student.updateGPA();
        System.out.printf("[MARK] %s → %s: %.1f/%.1f/%.1f = %.1f (%s)%n",
                getFullName(), student.getFullName(), att1, att2, finalExam,
                mark.getTotal(), mark.getLetterGrade());
    }

    public void viewStudents(Course course) {
        System.out.println("=== Students in " + course.getCourseName() + " ===");
        course.getEnrolledStudents().forEach(s ->
                System.out.printf("  %s | GPA: %.2f%n", s.getFullName(), s.getGpa()));
    }

    public void viewCourses() {
        System.out.println("=== My Courses ===");
        courses.forEach(c -> System.out.println("  " + c.getCourseName() + " (" + c.getCredits() + " cr)"));
    }

    public double getAverageRating() {
        return ratings.stream().mapToInt(TeacherRating::getScore).average().orElse(0);
    }

    public void addRating(TeacherRating rating) { ratings.add(rating); }
    public void addCourse(Course course) { courses.add(course); }
    public TeacherTitle getTitle() { return title; }
    public List<Course> getCourses() { return courses; }
    public List<TeacherRating> getRatings() { return ratings; }

    public abstract boolean isResearcher();

    @Override
    public String toString() {
        return super.toString() + " | title: " + title;
    }
}