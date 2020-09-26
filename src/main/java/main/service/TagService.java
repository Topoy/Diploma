package main.service;

import main.model.Tag;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TagService
{
    @Autowired
    private TagRepository tagRepository;

    public List getTagList()
    {
        List<Tag> tagList = new ArrayList<>();
        Iterable<Tag> tags = tagRepository.findAll();
        for (Tag tag : tags)
        {
            tagList.add(tag);
        }
        return tagList;
    }
}
