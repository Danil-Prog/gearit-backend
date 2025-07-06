package com.gearit.api.service;

import com.gearit.api.repository.*;
import org.hibernate.internal.build.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @AllowNonPortable
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
