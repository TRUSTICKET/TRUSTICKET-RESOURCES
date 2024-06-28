package com.trusticket.tructicketresources.api;

import com.trusticket.tructicketresources.services.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/api/v1/resource")
@AllArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("multipartFile") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("파일이 비어 있습니다.", HttpStatus.BAD_REQUEST);
        }

        String filePath;
        try {
            // 파일 저장 및 경로 반환
            filePath = resourceService.saveFile(file);
        } catch (IOException e) {
            return new ResponseEntity<>("파일 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(filePath, HttpStatus.OK);
    }


}