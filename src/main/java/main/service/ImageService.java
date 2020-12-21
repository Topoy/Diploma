package main.service;

import main.api.response.ImageResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class ImageService
{
    public ImageResponse uploadImage()
    {
        ImageResponse imageResponse = new ImageResponse();

        return imageResponse;
    }

    public BufferedImage resizeImage(BufferedImage originImage, int targetWidth, int targetHeight) throws IOException
    {
        Image resultingImage = originImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }
}
