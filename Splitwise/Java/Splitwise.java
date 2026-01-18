import Splitwise.Java.Entities.*;
import Splitwise.Java.Strategy.*;

class SplitWise {
    private static final Splitwise instance;
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Group> groups = new HashMap<>();

    private SplitWise() {}

    public static synchronized SplitWise getInstance() {
        if(instance != null) {
            return instance;
        }
        return new SplitWise();
    }

    public User addUser(String name, String email) {
        User user = new User(name, email);
        users.add(user);
        return user;
    }

    public Group addGroup(String name, List<User> users) {
        Group group = new Group(name, users);
        groups.add(group);
        return group;
    }

    public synchronized void addExpense(Expense.ExpenseBuilder builder) {
        Expense expense = builder.build();
        User paidBy = expense.getPaidBy();
        
        for(Split split : expense.getSplits()) {
            User user = split.getUser();
            if(user.getId().equals(paidBy.getId())) {
                continue;
            }
            paidBy.addBalance(user, split.getAmount());
            user.addBalance(paidBy, -split.getAmount());
        }
    }

    public synchronized void settleUp(String payer, String payee, Double amount) {
        User payerId = users.get(payer);
        User payeeId = users.get(payee);

        payerId.getBalanceSheet.updateBalance(payeeId, amount);
        payeeId.getBalanceSheet.updateBalance(payeeId, -amount);
    }

    public void showBalances(String name) {
        User user = users.get(name);
        user.getBalanceSheet().showBalance();
    }
}