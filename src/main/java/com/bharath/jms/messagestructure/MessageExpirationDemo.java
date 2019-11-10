package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//EXAMPLE OF EXPIRY TIME

public class MessageExpirationDemo {
    public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
        InitialContext context =new InitialContext();
        Queue queue = (Queue) context.lookup("queue/broker");
        Queue expiryQueue = (Queue) context.lookup("queue/expiryQueue");
        try(
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext())
        {
            //Producer
            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(100);
            producer.send(queue,"Arise awake and stop not till the goal is reached ");
            Thread.sleep(5000);

            //Consumer
            Message messageReceived = jmsContext.createConsumer(queue).receive(5000);

            System.out.println(messageReceived);
            System.out.println(jmsContext.createConsumer(expiryQueue).receiveBody(String.class));



        }
    }
}
