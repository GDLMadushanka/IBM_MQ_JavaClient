import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;

/**
 * Publisher example to a IMB MQ JMS Queue.
 */
public class QueuePublisher {
    public static void main(String[] args) throws JMSException {

        MQQueueConnectionFactory queueConnectionFactory = new MQQueueConnectionFactory();

        // Setting connection properties to connection factory
        queueConnectionFactory.setHostName("localhost");
        queueConnectionFactory.setPort(1414);

        // Setting queue manager and channel names
        queueConnectionFactory.setQueueManager("testmanager");
        queueConnectionFactory.setChannel("channel1");

        // Set transport type to MQ Client
        queueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

        // Creating a connection from factory
        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection("mqm", "mqm");
        queueConnection.start();

        // Create a queue and a session
        Queue queue = new MQQueue("testqueue");
        QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create and send a TextMessage
        QueueSender queueSender = queueSession.createSender(queue);
        Message m = queueSession.createTextMessage("Hello, World!");
        queueSender.send(m);

        // Create a QueueReceiver and wait for one message to be delivered
        QueueReceiver queueReceiver = queueSession.createReceiver(queue);
        Message response = queueReceiver.receive();

        System.out.println("Received message: " + response);

        // Finally closing connections
        queueSession.close();
        queueConnection.close();
    }
}
