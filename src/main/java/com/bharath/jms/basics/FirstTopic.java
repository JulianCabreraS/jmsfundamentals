package com.bharath.jms.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;


///EXAMPLE OF HOW TO USE TOPICS

public class FirstTopic {
    public static void main(String[] args)  {
        InitialContext initialContext = null;
        Connection connection = null;


        try {
            initialContext = new InitialContext();
            Topic topic = (Topic) initialContext.lookup("topic/myTopic");
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            //PRODUCER
            MessageProducer producer = session.createProducer(topic);
            //CONSUMERS
            MessageConsumer consumer1 = session.createConsumer(topic);
            MessageConsumer consumer2 = session.createConsumer(topic);

            TextMessage message = session.createTextMessage("All the power is with in me. I can do anything and everying");
            producer.send(message);
            connection.start();
            TextMessage textMessage1 = (TextMessage) consumer1.receive();
            System.out.println("Consumer 1 message received : " + textMessage1.getText());

            TextMessage textMessage2 = (TextMessage) consumer2.receive();
            System.out.println("Consumer 2 message received : " + textMessage2.getText());

            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            initialContext.close();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
