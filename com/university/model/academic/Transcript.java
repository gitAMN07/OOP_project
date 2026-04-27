package com.university.model.academic;

import com.university.model.user.Student;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Transcript implements Serializable {
    private Student student;
    private List<Mark> records;

    public Transcript(Student student) {
        this.student = student;
        this.records = new ArrayList<>();
    }

    public void addMark(Mark mark) { records.add(mark); }

    public double getGPA() {
        if (records.isEmpty()) return 0.0;
        double avg = records.stream().mapToDouble(Mark::getTotal).average().orElse(0);
        return Math.round((avg / 25.0) * 100.0) / 100.0;
    }

    public Optional<Mark> getMarkFor(Course course) {
        return records.stream().filter(m -> m.getCourse().equals(course)).findFirst();
    }

    public List<Mark> getAllMarks() { return records; }

    public String formatTranscript() {
        StringBuilder sb = new StringBuilder();
        sb.append("OFFICIAL TRANSCRIPT\n");
        sb.append(String.format("Student : %-27s\n", student.getFullName()));
        sb.append(String.format("Year    : %-27d\n", student.getYearOfStudy()));
        sb.append(String.format("GPA     : %-27.2f\n", getGPA()));
        if (records.isEmpty()) {
            sb.append("No marks recorded yet.\n");
        } else {
            for (Mark m : records) {
                sb.append(String.format("%-20s %5.1f  %-3s\n",
                        m.getCourse().getCourseName(),
                        m.getTotal(), m.getLetterGrade()));
            }
        }
        return sb.toString();
    }

    public String generateReport() { return formatTranscript(); }
}