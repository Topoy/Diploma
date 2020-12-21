package main.service;

import main.api.response.PostByIdResponse;
import main.api.response.PostResponse;
import main.api.unit.CommentUnit;
import main.api.unit.PostUnit;
import main.api.unit.UserUnit;
import main.model.*;
import main.repository.CommentRepository;
import main.repository.PostRepository;
import main.repository.PostVotesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService
{
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagService tagService;
    private final PostVoteService postVoteService;
    private final CommentService commentService;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, TagService tagService,
                       PostVoteService postVoteService, CommentService commentService)
    {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.tagService = tagService;
        this.postVoteService = postVoteService;
        this.commentService = commentService;
    }


    public PostResponse getPosts(int offset, int limit, String mode)
    {
        PostResponse postResponse = new PostResponse();
        postResponse.setCount((int)postRepository.count());
        postResponse.setPosts(getPostUnits(getPostsListDemandsOnMode(offset, limit, mode)));
        return postResponse;
    }

    private List<Post> getPostsList(int offset, int limit)
    {
        int pageNum = offset/limit;
        Pageable page = PageRequest.of(pageNum, limit);
        List<Post> postList = new ArrayList<>();
        Iterable<Post> posts = postRepository.findAll(page);
        for (Post post : posts)
        {
            postList.add(post);
        }
        return postList;
    }

    private List<Post> getPostsListDemandsOnMode(int offset, int limit, String mode)
    {
        int pageNum = offset/limit;
        Pageable page = PageRequest.of(pageNum, limit);
        List<Post> posts = new ArrayList<>();
        if (mode.equals("recent"))
        {
            posts = postRepository.getRecentPosts(page);
        }
        if (mode.equals("popular"))
        {
            posts = postRepository.getMostPopularPosts(page);
        }
        if (mode.equals("best"))
        {
            posts = postRepository.getBestPosts(page);
        }
        if (mode.equals("early"))
        {
            posts = postRepository.getEarlyPosts(page);
        }
        return new ArrayList<>(posts);

    }
    private List<PostUnit> getPostUnits(List<Post> specialPosts)
    {
        List<PostUnit> postUnits = new ArrayList<>();
        int maxAnnounceLength = 50;
        for (Post post : specialPosts)
        {
            PostUnit postUnit = new PostUnit();
            UserUnit userUnit = new UserUnit();
            userUnit.setId(post.getUser().getId());
            userUnit.setName(post.getUser().getName());
            postUnit.setId(post.getId());
            postUnit.setTimestamp(post.getTime().atZone(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli()/1000);
            postUnit.setUser(userUnit);
            postUnit.setTitle(post.getTitle());
            postUnit.setAnnounce(post.getText().substring(0, Math.min(post.getText().length(), maxAnnounceLength)) + "...");
            postUnit.setLikeCount(postVoteService.getVotesCount(post, "like"));
            postUnit.setDislikeCount(postVoteService.getVotesCount(post, "dislike"));
            postUnit.setCommentCount(commentService.getCommentsCount(post));
            postUnit.setViewCount(post.getViewCount());
            postUnits.add(postUnit);
        }
        return postUnits;
    }

    public PostResponse searchPost(int offset, int limit, String query)
    {
        PostResponse postResponse = new PostResponse();
        List<Post> foundPosts = getFoundPosts(offset, limit, query);
        postResponse.setCount(foundPosts.size());
        postResponse.setPosts(getPostUnits(foundPosts));
        return postResponse;
    }

    private List<Post> getFoundPosts(int offset, int limit, String query)
    {
        List<Post> foundPosts = new ArrayList<>();
        //List<Post> posts = getPostsList(offset, limit);
        List<Post> posts = postRepository.getAllPosts();
        for (Post post : posts)
        {
            if ((post.getTitle().contains(query)) || (post.getUser().getName().contains(query)) || (post.getText().contains(query)))
            {
                foundPosts.add(post);
            }
        }
        return foundPosts;
    }

    public PostResponse searchPostByDate(int offset, int limit, String date)
    {
        PostResponse postResponse = new PostResponse();
        List<Post> foundPostsByDate = getFoundPostsByDate(offset, limit, date);
        postResponse.setCount(foundPostsByDate.size());
        postResponse.setPosts(getPostUnits(foundPostsByDate));
        return postResponse;
    }

    private List<Post> getFoundPostsByDate(int offset, int limit, String date)
    {
        List<Post> foundPostsByDate = new ArrayList<>();
        List<Post> posts = getPostsList(offset, limit);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDate localDate1;
        for (Post post : posts)
        {
            try
            {
                localDate1 = post.getTime().toLocalDate();
            }
            catch (NullPointerException e)
            {
                System.out.println("Ошибка!");
                continue;
            }
            if (localDate1.isEqual(localDate))
            {
                foundPostsByDate.add(post);
            }
        }
        return foundPostsByDate;
    }

    private List<Post> getPostsByTag(String tag)
    {
        List<Post> foundPostsByTag = new ArrayList<>();
        List<Tag> tags = tagService.getTagList();

        for (Tag item : tags)
        {
           if (item.getName().equals(tag))
           {
               foundPostsByTag = item.getPosts();
           }
        }
        return foundPostsByTag;
    }

    public PostResponse searchPostsByTag(int offset, int limit, String tag)
    {
        PostResponse postResponse = new PostResponse();
        List<Post> postsByTag = getPostsByTag(tag);
        postResponse.setCount(postsByTag.size());
        postResponse.setPosts(getPostUnits(postsByTag));
        return postResponse;
    }

    public PostResponse getModerationPosts(int offset, int limit, StatusType status)
    {
        PostResponse postResponse = new PostResponse();
        List<Post> moderationPosts = getPostsForModeration(offset, limit, status);
        postResponse.setCount(moderationPosts.size());
        postResponse.setPosts(getPostUnits(moderationPosts));
        return postResponse;
    }

    @NotNull
    private List<Post> getPostsForModeration(int offset, int limit, StatusType status)
    {
        List<Post> moderationPosts = new ArrayList<>();
        List<Post> posts = getPostsList(offset, limit);

        for (Post post : posts)
        {
            try {
                if (post.getModerationStatus().equals(status))
                {
                    moderationPosts.add(post);
                }
            }
            catch (NullPointerException e)
            {
                System.out.println("Ошибка! У данного поста нет статуса!");
            }
        }
        return moderationPosts;
    }

    public PostResponse getMyPosts(int offset, int limit, String status)
    {
        PostResponse postResponse = new PostResponse();
        List<Post> authUserPosts = getAuthUserPosts(offset, limit, status);
        postResponse.setCount(authUserPosts.size());
        postResponse.setPosts(getPostUnits(authUserPosts));
        return postResponse;
    }

    private List<Post> getAuthUserPosts(int offset, int limit, String status)
    {
        List<Post> authUserPosts = new ArrayList<>();
        List<Post> posts = getPostsList(offset, limit);
        if (status.equals("inactive"))
        {
            for (Post post : posts)
            {
                if (post.getIsActive() == 0)
                {
                    authUserPosts.add(post);
                }
            }
            return authUserPosts;
        }
        if (status.equals("pending"))
        {
            for (Post post : posts)
            {
                if (post.getIsActive() == 1 && post.getModerationStatus().equals(StatusType.NEW))
                {
                    authUserPosts.add(post);
                }
            }
            return authUserPosts;
        }
        if (status.equals("declined"))
        {
            for (Post post : posts)
            {
                if (post.getIsActive() == 1 && post.getModerationStatus().equals(StatusType.DECLINED))
                {
                    authUserPosts.add(post);
                }
            }
            return authUserPosts;
        }
        else {
            for (Post post : posts)
            {
                if (post.getIsActive() == 1 && post.getModerationStatus().equals(StatusType.ACCEPTED))
                {
                    authUserPosts.add(post);
                }
            }
            return authUserPosts;
        }
    }

    public PostByIdResponse getPostById(int id)
    {
        PostByIdResponse postByIdResponse = new PostByIdResponse();
        UserUnit postAuthor = new UserUnit();

        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Нет элемента с id: " + id));

        postAuthor.setId(post.getUser().getId());
        postAuthor.setName(post.getUser().getName());
        postAuthor.setPhoto(post.getUser().getPhoto());

        List<CommentUnit> comments = getCommentUnits(id);

        postByIdResponse.setId(post.getId());
        postByIdResponse.setTimestamp(post.convertTimeToTimeStamp());
        postByIdResponse.setActive(true);
        postByIdResponse.setUser(postAuthor);
        postByIdResponse.setTitle(post.getTitle());
        postByIdResponse.setText(post.getText());
        postByIdResponse.setLikeCount(postVoteService.getVotesCount(post, "like"));
        postByIdResponse.setDislikeCount(postVoteService.getVotesCount(post, "dislike"));
        postByIdResponse.setViewCount(post.getViewCount());
        postByIdResponse.setComments(comments);
        postByIdResponse.setTags(tagService.getTagsByPost(post));

        return postByIdResponse;
    }

    public List<PostComment> getPostComments()
    {
        Iterable<PostComment> postCommentsList = commentRepository.findAll();
        List<PostComment> postComments = new ArrayList<>();
        for (PostComment postComment : postCommentsList)
        {
            postComments.add(postComment);
        }
        return postComments;
    }

    public List<CommentUnit> getCommentUnits(int id)
    {
        List<PostComment> postCommentList = getPostComments();
        List<CommentUnit> commentUnits = new ArrayList<>();
        for (PostComment postComment : postCommentList)
        {
            if (postComment.getPost().getId() == id)
            {
                CommentUnit commentUnit = new CommentUnit();
                UserUnit commentAuthor = new UserUnit();

                commentAuthor.setName(postComment.getUser().getName());
                commentAuthor.setId(postComment.getUser().getId());
                commentAuthor.setPhoto(postComment.getUser().getPhoto());

                commentUnit.setId(postComment.getId());
                commentUnit.setTimestamp(postComment.convertTimeToTimeStamp());
                commentUnit.setText(postComment.getText());
                commentUnit.setUser(commentAuthor);
                commentUnits.add(commentUnit);
            }
        }
        return commentUnits;
    }
}
