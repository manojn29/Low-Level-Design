from AutoCompleteEngine.Python.autoCompleteEngine import AutoCompleteEngine
from AutoCompleteEngine.Python.ranking import FrequencyStrategy, RelevanceStrategy


if __name__ == "__main__":
    # 1. Init Engine with Frequency Strategy
    engine = AutoCompleteEngine(FrequencyStrategy())

    # 2. Load some initial Data
    engine.initialize_java_context()
    engine.initialize_python_context()

    # --- Scenario 1: Java Context + Relevance Strategy ---
    engine.set_language_context("java")
    engine.set_ranking_strategy(RelevanceStrategy())
    
    print("Query 'pr' in JAVA (Relevance Strategy):")
    # Should prefer Keywords (private) over Methods (printf)
    results = engine.suggest("pr")
    for r in results: print(r)

    # --- Scenario 2: Switch to Python + Frequency Strategy ---
    engine.set_language_context("python")
    engine.set_ranking_strategy(FrequencyStrategy())

    # Simulate user using "print" a lot
    engine.record_usage("print")
    engine.record_usage("print")
    engine.record_usage("print")
    
    # Simulate user using "price" once
    engine.record_usage("price")

    print("\nQuery 'pr' in PYTHON (Frequency Strategy):")
    # Should show 'print' first because usage is higher
    results = engine.suggest("pr")
    for r in results: print(r)