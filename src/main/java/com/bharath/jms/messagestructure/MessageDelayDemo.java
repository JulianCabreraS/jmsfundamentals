package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//EXAMPLE OF EXPIRY TIME

public class MessageDelayDemo {
    public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
        InitialContext context =new InitialContext();
        Queue queue = (Queue) context.lookup("queue/broker");

        try(
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext())
        {
            //Producer
            JMSProducer producer = jmsContext.createProducer();
            producer.setDeliveryDelay(3000);
            producer.send(queue,"Arise awake and stop not till the goal is reached ");


            //Consumer
            Message messageReceived = jmsContext.createConsumer(queue).receive(5000);

            System.out.println(messageReceived);




        }
    }
}
