package com.capstone.Carvedream.global.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {

    // AmazonS3Client -> AmazonS3
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile -> File 전환 후 S3 버킷에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    // URL 이미지 다운로드 -> S3 버킷에 업로드
    public String uploadFromUrl(String imageUrl, String dirName) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int contentLength = connection.getContentLength();
        InputStream inputStream = connection.getInputStream();

        String fileName = dirName + "/" + System.currentTimeMillis() + getFileExtension(connection.getContentType());

        // S3에 업로드
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength); // 콘텐츠 길이 설정
            metadata.setContentType(connection.getContentType()); // MIME type 설정
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } finally {
            inputStream.close();
            connection.disconnect();
        }

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 파일 확장자 추출 (콘텐트 타입에 기반)
    private String getFileExtension(String contentType) {
        if (contentType.contains("image/jpeg")) {
            return ".jpg";
        } else if (contentType.contains("image/png")) {
            return ".png";
        } else if (contentType.contains("image/gif")) {
            return ".gif";
        }
        return ""; // 기본값 또는 알 수 없는 타입
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)    // PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

}
