package com.gearit.api.repository;

import com.gearit.api.entity.user.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Integer> {

    @Query(value = "SELECT passwordRecovery FROM PasswordRecovery AS passwordRecovery WHERE passwordRecovery.code = :code")
    Optional<PasswordRecovery> findByCode(String code);
}
