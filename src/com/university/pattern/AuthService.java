package com.university.pattern;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.university.exception.UserNotFoundException;
import com.university.model.user.User;

public class AuthService {
    private static UserSession currentSession = null;

    public static UserSession login(String login, String password) throws UserNotFoundException {
        Optional<User> found = Database.getInstance().findUserByLogin(login);
        if (found.isEmpty()) throw new UserNotFoundException(login);

        User user = found.get();
        if (!user.isActive()) {
            System.out.println("[AUTH] Account is banned: " + login);
            return null;
        }
        if (!user.checkPassword(password)) {
            System.out.println("[AUTH] Wrong password for: " + login);
            Database.getInstance().addLog("Failed login attempt: " + login);
            return null;
        }

        currentSession = new UserSession(user);
        Database.getInstance().addLog("LOGIN: " + login + " [" + user.getRole() + "]");
        System.out.println("[AUTH] Logged in: " + login + " as " + user.getRole());
        return currentSession;
    }

    public static void logout() {
        if (currentSession != null) {
            Database.getInstance().addLog("LOGOUT: " + currentSession.getUser().getLogin());
            System.out.println("[AUTH] Logged out: " + currentSession.getUser().getLogin());
            currentSession = null;
        }
    }

    public static User getCurrentUser() {
        return currentSession != null ? currentSession.getUser() : null;
    }

    public static boolean isAuthenticated() { return currentSession != null; }
    public static UserSession getSession()  { return currentSession; }
}