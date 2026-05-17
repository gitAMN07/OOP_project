package src.com.university.feature;

import java.util.*;
import java.util.stream.Collectors;

import src.com.university.model.research.ResearchPaper;
import src.com.university.model.research.Researcher;
import src.com.university.pattern.Database;

public class CitationGraph {
    private Map<ResearchPaper, List<ResearchPaper>> edges = new LinkedHashMap<>();

    public void addCitationLink(ResearchPaper cited, ResearchPaper citedBy) {
        edges.computeIfAbsent(cited, k -> new ArrayList<>()).add(citedBy);
        cited.addCitation();
        System.out.println("[CITATION] \"" + citedBy.getTitle()
                + "\" cites \"" + cited.getTitle() + "\"");
    }

    public ResearchPaper getMostInfluentialPaper() {
        return edges.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public List<ResearchPaper> getCitationNetwork(ResearchPaper paper) {
        return edges.getOrDefault(paper, Collections.emptyList());
    }

    public void printTopCitedResearchers() {
        List<Researcher> researchers = Database.getInstance().getAllResearchers();
        System.out.println("=== TOP CITED RESEARCHERS (University-wide) ===");
        researchers.stream()
                .sorted(Comparator.comparingInt(Researcher::getTotalCitations).reversed())
                .forEach(r -> System.out.printf("  h-index=%d | citations=%d | papers=%d%n",
                        r.getHIndex(), r.getTotalCitations(), r.getResearchPapers().size()));
    }

    public void printAllPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> all = Database.getInstance().getAllPapersSorted(comparator);
        System.out.println("=== ALL RESEARCH PAPERS (" + all.size() + ") ===");
        all.forEach(System.out::println);
    }
}