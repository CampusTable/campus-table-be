package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import com.campustable.be.domain.cafeteria.service.OperatingHoursService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OperatingHoursController implements OperatingHoursControllerDocs {

  private final OperatingHoursService operatingHoursService;

  @LogMonitoringInvocation
  @Override
  @PostMapping("/admin/cafeterias/{id}/operating-hours")
  public ResponseEntity<OperatingHoursResponse> createOperatingHoursByCafeteriaId(@PathVariable Long id,
      @RequestBody @Valid OperatingHoursRequest request) {

    OperatingHoursResponse response = operatingHoursService.createOperatingHoursByCafeteriaId(request, id);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoringInvocation
  @Override
  @GetMapping("/cafeterias/{id}/operating-hours")
  public ResponseEntity<List<OperatingHoursResponse>> getOperatingHoursByCafeteriaId(@PathVariable Long id) {

    List<OperatingHoursResponse> responses = operatingHoursService.getOperatingHoursByCafeteriaId(id);

    return ResponseEntity.ok().body(responses);
  }

  @LogMonitoringInvocation
  @Override
  @GetMapping("/operating-hours")
  public ResponseEntity<List<OperatingHoursResponse>> getAllOperatingHours() {

    List<OperatingHoursResponse> responses = operatingHoursService.getAllOperatingHours();

    return ResponseEntity.ok().body(responses);
  }

  @LogMonitoringInvocation
  @Override
  @PatchMapping("/admin/cafeterias/{id}/operating-hours")
  public ResponseEntity<OperatingHoursResponse>  updateOperatingHours(@PathVariable Long id,
      @RequestBody OperatingHoursRequest request){

    OperatingHoursResponse response = operatingHoursService.updateOperatingHours(request, id);

    return ResponseEntity.ok().body(response);
  }

  @LogMonitoringInvocation
  @Override
  @DeleteMapping("/admin/cafeterias/{id}/operating-hours")
    public ResponseEntity<Void> deleteOperatingHours(@PathVariable Long id){

    operatingHoursService.deleteOperatingHours(id);

    return ResponseEntity.noContent().build();
  }

}
