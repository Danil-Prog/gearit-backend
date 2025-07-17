package com.gearit.api.repository;

import com.gearit.api.entity.user.ConfirmCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, Long> {

    @Query(value = "SELECT confirmCode FROM ConfirmCode AS confirmCode WHERE confirmCode.code = :code")
    Optional<ConfirmCode> findByCode(String code);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ConfirmCode AS confirmCode WHERE confirmCode.code = :code")
    void deleteByCode(String code);
}
