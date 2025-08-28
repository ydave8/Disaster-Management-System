package com.dms.dms_backend.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alert {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  @Column(length=2000) private String message;
  private String area;

  @Enumerated(EnumType.STRING)
  private Severity severity; // LOW, MEDIUM, HIGH, CRITICAL

  private Instant expiresAt;
  private Instant createdAt;
  private Instant updatedAt;

  @PrePersist void prePersist(){ createdAt=Instant.now(); updatedAt=createdAt; }
  @PreUpdate  void preUpdate(){ updatedAt=Instant.now(); }

  public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
}
