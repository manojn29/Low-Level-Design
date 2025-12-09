from typing import List
from AutoCompleteEngine.Python.models import EntityType, WordEntry
from AutoCompleteEngine.Python.ranking import RankingStrategy
from AutoCompleteEngine.Python.trieNode import TrieNode


class AutoCompleteEngine:
    def __init__(self, strategy: RankingStrategy):
        self.root = TrieNode()
        self.ranking_strategy = strategy
        self.current_language = "text" # Default context

    def set_ranking_strategy(self, strategy: RankingStrategy):
        self.ranking_strategy = strategy

    def set_language_context(self, language: str):
        print(f"\n[System] Switching context to: {language}")
        self.current_language = language

    def add_word(self, word: str, entity_type: EntityType = EntityType.DEFAULT):
        """Adds a word to the Trie under the current language context."""
        self.root.insert_word(word, entity_type, self.current_language)

    def record_usage(self, word: str):
        """
        Simulates a user selecting a word. 
        Finds the word and increments its usage count for Frequency Strategy.
        """
        node = self.root.find_node(word)
        if node and node.is_end_of_word:
            node.usage_count += 1
            print(f"[Metrics] Incremented usage for '{word}' to {node.usage_count}")
        else:
            # If word doesn't exist, we might auto-add it
            self.add_word(word)

    def suggest(self, prefix: str) -> List[WordEntry]:
        """
        1. Find node for prefix.
        2. Collect all valid descendants.
        3. Rank using strategy.
        """
        prefix_node = self.root.find_node(prefix)
        if not prefix_node:
            return []

        candidates: List[WordEntry] = []
        # Use root's helper to collect words starting from the prefix node
        self.root.collect_words(prefix_node, self.current_language, candidates)
        
        # Apply Strategy
        return self.ranking_strategy.rank_entries(candidates, prefix)

    # --- Setup Helpers ---
    def initialize_java_context(self):
        # Helper to pre-fill some Java data
        self.root.insert_word("private", EntityType.KEYWORD, "java")
        self.root.insert_word("protected", EntityType.KEYWORD, "java")
        self.root.insert_word("println", EntityType.METHOD, "java")
        self.root.insert_word("printf", EntityType.METHOD, "java")
    
    def initialize_python_context(self):
        # Helper to pre-fill some Python data
        self.root.insert_word("print", EntityType.METHOD, "python")
        self.root.insert_word("priority_queue", EntityType.CLASS, "python")
        self.root.insert_word("price", EntityType.VARIABLE, "python")