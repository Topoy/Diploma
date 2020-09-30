package main.controller;

import main.api.response.AuthResponse;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan({"main.service"})
@RestController
public class ApiAuthController
{
    @Autowired
    AuthService authService;

    @GetMapping("api/auth/check")
    public AuthResponse checkUserAuth()
    {
        return authService.checkUserAuth();
    }
}
