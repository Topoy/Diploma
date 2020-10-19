package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.api.unit.AuthUserUnit;

public class AuthResponse
{
    private boolean result;

    @JsonProperty("user")
    private AuthUserUnit authUserUnit;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public AuthUserUnit getAuthUserUnit() {
        return authUserUnit;
    }

    public void setAuthUserUnit(AuthUserUnit authUserUnit) {
        this.authUserUnit = authUserUnit;
    }
}
