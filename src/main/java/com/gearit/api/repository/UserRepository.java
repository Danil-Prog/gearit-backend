package com.gearit.api.repository;

import com.gearit.api.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface UserRepository extends JpaRepository<UserProvider, Long> {
}
