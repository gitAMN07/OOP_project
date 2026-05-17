package src.com.university.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String login) {
        super("User not found: '" + login + "'.");
    }
}