package com.group6.swp391.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @Column(name = "create_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private ZonedDateTime createAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "update_at")
    private ZonedDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        createAt = ZonedDateTime.now(zoneId);
        updateAt = ZonedDateTime.now(zoneId);
    }

    @PreUpdate
    protected void onUpdate() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        updateAt = ZonedDateTime.now(zoneId);
    }
}
