from typing import Dict, List, Optional
from AutoCompleteEngine.Python.models import EntityType, WordEntry

class TrieNode:
    def __init__(self):
        self.children: Dict[str, 'TrieNode'] = {}
        self.is_end_of_word: bool = False
        self.word: Optional[str] = None
        self.usage_count: int = 0
        self.entity_type: EntityType = EntityType.DEFAULT
        self.language: str = "all" 

    def insert_word(self, word: str, entity_type: EntityType, language: str):
        node = self
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        
        # Mark leaf
        node.is_end_of_word = True
        node.word = word
        node.entity_type = entity_type
        node.language = language
        # Initialize usage count if new, otherwise keep existing
        if node.usage_count == 0:
            node.usage_count = 1

    def find_node(self, prefix: str) -> Optional['TrieNode']:
        """Traverse to the node representing the prefix."""
        node = self
        for char in prefix:
            if char not in node.children:
                return None
            node = node.children[char]
        return node

    def collect_words(self, node: 'TrieNode', current_language: str, results: List[WordEntry]):
        """DFS traversal to collect all valid words under this node."""
        if node.is_end_of_word:
            # Filter by language: match specific language or 'all'
            if node.language == current_language or node.language == "all":
                entry = WordEntry(
                    word=node.word,
                    entity_type=node.entity_type,
                    language=node.language,
                    usage_count=node.usage_count
                )
                results.append(entry)
        
        for child_node in node.children.values():
            self.collect_words(child_node, current_language, results)