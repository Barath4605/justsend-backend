package com.justsend.justsendbackend.Service;

import com.justsend.justsendbackend.Entity.DataEntity;
import com.justsend.justsendbackend.Entity.DataType;
import com.justsend.justsendbackend.Repository.DataRepository;
import com.justsend.justsendbackend.dtos.GetSenderDto;
import com.justsend.justsendbackend.dtos.PostSenderDto;
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

    public PostSenderDto save(GetSenderDto getSenderDto) {
        DataEntity dataEntity = new DataEntity();

        int ttl = getSenderDto.totalDays();

        if(ttl < 1 || ttl > 30) ttl = 3;

        dataEntity.setCode(code());
        dataEntity.setTextData(getSenderDto.text());
        dataEntity.setCreatedAt(Instant.now());
        dataEntity.setExpiresAt(Instant.now().plus(ttl, ChronoUnit.DAYS));
        dataEntity.setSizeBytes(getSenderDto.text().getBytes(StandardCharsets.UTF_8).length);
        dataEntity.setType(DataType.TEXT);


        dataRepository.save(dataEntity);

        return new PostSenderDto(
                dataEntity.getCode(),
                dataEntity.getExpiresAt()
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
