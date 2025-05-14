package com.trusticket.tructicketresources.api;

import com.trusticket.tructicketresources.dto.FileDto;
import com.trusticket.tructicketresources.services.ResourceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@RestController
@RequestMapping("/api/v1/resource")
@AllArgsConstructor
@Slf4j
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("multipartFile") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("파일이 비어 있습니다.", HttpStatus.BAD_REQUEST);
        }

        String name;
        try {
            // 파일 저장 및 경로 반환
            name = resourceService.saveFile(file);
        } catch (IOException e) {
            log.error(e.getMessage() + " " + e.getStackTrace().toString());
            return new ResponseEntity<>("파일 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(name, HttpStatus.OK);
    }


    @GetMapping("/{name}")
    public ResponseEntity<byte[]> getImage(@PathVariable String name) throws IOException {

        // 이미지 파일을 바이트 배열로 읽어옴
        FileDto result = this.resourceService.getImageFile(name);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(result.getMimeType()))  // 이미지 타입에 맞게 설정
                .body(result.getBytes());

    }


}