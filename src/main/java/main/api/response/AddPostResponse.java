package main.api.response;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AddPostResponse
{
    boolean result;
    LinkedHashMap<String, String> errors = new LinkedHashMap<>();

    public boolean getResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public HashMap<String, String> getErrors()
    {
        return errors;
    }

    public void setErrors(String title, String text)
    {
        this.errors.put("title", title);
        this.errors.put("text", text);
    }
}
