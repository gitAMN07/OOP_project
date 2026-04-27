package com.university.pattern;

import com.university.model.academic.Course;
import com.university.model.academic.CourseRegistration;
import com.university.model.user.Student;
import com.university.model.user.User;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Database {
    private static Database instance;

    private Map<String, User> users = new LinkedHashMap<>();
    private Map<String, Course> courses = new LinkedHashMap<>();
    private List<CourseRegistration> registrations = new ArrayList<>();
    private List<String> actionLog = new ArrayList<>();

    private static final String SAVE_PATH = "university_data.ser";

    private Database() {}

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    public void saveUser(User user) { users.put(user.getUserId(), user); }
    public void removeUser(String userId) { users.remove(userId); }
    public Optional<User> findUserById(String id) { return Optional.ofNullable(users.get(id)); }
    public Optional<User> findUserByLogin(String login) {
        return users.values().stream().filter(u -> u.getLogin().equals(login)).findFirst();
    }
    public List<User> getAllUsers() { return new ArrayList<>(users.values()); }
    public List<Student> getAllStudents() {
        return users.values().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    public void saveCourse(Course course) { courses.put(course.getCourseId(), course); }
    public Optional<Course> findCourseById(String id) { return Optional.ofNullable(courses.get(id)); }
    public List<Course> getAllCourses() { return new ArrayList<>(courses.values()); }

    public void addLog(String entry) {
        String stamped = "[" + java.time.LocalDateTime.now() + "] " + entry;
        actionLog.add(stamped);
    }
    public List<String> getActionLog() { return actionLog; }

    @SuppressWarnings("unchecked")
    public void load() {
        File f = new File(SAVE_PATH);
        if (!f.exists()) { System.out.println("[DB] No saved data found, starting fresh."); return; }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            users = (Map<String, User>) ois.readObject();
            courses = (Map<String, Course>) ois.readObject();
            System.out.println("[DB] Data loaded: " + users.size() + " users, " + courses.size() + " courses.");
        } catch (Exception e) {
            System.out.println("[DB] Load failed: " + e.getMessage());
        }
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
            oos.writeObject(users);
            oos.writeObject(courses);
            System.out.println("[DB] Data saved successfully.");
        } catch (IOException e) {
            System.out.println("[DB] Save failed: " + e.getMessage());
        }
    }
}