package com.gearit.api.entity.comment;

import com.gearit.api.entity.user.UserProvider;
import com.gearit.common.constants.TableNames;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Data;

@Data
@Entity
@Table(name = TableNames.COMMENTS)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_provider_id", referencedColumnName = "id")
    private UserProvider userProvider;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "text", nullable = false)
    private String text;
}
