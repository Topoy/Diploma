package main.controller;

import main.api.response.*;
import main.api.unit.CommentParameterUnit;
import main.model.ImmutableCredentials;
import main.service.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@ComponentScan({"main.service"})
@RestController
public class ApiGeneralController
{
    private final ImmutableCredentials immutableCredentials;
    private final SettingsService settingsService;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final ImageService imageService;
    private final StatisticsService statisticsService;
    private final CommentService commentService;
    private final EditProfileService editProfileService;


    public ApiGeneralController(ImmutableCredentials immutableCredentials, SettingsService settingsService, TagService tagService,
                                CalendarService calendarService, ImageService imageService, StatisticsService statisticsService,
                                CommentService commentService, EditProfileService editProfileService)
    {
        this.settingsService = settingsService;
        this.tagService = tagService;
        this.calendarService = calendarService;
        this.imageService = imageService;
        this.immutableCredentials = immutableCredentials;
        this.statisticsService = statisticsService;
        this.commentService = commentService;
        this.editProfileService = editProfileService;
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
    public ImageResponse uploadImage(@RequestParam("image") MultipartFile image)
    {
        return imageService.uploadImage(image);
    }

    @GetMapping("/api/statistics/all")
    public StatisticsResponse getAllStatistics()
    {
        return statisticsService.getAllStatistics();
    }

    @GetMapping("/api/statistics/my")
    public StatisticsResponse getMyStatistics(Principal principal)
    { return statisticsService.getMyStatistics(principal); }

    @PostMapping("/api/comment")
    public CommentResponse setComment(@RequestBody CommentParameterUnit commentParameterUnit, Principal principal)
    {
        return commentService.setComment(commentParameterUnit, principal);
    }

    @PostMapping(value = "/api/profile/my")
    public ProfileResponse editMyProfileWithPhoto(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "removePhoto") int removePhoto,
            @RequestParam(value = "photo") MultipartFile photo, Principal principal)
    {
        return editProfileService.editMyProfileWithPhoto(name, email, password, removePhoto, photo, principal);
    }



}
