package main.service;

import main.api.response.CommentResponse;
import main.api.unit.CommentParameterUnit;
import main.model.Post;
import main.model.PostComment;
import main.repository.CommentRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService
{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository,
                          UserRepository userRepository)
    {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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

    public CommentResponse setComment(CommentParameterUnit commentParameters, Principal principal)
    {
        CommentResponse commentResponse = new CommentResponse();
        PostComment postComment = new PostComment();
        List<Integer> postIds = getPostIds();
        List<Integer> commentIds = getCommentIds();

        if ((postIds.contains(commentParameters.getPostId()))
                && ((commentIds.contains(commentParameters.getParentId())
                || commentParameters.getParentId() == null)))
        {
            if ((commentParameters.getText().isEmpty()) || (commentParameters.getText().length() < 50))
            {
                commentResponse.setResult(false);
                commentResponse.setErrors("Текст комментария не задан или слишком короткий");
            }
            else {
                postComment.setParentId(commentParameters.getParentId());
                postComment.setPost(postRepository.findById(commentParameters.getPostId())
                        .orElseThrow(() -> new NoSuchElementException("Такой пост не найден")));
                postComment.setUser(userRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("Такой пользователь не найден")));
                postComment.setTime(LocalDateTime.now());
                postComment.setText(commentParameters.getText());
                commentRepository.save(postComment);

                commentResponse.setId(postComment.getId());
            }
        }
        return commentResponse;
    }

    private List<Integer> getPostIds()
    {
        List<Post> posts = postRepository.findAll();
        List<Integer> postIds = new ArrayList<>();
        for (Post post : posts)
        {
            postIds.add(post.getId());
        }
        return postIds;
    }

    private List<Integer> getCommentIds()
    {
        List<PostComment> comments = commentRepository.findAll();
        List<Integer> commentIds = new ArrayList<>();
        for (PostComment comment : comments)
        {
            commentIds.add(comment.getId());
        }
        return commentIds;
    }

}
