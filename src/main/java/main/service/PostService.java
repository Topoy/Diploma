package main.service;

import main.api.response.PostResponse;
import main.api.response.PostUnit;
import main.api.response.UserUnit;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService
{
    @Autowired
    PostRepository postRepository;

    public PostResponse getPosts()
    {
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(getPostsList().size());
        postResponse.setPosts(getPostUnits(getPostsList()));
        return postResponse;
    }

    private List getPostsList()
    {
        List<Post> postList = new ArrayList<>();
        Iterable<Post> posts = postRepository.findAll();
        for (Post post : posts)
        {
            postList.add(post);
        }
        return postList;
    }
    private List<PostUnit> getPostUnits(List<Post> specialPosts)
    {
        List<PostUnit> postUnits = new ArrayList<>();
        List<Post> postList = specialPosts;
        for (Post post : postList)
        {
            PostUnit postUnit = new PostUnit();
            UserUnit userUnit = new UserUnit();
            userUnit.setId(post.getUser().getId());
            userUnit.setName(post.getUser().getName());
            postUnit.setId(post.getId());
            postUnit.setTimestamp(1000000);
            postUnit.setUser(userUnit);
            postUnit.setTitle(post.getTitle());
            postUnit.setAnnounce("qwerty");
            postUnit.setLikeCount(1);
            postUnit.setDislikeCount(1);
            postUnit.setCommentCount(2);
            postUnit.setViewCount(2);
            postUnits.add(postUnit);
        }
        return postUnits;
    }

    public PostResponse searchPost(String query)
    {
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(getFoundPosts(query).size());
        postResponse.setPosts(getPostUnits(getFoundPosts(query)));
        return postResponse;
    }

    private List getFoundPosts(String query)
    {
        List<Post> foundPosts = new ArrayList<>();
        List<Post> posts = getPostsList();
        for (Post post : posts)
        {
            if ((post.getTitle().contains(query)) || (post.getUser().getName().contains(query)) || (post.getText().contains(query)))
            {
                foundPosts.add(post);
            }
        }
        return foundPosts;
    }


}
