package br.com.josemarsilva.mqseries_api_cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;


public class MqseriesApi {

	/*
	 * Constants ...
	 */
	

	/*
	 * Properties ...
	 */
	CliArgsParser cliArgsParser = null;

	
	/*
	 * Constructor 
	 */
	public MqseriesApi(CliArgsParser cliArgsParser) throws Exception {
		super();
		
		// Setter cliArgsParser ...
		this.cliArgsParser = cliArgsParser;
		
	}
	
	
	/*
	 * execute() 
	 */
	public void execute() throws Exception {
		
		// Variables
		JMSContext context = null;
		Destination destination = null;
		JMSProducer producer = null;
		JMSConsumer consumer = null;

		
		// Create a connection factory
		JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
		JmsConnectionFactory cf = ff.createConnectionFactory();

		// Set the properties
		cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, cliArgsParser.getHost());
		cf.setIntProperty(WMQConstants.WMQ_PORT, cliArgsParser.getPort());
		cf.setStringProperty(WMQConstants.WMQ_CHANNEL, cliArgsParser.getChannel());
		cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
		cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, cliArgsParser.getQmgr());
		cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, cliArgsParser.WMQ_APPLICATIONNAME);
		cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
		cf.setStringProperty(WMQConstants.USERID, cliArgsParser.getAppUser());
		cf.setStringProperty(WMQConstants.PASSWORD, cliArgsParser.getAppPassword());
		
		// Create JMS objects
		context = cf.createContext();
		destination = context.createQueue("queue:///" + cliArgsParser.getQueueName());

		// PUT or GET ?
		if (cliArgsParser.getAction().toLowerCase().contentEquals("put")) {
			
			// PUT		
			String messageBodyToSend = new String("");
			
			// File Option ? '-f <messageFile>'
			if (!cliArgsParser.getMessageFile().contentEquals("")) {
				messageBodyToSend = new String(Files.readAllBytes(Paths.get(cliArgsParser.getMessageFile())));
			} else {
				messageBodyToSend = cliArgsParser.getMessageBody();
			}
			TextMessage messageBody = context.createTextMessage(messageBodyToSend);
			
			// PUT -> Producer Sends message ...
			producer = context.createProducer();
			producer.send(destination, messageBody);
			System.out.println("Sent message:\n" + messageBody);
			
		} else if (cliArgsParser.getAction().toLowerCase().contentEquals("get")) {

			// GET ...
			consumer = context.createConsumer(destination); // autoclosable
			String receivedMessageBody = consumer.receiveBody(String.class, 15000); // in ms or 15 seconds
			System.out.println("\nReceived message:\n" + receivedMessageBody);
			
			// File Option ?
			if (!cliArgsParser.getMessageFile().contentEquals("")) {
				// Write receivedMessage to file ...
				Files.write(Paths.get(receivedMessageBody), receivedMessageBody.getBytes());
			}

		}

	}

	
}
