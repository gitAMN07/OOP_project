package com.university.pattern;

import com.university.model.user.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private User user;
    private LocalDateTime loginTime;
    private List<String> actionLog;

    public UserSession(User user) {
        this.user = user;
        this.loginTime = LocalDateTime.now();
        this.actionLog = new ArrayList<>();
    }

    public void logAction(String action) {
        String entry = "[" + LocalDateTime.now() + "] " + action;
        actionLog.add(entry);
        Database.getInstance().addLog(user.getLogin() + " -> " + action);
    }

    public User getUser()              { return user; }
    public LocalDateTime getLoginTime(){ return loginTime; }
    public List<String> getLog()       { return actionLog; }

    public long getDurationSeconds() {
        return java.time.Duration.between(loginTime, LocalDateTime.now()).getSeconds();
    }
}
