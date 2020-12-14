package main.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "post_comments")
public class PostComment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "parent_id")
    private Integer parentId;
    //@Column(name = "post_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;
    //@Column(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    private LocalDateTime time;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long convertTimeToTimeStamp()
    {
        ZonedDateTime zdt = ZonedDateTime.of(this.time, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli()/1000;
    }
}
