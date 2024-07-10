package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_inquiry_id")
    private int id;

    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(300)")
    private String fullName;

    @Column(name = "phone", nullable = false, length = 10)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", nullable = false, columnDefinition = "NVARCHAR(300)")
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    @Column(name = "note", columnDefinition = "NVARCHAR(300)")
    private String note;

    @Column(name = "status", nullable = false, columnDefinition = "NVARCHAR(300)")
    private String status;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }
}
