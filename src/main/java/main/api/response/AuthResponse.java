package main.api.response;

public class AuthResponse
{
    private boolean result;
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
