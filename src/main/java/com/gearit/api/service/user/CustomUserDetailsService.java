package com.gearit.api.service.user;

import com.gearit.api.repository.UserProviderRepository;
import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserProviderRepository userProviderRepository;

    @AllowNonPortable
    public CustomUserDetailsService(UserProviderRepository userProviderRepository) {
        this.userProviderRepository = userProviderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userProviderRepository.findByUsername(username);
    }
}
