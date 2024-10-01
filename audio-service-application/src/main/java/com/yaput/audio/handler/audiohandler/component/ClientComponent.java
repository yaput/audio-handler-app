package com.yaput.audio.handler.audiohandler.component;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Component
public class ClientComponent {

    @Bean
    public S3Client s3Client() {
        final String ACCESS_KEY = "test";
        final String SECRET_KEY = "test";

        // Desired region.
        Region region = Region.US_EAST_1;
        return S3Client.builder()
                .region(Region.US_EAST_1) // Due to localstack implementation, can be configured
                .endpointOverride(URI.create("http://localstack:4566")) // LocalStack S3 URL
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .region(region)
                .forcePathStyle(true)
                .build();
    }
}
