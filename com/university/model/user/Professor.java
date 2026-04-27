package com.university.model.user;

import com.university.enums.TeacherTitle;
import com.university.exception.LowHIndexException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Professor extends Teacher {
    private int hIndex;
    private List<Student> supervisedStudents;

    public Professor(String login, String password, String firstName,
                     String lastName, String department, int hIndex) {
        super(login, password, firstName, lastName, department, TeacherTitle.PROFESSOR);
        this.hIndex = hIndex;
        this.supervisedStudents = new ArrayList<>();
    }

    @Override public boolean isResearcher() { return true; }
    public List<Student> getSupervisedStudents() { return supervisedStudents; }

    @Override
    public String getInfo() {
        return "Professor: " + getFullName() + " | h-index: " + hIndex
                + " | rating: "
                + String.format("%.1f", getAverageRating());
    }
}