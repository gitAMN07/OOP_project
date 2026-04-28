package com.university.comparator;

import com.university.model.research.ResearchPaper;
import java.util.Comparator;

public class PaperByDateComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return b.getPublicationDate().compareTo(a.getPublicationDate());
    }
}
