package com.bharath.jmsbasics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {
    public static void main(String[] args) {
        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
            Queue queue = (Queue) initialContext.lookup("queue/broker");
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();

            //PRODUCER
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage("I am the creator of my destiny");
            producer.send(message);
            //CONSUMER
            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();

            TextMessage textMessage = (TextMessage) consumer.receive(5000);
            System.out.println("Message received : "+ textMessage.getText());

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        finally {
            if(initialContext!=null){
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }

            if(initialContext!=null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
