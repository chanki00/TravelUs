package com.DB_PASSWORD_REDACTED.trip.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/upload")
@Slf4j
public class FileUploadController {
    
    // application.properties에서 설정할 수 있도록 함
    @Value("${file.upload.path:/uploads/images/}")
    private String uploadPath;
    
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 파일 검증
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", "파일이 비어있습니다."
                ));
            }
            
            // 파일 크기 검증 (5MB 제한)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", "파일 크기는 5MB를 초과할 수 없습니다."
                ));
            }
            
            // 파일 타입 검증
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", "이미지 파일만 업로드 가능합니다."
                ));
            }
            
            // 업로드 디렉토리 생성
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 파일명 생성 (UUID + 원본 확장자)
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;
            
            // 파일 저장
            Path filePath = Paths.get(uploadPath, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // 웹에서 접근 가능한 URL 생성
            String fileUrl = "/uploads/images/" + filename;
            
            log.info("파일 업로드 성공: {}", fileUrl);
            
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "fileUrl", fileUrl,
                "filename", filename,
                "originalName", originalFilename,
                "size", file.getSize()
            ));
            
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "파일 업로드 중 오류가 발생했습니다."
            ));
        }
    }
}
