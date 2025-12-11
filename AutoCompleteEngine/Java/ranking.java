// Strategy interface for ranking algorithms

import java.util.Comparator;

interface RankingStrategy {
    List<String> rank(List<String> suggestions, String prefix);
}

// Concrete strategy: Rank by relevance (simple substring match)
class RelevanceStrategy implements RankingStrategy {
    @Override
    public List<String> rank(List<String> suggestions, String prefix) {
        suggestions.sort((a, b) -> {
            int compareScore = Double.compare(b.EntityType.getScore(), a.EntityType.getScore());
            if (compareScore != 0) {
                return compareScore;
            }
            return Integer.compare(a.word.length(), b.word.length());
        });
        return suggestions;
    }
}

class FrequencyStrategy implements RankingStrategy {
    @Override
    public List<String> rank(List<String> suggestions, String prefix) {
        suggestions.sort((a, b) -> {
            return Integer.compare(b.usageCount(), a.usageCount());
        });
        return suggestions;
    }
}