package com.university.pattern;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.university.model.academic.Course;
import com.university.model.academic.CourseRegistration;
import com.university.model.academic.News;
import com.university.model.research.ResearchPaper;
import com.university.model.research.Researcher;
import com.university.model.user.Student;
import com.university.model.user.User;

public class Database {
    private static Database instance;

    private Map<String, User> users = new LinkedHashMap<>();
    private Map<String, Course> courses = new LinkedHashMap<>();
    private List<CourseRegistration> registrations = new ArrayList<>();
    private List<ResearchPaper> papers = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();
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
    public List<Researcher> getAllResearchers() {
        return users.values().stream()
                .filter(u -> u instanceof Researcher)
                .map(u -> (Researcher) u)
                .collect(Collectors.toList());
    }

    public void saveCourse(Course course) { courses.put(course.getCourseId(), course); }
    public Optional<Course> findCourseById(String id) { return Optional.ofNullable(courses.get(id)); }
    public List<Course> getAllCourses() { return new ArrayList<>(courses.values()); }

    public void saveRegistration(CourseRegistration reg) { registrations.add(reg); }
    public List<CourseRegistration> getAllRegistrations() { return registrations; }
    public List<CourseRegistration> getPendingRegistrations() {
        return registrations.stream()
                .filter(r -> r.getStatus() == com.university.enums.RegistrationStatus.PENDING)
                .collect(Collectors.toList());
    }

    public void savePaper(ResearchPaper paper) { papers.add(paper); }
    public List<ResearchPaper> getAllPapers() { return papers; }
    public List<ResearchPaper> getAllPapersSorted(Comparator<ResearchPaper> c) {
        return papers.stream().sorted(c).collect(Collectors.toList());
    }

    public void saveNews(News news) { newsList.add(news); }
    public List<News> getAllNews() { return newsList; }

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
            registrations = (List<CourseRegistration>) ois.readObject();
            System.out.println("[DB] Data loaded: " + users.size() + " users, " + courses.size() + " courses.");
        } catch (Exception e) {
            System.out.println("[DB] Load failed: " + e.getMessage());
        }
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
            oos.writeObject(users);
            oos.writeObject(courses);
            oos.writeObject(registrations);
            System.out.println("[DB] Data saved successfully.");
        } catch (IOException e) {
            System.out.println("[DB] Save failed: " + e.getMessage());
        }
    }
}