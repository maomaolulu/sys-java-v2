//package com.ruoyi.admin;
//
//
//
//import io.minio.GetPresignedObjectUrlArgs;
//import io.minio.MinioClient;
//import io.minio.errors.*;
//import io.minio.http.Method;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.concurrent.TimeUnit;
//
///**
// * @Description
// * @Date 2023/5/8 13:50
// * @Author maoly
// **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class FileTest {
//
//    @Autowired
//    private MinioClient minioClient;
//
//    @Test
//    public void getFile() throws IOException {
//        Resource resource = new ClassPathResource("export/demo.docx");
//        InputStream inputStream = resource.getInputStream();
//        System.out.println(inputStream);
//        System.out.println(resource.getFile().exists());
//    }
//
//    @Test
//    public void getFile2() throws IOException, ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket("personnel").object("2023/05/24微信图片_20230404172941.png").method(Method.GET).expiry(60, TimeUnit.MINUTES).build());
//        System.out.println(url);
//    }
//}
