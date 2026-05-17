package com.university.model.academic;

import com.university.model.user.Employee;
import com.university.model.user.Manager;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Message implements Serializable {
    private final String messageId;
    private Employee from;
    private Employee to;
    private String subject;
    private String body;
    private LocalDateTime sentAt;
    private boolean isRead;

    public Message(Employee from, Employee to, String subject, String body) {
        this.messageId = UUID.randomUUID().toString();
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
    }

    public void markAsRead() { this.isRead = true; }

    public Employee getFrom()   { return from; }
    public Employee getTo()     { return to; }
    public String getSubject()  { return subject; }
    public String getBody()     { return body; }
    public boolean isRead()     { return isRead; }
    public LocalDateTime getSentAt() { return sentAt; }

    @Override
    public String toString() {
        return String.format("Message[from=%s to=%s | \"%s\" | read=%s]",
                from.getFullName(), to.getFullName(), subject, isRead);
    }
}