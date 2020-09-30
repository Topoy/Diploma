package main.controller;

import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.model.ImmutableCredentials;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController
{
    @Autowired
    private ImmutableCredentials immutableCredentials;
    private final SettingsService settingsService;
    private final TagService tagService;

    public ApiGeneralController(SettingsService settingsService, TagService tagService)
    {
        this.settingsService = settingsService;
        this.tagService = tagService;
    }

    @GetMapping("/api/init")
    public ImmutableCredentials getBlogInfo()
    {
        return immutableCredentials;
    }

    @GetMapping("api/settings")
    public SettingsResponse settings()
    {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("api/tag")
    public TagResponse getTags(@RequestParam String query)
    {
        return tagService.getTags(query);
    }

}
