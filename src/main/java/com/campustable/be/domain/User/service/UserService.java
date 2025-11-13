package com.campustable.be.domain.User.service;

import com.campustable.be.domain.User.dto.UserRequest;
import com.campustable.be.domain.User.dto.UserResponse;
import com.campustable.be.domain.User.entity.User;
import com.campustable.be.domain.User.repository.UserRepository;
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
public class UserService {

  private final UserRepository userRepository;

  //관리자든 일반유저든 모두 가져옴
  public List<UserResponse> getAllUsers(){

    List<User> users = userRepository.findAll();

    return users.stream()
        .map(UserResponse::from)
        .toList();
  }

  public UserResponse getMyUserInfo(String studentNumber){

    User user = userRepository.findByStudentNumber(studentNumber)
        .orElseThrow(()->{
          log.error("토큰은 유효하지만 토큰에 해당하는 유저의학번이 db에존재하지않습니다. {}", studentNumber);
          throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    return UserResponse.from(user);
  }

  //관리자 만드는 전용으로 사용할 예정입니다.
  public UserResponse createUser(UserRequest userRequst){

    Optional<User> existingUser = userRepository.findByLoginId(userRequst.getUserId());

    if (existingUser.isPresent()) {
      log.error("관리자id가 이미 존재합니다. {}", userRequst.getUserId());
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    User user = UserRequest.toEntity(userRequst);
    return UserResponse.from(userRepository.save(user));
  }

  //관리자 업데이트 전용으로 사용할 예정입니다.
  public UserResponse updateUser(UserRequest userRequst){

    User existingUser = userRepository.findByLoginId(userRequst.getUserId())
        .orElseThrow(()->{
          log.error("유저가 존재하지않습니다. {}", userRequst.getUserId());
          throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    existingUser.update(userRequst);
    return UserResponse.from(userRepository.save(existingUser));
  }

  //관리자가 특정 유저의 id를 인자로받아 삭제하는 메서드
  public void deleteUserById(Long userId){
    User user = userRepository.findById(userId)
        .orElseThrow(()->{
          log.error("유저삭제 진행중 유저가 발견뒤지않았습니다. {}", userId);
          throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    userRepository.delete(user);
  }

  //관리자가 아닌 유저가 자기자신을 지우는메서드
  public void deleteUser(String studentNumber){
    User user = userRepository.findByStudentNumber(studentNumber)
        .orElseThrow(()->{
          log.error("토큰은 유효하지만 토큰에 해당하는 유저의학번이 db에존재하지않습니다. {}", studentNumber);
          throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });
    userRepository.delete(user);
  }

}
