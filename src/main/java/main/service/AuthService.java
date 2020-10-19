package main.service;

import main.api.response.AuthResponse;
import main.api.unit.AuthUserUnit;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
    public AuthResponse checkUserAuth()
    {
        AuthResponse authResponse = new AuthResponse();
        AuthUserUnit authUserUnit = new AuthUserUnit();
        authResponse.setResult(true);
        authUserUnit.setId(1);
        authUserUnit.setName("Masha Lomova");
        authUserUnit.setPhoto("123456.jpg");
        authUserUnit.setEmail("lomova@gmail.com");
        authUserUnit.setModeration(true);
        authUserUnit.setModerationCount(2);
        authUserUnit.setSettings(true);
        authResponse.setAuthUserUnit(authUserUnit);
        return authResponse;
    }
}
