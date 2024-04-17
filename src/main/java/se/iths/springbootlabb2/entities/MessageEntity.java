package se.iths.springbootlabb2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "message_entity")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;


    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;


    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "created_date",nullable = false, updatable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(name = "last_modified_date",nullable = false)
    @LastModifiedDate
    private Instant lastModifiedDate;

    @Column(name = "last_modified_by")
    @LastModifiedBy
    private String lastModifiedBy;

}


