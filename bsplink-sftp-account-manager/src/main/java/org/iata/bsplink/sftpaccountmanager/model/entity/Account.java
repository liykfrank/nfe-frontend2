package org.iata.bsplink.sftpaccountmanager.model.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Account {

    @Id
    @NonNull
    @Column(nullable = false)
    private String login;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountMode mode;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ENABLED;

    @Column(nullable = false, updatable = false)
    private Instant creationTime;

    @Column(nullable = false)
    private Instant updatedTime;

    // length is based on a RSA public key of 16384 bytes
    @Column(length = 3000)
    private String publicKey;

    @Column(length = 60)
    private String password;

    /**
     * Sets the account's creation and update time.
     */
    @PrePersist
    public void onPrePersist() {

        creationTime = Instant.now();
        updatedTime = creationTime;
    }

    /**
     * Sets the account's update time.
     */
    @PreUpdate
    public void onPreUpdate() {

        updatedTime = Instant.now();
    }
}
