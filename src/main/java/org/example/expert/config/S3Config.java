package org.example.expert.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.region:ap-northeast-2}")
    private String region;

    @Value("${aws.access-key-id:empty}")
    private String accessKeyId;

    @Value("${aws.secret-access-key:empty}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        if ("empty".equals(accessKeyId)) {
            // Default mock for local if not set
            return S3Client.builder().region(Region.of(region)).build();
        }
        
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
