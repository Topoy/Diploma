package main.controller;

import main.api.response.SettingsResponse;
import main.model.ImmutableCredentials;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController
{
    @Autowired
    private ImmutableCredentials immutableCredentials;
    private final SettingsService settingsService;

    public ApiGeneralController(SettingsService settingsService)
    {
        this.settingsService = settingsService;
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

}
