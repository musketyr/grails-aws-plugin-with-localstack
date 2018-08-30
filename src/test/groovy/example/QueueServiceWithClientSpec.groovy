package example

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import grails.plugin.awssdk.sqs.AmazonSQSService
import grails.testing.services.ServiceUnitTest
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS

@Testcontainers
class QueueServiceWithClientSpec extends Specification implements ServiceUnitTest<QueueService> {

    @Shared LocalStackContainer localstack = new LocalStackContainer().withServices(SQS)

    AmazonSQS sqs = AmazonSQSClient.builder()
            .withEndpointConfiguration(localstack.getEndpointConfiguration(SQS))
            .withCredentials(localstack.defaultCredentialsProvider)
            .build()

    void setup() {
        service.amazonSQSService = new AmazonSQSService(grailsApplication: grailsApplication, client: sqs)
    }

    void "test queue created"() {
        when:
            String mainQueueURL = service.createMainQueue()
        then:
            sqs.listQueues().queueUrls.contains(mainQueueURL)
    }

}
