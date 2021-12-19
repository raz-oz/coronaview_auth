package com.rad.ms.corona_view.auth.aws.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

/**
 * SES is meant for sending high volume e-mail efficiently and securely. 
 * Once you have verified that you are the owner of an e-mail address, 
 * you can send e-mails through SES to any other e-mail address without 
 * the recipient's consent. SES takes care of the engineering required 
 * to ensure delivery of their e-mails (as might be obvious, without the
 * right levels of security, SES can be a spammers dream).
 * 
 * SNS is meant as a channel publisher/subscriber service. 
 * In order to receive e-mails from SNS, the end-user must first subscribe 
 * (via means enabled by the developer) and approve that subscription 
 * through e-mail before amazon delivers e-mails from the subscribed 
 * channel to that end-user. End-users can subscribe via e-mail, SMS, 
 * web-hooks and other means up to the user independent of the publisher.
 * 
 * On a practical level, we use SES to send our users e-mails about their 
 * content and we use SNS to send our developers notifications (via SMS and e-mail) 
 * when servers go down or have issues.
 * 
 * Configure the SNS Client that will publish messages to the topic
 * with the appropriate region.
 */
@Configuration
public class SnsConfig {

    @Autowired
    private AwsProperties awsProperties;

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .build();
    }
}