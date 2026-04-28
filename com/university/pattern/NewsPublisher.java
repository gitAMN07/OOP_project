package com.university.pattern;

import com.university.model.academic.News;
import com.university.model.user.Student;
import com.university.pattern.Database;
import java.util.ArrayList;
import java.util.List;

public class NewsPublisher {
    private static NewsPublisher instance;
    private List<Student> subscribers = new ArrayList<>();

    private NewsPublisher() {}

    public static NewsPublisher getInstance() {
        if (instance == null) instance = new NewsPublisher();
        return instance;
    }

    public void subscribe(Student student) {
        if (!subscribers.contains(student)) subscribers.add(student);
    }

    public void unsubscribe(Student student) { subscribers.remove(student); }

    public void subscribeAll() {
        Database.getInstance().getAllStudents().forEach(this::subscribe);
    }

    public void publish(News news) {
        String msg = "📢 " + news.getTitle() + ": " + news.getBody();
        subscribers.forEach(s -> s.notify(msg));
        System.out.println("[NEWS PUBLISHER] Notified " + subscribers.size() + " students.");
    }
}
