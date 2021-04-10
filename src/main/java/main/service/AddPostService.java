package main.service;

import main.api.response.AddPostResponse;
import main.api.unit.AddPostParameterUnit;
import main.model.Post;
import main.model.StatusType;
import main.model.User;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class AddPostService
{
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public AddPostResponse addPost(AddPostParameterUnit addPostParameterUnit)
    {
        return createPost(addPostParameterUnit, new Post());
    }

    public AddPostResponse editPost(AddPostParameterUnit postParameterUnit, int id)
    {
        Post post = postRepository.findById(id).get();
        return createPost(postParameterUnit, post);
    }

    private AddPostResponse createPost(AddPostParameterUnit addPostParameterUnit, Post post)
    {
        AddPostResponse addPostResponse = new AddPostResponse();
        if (addPostParameterUnit.getTitle().length() < 3 || addPostParameterUnit.getText().length() < 50)
        {
            addPostResponse.setResult(false);
            addPostResponse.setErrors(addPostParameterUnit.getTitle(), addPostParameterUnit.getText());
        }
        else {
            addPostResponse.setResult(true);
            Timestamp ts = new Timestamp(addPostParameterUnit.getTimestamp());
            post.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(addPostParameterUnit.getTimestamp()),
                    TimeZone.getTimeZone("Europe/Moscow").toZoneId()));
            post.setIsActive(addPostParameterUnit.getActive());
            post.setTitle(addPostParameterUnit.getTitle());
            post.setText(addPostParameterUnit.getText());
            post.setModerationStatus(StatusType.NEW);
            post.setUser(getAuthUser().orElseThrow(() -> new UsernameNotFoundException("Не найден такой пользователь")));

            postRepository.save(post);
        }
        return addPostResponse;
    }

    private Optional<User> getAuthUser()
    {
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        return user;
    }
}
