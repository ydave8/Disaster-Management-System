/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dms.dms_backend.repo;
import com.dms.dms_backend.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;


public interface IncidentRepository extends JpaRepository<Alert, Long> {
  List<Alert> findByAreaAndExpiresAtAfter(String area, Instant now);
}
