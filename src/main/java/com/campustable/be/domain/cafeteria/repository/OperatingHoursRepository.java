package com.campustable.be.domain.cafeteria.repository;

import com.campustable.be.domain.cafeteria.entity.Cafeteria;
import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {

  Optional<OperatingHours> findByCafeteria(Cafeteria cafeteria);
}