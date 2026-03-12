public abstract class Content {
    public String id;
    public String body;
    public User author;

    public Content(String Id, String body, User author) {
        this.id = Id;
        this.body = body;
        this.author = author;
    }
}