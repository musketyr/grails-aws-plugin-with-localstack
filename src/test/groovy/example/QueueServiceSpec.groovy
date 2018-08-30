package example

import grails.plugin.awssdk.sqs.AmazonSQSService
import grails.testing.services.ServiceUnitTest
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS

@Testcontainers
class QueueServiceSpec extends Specification implements ServiceUnitTest<QueueService> {

    @Shared LocalStackContainer localstack = new LocalStackContainer().withServices(SQS)

    @Override
    Closure doWithConfig() {{ c ->
        int port = localstack.getMappedPort(SQS.port)
        String address = localstack.containerIpAddress

        c.grails.plugin.awssdk.sqs.protocol = 'http'
        c.grails.plugin.awssdk.sqs.proxyPort = port
        c.grails.plugin.awssdk.sqs.proxyHost = address
    }}

    void setup() {
        service.amazonSQSService = prepareAmazonSQSService()
    }

    void "test queue created"() {
        when:
            String mainQueueURL = service.createMainQueue()
        then:
            service.amazonSQSService.client.listQueues()
                   .queueUrls.contains(mainQueueURL)
    }

    private AmazonSQSService prepareAmazonSQSService() {
        AmazonSQSService amazonSQSService = new AmazonSQSService(grailsApplication: grailsApplication)
        amazonSQSService.afterPropertiesSet()
        return amazonSQSService
    }
}
