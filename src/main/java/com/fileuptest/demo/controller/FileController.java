package com.fileuptest.demo.controller;

import com.fileuptest.demo.result.RestResult;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileController {

    //文件的保存路径
    private final static String FILE_BASE_PATH = "/data/file/html";

    //单文件/多文件上传页面
    @GetMapping("/upload")
    public String upload(ModelMap modelMap) {
        return "image/upload";
    }

    //处理单文件/多文件上传
    @PostMapping("/uploaded")
    @ResponseBody
    public RestResult uploaded(HttpServletRequest request) {

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        System.out.println("是否有上传文件:"+isMultipart);
        if (isMultipart == false) {
            return RestResult.error(1,"表单中不含文件");
        }

        List<MultipartFile> list = ((MultipartHttpServletRequest)request).getFiles("files");
        if (list.isEmpty()) {
            return RestResult.error(1,"文件不存在");
        }
        for (MultipartFile multipartFile : list) {
            if (list.isEmpty()) {
                continue;
            }
            // 文件名
            String fileName = multipartFile.getOriginalFilename();
            System.out.println("文件名： " + fileName);

            // 文件后缀
            int lastDot = fileName.lastIndexOf(".");
            lastDot++;
            String fileType = fileName.substring(lastDot);
                // 重新生成唯一文件名，用于存储数据库
                String fileSn = UUID.randomUUID().toString();
                Map<String, String> map2 = new HashMap<String, String>();
                String destFilePath = FILE_BASE_PATH+"/"+fileSn+"."+fileType;
                File dest = new File(destFilePath);
                try {
                    multipartFile.transferTo(dest);
                } catch (IOException e) {
                    System.out.println("save ioexception");
                    e.printStackTrace();
                    return RestResult.error(1,"上传失败");
                }
        }
        return RestResult.success(0,"上传成功");
    }
}

