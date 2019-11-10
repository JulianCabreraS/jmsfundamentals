package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//EXAMPLE OF PRIORITY
//10 HIGHEST 0 LOWEST
public class MessagePriority {
    public static void main(String[] args) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/broker");

        try(
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
                JMSContext jmsContext = connectionFactory.createContext())
        {
            //CREATE A PRODUCER
            JMSProducer producer = jmsContext.createProducer();
            String[] messages = new String[3];

            messages[0] = "Message One";
            messages[1] = "Message Two";
            messages[2] = "Message Threee";

            //SET THE PRIORITY HIGHER THE NUMBER, HIGHER THE PRIORITY
            producer.setPriority(3);
            producer.send(queue,messages[0]);

            producer.setPriority(4);
            producer.send(queue,messages[1]);

            producer.setPriority(9);
            producer.send(queue,messages[2]);

            //CREATE A CONSUMER
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            for(int i=0;i<3;i++){
                System.out.println(consumer.receiveBody(String.class));
            }

        }
    }
}
