package main.controller;

import main.api.response.PostResponse;
import main.model.Mode;
import main.model.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import main.service.PostService;

@ComponentScan({"main.service"})
@RestController
@RequestMapping("/api/post")
public class ApiPostController
{
    @Autowired
    PostService postService;

    @GetMapping(value = "")
    public PostResponse getPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode)
    {
        System.out.println("offset = " + offset + "; limit = " + limit + "; mode = " + mode);
        return postService.getPosts();
    }

    @GetMapping(value = "/search")
    public PostResponse searchPost(@RequestParam int offset, @RequestParam int limit, @RequestParam String query)
    {
        System.out.println("offset = " + offset + "; limit = " + limit + "; query = " + query);
        return postService.searchPost(query);
    }

    @GetMapping(value = "/byDate")
    public PostResponse searchPostByDate(@RequestParam int offset, @RequestParam int limit, @RequestParam String date)
    {
        System.out.println("offset = " + offset + "; limit = " + limit + "; date = " + date);
        return postService.searchPostByDate(date);
    }

    @GetMapping(value = "/byTag")
    public PostResponse searchPostByTag(@RequestParam int offset, @RequestParam int limit, @RequestParam String tag)
    {
        System.out.println("offset = " + offset + "; limit = " + limit + "; tag = " + tag);
        return postService.searchPostsByTag(tag);
    }

    @GetMapping(value = "/moderation")
    public PostResponse getModerationPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam StatusType status)
    {
        System.out.println("offset = " + offset + "; limit = " + limit + "; status = " + status);
        return postService.getModerationPosts(status);
    }

    @GetMapping(value = "/my")
    public PostResponse getMyPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam String status)
    {
        System.out.println("offset = " + offset + "; limit = " + limit + "; status = " + status);
        return postService.getMyPosts(status);
    }
}
