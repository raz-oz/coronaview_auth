package com.rad.ms.corona_view.auth.aws.sns.service;

import org.slf4j.*;
import org.springframework.stereotype.*;
import com.rad.ms.corona_view.auth.aws.config.*;
import com.rad.ms.corona_view.auth.aws.ses.service.*;
import com.rad.ms.corona_view.auth.aws.sns.model.*;
import com.rad.ms.corona_view.auth.aws.sns.model.SnsResponse;
import software.amazon.awssdk.core.exception.*;
import software.amazon.awssdk.http.*;
import software.amazon.awssdk.services.sns.*;
import software.amazon.awssdk.services.sns.model.*;

/**
 * Amazon SNS enables you to send notifications directly to your customers. 
 * Amazon SNS supports SMS text messaging to over 200 countries, mobile push notifications.
 * 
 * Convert the message to an Amazon PublishRequest and publish it. 
 * For more information refer to the Publish API documentation:
 * https://docs.aws.amazon.com/sns/latest/api/API_Publish.html
 * 
 * The code handles different types of exceptions that could be thrown by Amazon SNS; 
 * SdkException is a catch all for the other exceptions. 
 * To learn more, refer to the Exception handling section of the AWS Developer Guide and the SnsClient javadoc.
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/handling-exceptions.html
 */
@Service
public class AwsSnsPublisherImpl implements AwsSnsPublisher
{
	private static final Logger	LOG	= LoggerFactory.getLogger(AwsSnsPublisherImpl.class);

	private final SnsClient		snsClient;
	private final AwsProperties	awsProperties;
	private final SmsSender		smsSender;

	public AwsSnsPublisherImpl(SnsClient snsClient, AwsProperties awsProperties, SmsSender smsSenders)
	{
		this.snsClient = snsClient;
		this.awsProperties = awsProperties;
		this.smsSender = smsSenders;
	}

	@Override
	public SnsResponse publish(Message message, String phone)
	{
		return sendSms(message, phone);
	}	
	
	private SnsResponse sendSms(Message message, String phone)
	{
		try
		{
			PublishResponse pubTextSMS = smsSender.pubTextSMS(snsClient, AwsSesPublisher.HTMLBODY, phone);
			return new SnsResponse(pubTextSMS.sdkHttpResponse().statusCode(), 
								   pubTextSMS.responseMetadata().toString(), 
								   pubTextSMS.messageId());
		}
		catch (Exception ex)
		{
			LOG.error("sendSms Failed", ex);
		}
		
		return null;
	}	
	
	private SnsResponse sendMail(Message message)
	{
		SnsResponse response = null;

		try
		{
			PublishRequest request = RequestBuilder.build(awsProperties.getTopicArn(), message);
			LOG.info("Request: {}", request);

			PublishResponse publishResponse = snsClient.publish(request);
			LOG.info("Publish response: {}", publishResponse);

			SdkHttpResponse httpResponse = publishResponse.sdkHttpResponse();
			response = new SnsResponse(httpResponse.statusCode(), httpResponse.statusText().orElse(null), publishResponse.messageId());
			LOG.info("Response details: {}", response);
		}
		catch (SnsException e)
		{
			rethrow(e.statusCode(), e);
		}
		catch (SdkServiceException e)
		{
			rethrow(e.statusCode(), e);
		}
		catch (SdkClientException e)
		{
			rethrow(null, e);
		}
		catch (SdkException e)
		{
			rethrow(null, e);
		}
				
		return response;
	}

	private void rethrow(Integer statusCode, Exception e)
	{
		String detailedMessage = e.getClass().getSimpleName() + ": " + e.getMessage();
		SnsResponse response = new SnsResponse(statusCode, detailedMessage, null);
		LOG.error("Failed", e);
		throw new RuntimeException(response.toString());
	}
}
