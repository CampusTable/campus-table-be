package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import com.campustable.be.domain.cafeteria.service.OperatingHoursService;
import jakarta.validation.Valid;
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
public class OperatingHoursController{

  private final OperatingHoursService operatingHoursService;

  @PostMapping("/admin/cafeterias/{id}/operating-hours")
  public ResponseEntity<OperatingHoursResponse> createOperatingHoursByCafeteriaId(@PathVariable Long id,
      @RequestBody @Valid OperatingHoursRequest request) {

    OperatingHoursResponse response = operatingHoursService.createOperatingHoursByCafeteriaId(request, id);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/cafeterias/{id}/operating-hours")
  public ResponseEntity<OperatingHoursResponse> getOperatingHoursByCafeteriaId(@PathVariable Long id) {

    OperatingHoursResponse responses = operatingHoursService.getOperatingHoursByCafeteriaId(id);

    return ResponseEntity.ok().body(responses);
  }

  @PatchMapping("/admin/cafeterias/{id}/operating-hours")
  public ResponseEntity<OperatingHoursResponse>  updateOperatingHoursByCafeteriaId(@PathVariable Long id,
      @RequestBody @Valid OperatingHoursRequest request){

    OperatingHoursResponse response = operatingHoursService.updateOperatingHoursByCafeteriaId(request, id);

    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/admin/cafeterias/{id}/operating-hours")
    public ResponseEntity<Void> deleteOperatingHoursByCafeteriaId(@PathVariable Long id){

    operatingHoursService.deleteOperatingHoursByCafeteriaId(id);

    return ResponseEntity.noContent().build();
  }

}
