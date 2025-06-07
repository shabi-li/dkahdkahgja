package com.example.controller;
import com.aliyun.oss.OSS;
import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired; // ✅ 添加此行导入
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.example.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件相关操作接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private OSS ossClient;  // OSS客户端Bean

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.baseUrl}")
    private String ossBaseUrl;

    /**
     * 文件上传（改为阿里云OSS）
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        // 生成唯一文件名（时间戳+原文件名）
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        try {
            // 直接上传文件流到OSS
            ossClient.putObject(bucketName, fileName, file.getInputStream());
            // 生成OSS文件访问URL（baseUrl + 文件名）
            String url = ossBaseUrl + "/" + fileName;
            return Result.success(url);
        } catch (IOException e) {
            throw new CustomException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 文件下载（OSS文件可直接通过URL访问，此方法可删除或改为生成签名URL）
     */
    // @GetMapping("/download/{fileName}")
    // public void download(@PathVariable String fileName, HttpServletResponse response) {
    //     // 原本地下载逻辑可删除，或根据需要保留签名下载逻辑
    // }
}
