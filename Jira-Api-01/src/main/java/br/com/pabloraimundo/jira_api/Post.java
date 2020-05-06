package br.com.pabloraimundo.jira_api;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Post {
	
	public static Integer PostComment(String url, String user, String password, String issue, String inputJson) throws Exception {
		
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

		return  response.getStatusLine().getStatusCode();
	}

	public static Integer StatusTransition(String url, String user, String password, String issue, String transitionId) throws IOException {

		String userAndPassword = user + ":" + password;

		String authStr = Base64.getEncoder().encodeToString(userAndPassword.getBytes());

		String postEndpoint = url + "/rest/api/2/issue/" + issue + "/transitions";

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(postEndpoint);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("Authorization", "Basic " + authStr);

		String inputJson = "{\n" +
				"\"transition\": {\n" +
					"\"id\": \"" + transitionId + "\"\n" +
					"}\n" +
				"}";

		StringEntity stringEntity = new StringEntity(inputJson, "UTF-8");
		httpPost.setEntity(stringEntity);

		HttpResponse response = httpclient.execute(httpPost);

		return response.getStatusLine().getStatusCode();

	}
}
