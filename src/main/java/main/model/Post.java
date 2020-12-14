package main.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "posts")
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "is_active")
    private byte isActive;
    @Column(name = "moderation_status")
    @Enumerated(EnumType.STRING)
    private StatusType moderationStatus;
    @Column(name = "moderator_id")
    private int moderatorId;
    //@Column(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    private LocalDateTime time;
    private String title;
    private String text;
    @Column(name = "view_count")
    private int viewCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public StatusType getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(StatusType moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public long convertTimeToTimeStamp()
    {
        ZonedDateTime zdt = ZonedDateTime.of(this.time, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli()/1000;
    }
}
