package com.campustable.be.domain.cafeteria.service;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.dto.CafeteriaResponse;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.exception.CafeteriaAlreadyExistsException;
import com.campustable.be.domain.cafeteria.exception.CafeteriaNotFoundException;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;


@Service
@RequiredArgsConstructor
@Transactional
public class CafeteriaService {

  private final CafeteriaRepository cafeteriaRepository;

  @Transactional(readOnly = true)
  public CafeteriaResponse findCafeteriaByCode(String code){
    Cafeteria cafeteria = cafeteriaRepository.findByCode(code)
        .orElseThrow(()-> new CafeteriaNotFoundException());

    return CafeteriaResponse.from(cafeteria);
  }

  public CafeteriaResponse createCafeteria(CafeteriaRequest cafeteriaRequest){
    if (cafeteriaRepository.findByCode(cafeteriaRequest.getCode()).isPresent()) {
      throw new CafeteriaAlreadyExistsException();
    }
    Cafeteria cafeteria = toEntity(cafeteriaRequest);
    return CafeteriaResponse.from(cafeteriaRepository.save(cafeteria));
  }

  public CafeteriaResponse updateCafeteria(String code, CafeteriaRequest request){
    Cafeteria existingCafeteria = cafeteriaRepository.findByCode(code)
        .orElseThrow(() -> new CafeteriaNotFoundException());
    existingCafeteria.update(request);
    return CafeteriaResponse.from(cafeteriaRepository.save(existingCafeteria));
  }

  public void deleteCafeteria(@PathVariable String code){
    Cafeteria existringCafeteria = cafeteriaRepository.findByCode(code)
        .orElseThrow(() -> new CafeteriaNotFoundException());
    cafeteriaRepository.delete(existringCafeteria);
  }

  private Cafeteria toEntity(CafeteriaRequest request) {
    // Cafeteria 엔티티에 Builder 패턴이 구현되어 있다고 가정합니다.
    // DTO에서 엔티티로 변환할 때는 ID 필드 제외하고 나머지 필드만 사용합니다.
    return Cafeteria.builder()
        .code(request.getCode())
        .name(request.getName())
        .address(request.getAddress())
        .description(request.getDescription())
        .build();
  }
}
