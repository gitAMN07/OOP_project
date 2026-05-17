package src.com.university.model.academic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import src.com.university.model.user.Student;
import src.com.university.model.user.Teacher;

public class TeacherRating implements Serializable {
    private final String ratingId;
    private Teacher teacher;
    private Student student;
    private int score;
    private String comment;
    private LocalDate date;

    public TeacherRating(Teacher teacher, Student student, int score, String comment) {
        this.ratingId = UUID.randomUUID().toString();
        this.teacher = teacher;
        this.student = student;
        this.score = Math.max(1, Math.min(5, score));
        this.comment = comment;
        this.date = LocalDate.now();
    }

    public int getScore()     { return score; }
    public String getComment(){ return comment; }
    public Teacher getTeacher(){ return teacher; }
    public Student getStudent(){ return student; }

    @Override
    public String toString() {
        return String.format("Rating[%s → %s: %d/5 \"%s\"]",
                student.getFullName(), teacher.getFullName(), score, comment);
    }
}