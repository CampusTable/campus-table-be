package com.campustable.be.domain.user.service;

import com.campustable.be.domain.user.dto.UserRequest;
import com.campustable.be.domain.user.dto.UserResponse;
import com.campustable.be.domain.user.entity.User;
import com.campustable.be.domain.user.repository.UserRepository;
import com.campustable.be.domain.auth.dto.AuthResponse;
import com.campustable.be.domain.auth.entity.RefreshToken;
import com.campustable.be.domain.auth.provider.JwtProvider;
import com.campustable.be.domain.auth.repository.RefreshTokenRepository;
import com.campustable.be.domain.auth.service.AuthService;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthService authService;

  //관리자든 일반유저든 모두 가져옴
  public List<UserResponse> getAllUsers(){

    List<User> users = userRepository.findAll();

    return users.stream()
        .map(UserResponse::from)
        .toList();
  }

  public UserResponse getMyUserInfo(Long userId){

    User user = userRepository.findById(userId)
        .orElseThrow(()->{
          log.error("토큰은 유효하지만 토큰에 해당하는 유저의학번이 db에존재하지않습니다. "
                    + "토큰삭제를 진행합니다.{}", userId);

          throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    return UserResponse.from(user);
  }

  public AuthResponse createUser(UserRequest userRequest){

    Optional<User> existingUser = userRepository.findByLoginId(userRequest.getLoginId());

    if (existingUser.isPresent()){
      throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
    }

    User newUser = User.builder()
        .loginId(userRequest.getLoginId())
        .role(userRequest.getRole())
        .build();

    User user = userRepository.save(newUser);
    String refreshTokenId = UUID.randomUUID().toString();

    String accessToken = jwtProvider.createAccessToken(user);
    String refreshToken = jwtProvider.createRefreshToken(user, refreshTokenId);

    RefreshToken refresh = authService.setRefreshToken(refreshTokenId, user.getUserId());
    refreshTokenRepository.save(refresh);

    return AuthResponse.builder()
        .isNewUser(true)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .maxAge(jwtProvider.getRefreshInMs()/1000)
        .build();

  }

  public UserResponse updateUser(UserRequest userRequest){

    Long userId = Long.valueOf(SecurityContextHolder.getContext()
        .getAuthentication().getName());

    User existingUser = userRepository.findById(userId)
        .orElseThrow(()->{
          log.error("유저가 존재하지않습니다. {}", userId);
          throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    existingUser.update(userRequest);
    return UserResponse.from(userRepository.save(existingUser));
  }

  //관리자가 아닌 유저가 자기자신을 지우는메서드
  public void deleteUser(Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(()->{
          log.error("토큰은 유효하지만 토큰에 해당하는 userId:{}가 db에 존재하지 않습니다.", userId);
          throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    List<RefreshToken> tokensToDelete = refreshTokenRepository.findAllByUserId(userId);
    refreshTokenRepository.deleteAll(tokensToDelete);
    userRepository.delete(user);
  }
}
