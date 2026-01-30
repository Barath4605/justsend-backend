package com.justsend.justsendbackend.Controller;

import com.justsend.justsendbackend.Entity.DataEntity;
import com.justsend.justsendbackend.Repository.DataRepository;
import com.justsend.justsendbackend.Service.SenderService;
import com.justsend.justsendbackend.dtos.GetSenderDto;
import com.justsend.justsendbackend.dtos.PostSenderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class DropController {

    private final SenderService senderService;
    private final DataRepository dataRepository;

    @PostMapping("send")
    public ResponseEntity<PostSenderDto> save(@RequestBody GetSenderDto getSenderDto) {
        return ResponseEntity.ok(senderService.save(getSenderDto));
    }

    @GetMapping("/{code}")
    public ResponseEntity<GetSenderDto> get(@PathVariable String code) {

        Optional<DataEntity> opt = dataRepository.findByCode(code);

        if(opt.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        DataEntity data = opt.get();

        if(Instant.now().isAfter(data.getExpiresAt())) {
            return  ResponseEntity.status(HttpStatus.GONE).build();
        } else {
            return new ResponseEntity<>(new GetSenderDto(data.getTextData()), HttpStatus.OK);
        }

    }

}
