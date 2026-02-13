package com.justsend.justsendbackend.Service;

import com.justsend.justsendbackend.Entity.DataEntity;
import com.justsend.justsendbackend.Entity.DataType;
import com.justsend.justsendbackend.Repository.DataRepository;
import com.justsend.justsendbackend.dtos.ImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final DataRepository dataRepository;
    private final S3Client s3Client;

    public ImageResponseDto imageUpload(MultipartFile file) throws IOException {

        if(file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Image cannot be greater than 5 MB");
        }
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new RuntimeException("Only image files allowed");
        }

        String code = generateCode();

        String key = "images/" + code;
        String fileUrl = "https://justsend-images-prod.s3.ap-south-1.amazonaws.com/" + key;

        DataEntity entity = new DataEntity();
        entity.setCode(code.substring(0,6));
        entity.setType(DataType.IMAGE);
        entity.setFileUrl(fileUrl);
        entity.setFileKey(key);
        entity.setCreatedAt(Instant.now());
        entity.setExpiresAt(Instant.now().plus(Duration.ofDays(1)));
        entity.setSizeBytes(file.getSize());

        dataRepository.save(entity);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket("justsend-images-prod")
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return new ImageResponseDto(code.substring(0,6) , entity.getExpiresAt(), fileUrl);
    }

    private String generateCode() {
        return UUID.randomUUID()
                    .toString();
    }

}
