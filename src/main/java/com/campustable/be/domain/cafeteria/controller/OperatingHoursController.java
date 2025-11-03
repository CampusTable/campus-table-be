package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import com.campustable.be.domain.cafeteria.service.OperatingHoursService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OperatingHoursController implements OperatingHoursControllerDocs {

  private final OperatingHoursService operatingHoursService;

  @Override
  @PostMapping("/admin/cafeterias/{code}/operating-hours")
  public ResponseEntity<OperatingHoursResponse> createOperatingHours(@PathVariable String code,
      @RequestBody OperatingHoursRequest request) {
    OperatingHoursResponse response = operatingHoursService.createOperatingHours(code, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/cafeterias/{code}/operating-hours")
  public ResponseEntity<OperatingHoursResponse> getOperatingHours(@PathVariable String code) {
    OperatingHoursResponse responses = operatingHoursService.getOperatingHours(code);
    return ResponseEntity.ok().body(responses);
  }

  @PutMapping("/admin/cafeterias/{code}/operating-hours")
  public ResponseEntity<List<OperatingHoursResponse>>  updateOperatingHours(@PathVariable String code,
      @RequestBody OperatingHoursRequest request){
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/admin/cafeterias/{code}/operating-hours")
    public ResponseEntity<Void> deleteOperatingHours(@PathVariable String code){

    return ResponseEntity.noContent().build();
  }

}
