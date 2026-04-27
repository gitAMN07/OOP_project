package com.university.model.academic;

import com.university.model.user.Student;
import com.university.model.user.Teacher;
import java.util.*;

public class Course implements java.io.Serializable {
    private final String courseId;
    private String courseName;
    private int credits;
    private List<Teacher> instructors;
    private List<Lesson> lessons;
    private int maxStudents;
    private List<Student> enrolledStudents;
    private int targetYear;

    public Course(String courseName, int credits, int maxStudents, int targetYear) {
        this.courseId = UUID.randomUUID().toString();
        this.courseName = courseName;
        this.credits = credits;
        this.maxStudents = maxStudents;
        this.targetYear = targetYear;
        this.instructors = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    public void addInstructor(Teacher teacher) {
        if (!instructors.contains(teacher)) instructors.add(teacher);
    }

    public void addLesson(Lesson lesson) { lessons.add(lesson); }

    public boolean enrollStudent(Student student) {
        if (isFull()) return false;
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            return true;
        }
        return false;
    }

    public boolean isFull() { return enrolledStudents.size() >= maxStudents; }

    public String getCourseId()               { return courseId; }
    public String getCourseName()             { return courseName; }
    public int getCredits()                   { return credits; }
    public List<Teacher> getInstructors()     { return instructors; }
    public List<Lesson> getLessons()          { return lessons; }
    public int getMaxStudents()               { return maxStudents; }
    public List<Student> getEnrolledStudents(){ return enrolledStudents; }
    public int getTargetYear()                { return targetYear; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        return Objects.equals(courseId, ((Course) o).courseId);
    }

    @Override public int hashCode() { return Objects.hash(courseId); }

    @Override
    public String toString() {
        return String.format("Course[%s | %d cr | year=%d | enrolled=%d/%d]",
                courseName, credits, targetYear, enrolledStudents.size(), maxStudents);
    }
}