import java.util.*;

class User {
    private String name;
    private String id;
    private String email;
    private BalanceSheet balanceSheet;

    public User(String name, String id, String email) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.balanceSheet = new BalanceSheet(this);
    }

    public String getName() { return this.name; }

    public String getId() { return this.id; }

    public BalanceSheet getBalanceSheet() { return this.balanceSheet; }
}