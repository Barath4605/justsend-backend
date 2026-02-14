package com.justsend.justsendbackend.dtos;


public record GetTextDto(String text,
                         int totalDays,
                         com.justsend.justsendbackend.Entity.DataType type) {}
