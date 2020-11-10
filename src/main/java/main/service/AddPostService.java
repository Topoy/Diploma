package main.service;

import main.api.response.AddPostResponse;
import main.api.unit.AddPostParameterUnit;
import main.model.Post;
import main.model.StatusType;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Service
public class AddPostService
{
    @Autowired
    private PostRepository postRepository;

    public AddPostResponse addPost(AddPostParameterUnit addPostParameterUnit)
    {
        AddPostResponse addPostResponse = new AddPostResponse();
        if (addPostParameterUnit.getTitle().length() < 3 || addPostParameterUnit.getText().length() < 50)
        {
            addPostResponse.setResult(false);
            addPostResponse.setErrors(addPostParameterUnit.getTitle(), addPostParameterUnit.getText());
        }
        else {
            addPostResponse.setResult(true);
            Post post = new Post();
            Timestamp ts = new Timestamp(addPostParameterUnit.getTimestamp());
            post.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(addPostParameterUnit.getTimestamp()),
                        TimeZone.getTimeZone("Europe/Moscow").toZoneId()));
            post.setIsActive(addPostParameterUnit.getActive());
            post.setTitle(addPostParameterUnit.getTitle());
            post.setText(addPostParameterUnit.getText());
            post.setModerationStatus(StatusType.NEW);
            //post.setUser();

            postRepository.save(post);
        }
        return addPostResponse;
    }
}
