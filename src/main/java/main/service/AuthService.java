package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.ChangePasswordResponse;
import main.api.response.RegisterResponse;
import main.api.unit.*;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService
{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final ImageService imageService;
    private final AuthenticationManager authenticationManager;
    private final MailSenderService mailSenderService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public AuthService(PostRepository postRepository, UserRepository userRepository, CaptchaRepository captchaRepository,
                       ImageService imageService, AuthenticationManager authenticationManager,
                       MailSenderService mailSenderService)
    {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
        this.imageService = imageService;
        this.authenticationManager = authenticationManager;
        this.mailSenderService = mailSenderService;
    }
    public AuthResponse checkUserAuth(Principal principal)
    {
        if (principal == null)
        {
            return new AuthResponse();
        }
        return getLoginResponse(principal.getName());
    }

    public RegisterResponse registerUser(RegisterParametersUnit parameters)
    {
        int maxNameLength = 50;
        int minPasswordLength = 6;
        RegisterResponse registerResponse = new RegisterResponse();
        if (userRepository.getAllEmails().contains(parameters.getEmail()))
        {
            registerResponse.setResult(false);
            registerResponse.setErrors("Этот e-mail уже зарегистрирован", "", "", "");
        }
        else if (parameters.getName().length() > maxNameLength)
        {
            registerResponse.setResult(false);
            registerResponse.setErrors("", "Имя указано неверно", "", "");
        }
        else if (parameters.getPassword().length() < minPasswordLength)
        {
            registerResponse.setResult(false);
            registerResponse.setErrors("", "", "Пароль слишком короткий", "");
        }
        else if (!getCaptchaTextBySecretCode(parameters.getCaptchaSecret()).equals(parameters.getCaptcha()))
        {
            registerResponse.setResult(false);
            registerResponse.setErrors("", "", "", "Код с картинки введен неверно");
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
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginParametersUnit.getEmail(), loginParametersUnit.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        return getLoginResponse(user.getUsername());
    }

    private AuthResponse getLoginResponse(String email)
    {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        AuthUserUnit authUserUnit = new AuthUserUnit();
        authUserUnit.setId(currentUser.getId());
        authUserUnit.setName(currentUser.getName());
        authUserUnit.setPhoto(currentUser.getPhoto());
        authUserUnit.setEmail(currentUser.getEmail());
        authUserUnit.setModeration(currentUser.getIsModerator() == 1);
        authUserUnit.setModerationCount(0);
        authUserUnit.setSettings(false);

        AuthResponse loginResponse = new AuthResponse();
        loginResponse.setResult(true);
        loginResponse.setAuthUserUnit(authUserUnit);
        return loginResponse;
    }

    public CaptchaResponse getCaptcha() throws IOException
    {
        String captchaText = generateCode(5);
        String secret = generateCode(10);

        String base64Captcha = createBase64Captcha(captchaText);

        saveCaptcha(captchaText, secret);

        return getCaptchaResponse(secret, base64Captcha);
    }

    private String createBase64Captcha(String captchaText) throws IOException
    {
        Cage cage = new Cage();
        BufferedImage rawCaptchaImage = cage.drawImage(captchaText);
        BufferedImage resizeCaptchaImage = imageService.resizeImage(rawCaptchaImage, 100, 35);
        String encodedString = Base64.getEncoder().encodeToString(toByteArray(resizeCaptchaImage));
        return "data:image/png;base64, " + encodedString;
    }

    private void saveCaptcha(String captchaText, String secret)
    {
        CaptchaCode captcha = new CaptchaCode();
        captcha.setCode(captchaText);
        captcha.setSecretCode(secret);
        captcha.setTime(LocalDateTime.now());
        captchaRepository.save(captcha);
    }

    private CaptchaResponse getCaptchaResponse(String secret, String base64Captcha)
    {
        CaptchaResponse captchaResponse= new CaptchaResponse();
        captchaResponse.setImage(base64Captcha);
        captchaResponse.setSecret(secret);
        return captchaResponse;
    }

    public byte[] toByteArray(BufferedImage image)
    {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
            ImageOutputStream stream = new MemoryCacheImageOutputStream(baos);
            ImageIO.write(image, "jpg", stream);
            stream.close();
            return baos.toByteArray();
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Не удалось конвертировать изображение", ex);
        }
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

    public boolean restorePassword(RestorePasswordParameterUnit parameterUnit)
    {
        if (!userRepository.getAllEmails().contains(parameterUnit.getEmail()))
        {
            return false;
        }
        User user = userRepository.findByEmail(parameterUnit.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("Не найден пользователь"));
        user.setCode(UUID.randomUUID().toString());
        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Developers Stories. Please visit next link: " +
                        "http://localhost:8080/login/change-password/%s",
                user.getName(),
                user.getCode()
        );
        userRepository.save(user);
        mailSenderService.sendMail(parameterUnit.getEmail(), "Activation code", message);

        return true;
    }

    public ChangePasswordResponse changePassword(ChangePasswordParameterUnit passwordParameter)
    {
        ChangePasswordResponse passwordResponse = new ChangePasswordResponse();
        User user = getUserByCode(passwordParameter.getCode());

        if (user.getName() == null)
        {
            passwordResponse.setResult(false);
            String errorMessage = "Ссылка для восстановления пароля устарела. \n" +
                    "<a href= \n" +
                    "\"/auth/restore\">Запросить ссылку снова</a>";
            passwordResponse.setErrors(errorMessage, "", "");
        }
        else if (passwordParameter.getPassword().length() < 6)
        {
            passwordResponse.setResult(false);
            passwordResponse.setErrors("", "Пароль короче 6 символов", "");
        }
        else if (!passwordParameter.getCaptcha()
                .equals(getCaptchaTextBySecretCode(passwordParameter.getCaptchaSecret())))
        {
            passwordResponse.setResult(false);
            passwordResponse.setErrors("", "", "Код с картинки введен неверно");
        }
        else {
            user.setPassword(passwordEncoder.encode(passwordParameter.getPassword()));
            userRepository.save(user);
            passwordResponse.setResult(true);
        }

        return passwordResponse;
    }

    private User getUserByCode(String code)
    {
        List<User> users = userRepository.findAll();
        User userByCode = new User();
        for (User user : users)
        {
            if (user.getCode() == null)
            {
                continue;
            }
            if (user.getCode().equals(code))
                userByCode = user;
        }
        return userByCode;
    }



}
