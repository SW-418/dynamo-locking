package dev.samwells.dynamolocking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoLockConfig {
    @Value("${aws.access.key}")
    private String awsAccessKeyId;
    @Value("${aws.access.secret}")
    private String awsSecretAccessKey;
    @Value("${aws.region}")
    private String awsRegion;
    @Value("${aws.account.id}")
    private String awsAccountId;

    @Bean
    DynamoDbClient dynamoDbClient() {
        var credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials
                        .builder()
                        .validateCredentials(true)
                        .accessKeyId(awsAccessKeyId)
                        .secretAccessKey(awsSecretAccessKey)
                        .accountId(awsAccountId)
                        .build()
        );

        return DynamoDbClient
                .builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(awsRegion))
                .build();
    }
}
