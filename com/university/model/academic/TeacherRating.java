package com.university.model.academic;

import com.university.model.user.Teacher;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class TeacherRating implements Serializable {
    private final String ratingId;
    private Teacher teacher;
    private int score;
    private String comment;
    private LocalDate date;

    public TeacherRating(Teacher teacher, int score, String comment) {
        this.ratingId = UUID.randomUUID().toString();
        this.teacher = teacher;
        this.score = Math.max(1, Math.min(5, score));
        this.comment = comment;
        this.date = LocalDate.now();
    }

    public int getScore()     { return score; }
    public String getComment(){ return comment; }
    public Teacher getTeacher(){ return teacher; }

    @Override
    public String toString() {
        return String.format("Rating[%s → %s: %d/5 \"%s\"]",
                 teacher.getFullName(), score, comment);
    }
}