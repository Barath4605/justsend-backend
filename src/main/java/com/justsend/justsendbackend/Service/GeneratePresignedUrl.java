package com.justsend.justsendbackend.Service;

import com.justsend.justsendbackend.Repository.DataRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
public class GeneratePresignedUrl {

    private final S3Presigner presigner;
    private final S3Client s3Client;
    private final DataRepository dataRepository;

    public GeneratePresignedUrl(S3Presigner presigner, S3Client s3Client, DataRepository dataRepository) {
        this.presigner = presigner;
        this.s3Client = s3Client;
        this.dataRepository = dataRepository;
    }

    public String generatePresignedUrl(String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("justsend-images-prod")
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();

        PresignedGetObjectRequest presignedRequest =
                presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }
}
