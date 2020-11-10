package main.service;

import main.api.response.CalendarResponse;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalendarService
{
    @Autowired
    PostRepository postRepository;

    public CalendarResponse getCalendar(int year)
    {
        CalendarResponse calendarResponse = new CalendarResponse();
        ArrayList<Post> posts = getPosts();
        TreeSet<Integer> years = new TreeSet<>();
        TreeMap<String, Integer> postMap = new TreeMap<>();
        ArrayList<String> dates = new ArrayList<>();

        for (Post post : posts)
        {
            years.add(post.getTime().getYear());
            if (post.getTime().getYear() == year)
            {
                String stringYear = String.valueOf(year);
                int month = post.getTime().getMonthValue();
                String stringMonth = (month < 10) ? "0" + month : String.valueOf(month);
                int day = post.getTime().getDayOfMonth();
                String stringDay = (day < 10) ? "0" + day : String.valueOf(day);
                String date = stringYear + "-" + stringMonth + "-" + stringDay;
                dates.add(date);
            }
        }
        for (String date : dates)
        {
            if (postMap.containsKey(date))
            {
                postMap.put(date, postMap.get(date) + 1);
            }
            else
            {
                postMap.put(date, 1);
            }
        }
        calendarResponse.setYears(years);
        calendarResponse.setPosts(postMap);
        return calendarResponse;
    }

    private ArrayList<Post> getPosts()
    {
        ArrayList<Post> posts = new ArrayList<>();
        Iterable<Post> postIterator = postRepository.findAll();
        for (Post post : postIterator)
        {
            posts.add(post);
        }
        return posts;
    }
}
