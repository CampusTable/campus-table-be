package com.campustable.be.domain.auth.service;

import com.campustable.be.domain.User.entity.User;
import com.campustable.be.domain.User.repository.UserRepository;
import com.campustable.be.domain.auth.security.CustomUserDetails;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String studentNumber) throws UsernameNotFoundException{
    User user = userRepository.findByStudentNumber(studentNumber)
        .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

    return new CustomUserDetails(user);
  }


}
