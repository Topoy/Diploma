package main.controller;

import main.api.response.AddPostResponse;
import main.api.response.PostByIdResponse;
import main.api.response.PostResponse;
import main.api.unit.AddPostParameterUnit;
import main.model.StatusType;
import main.service.AddPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import main.service.PostService;

import java.util.ArrayList;

@ComponentScan({"main.service"})
@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    @Autowired
    PostService postService;

    @Autowired
    AddPostService addPostService;

    @GetMapping(value = "")
    public PostResponse getPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                 @RequestParam(required = false, defaultValue = "10") int limit,
                                 @RequestParam(required = false, defaultValue = "recent") String mode) {
        System.out.println("offset = " + offset + "; limit = " + limit + "; mode = " + mode);
        return postService.getPosts();
    }

    @GetMapping(value = "/search")
    public PostResponse searchPost(@RequestParam(required = false, defaultValue = "0") int offset,
                                   @RequestParam(required = false, defaultValue = "10") int limit,
                                   @RequestParam String query) {
        System.out.println("offset = " + offset + "; limit = " + limit + "; query = " + query);
        return postService.searchPost(query);
    }

    @GetMapping(value = "/byDate")
    public PostResponse searchPostByDate(@RequestParam(required = false, defaultValue = "0") int offset,
                                         @RequestParam(required = false, defaultValue = "10") int limit,
                                         @RequestParam String date) {
        System.out.println("offset = " + offset + "; limit = " + limit + "; date = " + date);
        return postService.searchPostByDate(date);
    }

    @GetMapping(value = "/byTag")
    public PostResponse searchPostByTag(@RequestParam(required = false, defaultValue = "0") int offset,
                                        @RequestParam(required = false, defaultValue = "10") int limit,
                                        @RequestParam String tag) {
        System.out.println("offset = " + offset + "; limit = " + limit + "; tag = " + tag);
        return postService.searchPostsByTag(tag);
    }

    @GetMapping(value = "/moderation")
    public PostResponse getModerationPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                           @RequestParam(required = false, defaultValue = "10") int limit,
                                           @RequestParam StatusType status) {
        System.out.println("offset = " + offset + "; limit = " + limit + "; status = " + status);
        return postService.getModerationPosts(status);
    }

    @GetMapping(value = "/my")
    public PostResponse getMyPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                   @RequestParam(required = false, defaultValue = "10") int limit,
                                   @RequestParam String status) {
        System.out.println("offset = " + offset + "; limit = " + limit + "; status = " + status);
        return postService.getMyPosts(status);
    }

    @GetMapping(value = "/{id}")
    public PostByIdResponse getPostById(@PathVariable("id") int id) {
        return postService.getPostById(id);
    }

    @PostMapping(value = "")
    public AddPostResponse addPost(@RequestBody AddPostParameterUnit postParameterUnit) {
        //addPostService.savePostParameters(timestamp, active, title, text, tags);
        return addPostService.addPost(postParameterUnit);
        //return "Hello, guys!";
    }
}