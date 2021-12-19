package com.rad.ms.corona_view.auth.aws.config;

import org.springframework.boot.context.event.*;
import org.springframework.context.*;

/**
 * Connect the application to AWS
 * -------------------------------
 * We need to set up an AWS access key and secret key in the Java system properties, 
 * so the app is able to connect to AWS. 
 * One way to accomplish this is to set up an application listener.
 * 
 * To avoid using your IAM administrator user for Amazon SNS operations, 
 * it is a best practice to create an IAM user for each person who needs 
 * administrative access to Amazon SNS.
 * To work with Amazon SNS, you need the AmazonSNSFullAccess policy 
 * and AWS credentials that are associated with your IAM user. 
 */
public class AwsAppListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>
{
	private String ACCESS_KEY_ID = "your_access_key"; 
	private String SECREY_KEY_ID = "your_secret_key"; 

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent)
	{		
		// Add AWS access key and secret key to Java system properties as soon as the environment is available
		if (System.getProperty("aws.accessKeyId") == null)		
			System.setProperty("aws.accessKeyId",     ACCESS_KEY_ID);
		if (System.getProperty("aws.secretAccessKey") == null)		
			System.setProperty("aws.secretAccessKey", SECREY_KEY_ID);
	}
}