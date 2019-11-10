package com.bharath.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//EXAMPLE OF HOW TO ATTACHE PROPERTIES

public class MessageTypesDemo {
    public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
        InitialContext context =new InitialContext();
        Queue queue = (Queue) context.lookup("queue/broker");

        try(
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext())
        {
            //----------------------PRODUCER
            JMSProducer producer = jmsContext.createProducer();

            //BYTE MESSAGE
            BytesMessage bytesMessage=jmsContext.createBytesMessage();
            bytesMessage.writeUTF("John");
            bytesMessage.writeLong(123l);
            producer.send(queue,bytesMessage);

            //MAP MESSAGE
            MapMessage mapMessage = jmsContext.createMapMessage();
            mapMessage.setBoolean("isCreditAvailable", false);
            producer.send(queue,mapMessage);

            //OBJECT MESSAGE
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            Patient patient = new Patient(123,"John");
            objectMessage.setObject(patient);
            producer.send(queue,objectMessage);


            //----------------------CONSUMER
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            //BytesMessage
            BytesMessage messageReceived = (BytesMessage) consumer.receive(5000);
            System.out.println(messageReceived.readUTF());
            System.out.println(messageReceived.readLong());

            //Consume Mapmessage
            MapMessage mapMessageReceived = (MapMessage) consumer.receive(5000);
            System.out.println(mapMessageReceived.getBoolean("isCreditAvailable"));

            //Consume Object Message
            ObjectMessage objectMessageReceived = (ObjectMessage) consumer.receive(5000);
            Patient patientReceived = (Patient)objectMessageReceived.getObject();
            System.out.println(patientReceived.getId()+" "+patientReceived.getName());


        }
    }
}
