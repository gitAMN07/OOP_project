package com.university.model.user;

import com.university.enums.Role;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class User implements Serializable {
    private final String userId;
    private String login;
    private String passwordHash;
    private Role role;
    private boolean isActive;

    public User(String login, String password, Role role) {
        this.userId = UUID.randomUUID().toString();
        this.login = login;
        this.passwordHash = hashPassword(password);
        this.role = role;
        this.isActive = true;
    }

    private String hashPassword(String raw) {
        return Integer.toHexString(raw.hashCode());
    }

    public boolean checkPassword(String raw) {
        return this.passwordHash.equals(hashPassword(raw));
    }

    public abstract String getInfo();

    public String getUserId() { return userId; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public Role getRole() { return role; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public void setPassword(String newPassword) { this.passwordHash = hashPassword(newPassword); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() { return Objects.hash(userId); }

    @Override
    public String toString() {
        return "[" + role + "] " + login + " (id=" + userId.substring(0, 8) + ")";
    }
}