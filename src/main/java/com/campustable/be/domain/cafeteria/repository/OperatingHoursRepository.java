package com.campustable.be.domain.cafeteria.repository;

import com.campustable.be.domain.cafeteria.entity.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {

  OperatingHours findByCafeteria_Code(String cafeteriaCode);
}