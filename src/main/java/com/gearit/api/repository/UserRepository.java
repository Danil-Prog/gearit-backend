package com.gearit.api.repository;

import com.gearit.api.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Repository
public interface UserRepository extends JpaRepository<UserProvider, Long> {

    @Query(value = "SELECT user FROM UserProvider AS user WHERE user.username == :username")
    UserDetails findByUsername(String username);
}
