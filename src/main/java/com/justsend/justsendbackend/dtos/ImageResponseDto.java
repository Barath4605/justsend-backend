package com.justsend.justsendbackend.dtos;

import java.time.Instant;

public record ImageResponseDto(String code, Instant expiryAt, String fileUrl) {}
