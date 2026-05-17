package src.com.university.model.academic;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import src.com.university.model.user.Manager;

public class News implements Serializable {
    private final String newsId;
    private String title;
    private String body;
    private LocalDateTime publishedAt;
    private Manager publishedBy;

    public News(String title, String body, Manager publishedBy) {
        this.newsId = UUID.randomUUID().toString();
        this.title = title;
        this.body = body;
        this.publishedAt = LocalDateTime.now();
        this.publishedBy = publishedBy;
    }

    public String getNewsId()         { return newsId; }
    public String getTitle()          { return title; }
    public String getBody()           { return body; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public Manager getPublishedBy()   { return publishedBy; }

    @Override
    public String toString() {
        return String.format("[NEWS] %s (by %s at %s)\n%s",
                title, publishedBy.getFullName(), publishedAt.toLocalDate(), body);
    }
}