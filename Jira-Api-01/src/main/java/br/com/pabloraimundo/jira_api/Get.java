package br.com.pabloraimundo.jira_api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Get {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Api Jira");
	}
	
	public static List<Fields> GetIssueStatus(String url, String user, String password, String issue) throws Exception {
		List<Fields> jiraValues = new ArrayList<Fields>();
		
		String userAndPassword = user + ":" + password;
		
		String authStr = Base64.getEncoder().encodeToString(userAndPassword.getBytes());
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url + "/rest/api/latest/issue/" + issue);
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + authStr).get(ClientResponse.class);

		if(resp.getStatus() != 200) {
			System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " - Erro ao conectar a api Jira. Possivelmente o numero da Issue informada esta incorreto. Status code: " + resp.getStatus());
		}
		
		String output = resp.getEntity(String.class);
		
		JSONObject jsonObject = new JSONObject(output);
		JSONObject fields = jsonObject.getJSONObject("fields");
		
		JSONObject status = fields.getJSONObject("status");
		String statusValue = status.getString("name");
		
		jiraValues.add(new Fields("Status", statusValue));
		
		return jiraValues;
	}

	public static String GetIssueName(String url, String user, String password, String issue) throws JSONException {
		String issueName = "";

		String userAndPassword = user + ":" + password;

		String authStr = Base64.getEncoder().encodeToString(userAndPassword.getBytes());
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url + "/rest/api/latest/issue/" + issue);
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + authStr).get(ClientResponse.class);

		if(resp.getStatus() != 200) {
			System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " - Erro ao conectar a api Jira. Possivelmente o numero da Issue informada esta incorreto. Status code: " + resp.getStatus());
		}

		String output = resp.getEntity(String.class);

		JSONObject jsonObject = new JSONObject(output);
		issueName = jsonObject.getString("key");

		return  issueName;
	}


}