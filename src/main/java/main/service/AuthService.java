package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.RegisterResponse;
import main.api.unit.AuthUserUnit;
import main.api.unit.LoginParametersUnit;
import main.api.unit.RegisterParametersUnit;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class AuthService
{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final ImageService imageService;

    public AuthService(PostRepository postRepository, UserRepository userRepository, CaptchaRepository captchaRepository,
                       ImageService imageService)
    {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
        this.imageService = imageService;
    }
    public AuthResponse checkUserAuth()
    {
        AuthResponse authResponse = new AuthResponse();
        AuthUserUnit authUserUnit = new AuthUserUnit();
        authResponse.setResult(true);
        authUserUnit.setId(1);
        authUserUnit.setName("Masha Lomova");
        authUserUnit.setPhoto("123456.jpg");
        authUserUnit.setEmail("lomova@gmail.com");
        authUserUnit.setModeration(true);
        authUserUnit.setModerationCount(2);
        authUserUnit.setSettings(true);
        authResponse.setAuthUserUnit(authUserUnit);
        return authResponse;
    }

    public RegisterResponse registerUser(RegisterParametersUnit parameters)
    {
        int maxNameLength = 50;
        int minPasswordLength = 6;
        RegisterResponse registerResponse = new RegisterResponse();
        if (userRepository.getAllEmails().contains(parameters.getEmail()))
        {
            registerResponse.setResult(false);
            registerResponse.setErrors("Этот e-mail уже зарегистрирован", parameters.getName(),
                                        parameters.getPassword(), parameters.getCaptcha());
        }
        else if (parameters.getName().length() > maxNameLength)
        {
            registerResponse.setResult(false);
            registerResponse.setErrors(parameters.getEmail(), "Имя указано неверно",
                                        parameters.getPassword(), parameters.getCaptcha());
        }
        else if (parameters.getPassword().length() < minPasswordLength)
        {
            registerResponse.setResult(false);
            registerResponse.setErrors(parameters.getEmail(), parameters.getName(),
                                "Пароль слишком короткий", parameters.getCaptcha());
        }
        else if (!getCaptchaTextBySecretCode(parameters.getCaptchaSecret()).equals(parameters.getCaptcha()))
        {
            registerResponse.setResult(false);
            registerResponse.setErrors(parameters.getEmail(), parameters.getName(),
                    parameters.getPassword(), "Код с картинки введен неверно");
        }
        else
        {
            User user = new User();
            user.setName(parameters.getName());
            user.setEmail(parameters.getEmail());
            user.setIsModerator((byte) 0);
            user.setPassword(parameters.getPassword());
            user.setRegTime(LocalDateTime.now());
            userRepository.save(user);
            registerResponse.setResult(true);
        }
        return registerResponse;
    }

    public AuthResponse loginUser(LoginParametersUnit loginParametersUnit)
    {
        AuthResponse loginResponse = new AuthResponse();

        return loginResponse;
    }

    public CaptchaResponse getCaptcha() throws IOException
    {
        CaptchaResponse captchaResponse= new CaptchaResponse();
        Cage cage = new GCage();
        String captchaText = generateCode(5);
        BufferedImage rawCaptchaImage = cage.drawImage(captchaText);
        BufferedImage resizeCaptchaImage = imageService.resizeImage(rawCaptchaImage, 100, 35);
        String encodedString = Base64.getEncoder().encodeToString(toByteArray(resizeCaptchaImage));
        String imageString = "data:image/png;base64, " + encodedString;
        String secret = generateCode(10);
        captchaResponse.setImage(imageString);
        captchaResponse.setSecret(secret);
        CaptchaCode captcha = new CaptchaCode();
        captcha.setCode(captchaText);
        captcha.setSecretCode(secret);
        captcha.setTime(LocalDateTime.now());
        captchaRepository.save(captcha);
        return captchaResponse;
    }

    public byte[] toByteArray(BufferedImage image) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream stream = new MemoryCacheImageOutputStream(baos);
        ImageIO.write(image, "jpg", stream);
        stream.close();
        return baos.toByteArray();
    }

    public String generateCode(int wordLength)
    {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordLength; i++)
        {
            char tmp = (char) ('1' + random.nextInt('9' - '0'));
            sb.append(tmp);
        }
        return sb.toString();
    }

    public String getCaptchaTextBySecretCode(String secretCode)
    {
        List<CaptchaCode> captchaCodeList = captchaRepository.findAll();
        String captchaText = "";
        for (CaptchaCode captchaCode : captchaCodeList)
        {
            if (captchaCode.getSecretCode().equals(secretCode))
            {
                captchaText = captchaCode.getCode();
            }
        }
        return captchaText;
    }
}
