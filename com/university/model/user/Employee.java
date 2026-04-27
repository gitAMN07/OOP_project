package com.university.model.user;

import com.university.enums.Role;
import java.util.ArrayList;
import java.util.List;

public abstract class Employee extends User {
    private String firstName;
    private String lastName;
    private String email;
    private double salary;
    private String department;

    public Employee(String login, String password, Role role,
                    String firstName, String lastName, String department) {
        super(login, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = login + "@university.edu";
        this.department = department;
    }

    public String getFullName() { return firstName + " " + lastName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getDepartment() { return department; }
   
    @Override
    public String toString() {
        return super.toString() + " | " + getFullName() + " | dept: " + department;
    }
}