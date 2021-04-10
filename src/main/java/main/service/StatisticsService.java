package main.service;

import main.api.response.StatisticsResponse;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.repository.PostRepository;
import main.repository.PostVotesRepository;
import main.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class StatisticsService
{
    private final PostRepository postRepository;
    private final PostVotesRepository postVotesRepository;
    private final UserRepository userRepository;

    public StatisticsService(PostRepository postRepository, PostVotesRepository postVotesRepository,
                             UserRepository userRepository)
    {
        this.postRepository = postRepository;
        this.postVotesRepository = postVotesRepository;
        this.userRepository = userRepository;
    }

    public StatisticsResponse getAllStatistics()
    {
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        statisticsResponse.setPostsCount(postRepository.getAllPosts().size());
        statisticsResponse.setLikesCount(postVotesRepository.getAllLikes().size());
        statisticsResponse.setDislikesCount(postVotesRepository.getAllDislikes().size());
        statisticsResponse.setViewsCount(postRepository.getAllViews());
        statisticsResponse.setFirstPublication(postRepository.getFirstPostTime().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        return statisticsResponse;
    }

    public StatisticsResponse getMyStatistics(Principal principal)
    {
        StatisticsResponse myStatistics = new StatisticsResponse();
        User authUser = getAuthUser(principal);

        myStatistics.setPostsCount(getUserPosts(authUser).size());
        myStatistics.setLikesCount(getUserVotes(authUser, postVotesRepository.getAllLikes()));
        myStatistics.setDislikesCount(getUserVotes(authUser, postVotesRepository.getAllDislikes()));
        myStatistics.setViewsCount(getUserViewsCount(authUser));
        myStatistics.setFirstPublication(getUserFirstPostTime(authUser));

        return myStatistics;
    }

    private User getAuthUser(Principal principal)
    {
        List<User> users = userRepository.findAll();
        for (User user : users)
        {
            if (user.getEmail().equals(principal.getName()))
            {
                return user;
            }
        }
        return new User();
    }

    private List<Post> getUserPosts(User user)
    {
        List<Post> posts = postRepository.findAll();
        List<Post> userPosts = new ArrayList<>();

        for (Post post : posts)
        {
            if (post.getUser().getName().equals(user.getName()))
            {
                userPosts.add(post);
            }
        }

        return userPosts;
    }

    private int getUserVotes(User user, List<PostVote> votes)
    {
        List<PostVote> userVotes = new ArrayList<>();

        for (PostVote vote : votes)
        {
            if (vote.getUser().getName().equals(user.getName()))
            {
                userVotes.add(vote);
            }
        }

        return userVotes.size();
    }

    private int getUserViewsCount(User user)
    {
        int userViewsCount = 0;
        List<Post> userPosts = getUserPosts(user);
        for (Post userPost : userPosts)
        {
            userViewsCount += userPost.getViewCount();
        }
        return userViewsCount;
    }

    private Long getUserFirstPostTime(User user)
    {
        List<Post> posts = getUserPosts(user);
        List<LocalDateTime> userPublicationsTime = new ArrayList<>();
        for (Post post : posts)
        {
            userPublicationsTime.add(post.getTime());
        }
        userPublicationsTime.sort(Comparator.comparing(ChronoLocalDateTime::getChronology));
        return userPublicationsTime.get(0).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }
}
