class EqualSplitStrategy implements SplitStrategy {

    public List<Split> calculateSplits(Double amount, User paidBy, List<User> participants, List<Split> splitValues) {
        if (participants.size() == 0) {
            throw new IllegalStateException("Participants cannot be empty");
        }
        float individualAmount = amount / participants.size();
        List<Split> splits = new ArrayList<>();
        for (User user: participants) {
            splits.add(new Split(user, individualAmount));
        }

        return splits;
    }
}