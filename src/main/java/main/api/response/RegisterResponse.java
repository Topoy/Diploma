package main.api.response;


import java.util.LinkedHashMap;

public class RegisterResponse
{
    private boolean result;
    LinkedHashMap<String, String> errors = new LinkedHashMap<>();

    public boolean getResult() { return result; }

    public void setResult(boolean result) { this.result = result; }

    public LinkedHashMap<String, String> getErrors() { return errors; }

    public void setErrors(String email, String name, String password, String captcha)
    {
        errors.put("email", email);
        errors.put("name", name);
        errors.put("password", password);
        errors.put("captcha", captcha);
    }
}
