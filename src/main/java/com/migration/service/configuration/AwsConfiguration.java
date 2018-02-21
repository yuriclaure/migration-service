package com.migration.service.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.PredefinedClientConfigurations;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads.CONSISTENT;
import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
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

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withClientConfiguration(dynamoDbClientConfiguration())
                .withCredentials(awsCredentials())
                .withRegion(awsProperties.getRegion())
                .build();
    }

    @Bean
    public IDynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB(), dynamoDBMapperConfig());
    }

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.builder()
                .withConsistentReads(CONSISTENT)
                .withSaveBehavior(UPDATE_SKIP_NULL_ATTRIBUTES)
                .build();
    }

    private ClientConfiguration dynamoDbClientConfiguration() {
        return PredefinedClientConfigurations.dynamoDefault()
                .withMaxConnections(awsProperties.getClient().getMaxConnections());
    }

    private ClientConfiguration clientConfiguration() {
        return new ClientConfiguration()
                .withMaxConnections(awsProperties.getClient().getMaxConnections());
    }

}
