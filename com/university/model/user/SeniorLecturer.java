package com.university.model.user;

import com.university.enums.TeacherTitle;
import com.university.model.research.ResearchPaper;
import com.university.model.research.ResearchProject;
import com.university.model.research.Researcher;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SeniorLecturer extends Teacher implements Researcher {
    private int hIndex;
    private List<ResearchPaper> researchPapers;
    private List<ResearchProject> researchProjects;
    private boolean isResearcherFlag;

    public SeniorLecturer(String login, String password, String firstName,
                          String lastName, String department, boolean isResearcher) {
        super(login, password, firstName, lastName, department, TeacherTitle.SENIOR_LECTOR);
        this.isResearcherFlag = isResearcher;
        this.hIndex = 0;
        this.researchPapers = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
    }

    @Override public boolean isResearcher() { return isResearcherFlag; }
    @Override public int getHIndex() { return hIndex; }
    public void setHIndex(int h) { this.hIndex = h; }
    @Override public List<ResearchPaper> getResearchPapers() { return researchPapers; }
    @Override public List<ResearchProject> getResearchProjects() { return researchProjects; }
    @Override public void addPaper(ResearchPaper paper) { researchPapers.add(paper); }
    @Override public void addProject(ResearchProject project) { researchProjects.add(project); }
    @Override public int getTotalCitations() {
        return researchPapers.stream().mapToInt(ResearchPaper::getCitations).sum();
    }
    @Override public void printPapers(Comparator<ResearchPaper> c) {
        System.out.println("=== Papers by " + getFullName() + " ===");
        researchPapers.stream().sorted(c).forEach(System.out::println);
    }

    @Override
    public String getInfo() {
        return "SeniorLecturer: " + getFullName() + " | researcher: " + isResearcherFlag
                + " | rating: " + String.format("%.1f", getAverageRating());
    }
}