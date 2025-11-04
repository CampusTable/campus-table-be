package com.campustable.be.domain.cafeteria.service;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import com.campustable.be.domain.cafeteria.repository.OperatingHoursRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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

  public OperatingHoursResponse createOperatingHoursByCafeteriaId(OperatingHoursRequest request, Long id){

    Cafeteria cafeteria = cafeteriaRepository.findById(id)
        .orElseThrow(()->{
          log.warn("createOperatingHours: 유효하지않은 cafe id");
          throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    OperatingHours operatingHours = operatingHoursRepository.save(
        request.toEntity(cafeteria));

    return OperatingHoursResponse.from(operatingHours);
  }

  public OperatingHoursResponse getOperatingHoursByCafeteriaId(Long id){

    Cafeteria cafeteria = cafeteriaRepository.findById(id)
        .orElseThrow(()->{
          log.warn("getOperatingHours: 유효하지않은 cafe id");
          throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    Optional<OperatingHours> operatingHours = operatingHoursRepository.findByCafeteria(cafeteria);
    if (operatingHours.isEmpty()) {
      return new OperatingHoursResponse(id);
    }
    return OperatingHoursResponse.from(operatingHours.get());
  }

  public List<OperatingHoursResponse> getAllOperatingHours(){

    List<OperatingHours> operatingHoursList = operatingHoursRepository.findAll();

    List<OperatingHoursResponse> response = operatingHoursList.stream()
        .map(OperatingHoursResponse::from)
        .toList();

    return response;
  }

  public OperatingHoursResponse updateOperatingHoursByCafeteriaId(OperatingHoursRequest request, Long id){

    Cafeteria cafeteria = cafeteriaRepository.findById(id)
        .orElseThrow(()->{
          log.warn("updateOperatingHours: 유효하지않은 cafe id");
          throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    Optional<OperatingHours> operatingHours = operatingHoursRepository.findByCafeteria(cafeteria);

    if (operatingHours.isEmpty()){
      OperatingHours newOperatingHours = request.toEntity(cafeteria);
      newOperatingHours =  operatingHoursRepository.save(newOperatingHours);
      return OperatingHoursResponse.from(newOperatingHours);
    }

    operatingHours.get().update(request);
    return OperatingHoursResponse.from(operatingHours.get());
  }

  public void deleteOperatingHoursByCafeteriaId(Long id){

    Cafeteria cafeteria = cafeteriaRepository.findById(id)
        .orElseThrow(()->{
          log.warn("deleteOperatingHours: 유효하지않은 cafe id");
          throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    Optional<OperatingHours> operatingHours = operatingHoursRepository.findByCafeteria(cafeteria);
    if (operatingHours.isEmpty()){
      log.warn("deleteOperatingHoursByCafeteriaId: 식당 ID {}에 삭제할 운영 시간 기록이 없습니다.", id);
    }
    else {
      operatingHoursRepository.delete(operatingHours.get());
    }

  }



}
