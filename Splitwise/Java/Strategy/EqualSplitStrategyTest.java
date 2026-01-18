import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class EqualSplitStrategyTest {
    
    private EqualSplitStrategy strategy;
    private User user1;
    private User user2;
    private User user3;
    
    @BeforeEach
    void setUp() {
        strategy = new EqualSplitStrategy();
        user1 = new User("user1", "user1@example.com");
        user2 = new User("user2", "user2@example.com");
        user3 = new User("user3", "user3@example.com");
    }
    
    @Test
    void testCalculateSplitsWithMultipleParticipants() {
        List<User> participants = List.of(user1, user2, user3);
        List<Split> splits = strategy.calculateSplits(300.0, user1, participants, new ArrayList<>());
        
        assertEquals(3, splits.size());
        assertEquals(100.0f, splits.get(0).getAmount(), 0.01);
        assertEquals(100.0f, splits.get(1).getAmount(), 0.01);
        assertEquals(100.0f, splits.get(2).getAmount(), 0.01);
    }
    
    @Test
    void testCalculateSplitsWithSingleParticipant() {
        List<User> participants = List.of(user1);
        List<Split> splits = strategy.calculateSplits(100.0, user1, participants, new ArrayList<>());
        
        assertEquals(1, splits.size());
        assertEquals(100.0f, splits.get(0).getAmount(), 0.01);
    }
    
    @Test
    void testCalculateSplitsWithEmptyParticipants() {
        assertThrows(IllegalStateException.class, 
            () -> strategy.calculateSplits(100.0, user1, new ArrayList<>(), new ArrayList<>()));
    }
    
    @Test
    void testCalculateSplitsWithDecimalAmount() {
        List<User> participants = List.of(user1, user2);
        List<Split> splits = strategy.calculateSplits(100.5, user1, participants, new ArrayList<>());
        
        assertEquals(2, splits.size());
        assertEquals(50.25f, splits.get(0).getAmount(), 0.01);
    }
}