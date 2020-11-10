package main.api.response;

import java.util.TreeMap;
import java.util.TreeSet;

public class CalendarResponse
{
    TreeSet<Integer> years = new TreeSet<>();
    TreeMap<String, Integer> posts = new TreeMap<>();

    public TreeMap<String, Integer> getPosts() {
        return posts;
    }

    public void setPosts(TreeMap<String, Integer> posts) {
        this.posts = posts;
    }

    public TreeSet<Integer> getYears() {
        return years;
    }

    public void setYears(TreeSet<Integer> years) {
        this.years = years;
    }

}
