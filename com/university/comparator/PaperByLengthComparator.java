package com.university.comparator;

import com.university.model.research.ResearchPaper;
import java.util.Comparator;

public class PaperByLengthComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.getPages(), a.getPages());
    }
}
