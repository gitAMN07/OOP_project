package src.com.university.model.research;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {
    private final String paperId;
    private String title;
    private List<String> authors;
    private String journal;
    private String doi;
    private LocalDate publicationDate;
    private int citations;
    private int pages;
    private List<String> keywords;
    private String abstractText;
    private boolean isOpenAccess;

    public ResearchPaper(String title, List<String> authors, String journal,
                         String doi, LocalDate publicationDate, int pages, boolean isOpenAccess) {
        this.paperId = UUID.randomUUID().toString();
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.doi = doi;
        this.publicationDate = publicationDate;
        this.citations = 0;
        this.pages = pages;
        this.isOpenAccess = isOpenAccess;
    }

    public void addCitation()           { this.citations++; }
    public void setCitations(int c)     { this.citations = c; }
    public void setAbstract(String abs) { this.abstractText = abs; }
    public void setKeywords(List<String> kw) { this.keywords = kw; }

    public String getPaperId()          { return paperId; }
    public String getTitle()            { return title; }
    public List<String> getAuthors()    { return authors; }
    public String getJournal()          { return journal; }
    public String getDoi()              { return doi; }
    public LocalDate getPublicationDate() { return publicationDate; }
    public int getCitations()           { return citations; }
    public int getPages()               { return pages; }
    public boolean isOpenAccess()       { return isOpenAccess; }
    public String getAbstractText()     { return abstractText; }

    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, this.citations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        return Objects.equals(doi, ((ResearchPaper) o).doi);
    }

    @Override public int hashCode() { return Objects.hash(doi); }

    @Override
    public String toString() {
        return String.format("Paper[\"%s\" | %s | %s | citations=%d | pages=%d | OA=%s]",
                title, String.join(", ", authors), publicationDate, citations, pages, isOpenAccess);
    }
}
