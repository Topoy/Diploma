package main.controller;

import main.api.response.CalendarResponse;
import main.api.response.ImageResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.model.ImmutableCredentials;
import main.service.CalendarService;
import main.service.ImageService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ApiGeneralController
{
    private final ImmutableCredentials immutableCredentials;
    private final SettingsService settingsService;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final ImageService imageService;

    public ApiGeneralController(ImmutableCredentials immutableCredentials, SettingsService settingsService, TagService tagService,
                                CalendarService calendarService, ImageService imageService)
    {
        this.settingsService = settingsService;
        this.tagService = tagService;
        this.calendarService = calendarService;
        this.imageService = imageService;
        this.immutableCredentials = immutableCredentials;
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
    public TagResponse getTags(@RequestParam(required = false, defaultValue = "") String query)
    {
        return tagService.getTags(query);
    }
    @GetMapping("/api/calendar")
    public CalendarResponse getCalendar(@RequestParam(required = false) int year)
    {
        return calendarService.getCalendar(year);
    }

    @PostMapping("/api/image")
    public ImageResponse uploadImage(@RequestParam("image") MultipartFile file)
    {
        return imageService.uploadImage();
    }

}
