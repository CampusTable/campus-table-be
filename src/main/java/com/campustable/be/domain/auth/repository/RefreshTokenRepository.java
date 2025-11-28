package com.campustable.be.domain.auth.repository;

import com.campustable.be.domain.auth.entity.RefreshToken;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

  List<RefreshToken> findAllByUserId(Long userId);
}
