package main.service;

import main.api.response.ProfileResponse;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EditProfileService
{
    private final UserRepository userRepository;
    private final ImageService imageService;

    public EditProfileService(UserRepository userRepository, ImageService imageService)
    {
        this.userRepository = userRepository;
        this.imageService = imageService;
    }
    public ProfileResponse editMyProfileWithPhoto(String name, String email, String password, int removePhoto,
                                                  MultipartFile photo, Principal principal)
    {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("Не найден такой пользователь"));
        currentUser.setName(name);
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        //MultipartFile resizePhoto = photo.
        String path = imageService.uploadImage(photo).getImagePath();
        currentUser.setPhoto(path);
        userRepository.save(currentUser);

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setPhoto(photo);
        profileResponse.setEmail(email);
        profileResponse.setName(name);
        profileResponse.setRemovePhoto(removePhoto);
        profileResponse.setPassword(password);

        return profileResponse;
    }


}
