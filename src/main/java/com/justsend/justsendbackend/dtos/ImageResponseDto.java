package com.justsend.justsendbackend.dtos;

import com.justsend.justsendbackend.Entity.DataType;

import java.time.Instant;

public record ImageResponseDto(String code,
                               Instant expiryAt,
                               String fileUrl,
                               DataType type) {}

