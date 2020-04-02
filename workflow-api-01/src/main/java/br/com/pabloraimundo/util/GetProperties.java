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

public class GetProperties {

    public static String GetJiraComment(String wichMessage){
        String comentarioBody = null;
        String dir = System.getProperty("user.dir");
        try {
            String comentarios = Files.readString(Paths.get(dir + "\\comentarios.json"));
            JSONObject jsonObject = new JSONObject(comentarios);
            JSONObject validacaoSucesso = jsonObject.getJSONObject(wichMessage);
            comentarioBody = validacaoSucesso.getString("message_template");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  comentarioBody;
    }
}
