package br.com.pabloraimundo.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GetJson {

    String ticket_status;
    String status_mainframe;
    String message_template;

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public String getStatus_mainframe() {
        return status_mainframe;
    }

    public void setStatus_mainframe(String status_mainframe) {
        this.status_mainframe = status_mainframe;
    }

    public String getMessage_template() {
        return message_template;
    }

    public void setMessage_template(String message_template) {
        this.message_template = message_template;
    }

    public static String GetJiraComment(String array, String status){
        String comentarioBody = null;
        String dir = System.getProperty("user.dir");
        try {
            String comentarios = Files.readString(Paths.get(dir + "\\comentarios.json"));
            JSONObject jsonObject = new JSONObject(comentarios);
            JSONArray jsonArray = jsonObject.getJSONArray(array);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<GetJson>>(){}.getType();
            List<GetJson> getJsons = gson.fromJson(jsonArray.toString(), listType);

            System.out.println(MessageLog.SearchingForStatus(status, array));
            GetJson ticketStatus = getJsons.stream().
                    filter(x -> x.getTicket_status().equalsIgnoreCase(status)).
                    findAny().
                    orElse(null);

            if (ticketStatus == null){
                System.out.println(MessageLog.SearchingForStatusFailed());
            } else {
                comentarioBody = ticketStatus.getMessage_template();
            }

        } catch (IOException e) {
            System.out.println(ExceptionsMessages.ErroAoBuscarArquivo(e));
        }
        catch (JSONException e) {
            System.out.println(ExceptionsMessages.ErroAoConverterArquivoJson(e));
        }

        return  comentarioBody;
    }

    public static String GetCustomFieldJson(String customFieldName){
        String key = null;
        String dir = System.getProperty("user.dir");
        try {
            String json = Files.readString(Paths.get(dir + "\\customFields.json"));
            JSONObject jsonObject = new JSONObject(json);
            JSONObject customFields = jsonObject.getJSONObject("customfields");
            JSONObject statusMainframe = customFields.getJSONObject(customFieldName);
            key = statusMainframe.getString("key");

        } catch (IOException e) {
            System.out.println(ExceptionsMessages.ErroAoBuscarArquivo(e));
        } catch (JSONException e) {
            System.out.println(ExceptionsMessages.ErroAoConverterArquivoJson(e));
        }

        return key;
    }
}
