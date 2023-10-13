package org.deil.utils.minio;

import io.minio.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class MinioTemplate {

    private static Logger log = LoggerFactory.getLogger(MinioTemplate.class);

    //public static String BUCKET = null;
    //
    //public static final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
    //
    //static {
    //    try {
    //        BUCKET = Objects.requireNonNull(patternResolver.getResource("bucket.json").getInputStream()).toString();
    //    } catch (IOException e) {
    //        log.error("MinioTemplate.static:{}", e.getMessage());
    //    }
    //}

    @Autowired
    private MinioClient client;

    public FileVo upload(MultipartFile file, String bucketName) {
        try {
            createBucket(bucketName);

            String oldName = file.getOriginalFilename();
            String fileName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + UUID.randomUUID().toString().replace("-", "") + oldName.substring(oldName.lastIndexOf("."));

            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), 0)
                            .contentType(file.getContentType()).build()
            );

            String url = this.getObjUrl(bucketName, fileName);
            return FileVo.builder()
                    .oldFileName(oldName)
                    .newFileName(fileName)
                    .fileUrl(url.substring(0, url.indexOf("?")))
                    .build();
        } catch (Exception e) {
            log.error("上传文件出错:{}", e);
            return null;
        }
    }

    public List<FileVo> uploads(List<MultipartFile> files, String bucketName) {
        try {
            List<FileVo> list = new ArrayList<>();
            createBucket(bucketName);

            for (MultipartFile file : files) {
                String oldName = file.getOriginalFilename();
                String fileName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + UUID.randomUUID().toString().replace("-", "") + oldName.substring(oldName.lastIndexOf("."));

                client.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(file.getInputStream(), file.getSize(), 0)
                                .contentType(file.getContentType()).build()
                );

                String url = this.getObjUrl(bucketName, fileName);
                list.add(
                        FileVo.builder()
                                .oldFileName(oldName)
                                .newFileName(fileName)
                                .fileUrl(url.substring(0, url.indexOf("?")))
                                .build()
                );
            }
            return list;
        } catch (Exception e) {
            log.error("上传文件出错:{}", e);
            return null;
        }
    }

    public void download(String bucketName, String fileName) throws Exception {
        client.downloadObject(DownloadObjectArgs.builder().bucket(bucketName).filename(fileName).build());
    }

    public String getObjUrl(String bucketName, String fileName) throws Exception {
        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .method(Method.GET)
                        .expiry(30, TimeUnit.SECONDS)
                        .build()
        );
    }

    public void delete(String bucketName, String fileName) throws Exception {
        client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }

    /**
     * {
     *     "Version": "2012-10-17",
     *     "Statement": [
     *         {
     *             "Action": ["action1", "action2", ...],
     *             "Effect": "Allow|Deny",
     *             "Principal": {"AWS": ["arn:aws:iam::account-id:user/user-name"]},
     *             "Resource": ["arn:aws:s3:::bucket-name/object-prefix", ...]
     *         },
     *         ...
     *     ]
     * }
     */
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            //String sb = "{\"Version\": \"2012-10-17\",\"Statement\": [{\"Effect\": \"Allow\",\"Principal\": {\"AWS\": [\"*\"]},\"Action\": [\"s3:GetBucketLocation\"],\"Resource\": [\"arn:aws:s3:::" + bucketName + "\"]},{\"Effect\": \"Allow\",\"Principal\": {\"AWS\": [\"*\"]},\"Action\": [\"s3:ListBucket\"],\"Resource\": [\"arn:aws:s3:::" + bucketName + "\"],\"Condition\": {\"StringEquals\": {\"s3:prefix\": [\"*\"]}}},{\"Effect\": \"Allow\",\"Principal\": {\"AWS\": [\"*\"]},\"Action\": [\"s3:GetObject\"],\"Resource\": [\"arn:aws:s3:::" + bucketName + "/**\"]}]}";
            String sb = "" +
                    "{" +
                    "    \"Version\": \"2012-10-17\"," +
                    "    \"Statement\": [" +
                    "        {" +
                    "            \"Effect\": \"Allow\"," +
                    "            \"Principal\": {" +
                    "                \"AWS\": [" +
                    "                    \"*\"" +
                    "                ]" +
                    "            }," +
                    "            \"Action\": [" +
                    "                \"s3:GetBucketLocation\"" +
                    "            ]," +
                    "            \"Resource\": [" +
                    "                \"arn:aws:s3:::" + bucketName + "\"" +
                    "            ]" +
                    "        }," +
                    "        {" +
                    "            \"Effect\": \"Allow\"," +
                    "            \"Principal\": {" +
                    "                \"AWS\": [" +
                    "                    \"*\"" +
                    "                ]" +
                    "            }," +
                    "            \"Action\": [" +
                    "                \"s3:ListBucket\"" +
                    "            ]," +
                    "            \"Resource\": [" +
                    "                \"arn:aws:s3:::" + bucketName + "\"" +
                    "            ]," +
                    "            \"Condition\": {" +
                    "                \"StringEquals\": {" +
                    "                    \"s3:prefix\": [" +
                    "                        \"*\"" +
                    "                    ]" +
                    "                }" +
                    "            }" +
                    "        }," +
                    "        {" +
                    "            \"Effect\": \"Allow\"," +
                    "            \"Principal\": {" +
                    "                \"AWS\": [" +
                    "                    \"*\"" +
                    "                ]" +
                    "            }," +
                    "            \"Action\": [" +
                    "                \"s3:GetObject\"" +
                    "            ]," +
                    "            \"Resource\": [" +
                    "                \"arn:aws:s3:::" + bucketName + "/**\"" +
                    "            ]" +
                    "        }" +
                    "    ]" +
                    "}";
            client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(sb).build());
        }
    }

}
