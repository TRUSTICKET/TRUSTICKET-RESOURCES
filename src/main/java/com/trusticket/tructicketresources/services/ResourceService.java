package com.trusticket.tructicketresources.services;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Service
public class ResourceService {

    private ClassPathResource staticResource = new ClassPathResource("static/file/");

    public String saveFile(MultipartFile file) throws IOException {


        String newFileName = generateUniqueFileName(file.getOriginalFilename());

        File uploadPath = staticResource.getFile();
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        Path destinationPath = Paths.get(uploadPath.getAbsolutePath(), newFileName);


        //서버 내부 스토리지에 업로드
        try {
            Files.copy(file.getInputStream(), destinationPath);
        } catch (IOException e) {
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
}
