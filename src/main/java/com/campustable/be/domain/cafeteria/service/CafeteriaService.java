package com.campustable.be.domain.cafeteria.service;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.dto.CafeteriaResponse;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.repository.CafeteriaRepository;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CafeteriaService {

  private final CafeteriaRepository cafeteriaRepository;

  public CafeteriaResponse createCafeteria(CafeteriaRequest request){

    if (cafeteriaRepository.findByName(request.getName()).isPresent()){
      log.error("이름이 중복되는 식당이 이미 존재합니다.");
      throw new CustomException(ErrorCode.CAFETERIA_ALREADY_EXISTS);
    }

    Cafeteria cafeteria = request.toEntity(request);
    return CafeteriaResponse.from(cafeteriaRepository.save(cafeteria));
  }

  @Transactional(readOnly = true)
  public CafeteriaResponse findCafeteria(Long id){

    Optional<Cafeteria> cafeteria = cafeteriaRepository.findById(id);
    if (cafeteria.isEmpty()) {
      log.error("id에 해당하는 식당이 존재하지 않습니다.");
      throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
    }

    return CafeteriaResponse.from(cafeteria.get());
  }

  @Transactional(readOnly = true)
  public List<CafeteriaResponse> findAllCafeteria(){

    List<Cafeteria> cafeteria = cafeteriaRepository.findAll();

    return cafeteria.stream()
        .map(CafeteriaResponse::from)
        .toList();
  }

  public CafeteriaResponse updateCafeteria(CafeteriaRequest request, Long id){

    Optional<Cafeteria> cafeteria = cafeteriaRepository.findById(id);

    if (cafeteria.isEmpty()){
      log.error("updateCafeteria: 요청body의 id:{}가 식당테이블에 존재하지않습니다.", id);
      throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
    }

    cafeteria.get().update(request);

    return CafeteriaResponse.from(cafeteriaRepository.save(cafeteria.get()));
  }

  public void deleteCafeteria(Long id){

    Optional<Cafeteria> cafeteria = cafeteriaRepository.findById(id);
    if (cafeteria.isEmpty()){
      log.error("deleteCafeteria: 요청id:{}가 식당테이블에 존재하지않습니다.",id);
      throw new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
    }

    cafeteriaRepository.delete(cafeteria.get());
  }
}
