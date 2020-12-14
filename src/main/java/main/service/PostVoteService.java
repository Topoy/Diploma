package main.service;

import main.model.Post;
import main.model.PostVote;
import main.repository.PostVotesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostVoteService
{
    private final PostVotesRepository postVotesRepository;

    public PostVoteService(PostVotesRepository postVotesRepository)
    {
        this.postVotesRepository = postVotesRepository;
    }

    public int getVotesCount(Post post, String vote)
    {
        List<PostVote> votes = new ArrayList<>();
        if (vote.equals("like"))
        {
            votes = postVotesRepository.getAllLikes();
        }
        else if (vote.equals("dislike"))
        {
            votes = postVotesRepository.getAllDislikes();
        }
        List<PostVote> likesByPost = new ArrayList<>();
        for (PostVote like : votes)
        {
            if (like.getPost().getId() == post.getId())
            {
                likesByPost.add(like);
            }
        }
        return likesByPost.size();
    }

}
