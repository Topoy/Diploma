package main.service;

import main.api.response.ImageResponse;
import org.springframework.stereotype.Service;

@Service
public class ImageService
{
    public ImageResponse uploadImage()
    {
        ImageResponse imageResponse = new ImageResponse();

        return imageResponse;
    }
}
