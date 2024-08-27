package com.group6.swp391.pojos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;
//    private T ...
//
//    @Column(name = "create_at")
//    @CreationTimestamp    // create at the first time
//    private Date createAt;
//
//    @Column(name = "update_at")
//    @UpdateTimestamp              // auto update when change
//    private Date updateAt;

}
