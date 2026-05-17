package src.com.university.exception;

public class NonResearcherJoinException extends Exception {
    public NonResearcherJoinException(String userName) {
        super("User '" + userName + "' is not a Researcher and cannot join a research project.");
    }
}