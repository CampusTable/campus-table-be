package com.campustable.be.domain.cafeteria.repository;

import com.campustable.be.domain.cafeteria.dto.CafeteriaRequest;
import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CafeteriaRepository extends JpaRepository<Cafeteria, Long> {

  Optional<Cafeteria> findByCode(String code);
}
