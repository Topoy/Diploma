package main.controller;

import main.api.response.PostResponse;
import main.model.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import main.service.PostService;

@ComponentScan({"main.service"})
@RestController
public class ApiPostController
{
    @Autowired
    PostService postService;

    @GetMapping(value = "/api/post")
    public PostResponse getPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode)
    {
        System.out.println("offset = " + offset + "; limit = " + limit + "; mode = " + mode);
        return postService.getPosts();
    }

    @GetMapping(value = "api/post/search")
    public PostResponse searchPost(@RequestParam int offset, @RequestParam int limit, @RequestParam String query)
    {
        System.out.println("offset = " + offset + "; limit = " + limit + "; query = " + query);
        return postService.searchPost(query);
        //return "Hello!";
    }

}
