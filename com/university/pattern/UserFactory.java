package com.university.pattern;

import com.university.enums.ManagerType;
import com.university.enums.Role;
import com.university.model.user.*;

public class UserFactory {
    public static User create(Role role, String login, String password,
                              String firstName, String lastName, String extra) {
        return switch (role) {
            case STUDENT  -> new Student(login, password, firstName, lastName,
                                         Integer.parseInt(extra));
            case TEACHER  -> new Professor(login, password, firstName, lastName,
                                            extra, 0);
            case MANAGER  -> new Manager(login, password, firstName, lastName,
                                          ManagerType.DEPARTMENT);
            case ADMIN    -> new Admin(login, password, firstName, lastName);
            default       -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}