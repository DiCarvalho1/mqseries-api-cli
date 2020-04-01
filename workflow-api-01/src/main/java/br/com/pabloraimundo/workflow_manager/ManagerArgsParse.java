package br.com.pabloraimundo.workflow_manager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.com.josemarsilva.mqseries_api_cli.CliArgsParser;

public class ManagerArgsParse {
	
	// Constants ...
	public final static String APP_NAME = new String("jira-api");
	public final static String APP_VERSION = new String("v.2020.03.17.1209");
	public final static String APP_USAGE = new String(APP_NAME + " [<args-options-list>] - "+ APP_VERSION);
		
//	Jira api properties...
	String apiChangeman;
    String tipoResposta;
    String codigoRetorno;
    String changemanPackage;
    String ticketUsername;
    String ticketId;
    String siteMaquina;
    String instancia;

	String returnMessage;
    String customFieldId;
    
//  Jira arg Properties...
    String url; 
    String user; 
    String password; 
    String subStatusId;
    
	CliArgsParser cliArgsParser = null;
        
    public ManagerArgsParse( String[] args, CliArgsParser cliArgsParser ) {

    	this.cliArgsParser = cliArgsParser;
    	
		// Options creating ...
		Options options = new Options();
		
		
		//Jira Options configuring  ...
        Option helpOption = Option.builder("help") 
        		.longOpt("help") 
        		.required(false) 
        		.desc("shows usage help message. See more https://github.com/josemarsilva/mqseries-api-cli") 
        		.build(); 
        Option urlOption = Option.builder("url")
        		.longOpt("url") 
        		.required(true) 
        		.desc("Jira url")
        		.hasArg()
        		.build();
        Option userOption = Option.builder("user")
        		.longOpt("user") 
        		.required(true) 
        		.desc("Jira username")
        		.hasArg()
        		.build();
        Option passwordOption = Option.builder("password")
        		.longOpt("password") 
        		.required(true)
        		.desc("Jira password")
        		.hasArg()
        		.build();
        Option subStatusIdOption = Option.builder("subStatusId")
        		.longOpt("subStatusId") 
        		.required(true) 
        		.desc("Sub-Status Id")
        		.hasArg()
        		.build();
        
        //Mq Options Configuring...
        Option hostOption = Option.builder("H")
        		.longOpt("host") 
        		.required(true) 
        		.desc("Host name or IP address. Ex: -H 127.0.0.1")
        		.hasArg()
        		.build();
        Option portOption = Option.builder("P")
        		.longOpt("port") 
        		.required(true)
        		.desc("Listener port number for your queue manager. Ex: -P 1414")
        		.hasArg()
        		.build();
        Option channelOption = Option.builder("C")
        		.longOpt("channel") 
        		.required(true) 
        		.desc("Channel name. Ex: -C DEV.APP.SVRCONN")
        		.hasArg()
        		.build();
        Option qmgrOption = Option.builder("Q")
        		.longOpt("qmgr") 
        		.required(true) 
        		.desc("Queue manager name. Ex: -Q QM1")
        		.hasArg()
        		.build();
        Option appPasswordOption = Option.builder("p")
        		.longOpt("app-password") 
        		.required(true) 
        		.desc("Application Password to connect to MQ. Ex: -p passw0rd")
        		.hasArg()
        		.build();
        Option queueNameOption = Option.builder("q")
        		.longOpt("queue-name") 
        		.required(true) 
        		.desc("Queue name mqseries-api-cli uses to put or get messages to and from. Ex: -q DEV.QUEUE.1")
        		.hasArg()
        		.build();
                
		// Options adding configuration ...
        options.addOption(helpOption);
        options.addOption(urlOption);
        options.addOption(userOption);
        options.addOption(passwordOption);
        options.addOption(subStatusIdOption);
        options.addOption(hostOption);
        options.addOption(portOption);
        options.addOption(channelOption);
        options.addOption(qmgrOption);
        options.addOption(appPasswordOption);
        options.addOption(queueNameOption);
        
        // CommandLineParser ...
        CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmdLine = parser.parse(options, args);
			
	        if (cmdLine.hasOption("help")) { 
	            HelpFormatter formatter = new HelpFormatter();
	            formatter.printHelp(APP_USAGE, options);
	            System.exit(0);
	        } else {
	        	
	        	// Set Jira properties from Options ...
	        	this.setUrl( cmdLine.getOptionValue("url", ""));
	        	this.setUser( cmdLine.getOptionValue("user", ""));
	        	this.setPassword( cmdLine.getOptionValue("password",""));
	        	this.setSubStatusId(cmdLine.getOptionValue("subStatusId",""));
	        		        	
	        	// Set MQ properties from Options...
	        	cliArgsParser.setHost( cmdLine.getOptionValue("host", "") );
	        	cliArgsParser.setPort( Integer.parseInt( cmdLine.getOptionValue("port") ) );
	        	cliArgsParser.setChannel( cmdLine.getOptionValue("channel", "") );
	        	cliArgsParser.setQmgr( cmdLine.getOptionValue("qmgr", "") );
	        	cliArgsParser.setAppPassword( cmdLine.getOptionValue("app-password", "") );
	        	cliArgsParser.setQueueName( cmdLine.getOptionValue("queue-name", "") );
	        }
			
		} catch (ParseException e) {
			System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter(); 
            formatter.printHelp(APP_USAGE, options); 
			System.exit(-1);
		} 
        
	}
    

	public String getApiChangeman() {
		return apiChangeman;
	}



	public void setApiChangeman(String apiChangeman) {
		this.apiChangeman = apiChangeman;
	}



	public String getTipoResposta() {
		return tipoResposta;
	}



	public void setTipoResposta(String tipoResposta) {
		this.tipoResposta = tipoResposta;
	}



	public String getCodigoRetorno() {
		return codigoRetorno;
	}



	public void setCodigoRetorno(String codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}



	public String getChangemanPackage() {
		return changemanPackage;
	}



	public void setChangemanPackage(String changemanPackage) {
		this.changemanPackage = changemanPackage;
	}



	public String getTicketUsername() {
		return ticketUsername;
	}



	public void setTicketUsername(String ticketUsername) {
		this.ticketUsername = ticketUsername;
	}



	public String getTicketId() {
		return ticketId;
	}



	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}



	public String getSiteMaquina() {
		return siteMaquina;
	}



	public void setSiteMaquina(String siteMaquina) {
		this.siteMaquina = siteMaquina;
	}



	public String getInstancia() { return instancia;	}



	public void setInstancia(String instancia) { this.instancia = instancia; }



	public String getCustomFieldId() {
		return customFieldId;
	}



	public void setCustomFieldId(String customFieldId) {
		this.customFieldId = customFieldId;
	}


	public String getReturnMessage() { return returnMessage; }



	public void setReturnMessage(String returnMessage) { this.returnMessage = returnMessage; }



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubStatusId() {
		return subStatusId;
	}


	public void setSubStatusId(String subStatusId) {
		this.subStatusId = subStatusId;
	}
	

}
