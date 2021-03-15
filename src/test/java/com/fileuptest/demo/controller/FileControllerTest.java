package com.fileuptest.demo.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
@AutoConfigureMockMvc
@SpringBootTest
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("上传文件成功")
    void uploaded() throws Exception {
        //文件之外的参数
        String key1 = "这是第一个文件的说明";
        String key2 = "这是第二个文件的说明";

        File file2 = new File("/data/image/2.jpg");
        MockMultipartFile mFile2 = new MockMultipartFile("files", "2.jpg",
                MediaType.TEXT_PLAIN_VALUE, new FileInputStream(file2));

        File file3 = new File("/data/image/3.jpg");
        MockMultipartFile mFile3 = new MockMultipartFile("files", "3.jpg",
                MediaType.TEXT_PLAIN_VALUE, new FileInputStream(file3));

        MvcResult mvcResult = mockMvc.perform(multipart("/file/uploaded")
                .file(mFile2)
                .file(mFile3)
                .param("key1", key1)
                .param("key2", key2))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
        int code = JsonPath.parse(content).read("$.code");
        assertThat(code, equalTo(0));
    }

    @Test
    @DisplayName("上传文件失败")
    void uploadedFail() throws Exception {
        //文件之外的参数
        String key1 = "这是第一个文件的说明";
        String key2 = "这是第二个文件的说明";

        MvcResult mvcResult = mockMvc.perform(post("/file/uploaded")
                .param("key1", key1)
                .param("key2", key2))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
        int code = JsonPath.parse(content).read("$.code");
        assertThat(code, equalTo(1));
    }
}