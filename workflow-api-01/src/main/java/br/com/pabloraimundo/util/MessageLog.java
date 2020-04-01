package br.com.pabloraimundo.util;

import br.com.pabloraimundo.jira_api.SubStatus;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageLog {

    public static String Horario(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + " ";
    }

    public static String UpdateSubStatus(String idOrquestrador){
        return Horario() + "ticket " + idOrquestrador + " atualizado";
    }

    public static String PostComment(String idOrquestrador){
        return Horario() + "Inserindo comentário no ticket " + idOrquestrador;
    }

    public static String UpdateFailed(String idOrquestrador){
        return Horario() + "Falha na atualização";
    }

    public static String UpdateSucessLog(String currentSubStatusValue, String subStatusUpdateDescription){
        return Horario() + "SubStatus atualizado De " + currentSubStatusValue + " para " + subStatusUpdateDescription;
    }

    public static String UpdateFailedLog(String subStatusUpdateDescription){
        return Horario() + "Não atualizado, " + subStatusUpdateDescription;
    }

}
