package src.com.university.exception;

public class CourseFullException extends Exception {
    public CourseFullException(String courseName) {
        super("Course '" + courseName + "' is full. No more students can be enrolled.");
    }
}