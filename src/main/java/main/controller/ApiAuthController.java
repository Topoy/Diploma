package main.controller;

import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.ChangePasswordResponse;
import main.api.response.RegisterResponse;
import main.api.unit.ChangePasswordParameterUnit;
import main.api.unit.LoginParametersUnit;
import main.api.unit.RegisterParametersUnit;
import main.api.unit.RestorePasswordParameterUnit;
import main.repository.UserRepository;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@ComponentScan({"main.service"})
@RestController
public class ApiAuthController
{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Autowired
    public ApiAuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                             AuthService authService)
    {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authService = authService;

    }

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

    @GetMapping("api/auth/check")
    public AuthResponse checkUserAuth(Principal principal)
    {
        return authService.checkUserAuth(principal);
    }

    @PostMapping("api/auth/restore")
    public boolean restorePassword(@RequestBody RestorePasswordParameterUnit restorePasswordParameterUnit)
    {
        return authService.restorePassword(restorePasswordParameterUnit);
    }

    @PostMapping("api/auth/password")
    public ChangePasswordResponse changePassword(@RequestBody ChangePasswordParameterUnit changePasswordParameterUnit)
    {
        return authService.changePassword(changePasswordParameterUnit);
    }


}
