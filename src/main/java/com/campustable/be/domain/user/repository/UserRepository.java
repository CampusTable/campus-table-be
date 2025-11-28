package com.campustable.be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.campustable.be.domain.user.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
 * Finds a User by their student number.
 *
 * @param studentNumber the student's unique identifier
 * @return an Optional containing the matching User if present, or Optional.empty() if none found
 */
Optional<User> findByStudentNumber(String studentNumber);

  /**
 * Finds a User by their login identifier.
 *
 * @param loginId the login identifier of the user
 * @return an Optional containing the User if found, or Optional.empty() if no matching user exists
 */
Optional<User> findByLoginId(String loginId);
}
