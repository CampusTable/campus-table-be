package com.campustable.be.domain.cafeteria.service;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import com.campustable.be.domain.cafeteria.repository.OperatingHoursRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OperatingHoursService {

  private final OperatingHoursRepository operatingHoursRepository;
  private final CafeteriaRepository cafeteriaRepository;

  public OperatingHoursResponse createOperatingHours(String code, OperatingHoursRequest request){
    String cafeteria_name = CafeteriaName.fromCode(code).getName();

    Cafeteria cafeteria = cafeteriaRepository.findByName(cafeteria_name)
        .orElseThrow(() -> new CustomException(ErrorCode.CAFETERIA_NOT_FOUND));
    return OperatingHoursResponse.from(operatingHoursRepository.save(request.toEntity(cafeteria)));
  }

  public OperatingHoursResponse getOperatingHours(String code){
    CafeteriaName cafeteriaEnum = CafeteriaName.fromCode(code);

    Cafeteria cafeteria = cafeteriaRepository.findByName(cafeteriaEnum.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.CAFETERIA_NOT_FOUND));

    return OperatingHoursResponse.from(operatingHoursRepository.findByCafeteria_CafeteriaId((cafeteria.getCafeteriaId())));

  }

  public OperatingHoursResponse updateOperatingHours(String code, OperatingHoursRequest request){
    CafeteriaName.fromCode(code);
    return null;
  }



}
