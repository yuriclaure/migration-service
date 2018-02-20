package com.migration.service.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import static com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder.standard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class AwsConfiguration {

    @Autowired
    private AwsProperties awsProperties;

    @Bean
    public AWSStaticCredentialsProvider awsCredentials() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsProperties.getCredentials().getAccessKeyId(), awsProperties.getCredentials().getSecretKey());
        return new AWSStaticCredentialsProvider(awsCreds);
    }

    @Bean
    public AmazonSQSAsync amazonSQSAsync() {
        return standard()
                .withClientConfiguration(clientConfiguration())
                .withRegion(awsProperties.getRegion())
                .withCredentials(awsCredentials())
                .build();
    }

    private ClientConfiguration clientConfiguration() {
        return new ClientConfiguration()
                .withMaxConnections(awsProperties.getClient().getMaxConnections());
    }

}
