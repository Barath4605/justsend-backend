package com.justsend.justsendbackend.dtos;

import java.time.Instant;

public record PostSenderDto(String code,
                            Instant expiryAt) {}
