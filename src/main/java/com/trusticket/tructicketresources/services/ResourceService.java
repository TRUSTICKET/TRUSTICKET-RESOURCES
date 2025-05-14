package com.trusticket.tructicketresources.services;

import com.trusticket.tructicketresources.dto.FileDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Service
@Slf4j
public class ResourceService {

    @Value("${spring.resources-dir}")
    private String uploadDirectory;

    public String saveFile(MultipartFile file) throws IOException {

        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉터리 생성
        }

        String originalFileName = file.getOriginalFilename();
        String mimeType = file.getContentType();

        //저장 파일명을 중복방지 고유명으로 변경
        String newFileName = generateUniqueFileName(originalFileName);
        Path filePath = Paths.get(uploadDirectory + File.separator + newFileName);

        //서버 내부 스토리지에 업로드
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            log.error(e.getMessage() + " " + e.getStackTrace().toString());
            throw new FileUploadException("파일 업로드 중 오류가 발생했습니다.");
        }
        return newFileName;
    }

    private String generateUniqueFileName(String originalFileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        // Random 객체 생성
        Random random = new Random();
        // 0 이상 100 미만의 랜덤한 정수 반환
        String randomNumber = Integer.toString(random.nextInt(Integer.MAX_VALUE));
        String timeStamp = dateFormat.format(new Date());
        return timeStamp + randomNumber + originalFileName;
    }

    public FileDto getImageFile(String filename) throws IOException{
        try {
            Path path = Paths.get(uploadDirectory + File.separator + filename);
            byte[] imageBytes = Files.readAllBytes(path);
            String mimeType = Files.probeContentType(path);
            return new FileDto(imageBytes, mimeType);
        } catch (IOException e) {
            throw new FileNotFoundException("파일 다운로드 중 오류가 발생했습니다.");
        }
    }


}
