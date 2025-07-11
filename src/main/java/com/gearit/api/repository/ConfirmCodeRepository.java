package com.gearit.api.repository;

import com.gearit.api.entity.user.ConfirmCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, Long> {

    @Query(value = "SELECT confirmCode FROM ConfirmCode AS confirmCode where confirmCode.code = :code")
    Optional<ConfirmCode> findByCode(String code);
}
