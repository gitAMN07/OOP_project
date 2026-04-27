package com.university.model.user;

import com.university.enums.TeacherTitle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tutor extends Teacher{
    private int hIndex;
    private boolean isResearcherFlag;

    public Tutor(String login, String password, String firstName,
                 String lastName, String department, boolean isResearcher) {
        super(login, password, firstName, lastName, department, TeacherTitle.TUTOR);
        this.isResearcherFlag = isResearcher;
        this.hIndex = 0;
    }

    @Override public boolean isResearcher() { return isResearcherFlag; }
    public void setHIndex(int h) { this.hIndex = h; }

    @Override
    public String getInfo() {
        return "Tutor: " + getFullName() + " | researcher: " + isResearcherFlag
                + " | rating: " + String.format("%.1f", getAverageRating());
    }
}