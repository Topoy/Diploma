package main.api.response;


import java.util.List;

public class PostResponse
{
    private int count;
    private List<PostUnit> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostUnit> getPosts() {
        return posts;
    }

    public void setPosts(List<PostUnit> posts) {
        this.posts = posts;
    }
}
