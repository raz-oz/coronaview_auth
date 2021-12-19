package com.rad.ms.corona_view.auth.aws.sns.service;

import com.rad.ms.corona_view.auth.aws.sns.model.*;

public interface AwsSnsPublisher {
    SnsResponse publish(Message message, String phone);
}