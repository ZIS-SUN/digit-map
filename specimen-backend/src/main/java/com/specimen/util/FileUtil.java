package com.specimen.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传工具类
 */
@Component
public class FileUtil {

    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 保存文件
     */
    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }

        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        // 保存文件
        Path filePath = Paths.get(uploadPath, filename);
        Files.copy(file.getInputStream(), filePath);

        return filename;
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(String filename) {
        try {
            Path filePath = Paths.get(uploadPath, filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取文件访问URL
     */
    public String getFileUrl(String filename) {
        return "/uploads/specimens/" + filename;
    }
}
