package br.com.josemarsilva.mqseries_api_cli;

import java.util.List;

/*
 * App class is public static void main( String[] args) class
 * 
 * @author josemarsilva@yahoo.com.br
 * @date   2020-02-29
 * 
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	String strCommandLineArguments = new String("");
    	for (int i=0;i<args.length;i++) {
    		strCommandLineArguments = new String(strCommandLineArguments.concat(" ").concat(args[i]));
    	}
    	
		// Parser Command Line Arguments ...
		CliArgsParser cliArgsParser = new CliArgsParser(args);
		
		// Create Object Instance ...
		MqseriesApi mqseriesApi = new MqseriesApi(cliArgsParser);

		// Execute Object Instance ...
		mqseriesApi.execute();

    }
    
    public static String GetMessageFromMq(CliArgsParser cliArgsParser) throws Exception    {
    	return MqseriesApi.Get(cliArgsParser);
    }
    
    public static List<String> GetAllMessagesFromMq(CliArgsParser cliArgsParser) throws Exception {
    	return MqseriesApi.GetAll(cliArgsParser);
    }
    
}
