package com.yaput.audio.handler.audiohandler.accessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class AudioObjectAccessorImpl implements AudioObjectAccessor {
    private final S3Client s3Client;
    private final String bucketName;

    @Autowired
    public AudioObjectAccessorImpl(S3Client s3Client, @Value("${s3-config.bucketName}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public void putAudioObject(String key, byte[] audioFile) throws AwsServiceException, SdkClientException {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentLength((long) audioFile.length)
                .contentType("audio/wav")
                .key(key)
                .build();

        InputStream inputStream = new ByteArrayInputStream(audioFile);
        var response = s3Client.putObject(objectRequest, RequestBody.fromInputStream(inputStream, audioFile.length));
    }

    @Override
    public InputStream getAudioObject(String filePath) throws AwsServiceException, SdkClientException {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();
        var audioFile = s3Client.getObjectAsBytes(objectRequest);
        if (audioFile == null) {
            return null;
        }
        return audioFile.asInputStream();
    }
}
