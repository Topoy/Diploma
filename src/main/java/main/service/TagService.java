package main.service;

import main.api.response.TagResponse;
import main.api.response.TagUnit;
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

    public TagResponse getTags(String query)
    {
        TagResponse tagResponse = new TagResponse();
        if (query.equals(""))
        {
            tagResponse.setTags(getTagList());
        }
        else
        {
            List<Tag> requiredTags = new ArrayList<>();
            List<Tag> tags = getTagList();
            for (Tag tag : tags)
            {
                if (tag.getName().equals(query))
                {
                    requiredTags.add(tag);
                }
            }
            tagResponse.setTags(getTagUnits(requiredTags));
        }
        return tagResponse;
    }
    private List<TagUnit> getTagUnits(List<Tag> specialTags)
    {
        List<TagUnit> tagUnits = new ArrayList<>();
        List<Tag> tagList = specialTags;
        for (Tag tag : tagList)
        {
            TagUnit tagUnit = new TagUnit();
            tagUnit.setName(tag.getName());
            tagUnit.setWeight(0.5);
            tagUnits.add(tagUnit);
        }
        return tagUnits;
    }
}
