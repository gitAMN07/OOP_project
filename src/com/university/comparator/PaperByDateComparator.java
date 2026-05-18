package com.university.comparator;

import java.util.Comparator;

import com.university.model.research.ResearchPaper;

public class PaperByDateComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return b.getPublicationDate().compareTo(a.getPublicationDate());
    }
}
