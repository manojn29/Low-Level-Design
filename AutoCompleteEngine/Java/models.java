import java.util.*;

enum EntityType {
    CLASS(1.0),
    METHOD(0.85),
    VARIABLE(0.7),
    KEYWORD(0.75),
    DEFAULT(0.5);

    private final double score;

    EntityType(double score) {
        this.score = score;
    }

    public double getScore() {
        return this.score;
    }
}