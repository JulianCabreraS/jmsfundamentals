package com.bharath.jms.basics.grouping;

import com.sun.javafx.collections.MappingChange;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.IllegalStateException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageGroupingDemo {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context =new InitialContext();
        Queue queue = (Queue) context.lookup("queue/broker");
        Map<String, String> receiveMessage =new ConcurrentHashMap<>();
        try(
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
                JMSContext jmsContext = connectionFactory.createContext();
                JMSContext jmsContext2 = connectionFactory.createContext())
        {
            //Producer
            JMSProducer producer = jmsContext.createProducer();
            JMSConsumer consumer1 = jmsContext2.createConsumer(queue);
            consumer1.setMessageListener(new MyListener("Consumer1", receiveMessage));

            JMSConsumer consumer2 = jmsContext2.createConsumer(queue);
            consumer2.setMessageListener(new MyListener("Consumer2", receiveMessage));

            int count=10;
            TextMessage[] messages = new TextMessage[count];
            for(int i=0;i<count;i++){
                messages[i] = jmsContext.createTextMessage("Group-0 message "+i);;
                messages[i].setStringProperty("JMSXGroupID", "Group-0");
                producer.send(queue,messages[i]);
            }

            for(TextMessage message:messages){
                if(!receiveMessage.get(message.getText()).equals("Consumer-1")){
                    throw new IllegalStateException("Group Message" +message.getText()+"has gone to the wrong receiver");
                }
            }
        }
    }

}

class MyListener implements MessageListener{

    private String name;
    private Map<String, String> receivedMessages;

    public MyListener(String name, Map<String, String> receivedMessages) {
        this.name = name;
        this.receivedMessages = receivedMessages;
    }

    @Override
    public void onMessage(Message message) {
        TextMessage message1 = (TextMessage) message;
        try {
            System.out.println("Message received is:" +message1.getText());
            System.out.println("Listener Name"+ name);
            receivedMessages.put(message1.getText(), name);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
