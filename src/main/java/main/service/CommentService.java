package main.service;

import main.model.Post;
import main.model.PostComment;
import main.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService
{
    private final CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    public int getCommentsCount(Post post)
    {
        Iterable<PostComment> comments = commentRepository.findAll();
        List<PostComment> commentsByPost = new ArrayList<>();

        for (PostComment comment : comments)
        {
            if (comment.getPost().getId() == post.getId())
            {
                commentsByPost.add(comment);
            }
        }
        return commentsByPost.size();
    }
}
