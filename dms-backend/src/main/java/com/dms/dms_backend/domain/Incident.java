/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dms.dms_backend.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Incident {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String type;            // e.g., FIRE, FLOOD
  @Column(length=2000) private String description;
  private double lat;
  private double lng;

  @Enumerated(EnumType.STRING)
  private Status status;          // OPEN, ACK, CLOSED

  private Instant createdAt;

  @PrePersist void pre(){ createdAt = Instant.now(); }

  public enum Status { OPEN, ACK, CLOSED }
}
