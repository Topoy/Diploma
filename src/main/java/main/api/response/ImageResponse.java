package main.api.response;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ImageResponse
{
    private String imagePath;
    private boolean result;
    private LinkedHashMap<String, String> errors = new LinkedHashMap<>();

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(String text)
    {
        this.errors.put("image", text);
    }
}
