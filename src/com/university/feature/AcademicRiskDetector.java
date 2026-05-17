package src.com.university.feature;

import java.util.List;
import java.util.stream.Collectors;

import src.com.university.enums.RiskLevel;
import src.com.university.model.user.Student;
import src.com.university.pattern.Database;

public class AcademicRiskDetector {

    public RiskLevel detectRisk(Student student) {
        return student.getRiskLevel();
    }

    public List<Student> getAtRiskStudents(RiskLevel level) {
        return Database.getInstance().getAllStudents().stream()
                .filter(s -> s.getRiskLevel().ordinal() >= level.ordinal())
                .collect(Collectors.toList());
    }

    public String generateWarning(Student student) {
        return switch (student.getRiskLevel()) {
            case CRITICAL -> "⛔ CRITICAL: Student at risk of academic dismissal — " + student.getFullName();
            case HIGH     -> "🔴 WARNING: Student at risk of academic dismissal — " + student.getFullName();
            case MEDIUM   -> "🟡 WARNING: Student showing declining performance — " + student.getFullName();
            case LOW      -> "🟢 OK: " + student.getFullName() + " is performing well.";
        };
    }

    public void runFullAnalysis() {
        List<Student> all = Database.getInstance().getAllStudents();
        System.out.println("=== ACADEMIC RISK ANALYSIS (" + all.size() + " students) ===");
        for (Student s : all) {
            System.out.println(generateWarning(s));
            System.out.printf("   GPA: %.2f | Fails: %d | Scholarship: %s%n",
                    s.getGpa(), s.getFailCount(), s.getScholarshipStatus());
        }

        long critical = all.stream().filter(s -> s.getRiskLevel() == RiskLevel.CRITICAL).count();
        long high     = all.stream().filter(s -> s.getRiskLevel() == RiskLevel.HIGH).count();
        long medium   = all.stream().filter(s -> s.getRiskLevel() == RiskLevel.MEDIUM).count();
        System.out.println("--- Summary ---");
        System.out.println("CRITICAL: " + critical + " | HIGH: " + high + " | MEDIUM: " + medium);
    }
}