package com.rad.ms.corona_view.auth.aws.sns.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import com.rad.ms.corona_view.auth.aws.config.*;
import software.amazon.awssdk.services.sns.*;
import software.amazon.awssdk.services.sns.model.*;

@Service
public class SmsSender
{
	private static final Logger	LOG	= LoggerFactory.getLogger(SmsSender.class);

	@Autowired
	private AwsProperties awsProperties;
	
	public PublishResponse pubTextSMS(SnsClient snsClient, String message, String phoneNumber)
	{
		try
		{
			PublishRequest request = PublishRequest.builder().
													message(message).
													phoneNumber(phoneNumber)
													.build();

			PublishResponse result = snsClient.publish(request);
			
			LOG.info(result + " " + result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());
			return result;
		}
		catch (SnsException e)
		{
			LOG.error("pubTextSMS", e.awsErrorDetails().errorMessage());
		}
		return null;
	}
}
