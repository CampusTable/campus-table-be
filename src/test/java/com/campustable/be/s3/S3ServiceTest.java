package com.campustable.be.s3;

import com.campustable.be.domain.s3.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile; // 가짜 파일 만드는 도구

import java.io.IOException;

@SpringBootTest
class S3ServiceTest {

  @Autowired
  private S3Service s3Service;

  @Test
  @DisplayName("S3 이미지 업로드 테스트")
  void uploadTest() throws IOException {
    // 1. 가짜 이미지 파일 생성 (이름, 원래이름, 타입, 내용)
    // MockMultipartFile은 스프링 테스트에서 제공하는 '가짜 파일'입니다.
    MockMultipartFile fakeImage = new MockMultipartFile(
        "image",                        // 필드명
        "test-image.jpg",               // 파일명
        "image/jpeg",                   // 파일 타입
        "Hello S3".getBytes()           // 파일 내용 (바이트)
    );

    // 2. 업로드 실행!
    String url = s3Service.uploadFile(fakeImage);

    // 3. 결과 출력
    System.out.println("========================================");
    System.out.println("🎉 업로드 성공!");
    System.out.println("📍 이미지 주소: " + url);
    System.out.println("========================================");
  }
}