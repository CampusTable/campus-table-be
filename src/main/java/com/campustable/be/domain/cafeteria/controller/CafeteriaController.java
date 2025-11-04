package com.campustable.be.domain.cafeteria.controller;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.dto.CafeteriaResponse;
import com.campustable.be.domain.cafeteria.service.CafeteriaService;
import com.campustable.be.global.aop.LogMonitoringInvocation;
import java.nio.file.Path;
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
  @GetMapping("/cafeterias/{id}")
  @Override
  public ResponseEntity<CafeteriaResponse> getCafeteria(@PathVariable Long id){

    return ResponseEntity.ok().body(cafeteriaService.findCafeteria(id));
  }

  @LogMonitoringInvocation
  @GetMapping("/cafeterias")
  @Override
  public ResponseEntity<List<CafeteriaResponse>> getAllCafeteria(){

    return ResponseEntity.ok().body(cafeteriaService.findAllCafeteria());
  }

  @LogMonitoringInvocation
  @PatchMapping("/admin/cafeterias/{id}")
  @Override
  public ResponseEntity<CafeteriaResponse> updateCafeteria(
      @RequestBody CafeteriaRequest request, @PathVariable Long id) {
    CafeteriaResponse response = cafeteriaService.updateCafeteria(request, id);
    return ResponseEntity.ok(response);
  }

  @LogMonitoringInvocation
  @DeleteMapping("/admin/cafeterias/{id}")
  @Override
  public ResponseEntity<Void> deleteCafeteria(@PathVariable Long id) {
    cafeteriaService.deleteCafeteria(id);
    return ResponseEntity.noContent().build();
  }

}