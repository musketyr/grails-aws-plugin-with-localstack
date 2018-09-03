package example;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

public class QueueServiceTest {

    @ClassRule
    public static LocalStackContainer localstack = new LocalStackContainer().withServices(SQS);

    @Test
    public void testQueueCreated() {
        final AmazonSQS sqs = AmazonSQSClient
                .builder()
                .withEndpointConfiguration(localstack.getEndpointConfiguration(SQS))
                .withCredentials(localstack.getDefaultCredentialsProvider()).build();
        String mainQueueURL = sqs.createQueue("Main").getQueueUrl();
        Assert.assertTrue(sqs.listQueues().getQueueUrls().contains(mainQueueURL));
    }

}
