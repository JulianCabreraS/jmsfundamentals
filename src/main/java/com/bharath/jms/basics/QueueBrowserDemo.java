package com.bharath.jms.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;


//EXAMPLE ON HOW TO BROWSE MESSAGE
public class QueueBrowserDemo {
    public static void main(String[] args) {
        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            Queue queue = (Queue) initialContext.lookup("queue/broker");
            MessageProducer producer = session.createProducer(queue);
            TextMessage message1 = session.createTextMessage("Message 1");
            TextMessage message2 = session.createTextMessage("Message 2");
            producer.send(message1);
            producer.send(message2);

            QueueBrowser browser = session.createBrowser(queue);
            Enumeration enumeration = browser.getEnumeration();

            while(enumeration.hasMoreElements())
            {
                TextMessage message = (TextMessage) enumeration.nextElement();
                System.out.println("Browsing "+ message.getText());
            }


            //Message
            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage textMessage = (TextMessage) consumer.receive(5000);
            System.out.println("Message received : "+ textMessage.getText());
            textMessage = (TextMessage) consumer.receive(5000);
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
