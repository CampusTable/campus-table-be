package com.campustable.be.domain.s3.service;

import com.campustable.be.domain.s3.util.FileUtil;
import com.campustable.be.global.common.CommonUtil;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

  private final S3Template s3Template;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;


  /**
   * 파일 검증 및 원본 파일명 반환
   *
   * @param file 요청된 MultipartFile
   * @return 원본 파일명
   */
  private static String validateAndExtractFilename(MultipartFile file) {
    // 파일 검증
    if (FileUtil.isNullOrEmpty(file)) {
      log.error("파일이 비어있거나 존재하지 않습니다.");
      throw new CustomException(ErrorCode.INVALID_FILE_REQUEST);
    }

    // 원본 파일 명 검증
    String originalFilename = file.getOriginalFilename();

    if (CommonUtil.nvl(originalFilename, "").isEmpty()) {
      log.error("원본 파일명이 비어있거나 존재하지 않습니다.");
      throw new CustomException(ErrorCode.INVALID_FILE_REQUEST);
    }
    return originalFilename;
  }

  public String uploadFile(MultipartFile file, String dirName) {

    String originalFilename = validateAndExtractFilename(file);

    String storedPath = dirName + "/" + UUID.randomUUID() + "_" + originalFilename;
    log.debug("생성된 파일명: {}", storedPath);

    try (InputStream inputStream = file.getInputStream()) {

      S3Resource resource = s3Template.upload(bucket, storedPath, inputStream);

      String s3Url = resource.getURL().toString();
      log.info("S3 업로드 성공: {}", s3Url);

      return s3Url;

    } catch (S3Exception e) {
      log.error("AmazonServiceException - S3 파일 업로드 실패. 버킷: {}, 파일명: {}, 에러: {}", bucket, storedPath, e.getMessage());
      throw new CustomException(ErrorCode.S3_UPLOAD_AMAZON_CLIENT_ERROR);
    } catch (IOException e) {
      log.error("IOException - 파일 스트림 처리 중 에러 발생. 원본 파일명: {}, 파일명: {} 에러: {}", originalFilename, storedPath, e.getMessage());
      throw new CustomException(ErrorCode.S3_UPLOAD_ERROR);
    }

  }

  public void deleteFile(String storedPath) {
    if (CommonUtil.nvl(storedPath, "").isEmpty()) {
      log.warn("요청된 파일 경로가 없습니다.");
      return;
    }

    try {

      String key = extractKeyFromUrl(storedPath);

      s3Template.deleteObject(bucket, key);
      log.info("S3 파일 삭제 성공: {}", storedPath);

    } catch (S3Exception e) {
      log.error("S3Exception - S3 파일 삭제 실패. 버킷: {}, 파일명: {}, 에러: {}", bucket, storedPath, e.getMessage());
      throw new CustomException(ErrorCode.S3_DELETE_AMAZON_SERVICE_ERROR);
    } catch (RuntimeException e) {
      // 기존 AmazonClientException 역할 (네트워크 등 클라이언트 에러)
      log.error("RuntimeException - S3 파일 삭제 실패. 버킷: {}, 파일명: {}, 에러: {}", bucket, storedPath, e.getMessage());
      throw new CustomException(ErrorCode.S3_DELETE_AMAZON_CLIENT_ERROR);
    } catch (Exception e) {
      log.error("S3 파일 삭제 실패. 버킷: {}, 파일명: {}, 에러: {}", bucket, storedPath, e.getMessage());
      throw new CustomException(ErrorCode.S3_DELETE_ERROR);

    }
  }

  private String extractKeyFromUrl(String fileUrl) {
    if (fileUrl.contains(".com/")) {
      String key = fileUrl.substring(fileUrl.lastIndexOf(".com/") + 5);
      log.info("추출된 S3 Key: [{}]", key);
      return key;
    }
    return fileUrl;
  }


}
