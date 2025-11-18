package com.campustable.be.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  /**
   * 커스텀 예외 처리
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    log.error("CustomException 발생: {}", e.getMessage(), e);

    ErrorCode errorCode = e.getErrorCode();

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(errorCode)
        .errorMessage(errorCode.getMessage())
        .build();

    return ResponseEntity.status(errorCode.getStatus()).body(response);
  }

  /**
   * JSON 역직렬화 (Enum, LocalTime 등) 오류 처리 핸들러
   * - 클라이언트가 Enum 필드에 잘못된 문자열 값을 전송했을 때 발생
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleJsonParsingException(HttpMessageNotReadableException e) {
    log.error("JSON Parsing Error 발생: {}", e.getMessage(), e);

    // 예외의 근본 원인(Root Cause)을 확인하여 Enum 오류인지 판단
    Throwable rootCause = e.getRootCause();

    if (rootCause instanceof InvalidFormatException) {
      InvalidFormatException ife = (InvalidFormatException) rootCause;

      // 🚨 DayOfWeekEnum 관련 오류인지 확인 (클래스 이름을 포함하는지 검사)
      if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {

        String allowedValues = Arrays.stream(ife.getTargetType().getEnumConstants())
            .map(Object::toString) // 각 Enum 상수를 문자열로 변환
            .collect(Collectors.joining(", ")); // ⬅️ 쉼표로 연결

        String fieldName = ife.getPath().get(0).getFieldName();

        String customMessage = String.format("'%s' 필드의 값이 유효하지 않습니다. 허용된 값: [%s]", fieldName, allowedValues);

        ErrorResponse response = ErrorResponse.builder()
            .errorCode(ErrorCode.INVALID_INPUT_VALUE) // 400 Bad Request에 해당하는 커스텀 에러 코드
            .errorMessage(customMessage)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
    }

    // 위의 Enum/LocalTime 오류가 아닌 단순한 JSON 문법 오류일 경우, 400 Bad Request로 처리
    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INVALID_REQUEST) // 별도의 400 코드를 사용할 수 있음
        .errorMessage("요청 본문(JSON)의 형식이 올바르지 않거나 필드 값이 잘못되었습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }


  /**
   * 그 외 예외 처리
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Unhandled Exception 발생: {}", e.getMessage(), e);

    // 예상치 못한 에러 => 500
    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
        .errorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
