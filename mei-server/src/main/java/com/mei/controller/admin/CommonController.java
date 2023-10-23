package com.mei.controller.admin;

import com.mei.constant.MessageConstant;
import com.mei.result.Result;
import com.mei.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 08:58
 **/
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @ApiOperation("文件上传接口")
    @PostMapping("upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传: {}", file.getName());
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String name = UUID.randomUUID() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), name);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
