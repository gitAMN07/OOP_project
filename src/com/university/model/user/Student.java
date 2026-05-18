package com.university.model.user;

import java.util.*;

import com.university.enums.*;
import com.university.exception.CreditLimitException;
import com.university.exception.FailLimitException;
import com.university.model.academic.*;
import com.university.model.research.ResearchPaper;
import com.university.model.research.ResearchProject;
import com.university.model.research.Researcher;

public class Student extends User implements Comparable<Student> {
    private String firstName;
    private String lastName;
    private int yearOfStudy;
    private double gpa;
    private int credits;
    private int failCount;
    private Transcript transcript;
    private List<CourseRegistration> registrations;
    private Researcher supervisor;
    private ScholarshipStatus scholarshipStatus;
    private RiskLevel riskLevel;
    private List<String> notifications;

    private boolean isResearcher;
    private int hIndex;
    private List<ResearchPaper> researchPapers;
    private List<ResearchProject> researchProjects;

    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS = 3;

    public Student(String login, String password, String firstName,
                   String lastName, int yearOfStudy) {
        super(login, password, Role.STUDENT);
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearOfStudy = yearOfStudy;
        this.gpa = 0.0;
        this.credits = 0;
        this.failCount = 0;
        this.transcript = new Transcript(this);
        this.registrations = new ArrayList<>();
        this.scholarshipStatus = ScholarshipStatus.NOT_ASSIGNED;
        this.riskLevel = RiskLevel.LOW;
        this.notifications = new ArrayList<>();
        this.isResearcher = false;
        this.hIndex = 0;
        this.researchPapers = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
    }

    public CourseRegistration registerForCourse(Course course)
            throws CreditLimitException, FailLimitException {
        if (failCount >= MAX_FAILS)
            throw new FailLimitException(failCount);
        if (credits + course.getCredits() > MAX_CREDITS)
            throw new CreditLimitException(credits, course.getCredits(), MAX_CREDITS);

        CourseRegistration reg = new CourseRegistration(this, course);
        registrations.add(reg);
        System.out.println("[REGISTRATION] " + getFullName() + " applied for: " + course.getCourseName());
        return reg;
    }

    public void updateGPA() {
        List<Mark> marks = transcript.getAllMarks();
        if (marks.isEmpty()) { this.gpa = 0.0; return; }
        double total = marks.stream().mapToDouble(Mark::getTotal).sum();
        this.gpa = total / marks.size() / 25.0;
        this.gpa = Math.min(4.0, Math.round(gpa * 100.0) / 100.0);

        long fails = marks.stream().filter(Mark::isFail).count();
        this.failCount = (int) fails;
        updateRiskLevel();
        updateScholarshipStatus();
    }

    private void updateRiskLevel() {
        if (failCount >= MAX_FAILS || gpa < 1.5) riskLevel = RiskLevel.CRITICAL;
        else if (failCount == 2 || gpa < 2.0)   riskLevel = RiskLevel.HIGH;
        else if (failCount == 1 || gpa < 2.5)   riskLevel = RiskLevel.MEDIUM;
        else                                      riskLevel = RiskLevel.LOW;
    }

    private void updateScholarshipStatus() {
        if (gpa >= 3.5)      scholarshipStatus = ScholarshipStatus.ACTIVE;
        else if (gpa >= 2.5) scholarshipStatus = ScholarshipStatus.SUSPENDED;
        else                 scholarshipStatus = ScholarshipStatus.REVOKED;
    }

    public void rateTeacher(Teacher teacher, int score, String comment) {
        if (score < 1 || score > 5) {
            System.out.println("Score must be between 1 and 5."); return;
        }
        TeacherRating rating = new TeacherRating(teacher, this, score, comment);
        teacher.addRating(rating);
        System.out.println("[RATING] " + getFullName() + " rated " +
                teacher.getFullName() + ": " + score + "/5");
    }

    public void setSupervisor(Researcher supervisor) throws com.university.exception.LowHIndexException {
        if (supervisor.getHIndex() < 3)
            throw new com.university.exception.LowHIndexException(supervisor.getHIndex());
        if (yearOfStudy != 4)
            System.out.println("Warning: supervisor is typically assigned to 4th year students.");
        this.supervisor = supervisor;
        System.out.println("[SUPERVISOR] " + getFullName() + " assigned supervisor with h-index="
                + supervisor.getHIndex());
    }

    public void setSupervisorDirect(Researcher supervisor) { this.supervisor = supervisor; }

