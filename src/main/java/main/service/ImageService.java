package main.service;

import main.api.response.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Random;

@Service
public class ImageService
{
    public ImageResponse uploadImage(MultipartFile image)
    {
        ImageResponse imageResponse = new ImageResponse();
        String path = getRandomPath();
        File fileDirectory = new File(path);
        fileDirectory.mkdirs();
        try {
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(path + image.getOriginalFilename()));
            stream.write(image.getBytes());
            stream.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        if (image.getSize() > 5242880)
        {
            imageResponse.setResult(false);
            imageResponse.setErrors("Размер файла превышает допустимый размер");
        }
        else
        {
            imageResponse.setResult(true);
            imageResponse.setImagePath(path + image.getOriginalFilename());
        }

        return imageResponse;
    }

    public BufferedImage resizeImage(BufferedImage originImage, int targetWidth, int targetHeight) throws IOException
    {
        Image resultingImage = originImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private String getRandomPath()
    {
        final char[] allAllowed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new SecureRandom();
        int length = 12;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            sb.append(allAllowed[random.nextInt(allAllowed.length)]);
        }

        String firstSubfolder = sb.substring(0, 2);
        String secondSubfolder = sb.substring(2, 4);
        String thirdSubfolder = sb.substring(4, 6);
        String imageName = sb.substring(6, 12);

        return "upload/" + firstSubfolder + "/" + secondSubfolder + "/"
                + thirdSubfolder;
    }
}
