package src.classes;

public class Notifications {

    private final int type;
    private final String message;

    public Notifications(int type, String data) {
        this.type = type;
        this.message = data;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
