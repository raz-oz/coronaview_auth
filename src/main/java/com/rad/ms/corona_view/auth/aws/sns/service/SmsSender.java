package com.rad.ms.corona_view.auth.aws.sns.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import com.rad.ms.corona_view.auth.aws.config.*;
import com.rad.ms.corona_view.auth.aws.sns.model.SnsResponse;
import software.amazon.awssdk.regions.*;
import software.amazon.awssdk.services.sns.*;
import software.amazon.awssdk.services.sns.model.*;

@Service
public class SmsSender
{
	private static final Logger	LOG	= LoggerFactory.getLogger(SmsSender.class);

	@Autowired
	private AwsProperties awsProperties;
	
	public SnsResponse pubTextSMS(SnsClient snsClient, String message, String phoneNumber)
	{
		try
		{
	        SnsClient snsClient2 = SnsClient.builder().region(Region.of(awsProperties.getRegion())).build();
			
			PublishRequest request = PublishRequest.builder().
													message(message).
													phoneNumber(phoneNumber)
													.build();

			PublishResponse result = snsClient2.publish(request);
			
	        snsClient2.close();
			
			LOG.info(result + " " + result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());
			return new SnsResponse(result.sdkHttpResponse().statusCode(), 
					result.responseMetadata().toString(), 
					result.messageId());
		}
		catch (SnsException e)
		{
			LOG.error("pubTextSMS", e.awsErrorDetails().errorMessage());
			return new SnsResponse(500, e.getMessage(), "0");
		}
	}
}
