package com.university.model.academic;

import com.university.enums.LessonType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Lesson implements Serializable {
    private final String lessonId;
    private String topic;
    private LessonType lessonType;
    private LocalDate date;
    private int durationMinutes;

    public Lesson(String topic, LessonType lessonType, LocalDate date, int durationMinutes) {
        this.lessonId = UUID.randomUUID().toString();
        this.topic = topic;
        this.lessonType = lessonType;
        this.date = date;
        this.durationMinutes = durationMinutes;
    }

    public String getLessonId()         { return lessonId; }
    public String getTopic()            { return topic; }
    public LessonType getLessonType()   { return lessonType; }
    public LocalDate getDate()          { return date; }
    public int getDurationMinutes()     { return durationMinutes; }

    @Override
    public String toString() {
        return String.format("[%s] %s on %s (%d min)", lessonType, topic, date, durationMinutes);
    }
}