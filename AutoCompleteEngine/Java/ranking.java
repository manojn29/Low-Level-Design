// Strategy interface for ranking algorithms
interface RankingStrategy {
    List<String> rank(List<String> suggestions, String query);
}

// Concrete strategy: Rank by relevance (simple substring match)
