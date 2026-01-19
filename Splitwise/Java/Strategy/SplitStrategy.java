package Splitwise.Java.Strategy;
import Splitwise.Java.Entities.*;

public interface SplitStrategy {
    public List<Split> calculateSplits(Double amount, User paidBy, List<User> participants, List<Split> splitValues);
}