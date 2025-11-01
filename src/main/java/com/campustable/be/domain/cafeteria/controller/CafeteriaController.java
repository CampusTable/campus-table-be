package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.dto.CafeteriaResponse;
import com.campustable.be.domain.cafeteria.service.CafeteriaService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
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
public class CafeteriaController implements CafeteriaControllerDocs{

  private final CafeteriaService cafeteriaService;

  @LogMonitoringInvocation
  @PostMapping("/admin/cafeterias")
  @Override
  public ResponseEntity<CafeteriaResponse> createCafeteria(@RequestBody CafeteriaRequest request) {
    CafeteriaResponse response = cafeteriaService.createCafeteria(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoringInvocation
  @GetMapping("/cafeterias/{code}")
  @Override
  public ResponseEntity<CafeteriaResponse> getCafeteriaByCode(@PathVariable String code){

    return ResponseEntity.ok().body(cafeteriaService.findCafeteriaByCode(code));
  }

  @LogMonitoringInvocation
  @PatchMapping("/admin/cafeterias/{code}")
  @Override
  public ResponseEntity<CafeteriaResponse> updateCafeteria(@PathVariable String code,
      @RequestBody CafeteriaRequest request) {
    CafeteriaResponse response = cafeteriaService.updateCafeteria(code, request);
    return ResponseEntity.ok(response);
  }

  @LogMonitoringInvocation
  @DeleteMapping("/admin/cafeterias/{code}")
  @Override
  public ResponseEntity<Void> deleteCafeteria(@PathVariable String code) {
    cafeteriaService.deleteCafeteria(code);
    return ResponseEntity.noContent().build();
  }

}