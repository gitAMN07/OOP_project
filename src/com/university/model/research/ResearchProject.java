package com.university.model.research;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.university.exception.NonResearcherJoinException;
import com.university.model.user.User;

public class ResearchProject implements Serializable {
    private final String projectId;
    private String topic;
    private List<Researcher> participants;
    private List<ResearchPaper> publishedPapers;
    private LocalDate startDate;
    private LocalDate endDate;
    private double budget;
    private boolean isActive;

    public ResearchProject(String topic, LocalDate startDate, double budget) {
        this.projectId = UUID.randomUUID().toString();
        this.topic = topic;
        this.startDate = startDate;
        this.budget = budget;
        this.isActive = true;
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }
    
    public void addParticipant(User user) throws NonResearcherJoinException {
        if (!(user instanceof Researcher)) {
            throw new NonResearcherJoinException(user.getLogin());
        }
        Researcher r = (Researcher) user;
        if (!participants.contains(r)) {
            participants.add(r);
            r.addProject(this);
            System.out.println("[PROJECT] " + user.getLogin() + " joined project: " + topic);
        }
    }

    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
        System.out.println("[PROJECT] Paper added to project '" + topic + "': " + paper.getTitle());
    }

    public void close() {
        this.isActive = false;
        this.endDate = LocalDate.now();
        System.out.println("[PROJECT] Project closed: " + topic);
    }

    public String getProjectId()                  { return projectId; }
    public String getTopic()                      { return topic; }
    public List<Researcher> getParticipants()     { return participants; }
    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }
    public LocalDate getStartDate()               { return startDate; }
    public double getBudget()                     { return budget; }
    public boolean isActive()                     { return isActive; }

    @Override
    public String toString() {
        return String.format("Project[\"%s\" | participants=%d | papers=%d | active=%s]",
                topic, participants.size(), publishedPapers.size(), isActive);
    }
}
