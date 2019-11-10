package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;


//EXAMPLE OF REPLY QUEUES AND CORRELATION ID

public class RequestReplyDemo {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context =new InitialContext();
        Queue requestQueue = (Queue) context.lookup("queue/requestQueue");
        //Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
        try(
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext())
        {
            //PROCESS FOR SENDING ORIGINAL MESSAGE
            JMSProducer producer = jmsContext.createProducer();
            TemporaryQueue temporaryQueue = jmsContext.createTemporaryQueue();
            TextMessage textMessage = jmsContext.createTextMessage("Arise Awake and stop till the goal is reached");
            textMessage.setJMSReplyTo(temporaryQueue);
            producer.send(requestQueue,textMessage);
            System.out.println(textMessage.getJMSMessageID());

            JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
            TextMessage messageReceived = (TextMessage) consumer.receive();
            System.out.println(messageReceived.getText());

            ///PROCeSS FOR RECEIVING AND REPLY
            JMSProducer replyProducer = jmsContext.createProducer();
            TextMessage replyMessage = jmsContext.createTextMessage("You are awesome");
            replyMessage.setJMSCorrelationID(messageReceived.getJMSMessageID());
            replyProducer.send(messageReceived.getJMSReplyTo(), replyMessage);

            JMSConsumer replyConsumer = jmsContext.createConsumer(temporaryQueue);
            TextMessage replyReceived = (TextMessage) replyConsumer.receive();
            System.out.println(replyMessage.getJMSCorrelationID());


        }
    }
}
