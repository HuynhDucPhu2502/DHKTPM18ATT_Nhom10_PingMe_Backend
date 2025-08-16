package me.huynhducphu.PingMe_Backend.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Admin 8/16/2025
 **/
public interface S3Service {
    String uploadFile(
            MultipartFile file, String key,
            boolean getUrl, int maxFileSize
    );

    String uploadFile(
            MultipartFile file, String folder,
            String fileName, boolean getUrl,
            int maxFileSize
    );

    void deleteFileByKey(String key);

    void deleteFileByUrl(String url);
}
