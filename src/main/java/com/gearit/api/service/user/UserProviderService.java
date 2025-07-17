package com.gearit.api.service.user;

import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.BadRequestException;
import com.gearit.api.repository.UserProviderRepository;
import com.gearit.api.utils.UserProviderValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProviderService {

    private final UserProviderRepository userProviderRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserProviderService(
            UserProviderRepository userProviderRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userProviderRepository = userProviderRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserProvider getUserProviderByEmailOrNull(String email) {
        return userProviderRepository.findByEmail(email).orElse(null);
    }

    public UserProvider getUserProviderByEmailOrThrow(String email) {
        return userProviderRepository.findByEmail(email).orElseThrow(() ->
                new BadRequestException("User with this email address was not found.")
        );
    }

    public UserProvider getUserProviderById(Long id) {
        return userProviderRepository.findById(id).orElse(null);
    }

    public UserProvider createUserProvider(UserProvider userProvider) {
        UserProviderValidator.validateUserProvider(userProvider);

        if (userProvider.getPassword() != null && !userProvider.getPassword().isEmpty()) {
            userProvider.setPassword(bCryptPasswordEncoder.encode(userProvider.getPassword()));
        }

        return userProviderRepository.save(userProvider);
    }

    public void updateUserProvider(UserProvider userProvider) {
        UserProviderValidator.validateUserProvider(userProvider);
        userProviderRepository.save(userProvider);
    }
}

