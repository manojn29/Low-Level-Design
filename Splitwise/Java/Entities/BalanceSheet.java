import java.util.*;

class BalanceSheet {
    private final User owner;
    private final Map<User, Double> balances = new HashMap<>();

    public BalanceSheet(User owner) {
        this.owner = owner;
    }

    public User getOwner() { return this.owner; }
    public Map<User, Double> getBalances() { return this.balances; }

    public synchronized void updateBalance(User user, double amount) {
        if (user.getName().equals(this.owner.getName())) {
            return; // No self balance update
        }
        this.balances.put(user, this.balances.getOrDefault(user, 0.0) + amount);
    }

    public void showBalance() {
        if (balances.isEmpty()) {
            System.out.println("No balances");
            return;
        }

        for (Map.Entry<User, Double> entry : balances.entrySet()) {
            User user = entry.getKey();
            double amount = entry.getValue();
            if (amount > 0) {
                System.out.println(user.getName() + " owes " + this.owner.getName() + ": " + amount);
            } else if (amount < 0) {
                System.out.println(this.owner.getName() + " owes " + user.getName() + ": " + (-amount));
            }
        }
    }

}