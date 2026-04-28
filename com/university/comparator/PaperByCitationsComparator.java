package com.university.comparator;

import com.university.model.research.ResearchPaper;
import java.util.Comparator;

public class PaperByCitationsComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.getCitations(), a.getCitations());
    }
}
