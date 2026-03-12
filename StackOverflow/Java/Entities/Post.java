import java.util.concurrent.atomic.AtomicInteger;

public abstract class Post extends Content {
    AtomicInteger voteCount = new AtomicInteger(0);
    Map<String, VoteType> voters = new ConcurrentHashMap<>();
    Map<String, Comment> comments = new ConcurrentHashMap<>();
    List<Observer> observers = new CopyOnWriteArrayList<>();

    public Post(String id, String body, User user) {
        super(id, body, user);
    }

    public void addOberver(Observer observer) {
        this.observers.add(observer);
    }

    protected void notifyObservers(Event event) {
        observers.foreach(o -> o.onPostEvent());
    }

    public void vote(User author, VoteType voteType) {
        String userId = author.getUserId();
        if (voters.contains(userId)) {
            return; // user has already voted
        }
    }
} 