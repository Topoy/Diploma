package main.api.unit;

public class CommentUnit
{
    private int id;
    private long timestamp;
    private String Text;
    private UserUnit user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public UserUnit getUser() {
        return user;
    }

    public void setUser(UserUnit user) {
        this.user = user;
    }
}
