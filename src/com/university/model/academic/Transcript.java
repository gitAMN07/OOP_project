package src.com.university.model.academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import src.com.university.model.user.Student;

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
    sb.append("╔══════════════════════════════════════════╗\n");
    sb.append("║           OFFICIAL TRANSCRIPT            ║\n");
    sb.append("╠══════════════════════════════════════════╣\n");
    sb.append(String.format("║  Student : %-30s║\n", student.getFullName()));
    sb.append(String.format("║  Year    : %-30d║\n", student.getYearOfStudy()));
    sb.append(String.format("║  GPA     : %-30.2f║\n", getGPA()));
    sb.append(String.format("║  Risk    : %-30s║\n", student.getRiskLevel()));
    sb.append(String.format("║  Scholar : %-30s║\n", student.getScholarshipStatus()));
    sb.append("╠══════════╦═════════╦═══════╦══════════╣\n");
    sb.append("║ Course   ║  Total  ║ Grade ║  Status  ║\n");
    sb.append("╠══════════╬═════════╬═══════╬══════════╣\n");
    if (records.isEmpty()) {
        sb.append("║              No marks recorded yet.          ║\n");
    } else {
        for (Mark m : records) {
            sb.append(String.format("║ %-8s ║  %5.1f  ║  %-4s ║  %-7s ║\n",
                m.getCourse().getCourseName().length() > 8
                    ? m.getCourse().getCourseName().substring(0, 8)
                    : m.getCourse().getCourseName(),
                m.getTotal(),
                m.getLetterGrade(),
                m.isFail() ? "FAIL" : "PASS"));
        }
    }
    sb.append("╚══════════╩═════════╩═══════╩══════════╝\n");
    return sb.toString();
}

    public String generateReport() { return formatTranscript(); }
}