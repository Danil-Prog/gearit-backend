package com.gearit.api.service;

import org.springframework.security.core.userdetails.*;

public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
