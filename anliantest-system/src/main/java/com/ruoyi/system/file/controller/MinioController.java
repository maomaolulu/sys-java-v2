package com.ruoyi.system.file.controller;

import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.system.file.domain.Res;
import com.ruoyi.system.file.domain.SysAttachment;
import com.ruoyi.system.file.domain.SysStaticImg;
import com.ruoyi.system.file.service.SysAttachmentService;
import com.ruoyi.system.file.service.SysStaticImgService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文件服务
 *
 * @menu 文件服务
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class MinioController {
    @Resource
    private MinioClient minioClient;

    private final SysAttachmentService attachmentService;
    private final SysStaticImgService staticImgService;


    @Autowired
    public MinioController(SysAttachmentService attachmentService, SysStaticImgService staticImgService) {
        this.attachmentService = attachmentService;
        this.staticImgService = staticImgService;
    }

    @Value("${minio.endpoint}")
    private String minio_url;


    /**
     * 临时文件转有效文件
     *
     * @return
     */
    @PostMapping("/transform")
    public int transform() {
        return attachmentService.transform();
    }


    /**
     * 获取文件列表
     *
     * @param bucketName
     * @return
     */
    @GetMapping("/list")
    public List<SysAttachment> getList(@RequestParam("bucketName") String bucketName) {
        List<SysAttachment> list = attachmentService.getList(bucketName);
        for (SysAttachment attachment : list) {
            attachment.setPreUrl(getFileUrl(bucketName, attachment.getPath()));
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }


    /**
     * 获取文件列表
     *
     * @param tempId
     * @param types
     * @return
     */
/*    @GetMapping("/list_temp")
    public List<SysAttachment> getListByTempId(@RequestParam("tempId") String tempId, @RequestParam("types") String types) {
        List<SysAttachment> list = attachmentService.getListByTempId(types, tempId);

        for (SysAttachment attachment : list) {
            attachment.setPreUrl(getFileUrls(types, attachment.getUrl()));
        }
        if (list == null) {
            list = new ArrayList<>();
        }

        return list;
    }*/

    /**
     * 上传文件
     *
     * @return
     */
    @PostMapping("/upload")
    @OperateLog(title = "上传文件", isSaveRequestData = false)
    @Transactional(rollbackFor = Exception.class)
    public Res upload(SysAttachment sysAttachment) {

        Res res = attachmentService.upload(sysAttachment);

        return res;
    }


    /**
     * 获取静态文件列表
     *
     * @return
     */
    @GetMapping("/list_static")
    public Res getListStatic() {
        List<SysStaticImg> list = staticImgService.getList();
        for (SysStaticImg img : list) {
            String fileUrl = getFileUrl(img.getTypes(), img.getPath());
            img.setUrl(fileUrl);
        }
        Res res = new Res();
        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(list);
        return res;
    }

    /**
     * 下载静态文件
     *
     * @param response
     * @param path
     */
    @GetMapping("/download_static")
    public void download(HttpServletResponse response, @RequestParam(name = "path") String path, @RequestParam(name = "fileName") String fileName) {
        InputStream in = null;
        try {

            ObjectStat stat = minioClient.statObject(StatObjectArgs.builder().bucket("oa-static").object(path.concat(fileName)).build());
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            in = minioClient.getObject(GetObjectArgs.builder().bucket("oa-static").object(path.concat(fileName)).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 预览静态文件
     *
     * @param response
     * @param path
     */
    @GetMapping("/pre_static")
    public void preStatic(HttpServletResponse response, @RequestParam(name = "path") String path, @RequestParam(name = "fileName") String fileName) {
        InputStream in = null;
        try {

            ObjectStat stat = minioClient.statObject(StatObjectArgs.builder().bucket("oa-static").object(path.concat(fileName)).build());
            response.setContentType(stat.contentType());
            in = minioClient.getObject(GetObjectArgs.builder().bucket("oa-static").object(path.concat(fileName)).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @PostMapping("/upload_static")
    @OperateLog(title = "上传静态文件")
    @Transactional(rollbackFor = Exception.class)
    public Res uploadStatic(@RequestParam(name = "file", required = false) MultipartFile file, @RequestParam(name = "path") String path, @RequestParam(name = "types") String types,
                            @RequestParam(name = "pathNew") String pathNew, @RequestParam(name = "typesNew") String typesNew) {
        Res res = new Res();
        res.setCode(500);

        if (file == null) {
            res.setMessage("上传文件不能为空");
            return res;
        }
        try {
            Date date = new Date();
            String timestamp = String.valueOf(date.getTime());
            String originFileName = file.getOriginalFilename();
            InputStream in = file.getInputStream();
            String newFileName = originFileName.split("\\.")[0].concat(timestamp).concat(".").concat(originFileName.split("\\.")[1]);
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(types)
                    .object(path.concat(newFileName))
                    .stream(in, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            // 没有bucket则创建
            boolean exist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(types).build());
            if (!exist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(types).build());
            }
            minioClient.putObject(args);

            in.close();
            InputStream in2 = file.getInputStream();
            PutObjectArgs args2 = PutObjectArgs.builder()
                    .bucket(typesNew)
                    .object(pathNew)
                    .stream(in2, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            // 没有bucket则创建
            boolean exist2 = minioClient.bucketExists(BucketExistsArgs.builder().bucket(typesNew).build());
            if (!exist2) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(typesNew).build());
            }
            minioClient.putObject(args2);
            in2.close();

            String url = path.concat(newFileName);
            SysStaticImg img = new SysStaticImg();
            img.setPath(url);
            img.setTypes(types);
            img.setName(originFileName);
            staticImgService.insert(img);
        } catch (Exception e) {
            log.error(e.getMessage());
            res.setMessage("上传失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return res;
        }

        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(null);

        return res;
    }

    /**
     * 预览url
     *
     * @param bucketName
     * @param path
     * @return
     */
    @SneakyThrows(Exception.class)
    private String getFileUrl(String bucketName, String path) {
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(path).method(Method.GET).expiry(60, TimeUnit.MINUTES).build());

        return url;
    }

    /**
     * pdf预览
     *
     * @param path pdf路径
     * @return
     */
    @GetMapping("/pre_pdf")
    public String getPreFdf(String bucketName, String path) {
        try {
            return getFileUrl(bucketName, path);
//            return getFileUrl("tmp", path.split("tmp/")[1]);
        } catch (Exception e) {
            log.error("pdf预览", e);
            return "";
        }
    }


    /**
     * 获取url
     *
     * @param bucketName
     * @return
     */
    @SneakyThrows(Exception.class)
    @GetMapping("/getUrls")
    public String getFileUrls(String bucketName, String path) {

        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(path).method(Method.GET).build());
        return url;
    }

    /**
     * 下载
     *
     * @param response
     * @param bucketName
     * @param fileName
     * @param path
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, @RequestParam(name = "bucketName") String bucketName,
                         @RequestParam(name = "path") String path, @RequestParam(name = "fileName") String fileName) {
        // 打印参数
        System.out.println("bucketName:" + bucketName + " path:" + path + " fileName:" + fileName);
        InputStream in = null;
        try {

            ObjectStat stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(path).build());

            response.setContentType(stat.contentType());
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

//    /**
//     * 下载
//     *
//     * @param response
//     * @param bucketName
//     * @param fileName
//     * @param path
//     */
//    @GetMapping("/download2")
//    public void download2(HttpServletResponse response, @RequestParam(name = "bucketName") String bucketName,
//                          @RequestParam(name = "path") String path, @RequestParam(name = "fileName") String fileName) {
//        // 打印参数
//        System.out.println("bucketName:" + bucketName + " path:" + path + " fileName:" + fileName);
//        InputStream in = null;
//        try {
//
//            ObjectStat stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(path.concat(fileName)).build());
//
//            response.setContentType(stat.contentType());
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//
//            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path.concat(fileName)).build());
//            IOUtils.copy(in, response.getOutputStream());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    log.error(e.getMessage());
//                }
//            }
//        }
//    }

    /**
     * 预览文件
     *
     * @param response
     * @param bucketName
     * @param path
     */
    @GetMapping("/pre_file")
    public void preFile(HttpServletResponse response, @RequestParam(name = "bucketName") String bucketName, @RequestParam(name = "path") String path) {
        // 打印参数
        System.out.println("bucketName:" + bucketName + " path:" + path);
        InputStream in = null;
        try {

            ObjectStat stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(path).build());

            response.setContentType(stat.contentType());
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶
     * @param path       路径 如:“aaa/aaa.jpg”
     * @return
     */
    @GetMapping("/delete")
    @OperateLog(title = "删除文件")
    @Transactional(rollbackFor = Exception.class)
    public Res delete(String bucketName, String path) {
        Res res = new Res();
        res.setCode(200);
        res.setMessage("删除成功");
        try {
            attachmentService.delete(bucketName, path);
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("删除失败");
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return res;
    }

    /**
     * 删除静态文件
     *
     * @param bucketName 桶
     * @param path       路径 如:“aaa/aaa.jpg”
     * @return
     */
    @GetMapping("/delete_static")
    @OperateLog(title = "删除文件")
    @Transactional(rollbackFor = Exception.class)
    public Res deleteStatic(String bucketName, String path) {
        Res res = new Res();
        res.setCode(200);
        res.setMessage("删除成功");
        try {
            staticImgService.delete(bucketName, path);
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("删除失败");
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return res;
    }


    /**
     * 批量删除文件
     *
     * @param pathList
     * @param bucketName
     * @return
     */
    @GetMapping("/deleteBatch")
    @OperateLog(title = "批量删除文件")
    @Transactional(rollbackFor = Exception.class)
    public List<DeleteError> deleteBatch(@RequestParam("pathList") List<String> pathList, @RequestParam("bucketName") String bucketName) {

        List<DeleteError> deleteErrors = attachmentService.deleteBatch(pathList, bucketName);
        return deleteErrors;
    }


//    /**
//     * 更新文件,新文件转有效文件，删除旧文件
//     *
//     * @param types    桶
//     * @param path     路径 如:“aaa/aaa.jpg”
//     * @return
//     */
//    @GetMapping("/delete_update")
//    @OperateLog(title = "更新文件")
//    public Res update(String types, String path) {
//        Res res = new Res();
//        res.setCode(200);
//        res.setMessage("删除成功");
//        try {
//            // 有路径按路径删
//            if (StrUtil.isNotBlank(path)) {
//                extractedDelete(types, path);
//            } else {
//                // 没路径按模块和父级id删旧文件
//                List<SysAttachment> list = attachmentService.getList(types);
//                list.stream().filter(f -> "0".equals(f.getDelFlag())).forEach(attachment -> {
//                    extractedDelete(attachment.getTypes(), attachment.getUrl());
//                });
//            }
//            // 将新临时文件转有效文件
//            update(parentId, null);
//        } catch (Exception e) {
//            res.setCode(500);
//            res.setMessage("更新失败");
//            log.error(e.getMessage());
//        }
//        return res;
//    }

    /**
     * 删除文件记录和文件
     *
     * @param bucketName
     * @param path
     */
    @SneakyThrows(Exception.class)
    private void extractedDelete(String bucketName, String path) {
        attachmentService.delete(bucketName, path);
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
    }

}