    public void viewMarks() {
    System.out.println("╔══════════════════════════════════════════════════════╗");
    System.out.printf( "║  Marks for: %-40s║%n", getFullName());
    System.out.println("╠══════════════════╦═════╦═════╦═══════╦═══════╦══════╣");
    System.out.println("║ Course           ║ AT1 ║ AT2 ║ Final ║ Total ║Grade ║");
    System.out.println("╠══════════════════╬═════╬═════╬═══════╬═══════╬══════╣");
    if (transcript.getAllMarks().isEmpty()) {
        System.out.println("║              No marks recorded yet.                  ║");
    } else {
        transcript.getAllMarks().forEach(m -> System.out.printf(
            "║ %-16s ║ %3.0f ║ %3.0f ║  %3.0f  ║ %5.1f ║  %-3s ║%n",
            m.getCourse().getCourseName().length() > 16
                ? m.getCourse().getCourseName().substring(0, 16)
                : m.getCourse().getCourseName(),
            m.getAttestation1(), m.getAttestation2(),
            m.getFinalExam(), m.getTotal(), m.getLetterGrade()));
    }
    System.out.println("╚══════════════════╩═════╩═════╩═══════╩═══════╩══════╝");
    System.out.printf("  Overall GPA: %.2f | Fails: %d/%d%n", gpa, failCount, MAX_CREDITS);
}

    public void printTranscript() { System.out.println(transcript.formatTranscript()); }

    public void printAcademicStatus() {
    String riskIcon = switch (riskLevel) {
        case CRITICAL -> "⛔ CRITICAL";
        case HIGH     -> "🔴 HIGH";
        case MEDIUM   -> "🟡 MEDIUM";
        case LOW      -> "🟢 LOW";
    };
    String scholarIcon = switch (scholarshipStatus) {
        case ACTIVE      -> "✅ ACTIVE";
        case SUSPENDED   -> "⚠️  SUSPENDED";
        case REVOKED     -> "❌ REVOKED";
        case NOT_ASSIGNED -> "— NOT ASSIGNED";
    };
    System.out.println("╔══════════════════════════════════════╗");
    System.out.println("║        ACADEMIC STATUS REPORT        ║");
    System.out.println("╠══════════════════════════════════════╣");
    System.out.printf( "║  Student    : %-23s║%n", getFullName());
    System.out.printf( "║  Year       : %-23d║%n", yearOfStudy);
    System.out.printf( "║  GPA        : %-23.2f║%n", gpa);
    System.out.printf( "║  Credits    : %d/21%-20s║%n", credits, "");
    System.out.printf( "║  Fails      : %d/3 %-20s║%n", failCount, "");
    System.out.printf( "║  Risk Level : %-23s║%n", riskIcon);
    System.out.printf( "║  Scholarship: %-23s║%n", scholarIcon);
    System.out.println("╚══════════════════════════════════════╝");
    if (riskLevel == RiskLevel.CRITICAL || riskLevel == RiskLevel.HIGH) {
        System.out.println("⚠️  WARNING: Student at risk of academic dismissal");
    }
}

    public void notify(String message) {
        notifications.add(message);
        System.out.println("[NOTIFICATION → " + getFullName() + "] " + message);
    }

    public void enableResearcher(int hIndex) {
        this.isResearcher = true;
        this.hIndex = hIndex;
    }

    public void addResearchPaper(ResearchPaper paper) {
        if (!isResearcher) { System.out.println("Student is not a researcher."); return; }
        researchPapers.add(paper);
    }

    public String getFullName()               { return firstName + " " + lastName; }
    public String getFirstName()              { return firstName; }
    public String getLastName()               { return lastName; }
    public int getYearOfStudy()               { return yearOfStudy; }
    public double getGpa()                    { return gpa; }
    public int getCredits()                   { return credits; }
    public int getFailCount()                 { return failCount; }
    public Transcript getTranscript()         { return transcript; }
    public List<CourseRegistration> getRegistrations() { return registrations; }
    public Researcher getSupervisor()         { return supervisor; }
    public ScholarshipStatus getScholarshipStatus() { return scholarshipStatus; }
    public RiskLevel getRiskLevel()           { return riskLevel; }
    public boolean isResearcher()             { return isResearcher; }
    public int getHIndex()                    { return hIndex; }
    public List<ResearchPaper> getResearchPapers() { return researchPapers; }
    public List<ResearchProject> getResearchProjects() { return researchProjects; }

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student s = (Student) o;
        return Objects.equals(getUserId(), s.getUserId());
    }

    @Override
    public int hashCode() { return Objects.hash(getUserId()); }

    @Override
    public String toString() {
        return String.format("Student[%s | year=%d | GPA=%.2f | fails=%d | risk=%s]",
                getFullName(), yearOfStudy, gpa, failCount, riskLevel);
    }

    @Override
    public String getInfo() {
        return String.format("Student: %s | Year: %d | GPA: %.2f | Scholarship: %s | Risk: %s",
                getFullName(), yearOfStudy, gpa, scholarshipStatus, riskLevel);
    }
}