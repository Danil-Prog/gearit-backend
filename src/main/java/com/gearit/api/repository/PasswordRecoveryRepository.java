package com.gearit.api.repository;

import com.gearit.api.entity.user.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Integer> {
}
