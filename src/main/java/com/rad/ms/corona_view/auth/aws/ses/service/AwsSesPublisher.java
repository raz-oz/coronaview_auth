package com.rad.ms.corona_view.auth.aws.ses.service;

import org.slf4j.*;
import org.springframework.stereotype.*;
import com.amazonaws.regions.*;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.simpleemail.model.Message;
import com.rad.ms.corona_view.auth.aws.sns.model.*;

/**
 * Amazon Simple Email Service (SES) is a cost-effective, 
 * flexible, and scalable email service that enables developers 
 * to send mail from within any application.
 */
@Service
public class AwsSesPublisher {

  private static final Logger	LOG	= LoggerFactory.getLogger(AwsSesPublisher.class);

  // The configuration set to use for this email. If you do not want to use a
  // configuration set, comment the following variable and the 
  // .withConfigurationSetName(CONFIGSET); argument below.
  private static final String CONFIGSET = "ConfigSet";

  // The subject line for the email.
  private static final String SUBJECT = "CoronaView – Email Address Verification Request";
  
  // The HTML body for the email.
  public static final String HTMLBODY = "<h1>RAD Starship Application (2021)</h1>"
      + "<p>Dear User,\r\n"
      + "We have received a request to authorize this email address for use with CoronaView Application. "
      + "If you requested this verification, please go to the following URL to confirm that you are authorized to use this email address:\r\n"
      + "<a href='https://aws.amazon.com/ses/'>"+ "Here </a><BR>"
      + "Your request will not be processed unless you confirm the address using this URL. This link expires 24 hours after your original verification request.\r\n"
      + "";

  // The email body for recipients with non-HTML email clients.
  static final String TEXTBODY = "This email was sent through Amazon SES "+ "using the AWS SDK for Java.";

	public SnsResponse sendMail(com.rad.ms.corona_view.auth.aws.sns.model.Message message, String from, String to)
	{
		SnsResponse response = null;

		try
		{
			// Replace US_WEST_2 with the AWS Region you're using for Amazon SES.
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();

			SendEmailRequest request = new SendEmailRequest()
					.withSource(from)
					.withDestination(new Destination().withToAddresses(to))
					.withMessage(new Message().
							withBody(new Body().
							withHtml(new Content().withCharset("UTF-8").
							withData(HTMLBODY)).
							withText(new Content().withCharset("UTF-8").withData(TEXTBODY))).
							withSubject(new Content().withCharset("UTF-8").
							withData(SUBJECT)));

			// Comment or remove the next line if you are not using a
			// configuration set
			// .withConfigurationSetName(CONFIGSET);

			SendEmailResult result = client.sendEmail(request);
			LOG.info("Email sent!");
			response = new SnsResponse(result.getSdkHttpMetadata().getHttpStatusCode(), "", result.getMessageId());
		}
		catch (Exception e)
		{
			LOG.error("The email was not sent. Error message: " + e.getMessage(), e);
			rethrow(null, e.getClass().getSimpleName() + ": " + e.getMessage());
		}
		
		return response;
	}
	
	private void rethrow(Integer statusCode, String detailedMessage)
	{
		SnsResponse response = new SnsResponse(statusCode, detailedMessage, null);
		throw new RuntimeException(response.toString());
	}
}