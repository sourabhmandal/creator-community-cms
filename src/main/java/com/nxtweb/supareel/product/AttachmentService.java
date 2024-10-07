package com.nxtweb.supareel.product;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.nxtweb.supareel.config.AWSConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.iap.ByteArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentService {

    @Value("${application.file.bucket.name}")
    private String bucketName;

    private final AmazonS3 awsClient;

    public String uploadFile(MultipartFile multipartFile) {
        String fileName = currentTimeMillis() + "_" + Objects.requireNonNull(multipartFile.getOriginalFilename())
                .replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String ext = fileName.substring(fileName.lastIndexOf("."));

        File file = convertMultipartFiletoFile(multipartFile);
        awsClient.putObject(new PutObjectRequest(bucketName, fileName, file));
        boolean isLocalFileDeleted = file.delete();

        if(!isLocalFileDeleted) {
            log.warn("local file not deleted after upload to S3 bucket");
        }
        return fileName;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        S3Object s3Object = awsClient.getObject(new GetObjectRequest(bucketName, fileName));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        return IOUtils.toByteArray(inputStream);
    }

    public boolean deleteFile(String fileName) {
        awsClient.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return true;
    }

    private File convertMultipartFiletoFile(MultipartFile multipartFile) {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            log.error("unable to upload file :: {}", e.getMessage());
        }
        return convertedFile;
    }

}
