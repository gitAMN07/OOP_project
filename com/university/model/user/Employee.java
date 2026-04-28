package com.university.model.user;

import com.university.enums.Role;
import com.university.model.academic.Message;
import java.util.ArrayList;
import java.util.List;

public abstract class Employee extends User {
    private String firstName;
    private String lastName;
    private String email;
    private double salary;
    private String department;
    private List<Message> inbox;
    private List<Message> sentMessages;

    public Employee(String login, String password, Role role,
                    String firstName, String lastName, String department) {
        super(login, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = login + "@university.edu";
        this.department = department;
        this.inbox = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
    }

    public void sendMessage(Employee to, String subject, String body) {
        Message msg = new Message(this, to, subject, body);
        this.sentMessages.add(msg);
        to.receiveMessage(msg);
        System.out.println("[MESSAGE] " + getFullName() + " → " + to.getFullName() + ": " + subject);
    }

    public void receiveMessage(Message msg) { inbox.add(msg); }

    public void printInbox() {
        if (inbox.isEmpty()) { System.out.println("Inbox is empty."); return; }
        inbox.forEach(m -> System.out.println("  From: " + m.getFrom().getFullName() +
                " | " + m.getSubject() + (m.isRead() ? "" : " [NEW]")));
    }

    public String getFullName() { return firstName + " " + lastName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getDepartment() { return department; }
    public List<Message> getInbox() { return inbox; }
    public List<Message> getSentMessages() { return sentMessages; }

    @Override
    public String toString() {
        return super.toString() + " | " + getFullName() + " | dept: " + department;
    }
}