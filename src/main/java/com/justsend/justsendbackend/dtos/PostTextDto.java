package com.justsend.justsendbackend.dtos;

import com.justsend.justsendbackend.Entity.DataType;

import java.time.Instant;

public record PostTextDto(String code,
                          Instant expiryAt,
                          DataType type) {}
