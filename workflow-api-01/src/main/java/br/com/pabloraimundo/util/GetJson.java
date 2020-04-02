package br.com.pabloraimundo.util;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetJson {

    public static String GetJiraComment(String wichMessage){
        String comentarioBody = null;
        String dir = System.getProperty("user.dir");
        try {
            String comentarios = Files.readString(Paths.get(dir + "\\comentarios.json"));
            JSONObject jsonObject = new JSONObject(comentarios);
            JSONObject validacaoSucesso = jsonObject.getJSONObject(wichMessage);
            comentarioBody = validacaoSucesso.getString("message_template");
        } catch (IOException e) {
            System.out.println(ExceptionsMessages.ErroAoBuscarArquivo(e));
        } catch (JSONException e) {
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
