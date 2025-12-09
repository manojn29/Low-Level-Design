from typing import List
from models import WordEntry
import abc

class RankingStrategy(abc.ABC):
    """
    Abstract Base Class for Ranking Strategies.
    """
    @abc.abstractmethod
    def rank_entries(self, entries: List[WordEntry], prefix: str) -> List[WordEntry]:
        pass

class FrequencyStrategy(RankingStrategy):
    """
    Sorts based on Usage Count (Descending).
    """
    def rank_entries(self, entries: List[WordEntry], prefix: str) -> List[WordEntry]:
        # Sort by usage_count desc
        return sorted(entries, key=lambda x: x.usage_count, reverse=True)

class RelevanceStrategy(RankingStrategy):
    """
    Sorts based on EntityType Score (Descending), then length of word (shorter is better).
    """
    def rank_entries(self, entries: List[WordEntry], prefix: str) -> List[WordEntry]:
        # Sort by Entity Score desc, then by Word Length asc
        return sorted(entries, key=lambda x: (-x.entity_type.score, len(x.word)))