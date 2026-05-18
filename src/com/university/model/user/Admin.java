package com.university.model.user;

import java.util.List;

import com.university.enums.Role;
import com.university.pattern.Database;

public class Admin extends Employee {
    public Admin(String login, String password, String firstName, String lastName) {
        super(login, password, Role.ADMIN, firstName, lastName, "Administration");
    }

    public void addUser(User user) {
        Database.getInstance().saveUser(user);
        log("Added user: " + user.getLogin());
        System.out.println("[ADMIN] User added: " + user.getLogin());
    }

    public void removeUser(String userId) {
        Database.getInstance().removeUser(userId);
        log("Removed user id: " + userId);
        System.out.println("[ADMIN] User removed: " + userId);
    }

    public void updateUserLogin(String userId, String newLogin) {
        Database.getInstance().findUserById(userId).ifPresent(u -> {
            u.setLogin(newLogin);
            log("Updated login for user id=" + userId + " → " + newLogin);
        });
    }

    public void resetPassword(String userId, String newPassword) {
        Database.getInstance().findUserById(userId).ifPresent(u -> {
            u.setPassword(newPassword);
            log("Reset password for user id=" + userId);
            System.out.println("[ADMIN] Password reset for: " + userId);
        });
    }

    public void banUser(String userId) {
        Database.getInstance().findUserById(userId).ifPresent(u -> {
            u.setActive(false);
            log("Banned user: " + u.getLogin());
            System.out.println("[ADMIN] User banned: " + u.getLogin());
        });
    }

    public void viewLogs() {
        List<String> logs = Database.getInstance().getActionLog();
        System.out.println("=== SYSTEM LOG (" + logs.size() + " entries) ===");
        logs.forEach(System.out::println);
    }

    private void log(String action) {
        Database.getInstance().addLog("[ADMIN:" + getLogin() + "] " + action);
    }

    @Override
    public String getInfo() {
        return "Admin: " + getFullName() + " | login: " + getLogin();
    }
}