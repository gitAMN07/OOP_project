package com.university.model.academic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.university.enums.RegistrationStatus;
import com.university.model.user.Manager;
import com.university.model.user.Student;

public class CourseRegistration implements Serializable {
    private final String registrationId;
    private Student student;
    private Course course;
    private RegistrationStatus status;
    private LocalDate requestDate;
    private Manager reviewedBy;
    private String rejectionReason;

    public CourseRegistration(Student student, Course course) {
        this.registrationId = UUID.randomUUID().toString();
        this.student = student;
        this.course = course;
        this.status = RegistrationStatus.PENDING;
        this.requestDate = LocalDate.now();
    }

    public void approve(Manager manager) {
        this.status = RegistrationStatus.APPROVED;
        this.reviewedBy = manager;
        course.enrollStudent(student);
    }

    public void reject(Manager manager, String reason) {
        this.status = RegistrationStatus.REJECTED;
        this.reviewedBy = manager;
        this.rejectionReason = reason;
    }

    public String getRegistrationId() { return registrationId; }
    public Student getStudent()       { return student; }
    public Course getCourse()         { return course; }
    public RegistrationStatus getStatus() { return status; }
    public LocalDate getRequestDate() { return requestDate; }
    public Manager getReviewedBy()    { return reviewedBy; }

    @Override
    public String toString() {
        return String.format("Registration[%s → %s | status=%s | date=%s]",
                student.getFullName(), course.getCourseName(), status, requestDate);
    }
}