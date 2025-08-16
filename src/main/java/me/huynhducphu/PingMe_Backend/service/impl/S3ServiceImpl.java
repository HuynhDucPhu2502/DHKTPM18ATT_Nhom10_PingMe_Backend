package me.huynhducphu.PingMe_Backend.service.impl;

import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.advice.exception.S3UploadException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

/**
 * Admin 8/16/2025
 **/
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements me.huynhducphu.PingMe_Backend.service.S3Service {

    private final S3Client s3Client;

    private final String awsBucketName;
    private final String awsRegion;

    @Override
    public String uploadFile(
            MultipartFile file, String key,
            boolean getUrl, int maxFileSize
    ) {
        try {
            if (file == null || file.isEmpty())
                throw new S3UploadException(
                        "Tệp được gửi lên bị rỗng",
                        HttpStatus.BAD_REQUEST
                );

            if (file.getSize() > maxFileSize)
                throw new S3UploadException(
                        "Tệp quá lớn (> " + maxFileSize + " bytes)",
                        HttpStatus.PAYLOAD_TOO_LARGE
                );

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(awsBucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

            if (getUrl)
                return String.format("https://%s.s3.%s.amazonaws.com/%s", awsBucketName, awsRegion, key);
            else return key;
        } catch (Exception e) {
            throw new S3UploadException(
                    "Không tải được dữ liệu tệp",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public String uploadFile(
            MultipartFile file, String folder,
            String fileName, boolean getUrl,
            int maxFileSize
    ) {
        String key = String.format("%s/%s", folder, fileName);
        return uploadFile(file, key, getUrl, maxFileSize);
    }

    @Override
    public void deleteFileByKey(String key) {
        try {
            if (key == null)
                throw new S3UploadException(
                        "Không tìm thấy tệp cần xóa",
                        HttpStatus.NOT_FOUND
                );

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(awsBucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);

        } catch (Exception e) {
            throw new S3UploadException(
                    "Không thể xóa tệp",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public void deleteFileByUrl(String url) {
        String base = String.format("https://%s.s3.%s.amazonaws.com/", awsBucketName, awsRegion);
        if (!url.startsWith(base)) {
            throw new S3UploadException(
                    "URL không hợp lệ",
                    HttpStatus.BAD_REQUEST
            );
        }

        String key = url.substring(base.length());

        deleteFileByKey(key);
    }
}
