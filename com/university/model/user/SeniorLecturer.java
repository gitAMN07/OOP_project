package com.university.model.user;

import com.university.enums.TeacherTitle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SeniorLecturer extends Teacher {
    private int hIndex;
    private boolean isResearcherFlag;

    public SeniorLecturer(String login, String password, String firstName,
                          String lastName, String department, boolean isResearcher) {
        super(login, password, firstName, lastName, department, TeacherTitle.SENIOR_LECTOR);
        this.isResearcherFlag = isResearcher;
        this.hIndex = 0;
    }

    @Override public boolean isResearcher() { return isResearcherFlag; }
    
    @Override
    public String getInfo() {
        return "SeniorLecturer: " + getFullName() + " | researcher: " + isResearcherFlag
                + " | rating: " + String.format("%.1f", getAverageRating());
    }
}