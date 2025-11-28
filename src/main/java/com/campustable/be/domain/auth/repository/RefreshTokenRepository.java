package com.campustable.be.domain.auth.repository;

import com.campustable.be.domain.auth.entity.RefreshToken;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

  /**
 * Finds all refresh tokens belonging to the specified user.
 *
 * @param userId the ID of the user whose refresh tokens should be retrieved
 * @return a list of RefreshToken entities associated with the given userId; may be empty if none exist
 */
List<RefreshToken> findAllByUserId(Long userId);
}