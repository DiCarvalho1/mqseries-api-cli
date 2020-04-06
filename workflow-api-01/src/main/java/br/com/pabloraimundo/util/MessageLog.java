package br.com.pabloraimundo.util;

import br.com.pabloraimundo.jira_api.SubStatus;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageLog {

    public static String Horario(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " - ";
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

    public static String SearchingForStatus(String status, String array){
        return Horario() + "Buscando no arquivo JSON \"comentarios\" por ticket_status: " + status + " dentro do array: " + array;
    }

    public static String SearchingForStatusFailed(){
        return Horario() + "Nenhum ticket_status encontrado no arquivo JSON para o status atual do ticket";
    }

    public static String NoCommentsFound(){
        return Horario() + "Impossível continuar sem algum comentário para adicionar ao Jira, verifique o Status do ticket e os nomes nos ticket_status do arquivo JSON";
    }

}
