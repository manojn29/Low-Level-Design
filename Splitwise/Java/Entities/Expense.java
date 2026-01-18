import java.time.LocalDateTime;
import java.util.*;
import Splitwise.Java.Strategy.SplitStrategy;

class Expense {

    private final String id;
    private final String description;
    private final User paidBy;
    private final Double amount;
    private final List<Split> splits;
    private final LocalDateTime timestamp;

    private Expense(ExpenseBuilder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.paidBy = builder.paidBy;
        this.amount = builder.amount;
        this.timestamp = LocalDateTime.now();

        this.splits = builder.splitStrategy.calculateSplits(builder.amount, builder.paidBy, builder.participants, builder.splits);
    }

    public static class ExpenseBuilder {
        private String id;
        private String description;
        private SplitStrategy splitStrategy;
        private double amount;
        private User paidBy;
        private List<User> participants;
        private List<Double> splits;

        public ExpenseBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public ExpenseBuilder setDecription(String description) {
            this.description = description;
            return this;
        }

        public ExpenseBuilder setSplitStrategy(SplitStrategy splitStrategy) {
            this.splitStrategy = splitStrategy;
            return this;
        }

        public ExpenseBuilder setAmount(Double amount) {
            this.amount = amount;
            return this;
        }

        public ExpenseBuilder setPaidBy(User paidBy) {
            this.paidBy = paidBy;
            return this;
        }

        public ExpenseBuilder setParticipants(List<User> users) {
            this.participants = users;
            return this;
        }

        public ExpenseBuilder setSplits(List<Double> splits) {
            this.splits = splits;
            return this;
        }

        public Expense build() {
            if(splitStrategy == null) {
                throw new IllegalStateException("Split strategy is required");
            }
            return new Expense(this);
        }
    }
}