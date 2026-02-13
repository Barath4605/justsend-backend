package com.justsend.justsendbackend.Controller;

import com.justsend.justsendbackend.Entity.DataEntity;
import com.justsend.justsendbackend.Entity.DataType;
import com.justsend.justsendbackend.Repository.DataRepository;
import com.justsend.justsendbackend.Service.GeneratePresignedUrl;
import com.justsend.justsendbackend.Service.ImageUploadService;
import com.justsend.justsendbackend.Service.SenderService;
import com.justsend.justsendbackend.dtos.GetSenderDto;
import com.justsend.justsendbackend.dtos.ImageResponseDto;
import com.justsend.justsendbackend.dtos.PostSenderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class DropController {

    private final SenderService senderService;
    private final ImageUploadService uploadService;
    private final DataRepository dataRepository;
    private final GeneratePresignedUrl generatePresignedUrl;

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
            return new ResponseEntity<>(new GetSenderDto(data.getTextData(), 0), HttpStatus.OK);
        }
    }

    @PostMapping("image")
    public ResponseEntity<ImageResponseDto> uploadImage(@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(uploadService.imageUpload(file));
    }

    @GetMapping("/image/{code}")
    ResponseEntity<ImageResponseDto> getImage(@PathVariable String code) {
        Optional<DataEntity> opt = dataRepository.findByCode(code);

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DataEntity data = opt.get();

        if (data.getType() != DataType.IMAGE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (Instant.now().isAfter(data.getExpiresAt())) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }

        return ResponseEntity.ok(
                new ImageResponseDto(
                        data.getCode(),
                        data.getExpiresAt(),
                        generatePresignedUrl.generatePresignedUrl(data.getFileKey())

                )
        );
    }
}
