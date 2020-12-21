package main.service;

import main.api.response.StatisticsResponse;
import main.repository.PostRepository;
import main.repository.PostVotesRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class StatisticsService
{
    private final PostRepository postRepository;
    private final PostVotesRepository postVotesRepository;

    public StatisticsService(PostRepository postRepository, PostVotesRepository postVotesRepository)
    {
        this.postRepository = postRepository;
        this.postVotesRepository = postVotesRepository;
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
}
