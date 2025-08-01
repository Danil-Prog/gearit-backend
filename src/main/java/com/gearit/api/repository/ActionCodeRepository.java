package com.gearit.api.repository;

import com.gearit.api.entity.actioncode.ActionCode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ActionCodeRepository extends JpaRepository<ActionCode, Long> {

    @Query(value = "SELECT actionCode FROM ActionCode AS actionCode WHERE actionCode.code = :code")
    Optional<ActionCode> findByCode(String code);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ActionCode AS actionCode WHERE actionCode.code = :code")
    void deleteByCode(String code);

    @Query(value = "SELECT actionCode FROM ActionCode AS actionCode WHERE actionCode.expiresAt <= :now")
    List<ActionCode> findAllExpires(Instant now);
}
