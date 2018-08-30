package example

import grails.plugin.awssdk.sqs.AmazonSQSService

class QueueService {

    AmazonSQSService amazonSQSService

    String createMainQueue() {
        amazonSQSService.createQueue('Main')
    }
}
