class Transaction {
    private final User fromUser;
    private final User toUser;
    private final Double amount;

    public Transaction(User fromUser, User toUser, Double amount) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }
}