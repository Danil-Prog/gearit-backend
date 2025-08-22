package com.gearit.api.repository;

import com.gearit.api.entity.user.UserProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProviderRepository extends JpaRepository<UserProvider, Long>, JpaSpecificationExecutor<UserProvider> {

    @Query(value = "SELECT user FROM UserProvider AS user WHERE user.email = :username")
    UserDetails findByUsername(String username);

    @Query(value = "SELECT user FROM UserProvider AS user WHERE user.email = :email")
    Optional<UserProvider> findByEmail(String email);

    @Query(value = "SELECT user FROM UserProvider AS user WHERE user.id = :id")
    Optional<UserProvider> findById(@NonNull Long id);
}
