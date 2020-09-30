package main.api.response;

import java.util.List;

public class TagResponse
{
    private List<TagUnit> tags;

    public List<TagUnit> getTags() {
        return tags;
    }

    public void setTags(List<TagUnit> tags) {
        this.tags = tags;
    }
}
