from enum import Enum
from dataclasses import dataclass

class EntityType(Enum):
    """
    Enum representing the type of the word (Keyword, Method, etc.)
    The value represents the 'Relevance Score' (higher is more relevant).
    """
    KEYWORD = 1.0
    CLASS = 0.85
    METHOD = 0.9
    VARIABLE = 0.8
    DEFAULT = 0.5

    @property
    def score(self):
        return self.value

@dataclass
class WordEntry:
    """
    DTO to transport word details from Trie to the Ranking Strategy.
    """
    word: str
    entity_type: EntityType
    language: str
    usage_count: int

    def __repr__(self):
        return f"'{self.word}' ({self.entity_type.name}, Usage: {self.usage_count})"