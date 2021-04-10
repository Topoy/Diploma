package main.api.response;

import java.util.LinkedHashMap;

public class ChangePasswordResponse
{
    private boolean result;
    private LinkedHashMap<String, String> errors = new LinkedHashMap<>();

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LinkedHashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(String code, String password, String captcha)
    {
        errors.put("code", code);
        errors.put("password", password);
        errors.put("captcha", captcha);
    }
}
