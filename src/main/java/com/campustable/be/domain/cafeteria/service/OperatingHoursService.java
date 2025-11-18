package com.campustable.be.domain.cafeteria.service;

import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.campustable.be.domain.cafeteria.dto.OperatingHoursResponse;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.DayOfWeekEnum;
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

    DayOfWeekEnum dayOfWeek = request.getDayOfWeek();

    Optional<OperatingHours> existOperatingHours = operatingHoursRepository.findByCafeteriaAndDayOfWeek(cafeteria, dayOfWeek);
    if (existOperatingHours.isPresent()) {
      log.warn("createOperatingHours: 이미 해당요일의 OperatingHours가 존재합니다. 생성아아닌 수정을 통해 진행해주세요.");
      throw new CustomException(ErrorCode.OPERATING_HOURS_ALREADY_EXISTS);
    }

    OperatingHours operatingHours = operatingHoursRepository.save(
        request.toEntity(cafeteria));

    return OperatingHoursResponse.from(operatingHours);
  }

  @Transactional(readOnly = true)
  public List<OperatingHoursResponse> getOperatingHoursByCafeteriaId(Long id){

    Cafeteria cafeteria = cafeteriaRepository.findById(id)
        .orElseThrow(()->{
          log.warn("getOperatingHours: 유효하지않은 cafe id");
          throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    List<OperatingHours> operatingHours = operatingHoursRepository.findByCafeteria(cafeteria);


    return operatingHours.stream()
        .map(OperatingHoursResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<OperatingHoursResponse> getAllOperatingHours(){

    List<OperatingHours> operatingHoursList = operatingHoursRepository.findAll();

    return operatingHoursList.stream()
        .map(OperatingHoursResponse::from)
        .toList();
  }

  public OperatingHoursResponse updateOperatingHours(OperatingHoursRequest request, Long id){

    Optional<OperatingHours> operatingHours = operatingHoursRepository.findById(id);
    if (operatingHours.isEmpty()){
      log.error("operatingHoursId not found {}", id);
      throw new CustomException(ErrorCode.OPERATING_HOURS_NOT_FOUND);
    }

    operatingHours.get().update(request);
    return OperatingHoursResponse.from(operatingHours.get());
  }

  public void deleteOperatingHours(Long id){

    Optional<OperatingHours> operatingHours = operatingHoursRepository.findById(id);
    if (operatingHours.isEmpty()){
      log.warn("operatingHoursId : {} 가 존재하지않습니다.", id);
    }
    else {
      operatingHoursRepository.delete(operatingHours.get());
    }
  }
}
