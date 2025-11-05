package com.campustable.be.domain.cafeteria.service;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import com.campustable.be.domain.cafeteria.repository.OperatingHoursRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    Optional<OperatingHours> existOperatingHours = operatingHoursRepository.findByCafeteria(cafeteria);
    if (existOperatingHours.isPresent()) {
      log.warn("createOperatingHours: 이미 OperatingHours가 존재합니다. 생성아아닌 수정을 통해 진행해주세요.");
      throw new CustomException(ErrorCode.OPERATING_HOURS_ALREADY_EXISTS);
    }

    OperatingHours operatingHours = operatingHoursRepository.save(
        request.toEntity(cafeteria));

    return OperatingHoursResponse.from(operatingHours);
  }

  @Transactional(readOnly = true)
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

  @Transactional(readOnly = true)
  public List<OperatingHoursResponse> getAllOperatingHours(){

    List<OperatingHours> operatingHoursList = operatingHoursRepository.findAll();

    return operatingHoursList.stream()
        .map(OperatingHoursResponse::from)
        .toList();
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
