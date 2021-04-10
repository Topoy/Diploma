package main.api.response;

import java.util.LinkedHashMap;

public class CommentResponse
{
    private int id;
    private boolean result;
    private LinkedHashMap<String, String> errors = new LinkedHashMap();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LinkedHashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(String text)
    {
        errors.put("text", text);
    }
}
