package br.com.pabloraimundo.jira_api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
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
	
	public static List<Fields> GetJiraValues(String url, String user, String password, String issue, String customFieldName) throws Exception {
		List<Fields> jiraValues = new ArrayList<Fields>();
		
		String userAndPassword = user + ":" + password;
		
		String authStr = Base64.getEncoder().encodeToString(userAndPassword.getBytes());
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url + "/rest/api/latest/issue/" + issue);
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + authStr).get(ClientResponse.class);

		if(resp.getStatus() != 200) {
			System.err.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + " Erro ao conectar à api Jira. Possivelmente o número da Issue informada está incorreto. Status code: " + resp.getStatus());
		}
		
		String output = resp.getEntity(String.class);
		
		JSONObject jsonObject = new JSONObject(output);
		JSONObject fields = jsonObject.getJSONObject("fields");
		
		JSONObject customField = fields.getJSONObject(customFieldName);
		String customFieldValue = customField.getString("value");
		
		jiraValues.add(new Fields("SubStatus", customFieldValue));
		
		JSONObject status = fields.getJSONObject("status");
		String statusValue = status.getString("name");
		
		jiraValues.add(new Fields("Status", statusValue));
		
		return jiraValues;
	}

}