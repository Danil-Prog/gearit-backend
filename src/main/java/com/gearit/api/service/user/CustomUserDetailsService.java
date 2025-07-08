package com.gearit.api.service.user;

import com.gearit.api.repository.*;
import org.hibernate.internal.build.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

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
