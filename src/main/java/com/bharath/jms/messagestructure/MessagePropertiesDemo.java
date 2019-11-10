package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//EXAMPLE OF HOW TO ATTACHE PROPERTIES

public class MessagePropertiesDemo {
    public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
        InitialContext context =new InitialContext();
        Queue queue = (Queue) context.lookup("queue/broker");

        try(
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext())
        {
            //PRODUCER
            JMSProducer producer = jmsContext.createProducer();
            TextMessage textMessage = jmsContext.createTextMessage("Arise awake and stop not till the goal is reached ");
            textMessage.setBooleanProperty("loggedIn",true);
            textMessage.setStringProperty("userToken", "abc123");
            producer.send(queue,textMessage);

            //CONSUMER
            Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
            System.out.println(messageReceived);
            System.out.println(textMessage.getBooleanProperty("loggedIn"));




        }
    }
}
