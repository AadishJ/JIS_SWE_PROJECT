package com.jis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Case {

    @Id
    private String cin;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String defendantDetails;

    @Column(nullable = false)
    private String crimeType;

    @Column(columnDefinition = "TEXT")
    private String arrestInfo;

    @Column(columnDefinition = "TEXT")
    private String prosecutorDetails;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(columnDefinition = "TEXT")
    private String judgmentSummary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    private LocalDateTime createdAt;

    public enum Status {
        PENDING, CLOSED
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.PENDING;
        }
    }
}