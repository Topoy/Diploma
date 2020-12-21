package main.controller;

import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.RegisterResponse;
import main.api.unit.LoginParametersUnit;
import main.api.unit.RegisterParametersUnit;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@ComponentScan({"main.service"})
@RestController
public class ApiAuthController
{
    @Autowired
    AuthService authService;

    @PostMapping("api/auth/register")
    public RegisterResponse registerUser(@RequestBody RegisterParametersUnit registerParametersUnit)
    {
        return authService.registerUser(registerParametersUnit);
    }

    @GetMapping("api/auth/captcha")
    public CaptchaResponse getCaptcha() throws IOException {
        return authService.getCaptcha();
    }

    @PostMapping("api/auth/login")
    public AuthResponse loginUser(@RequestBody LoginParametersUnit loginParametersUnit)
    {
        return authService.loginUser(loginParametersUnit);
    }

    //  @GetMapping("api/auth/check")
    /*public AuthResponse checkUserAuth()
    {
        return authService.checkUserAuth();
    }*/

}
