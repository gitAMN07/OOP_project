package com.university.model.academic;

import com.university.model.user.Student;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Mark implements Serializable {
    private final String markId;
    private Course course;
    private Student student;
    private double attestation1;
    private double attestation2;
    private double finalExam;

    public Mark(Course course, Student student) {
        this.markId = UUID.randomUUID().toString();
        this.course = course;
        this.student = student;
    }

    public double getTotal() {
        return attestation1 * 0.3 + attestation2 * 0.3 + finalExam * 0.4;
    }

    public boolean isFail() { return getTotal() < 50.0; }

    public String getLetterGrade() {
        double total = getTotal();
        if (total >= 90) return "A";
        if (total >= 80) return "B";
        if (total >= 70) return "C";
        if (total >= 60) return "D";
        if (total >= 50) return "E";
        return "F";
    }

    public void setAttestation1(double v) { this.attestation1 = clamp(v); }
    public void setAttestation2(double v) { this.attestation2 = clamp(v); }
    public void setFinalExam(double v)    { this.finalExam = clamp(v); }

    private double clamp(double v) { return Math.max(0, Math.min(100, v)); }

    public String getMarkId()       { return markId; }
    public Course getCourse()       { return course; }
    public Student getStudent()     { return student; }
    public double getAttestation1() { return attestation1; }
    public double getAttestation2() { return attestation2; }
    public double getFinalExam()    { return finalExam; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mark)) return false;
        return Objects.equals(markId, ((Mark) o).markId);
    }

    @Override public int hashCode() { return Objects.hash(markId); }

    @Override
    public String toString() {
        return String.format("Mark[%s: att1=%.0f att2=%.0f final=%.0f → total=%.1f (%s)]",
                course.getCourseName(), attestation1, attestation2, finalExam,
                getTotal(), getLetterGrade());
    }
}