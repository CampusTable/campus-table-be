package com.campustable.be.domain.cafeteria.service;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.CafeteriaCodeEnum;
import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import com.campustable.be.domain.cafeteria.repository.OperatingHoursRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
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
    if (CafeteriaCodeEnum.fromName(code) == null){
      log.warn("Invalid cafeteria code: {}", code);
      throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
    }
    Cafeteria cafeteria = cafeteriaRepository.findByCode(code)
        .orElseThrow(() -> new CustomException(ErrorCode.CAFETERIA_NOT_FOUND));
    return OperatingHoursResponse.from(operatingHoursRepository.save(request.toEntity(cafeteria)));
  }

  public OperatingHoursResponse getOperatingHours(String code){
    CafeteriaCodeEnum cafeteriaEnum = CafeteriaCodeEnum.fromName(code);

    Cafeteria cafeteria = cafeteriaRepository.findByCode(cafeteriaEnum.getCode())
        .orElseThrow(() -> new CustomException(ErrorCode.CAFETERIA_NOT_FOUND));

    return OperatingHoursResponse.from(operatingHoursRepository.findByCafeteria_Code(code));

  }

  public OperatingHoursResponse updateOperatingHours(String code, OperatingHoursRequest request){
    if (CafeteriaCodeEnum.fromName(code) == null){
      throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
    }
    return null;
  }



}
