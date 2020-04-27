package br.com.pabloraimundo.jira_api;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class 	Put {
	
	public static void UpdateSubStatus(String url, String user, String password, String issue, String customFieldId, SubStatus subStatus) {
	
		String userAndPassword = user + ":" + password; 
		
		String authStr = Base64.getEncoder().encodeToString(userAndPassword.getBytes());
	    
		String putEndpoint = url + "/rest/api/2/issue/" + issue;
	
	    CloseableHttpClient httpclient = HttpClients.createDefault();
	
	    HttpPut httpPut = new HttpPut(putEndpoint);
	    httpPut.setHeader("Accept", "application/json");
	    httpPut.setHeader("Content-type", "application/json");
	    httpPut.setHeader("Authorization", "Basic " + authStr);
	    
	    String inputJson = "{\n" +
	        "  \"fields\": {\n" +
	        "  \"" + customFieldId + "\": {\n" +
	        "  \"id\": \"" + subStatus.getValue() + "\"\n" +
	        "} \n" +
	        "} \n" +
	        "}";
	
	    StringEntity stringEntity = new StringEntity(inputJson, "UTF-8");
	    httpPut.setEntity(stringEntity);

		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPut);
		} catch (IOException e) {
			System.out.println("Erro ao executar chamada rest: " + e);
		}

//	    System.out.println(response.getStatusLine().getStatusCode());

		if(response.getStatusLine().getStatusCode() != 204) {
			System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " Erro ao atualizar SubStatus junto à api Jira. Status Code: " + response.getStatusLine().getStatusCode());
		}
	
	}

	public static void UpdateListaDeComponentes(String url, String user, String password, String issue, String inputJson){

		String userAndPassword = user + ":" + password;

		String authStr = Base64.getEncoder().encodeToString(userAndPassword.getBytes());

		String putEndpoint = url + "/rest/api/2/issue/" + issue;

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPut httpPut = new HttpPut(putEndpoint);
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");
		httpPut.setHeader("Authorization", "Basic " + authStr);

		StringEntity stringEntity = new StringEntity(inputJson, "UTF-8");
		httpPut.setEntity(stringEntity);

		HttpResponse response = null;

		try {
			response = httpclient.execute(httpPut);
		} catch (IOException e) {
			System.out.println("Erro ao executar chamada rest: " + e);
		}

//	    System.out.println(response.getStatusLine().getStatusCode());

		if(response.getStatusLine().getStatusCode() != 204) {
			System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " Erro ao atualizar lista de componentes junto à api Jira. Status Code: " + response.getStatusLine().getStatusCode());
		}
	}
	
}