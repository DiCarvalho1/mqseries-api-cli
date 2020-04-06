package br.com.pabloraimundo.jira_api;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Post {
	
	public static void PostComment(String url, String user, String password, String issue, String inputJson) throws Exception {
		
		String userAndPassword = user + ":" + password;
		
		String authStr = Base64.getEncoder().encodeToString(userAndPassword.getBytes());
	    
		String postEndpoint = url + "/rest/api/2/issue/" + issue + "/comment";
	
	    CloseableHttpClient httpclient = HttpClients.createDefault();
	    
	    HttpPost httpPost = new HttpPost(postEndpoint);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    httpPost.setHeader("Authorization", "Basic " + authStr);
	    	
	    StringEntity stringEntity = new StringEntity(inputJson, "UTF-8");
	    httpPost.setEntity(stringEntity);
	    	
	    HttpResponse response = httpclient.execute(httpPost);
	
//	    System.out.println(response.getStatusLine().getStatusCode());

		if(response.getStatusLine().getStatusCode() != 201) {
			System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " Erro ao postar comentário junto à api Jira. Status Code: " + response.getStatusLine().getStatusCode());
		}
	
	}
}
