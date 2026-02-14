package com.justsend.justsendbackend.Service;

import com.justsend.justsendbackend.Entity.DataEntity;
import com.justsend.justsendbackend.Entity.DataType;
import com.justsend.justsendbackend.Repository.DataRepository;
import com.justsend.justsendbackend.dtos.GetTextDto;
import com.justsend.justsendbackend.dtos.PostTextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SenderService {

    private final DataRepository dataRepository;

    public PostTextDto save(GetTextDto getTextDto) {
        DataEntity dataEntity = new DataEntity();

        int ttl = getTextDto.totalDays();

        if(ttl < 1 || ttl > 30) ttl = 3;

        dataEntity.setCode(code());
        dataEntity.setTextData(getTextDto.text());
        dataEntity.setCreatedAt(Instant.now());
        dataEntity.setExpiresAt(Instant.now().plus(ttl, ChronoUnit.DAYS));
        dataEntity.setSizeBytes(getTextDto.text().getBytes(StandardCharsets.UTF_8).length);
        dataEntity.setType(DataType.TEXT);


        dataRepository.save(dataEntity);

        return new PostTextDto(
                dataEntity.getCode(),
                dataEntity.getExpiresAt(),
                dataEntity.getType()
        );
    }

//    ================ HELPER FUNCTIONS ================

    public String code() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6);
    }
}
