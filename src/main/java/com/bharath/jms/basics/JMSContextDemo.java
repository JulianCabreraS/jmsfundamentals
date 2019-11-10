package com.bharath.jms.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//EXAMPLE OF USING JMS 2.0
public class JMSContextDemo {
    public static void main(String[] args) throws NamingException {
        InitialContext context =new InitialContext();
        Queue queue = (Queue) context.lookup("queue/broker");
        try(
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext())
        {
            jmsContext.createProducer().send((Destination) queue,"Arise Awake and stop till the goal is reached");
            String receiveBody = jmsContext.createConsumer((Destination) queue).receiveBody(String.class);
            System.out.println(receiveBody);

        }
    }
}
