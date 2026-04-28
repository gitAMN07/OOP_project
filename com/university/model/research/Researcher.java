package com.university.model.research;

import java.util.Comparator;
import java.util.List;

public interface Researcher {
    int getHIndex();
    List<ResearchPaper> getResearchPapers();
    List<ResearchProject> getResearchProjects();
    void addPaper(ResearchPaper paper);
    void addProject(ResearchProject project);
    int getTotalCitations();
    void printPapers(Comparator<ResearchPaper> c);
}
