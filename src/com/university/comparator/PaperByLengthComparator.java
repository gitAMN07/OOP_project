package src.com.university.comparator;

import java.util.Comparator;

import src.com.university.model.research.ResearchPaper;

public class PaperByLengthComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.getPages(), a.getPages());
    }
}
