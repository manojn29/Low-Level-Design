class Split {
    private final User user;
    private final float amount;

    public Split(User user, float amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return this.user;
    }

    public float getAmount() {
        return this.amount;
    }
}