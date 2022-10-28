package com.exapmle.aliyun.controller;


import com.example.common_utils.entity.R;
import com.exapmle.aliyun.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class OssController {
    @Autowired
    private OssService ossService;

    //上传头像的方法
    @PostMapping("fileUpload")
    public R uploadOssFile(MultipartFile file) {
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        return R.success(url);
    }
}
