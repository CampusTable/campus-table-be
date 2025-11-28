package com.campustable.be.domain.auth.service;

import com.campustable.be.domain.user.entity.User;
import com.campustable.be.domain.user.repository.UserRepository;
import com.campustable.be.domain.auth.security.CustomUserDetails;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * Load user details using a user ID provided as a string.
   *
   * @param idString the user ID as a string; expected to be a numeric value parseable to a Long
   * @return a {@link UserDetails} instance for the located user
   * @throws UsernameNotFoundException if {@code idString} is not a valid numeric ID
   * @throws CustomException with {@code ErrorCode.USER_NOT_FOUND} if no user exists for the parsed ID
   */
  @Override
  public UserDetails loadUserByUsername(String idString) throws UsernameNotFoundException {
    try {
      Long id = Long.valueOf(idString);

      User user = userRepository.findById(id)
          .orElseThrow(() -> {
            log.error("유저를 발견할수없습니다 {}",id);
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
          });

      return new CustomUserDetails(user);

    } catch (NumberFormatException e) {
      throw new UsernameNotFoundException("Invalid User ID format.");
    }
  }


}