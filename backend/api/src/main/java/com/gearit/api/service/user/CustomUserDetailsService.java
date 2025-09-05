package com.gearit.api.service.user;

import com.gearit.api.repository.UserProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserProviderRepository userProviderRepository;

    @Autowired
    public CustomUserDetailsService(UserProviderRepository userProviderRepository) {
        this.userProviderRepository = userProviderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userProviderRepository.findByEmail(email).orElse(null);
    }
}
