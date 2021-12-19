package com.rad.ms.corona_view.auth.aws.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.rad.ms.corona_view.auth.aws.ses.service.*;
import com.rad.ms.corona_view.auth.aws.sns.model.*;
import com.rad.ms.corona_view.auth.aws.sns.service.*;

/**
 * Create an end-point to publish a message to the topic. The end-point returns the status code, message, and message ID returned by Amazon SNS. The
 * message ID is a unique identifier assigned to a published message. If an exception occurs, the end-point returns any status code and error message
 * returned by Amazon SNS.
 *
 * https://reflectoring.io/spring-cloud-aws-ses/
 */
@RestController
public class VerificationMessageController
{
	private final AwsSesPublisher	messageSesPublisher;
	private final AwsSnsPublisher	messageSnsPublisher;

	@Autowired
	public VerificationMessageController(AwsSnsPublisher messagePublisher, AwsSesPublisher messageSesPublisher)
	{
		this.messageSnsPublisher = messagePublisher;
		this.messageSesPublisher = messageSesPublisher;
	}

	@PostMapping("/verifyByMail")
	@ResponseStatus(HttpStatus.CREATED)
	public SnsResponse sendVerificationMail(@RequestBody Message message)
	{
		// This address must be verified with Amazon SES.
		String from = System.getProperty("aws.mail.sender");

		// Replace recipient@example.com with a "To" address. If your account
		// is still in the sandbox, this address must be verified.
		String to = message.getMailTo();
		if (to == null || to.isEmpty())
			throw new IllegalArgumentException("Verification Mail is missing");
		
		return messageSesPublisher.sendMail(message, from, to);
	}

	@PostMapping("/verifyBySms")
	@ResponseStatus(HttpStatus.CREATED)
	public SnsResponse publishMessage(@RequestBody Message message)
	{
		// This phone must be verified by AWS
		String phone = message.getPhoneTo();
		if (phone == null || phone.isEmpty())
			throw new IllegalArgumentException("Verification Phone number is missing");
				
		return messageSnsPublisher.publish(message, phone);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	private String handleException(RuntimeException e)
	{
		return e.getMessage();
	}
}