import java.util.*;
import Splitwise.Java.Strategy.*;

import Splitwise.Java.Strategy.SplitStrategy;

class Group {
    private String name;
    private String id;
    private List<User> users;

    public Group(String name, List<User> users) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.users = users;
    }

    public String getName() { return this.name; }
    public String getId() { return this.id; }
    public List<User> getUsers() {
        return new ArrayList<>(this.users);
    }
}