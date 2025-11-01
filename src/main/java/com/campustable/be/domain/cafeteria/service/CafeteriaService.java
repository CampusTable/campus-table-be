package com.campustable.be.domain.cafeteria.service;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.dto.CafeteriaResponse;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.CafeteriaName;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;


@Service
@RequiredArgsConstructor
@Transactional
public class CafeteriaService {

  private final CafeteriaRepository cafeteriaRepository;

  public CafeteriaResponse createCafeteria(CafeteriaRequest cafeteriaRequest){

    //유효하지않은 식당이름 (Enum에 정의되지않음)
    CafeteriaName cafeteriaName = CafeteriaName.fromName(cafeteriaRequest.getName());

    if (cafeteriaRepository.findByName(cafeteriaRequest.getName()).isPresent()) {
      throw new CustomException(ErrorCode.CAFETERIA_ALREADY_EXISTS);
    }
    Cafeteria cafeteria = cafeteriaRequest.toEntity(cafeteriaRequest);
    return CafeteriaResponse.from(cafeteriaRepository.save(cafeteria));
  }

  @Transactional(readOnly = true)
  public CafeteriaResponse findCafeteriaByCode(String code){

    CafeteriaName cafeteriaName = CafeteriaName.fromCode(code);

    Cafeteria cafeteria = cafeteriaRepository.findByName(cafeteriaName.getName())
        .orElseThrow(()-> new CustomException(ErrorCode.CAFETERIA_NOT_FOUND));

    return CafeteriaResponse.from(cafeteria);
  }

  public CafeteriaResponse updateCafeteria(String code, CafeteriaRequest request){

    CafeteriaName cafeteriaName = CafeteriaName.fromCode(code);

    Cafeteria existingCafeteria = cafeteriaRepository.findByName(cafeteriaName.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.CAFETERIA_NOT_FOUND));
    existingCafeteria.update(request);
    return CafeteriaResponse.from(cafeteriaRepository.save(existingCafeteria));
  }

  public void deleteCafeteria(@PathVariable String code){

    //enum에 code(key)가 존재하는지 확인하고 존재하지않는경우 에러를 발생시킨다.
    CafeteriaName cafeteriaName = CafeteriaName.fromCode(code);

    Cafeteria existringCafeteria = cafeteriaRepository.findByName(cafeteriaName.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.CAFETERIA_NOT_FOUND));
    cafeteriaRepository.delete(existringCafeteria);
  }
}
