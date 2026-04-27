package com.university.model.user;

import com.university.enums.*;
import com.university.exception.CreditLimitException;
import com.university.exception.FailLimitException;
import com.university.model.academic.*;
import java.util.*;

public class Student extends User implements Comparable<Student> {
    private String firstName;
    private String lastName;
    private int yearOfStudy;
    private double gpa;
    private int credits;
    private int failCount;
    private List<CourseRegistration> registrations;
    private ScholarshipStatus scholarshipStatus;
    private RiskLevel riskLevel;
    private List<String> notifications;

    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS = 3;

    public Student(String login, String password, String firstName,
                   String lastName, int yearOfStudy) {
        super(login, password, Role.STUDENT);
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearOfStudy = yearOfStudy;
        this.gpa = 0.0;
        this.credits = 0;
        this.failCount = 0;
        this.registrations = new ArrayList<>();
        this.scholarshipStatus = ScholarshipStatus.NOT_ASSIGNED;
        this.riskLevel = RiskLevel.LOW;
        this.notifications = new ArrayList<>();
                   }

    public CourseRegistration registerForCourse(Course course)
            throws CreditLimitException, FailLimitException {
        if (failCount >= MAX_FAILS)
            throw new FailLimitException(failCount);
        if (credits + course.getCredits() > MAX_CREDITS)
            throw new CreditLimitException(credits, course.getCredits(), MAX_CREDITS);

        CourseRegistration reg = new CourseRegistration(this, course);
        registrations.add(reg);
        System.out.println("[REGISTRATION] " + getFullName() + " applied for: " + course.getCourseName());
        return reg;
    }

    private void updateRiskLevel() {
        if (failCount >= MAX_FAILS || gpa < 1.5) riskLevel = RiskLevel.CRITICAL;
        else if (failCount == 2 || gpa < 2.0)   riskLevel = RiskLevel.HIGH;
        else if (failCount == 1 || gpa < 2.5)   riskLevel = RiskLevel.MEDIUM;
        else                                      riskLevel = RiskLevel.LOW;
    }

    private void updateScholarshipStatus() {
        if (gpa >= 3.5)      scholarshipStatus = ScholarshipStatus.ACTIVE;
        else if (gpa >= 2.5) scholarshipStatus = ScholarshipStatus.SUSPENDED;
        else                 scholarshipStatus = ScholarshipStatus.REVOKED;
    }

    public void rateTeacher(Teacher teacher, int score, String comment) {
        if (score < 1 || score > 5) {
            System.out.println("Score must be between 1 and 5."); return;
        }
        TeacherRating rating = new TeacherRating(teacher, this, score, comment);
        teacher.addRating(rating);
        System.out.println("[RATING] " + getFullName() + " rated " +
                teacher.getFullName() + ": " + score + "/5");
    }

    public void notify(String message) {
        notifications.add(message);
        System.out.println("[NOTIFICATION → " + getFullName() + "] " + message);
    }

    public String getFullName()               { return firstName + " " + lastName; }
    public String getFirstName()              { return firstName; }
    public String getLastName()               { return lastName; }
    public int getYearOfStudy()               { return yearOfStudy; }
    public double getGpa()                    { return gpa; }
    public int getCredits()                   { return credits; }
    public int getFailCount()                 { return failCount; }
    public ScholarshipStatus getScholarshipStatus() { return scholarshipStatus; }
   
    @Override
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student s = (Student) o;
        return Objects.equals(getUserId(), s.getUserId());
    }

    @Override
    public int hashCode() { return Objects.hash(getUserId()); }

    @Override
    public String toString() {
        return String.format("Student[%s | year=%d | GPA=%.2f | fails=%d | risk=%s]",
                getFullName(), yearOfStudy, gpa, failCount);
    }

    @Override
    public String getInfo() {
        return String.format("Student: %s | Year: %d | GPA: %.2f | Scholarship: %s | Risk: %s",
                getFullName(), yearOfStudy, gpa, scholarshipStatus);
    }
}