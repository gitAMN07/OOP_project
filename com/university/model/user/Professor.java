package com.university.model.user;

import com.university.enums.TeacherTitle;
import com.university.exception.LowHIndexException;
import com.university.model.research.ResearchPaper;
import com.university.model.research.ResearchProject;
import com.university.model.research.Researcher;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Professor extends Teacher implements Researcher {
    private int hIndex;
    private List<ResearchPaper> researchPapers;
    private List<ResearchProject> researchProjects;
    private List<Student> supervisedStudents;

    public Professor(String login, String password, String firstName,
                     String lastName, String department, int hIndex) {
        super(login, password, firstName, lastName, department, TeacherTitle.PROFESSOR);
        this.hIndex = hIndex;
        this.researchPapers = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
        this.supervisedStudents = new ArrayList<>();
    }

    public void addSupervisedStudent(Student student) throws LowHIndexException {
        if (this.hIndex < 3) throw new LowHIndexException(this.hIndex);
        this.supervisedStudents.add(student);
        student.setSupervisorDirect(this);
        System.out.println("[SUPERVISOR] " + getFullName() + " assigned to " + student.getFullName());
    }

    @Override public int getHIndex() { return hIndex; }
    @Override public List<ResearchPaper> getResearchPapers() { return researchPapers; }
    @Override public List<ResearchProject> getResearchProjects() { return researchProjects; }

    @Override
    public void addPaper(ResearchPaper paper) {
        researchPapers.add(paper);
        System.out.println("[RESEARCH] Paper added: " + paper.getTitle());
    }

    @Override
    public void addProject(ResearchProject project) { researchProjects.add(project); }

    @Override
    public int getTotalCitations() {
        return researchPapers.stream().mapToInt(ResearchPaper::getCitations).sum();
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> c) {
        System.out.println("=== Papers by " + getFullName() + " ===");
        researchPapers.stream().sorted(c).forEach(System.out::println);
    }

    @Override public boolean isResearcher() { return true; }
    public List<Student> getSupervisedStudents() { return supervisedStudents; }

    @Override
    public String getInfo() {
        return "Professor: " + getFullName() + " | h-index: " + hIndex
                + " | papers: " + researchPapers.size() + " | rating: "
                + String.format("%.1f", getAverageRating());
    }
}
