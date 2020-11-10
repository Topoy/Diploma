package main.service;

import main.api.response.PostByIdResponse;
import main.api.response.PostResponse;
import main.api.unit.CommentUnit;
import main.api.unit.PostUnit;
import main.api.unit.UserUnit;
import main.model.Post;
import main.model.PostComment;
import main.model.StatusType;
import main.model.Tag;
import main.repository.CommentRepository;
import main.repository.PostRepository;
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

    public PostService(PostRepository postRepository, CommentRepository commentRepository, TagService tagService)
    {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.tagService = tagService;
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
            postUnit.setAnnounce(post.getText().substring(0, 50) + "...");
            postUnit.setLikeCount(1);
            postUnit.setDislikeCount(1);
            postUnit.setCommentCount(2);
            postUnit.setViewCount(2);
            postUnits.add(postUnit);
        }
        return postUnits;
    }

    public PostResponse searchPost(int offset, int limit, String query)
    {
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(getFoundPosts(offset, limit, query).size());
        postResponse.setPosts(getPostUnits(getFoundPosts(offset, limit, query)));
        return postResponse;
    }

    private List<Post> getFoundPosts(int offset, int limit, String query)
    {
        List<Post> foundPosts = new ArrayList<>();
        List<Post> posts = getPostsList(offset, limit);
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
        postResponse.setCount(getFoundPostsByDate(offset, limit, date).size());
        postResponse.setPosts(getPostUnits(getFoundPostsByDate(offset, limit, date)));
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
        postResponse.setCount(getPostsByTag(tag).size());
        postResponse.setPosts(getPostUnits(getPostsByTag(tag)));
        return postResponse;
    }

    public PostResponse getModerationPosts(int offset, int limit, StatusType status)
    {
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(getPostsForModeration(offset, limit, status).size());
        postResponse.setPosts(getPostUnits(getPostsForModeration(offset, limit, status)));
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
        postResponse.setCount(getAuthUserPosts(offset, limit, status).size());
        postResponse.setPosts(getPostUnits(getAuthUserPosts(offset, limit, status)));
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
        UserUnit userUnit = new UserUnit();

        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Нет элемента с id: " + id));

        userUnit.setId(post.getUser().getId());
        userUnit.setName(post.getUser().getName());
        userUnit.setPhoto(post.getUser().getPhoto());

        List<CommentUnit> comments = getCommentUnits(id, userUnit);

        postByIdResponse.setId(post.getId());
        postByIdResponse.setTimestamp(post.convertTimeToTimeStamp());
        postByIdResponse.setActive(true);
        postByIdResponse.setUser(userUnit);
        postByIdResponse.setTitle(post.getTitle());
        postByIdResponse.setText(post.getText());
        postByIdResponse.setLikeCount(3);
        postByIdResponse.setDislikeCount(0);
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

    public List<CommentUnit> getCommentUnits(int id, UserUnit user)
    {
        List<PostComment> postCommentList = getPostComments();
        List<CommentUnit> commentUnits = new ArrayList<>();
        for (PostComment postComment : postCommentList)
        {
            if (postComment.getPost().getId() == id)
            {
                CommentUnit commentUnit = new CommentUnit();
                commentUnit.setId(postComment.getId());
                commentUnit.setTimestamp(postComment.convertTimeToTimeStamp());
                commentUnit.setText(postComment.getText());
                commentUnit.setUser(user);
                commentUnits.add(commentUnit);
            }
        }
        return commentUnits;
    }
}
