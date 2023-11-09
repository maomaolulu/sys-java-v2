//package com.ruoyi.admin;
//
//import com.mongodb.client.result.DeleteResult;
//import com.ruoyi.admin.TestAutoEntity;
//import com.ruoyi.admin.TestEntity;
//import io.minio.GetPresignedObjectUrlArgs;
//import io.minio.MinioClient;
//import io.minio.errors.*;
//import io.minio.http.Method;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//
///**
// * @Description
// * @Date 2023/5/8 13:50
// * @Author maoly
// **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class MongoTest {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private MinioClient minioClient;
//
//    @Test
//    public void testMongoQuery(){
//        Query query = new Query((Criteria.where("_id").is("64548435592b000096004e1e")));
//        TestEntity value = mongoTemplate.findOne(query, TestEntity.class);
//        System.out.println(value);
//    }
//
//    @Test
//    public void testMongoAdd(){
//        TestEntity test = new TestEntity();
//        test.setId("11111111");
//        test.setName("测试");
//        TestEntity insert = mongoTemplate.insert(test);
//        System.out.println(insert);
//    }
//
//    @Test
//    public void testMongoDelete(){
//        TestEntity test = new TestEntity();
//        test.setId("11111111");
//        test.setName("测试");
//        DeleteResult result = mongoTemplate.remove(test);
//        System.out.println("========================");
//        System.out.println(result.wasAcknowledged());
//        System.out.println(result.getDeletedCount());
//        System.out.println(result);
//    }
//
//    @Test
//    public void testMongoAuto(){
//        TestEntity test = new TestEntity();
//        test.setId("11111111");
//        test.setName("测试");
//        TestAutoEntity testAutoEntity = new TestAutoEntity();
//        testAutoEntity.setTestEntity(test);
//        testAutoEntity.setRemark("测试一下自动建表");
//        mongoTemplate.insert(testAutoEntity);
//    }
//
//    @Test
//    public void getUrl() throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        String url =  minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket("liangyuan").object("compangLY2023/05/15上海量远用户管理.pdf").method(Method.GET).build());
//
//        System.out.println(url);
//    }
//}
