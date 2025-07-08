package com.gearit.api.service.user;

import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.repository.UserProviderRepository;
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

    public UserProvider findUserProviderByEmailOrNull(String email) {
        return userProviderRepository.findByEmail(email).orElse(null);
    }

    public UserProvider findUserProviderByLoginOrEmailOrNull(String login, String email) {
        return userProviderRepository.findByUsernameOrEmail(login, email).orElse(null);
    }

    public void createUserProvider(UserProvider userProvider) {
        if (userProvider.getPassword() != null && !userProvider.getPassword().isEmpty()) {
            userProvider.setPassword(bCryptPasswordEncoder.encode(userProvider.getPassword()));
        }
        userProviderRepository.save(userProvider);
    }


}

