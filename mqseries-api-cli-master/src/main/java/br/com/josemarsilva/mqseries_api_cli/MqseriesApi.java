package br.com.josemarsilva.mqseries_api_cli;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;


/*
 * MqseriesApi class is responsible to implements execution of MQSeries commands line arguments PUT and GET
 * 
 * @author josemarsilva@yahoo.com.br
 * @date   2020-02-29
 * 
 */
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
		cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, cliArgsParser.WMQ_APPLICATIONNAME_DEFAULT);
		cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, ( cliArgsParser.getUserAuthenticationMqcsp().contentEquals("false") ? false : true ));
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
				System.out.println("Reading MessageBodyToSend from file '%s'".replaceAll("%s",cliArgsParser.getMessageFile()));
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
			String receivedMessageBody = consumer.receiveBody(String.class, cliArgsParser.TIMEOUT_CONSUMER_RECEIVE);
			if (receivedMessageBody==null) {
				receivedMessageBody = new String("");
			}
			System.out.println("\nReceived message:\n" + receivedMessageBody);
			
			// File Option ? '-f <messageFile>'
			if (!cliArgsParser.getMessageFile().contentEquals("")) {
				System.out.println("Writing ReceivedMessage to file '%s'".replaceAll("%s",cliArgsParser.getMessageFile()));
				Files.write(Paths.get(cliArgsParser.getMessageFile()), receivedMessageBody.getBytes());
			}

		} else if (cliArgsParser.getAction().toLowerCase().contentEquals("get-all")) {

			// GET-ALL ...
			consumer = context.createConsumer(destination); // autoclosable
			
			// while not END-OF-QUEUE
			boolean endOfQueue = false;
			int index = 1;
			while (!endOfQueue) {
				String receivedMessageBody = consumer.receiveBody(String.class, cliArgsParser.TIMEOUT_CONSUMER_RECEIVE);
				if (receivedMessageBody==null) {
					endOfQueue = true;
					receivedMessageBody = new String("");
				}
				System.out.println("\nReceived message:\n" + receivedMessageBody);
				
				// File Option ? '-f <messageFile>'
				if (!endOfQueue && !cliArgsParser.getMessageFile().contentEquals("")) {
					String messageFileName = new String( cliArgsParser.getMessageFile().replaceAll("\\.", "(" + String.valueOf(index++) + ")\\.") );
					System.out.println("Writing ReceivedMessage to file '%s'".replaceAll("%s",messageFileName));
					Files.write(Paths.get(messageFileName), receivedMessageBody.getBytes());
				}
			}
		}

	}

	public static String Get(CliArgsParser cliArgsParser) throws Exception {
				
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
		cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, cliArgsParser.WMQ_APPLICATIONNAME_DEFAULT);
		cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, ( cliArgsParser.getUserAuthenticationMqcsp().contentEquals("false") ? false : true ));
		cf.setStringProperty(WMQConstants.USERID, cliArgsParser.getAppUser());
		cf.setStringProperty(WMQConstants.PASSWORD, cliArgsParser.getAppPassword());
		
		// Create JMS objects
		context = cf.createContext();
		destination = context.createQueue("queue:///" + cliArgsParser.getQueueName());
		
		// GET ...
		consumer = context.createConsumer(destination); // autoclosable
		String receivedMessageBody = consumer.receiveBody(String.class, cliArgsParser.TIMEOUT_CONSUMER_RECEIVE);
		if (receivedMessageBody==null) {
			receivedMessageBody = new String("");
		}
				
		// File Option ? '-f <messageFile>'
		if (!cliArgsParser.getMessageFile().contentEquals("")) {
			System.out.println("Writing ReceivedMessage to file '%s'".replaceAll("%s",cliArgsParser.getMessageFile()));
			Files.write(Paths.get(cliArgsParser.getMessageFile()), receivedMessageBody.getBytes());
		}
		
		return receivedMessageBody;
	}
	
	public static List<String> GetAll(CliArgsParser cliArgsParser) throws JMSException {
		
		List<String> messages = new ArrayList();
		
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
		cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, cliArgsParser.WMQ_APPLICATIONNAME_DEFAULT);
		cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, ( cliArgsParser.getUserAuthenticationMqcsp().contentEquals("false") ? false : true ));
		cf.setStringProperty(WMQConstants.USERID, cliArgsParser.getAppUser());
		cf.setStringProperty(WMQConstants.PASSWORD, cliArgsParser.getAppPassword());
		
		// Create JMS objects
		context = cf.createContext();
		destination = context.createQueue("queue:///" + cliArgsParser.getQueueName());
		
		// GET-ALL ...
		consumer = context.createConsumer(destination); // autoclosable
		
		// while not END-OF-QUEUE
		boolean endOfQueue = false;
		while (!endOfQueue) {
			String receivedMessageBody = consumer.receiveBody(String.class, cliArgsParser.TIMEOUT_CONSUMER_RECEIVE);
			if (receivedMessageBody==null) {
				endOfQueue = true;
				receivedMessageBody = new String("");
			} else {
				messages.add(receivedMessageBody);
			}			
		}
		
		return messages;
	}
}
