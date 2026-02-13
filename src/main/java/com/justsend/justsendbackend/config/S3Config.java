package com.justsend.justsendbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {

        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(
                        System.getenv("AWS_ACCESS_KEY_ID"),
                        System.getenv("AWS_SECRET_ACCESS_KEY")
                );

        return S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials)
                )
                .build();
    }
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}
