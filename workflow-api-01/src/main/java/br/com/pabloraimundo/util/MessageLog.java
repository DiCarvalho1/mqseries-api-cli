package br.com.pabloraimundo.util;

import br.com.pabloraimundo.jira_connection.Jira_Rest;
import br.com.pabloraimundo.workflow_manager.ManagerArgsParse;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageLog {

    public static void SysOut(String alert){
        System.out.println(alert);
    }

    public static String Horario(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " - ";
    }

    public static String UpdateStatus(ManagerArgsParse managerArgsParse , String idOrquestrador){
        Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
        return Horario() + "Transição de status realizada no Ticket: " + jira_rest.GetJiraIssueName(idOrquestrador) + " (" + idOrquestrador + ")";
    }

    public static String UpdateStatusFailed(ManagerArgsParse managerArgsParse, String idOrquestrador, String statusCode){
        Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
        return Horario() + "Erro durante transição de status no Ticket: " + jira_rest.GetJiraIssueName(idOrquestrador) + " (" + idOrquestrador + "). Status Code: " + statusCode;
    }

    public static String UpdateStatusWithFailure(ManagerArgsParse managerArgsParse, String idOrquestrador){
        Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
        return Horario() + "Mensagem de erro vindo da fila MQ, transição de status realizada no Ticket: " + jira_rest.GetJiraIssueName(idOrquestrador) + " (" + idOrquestrador + ")";
    }

    public static String PostCommentSucceded(ManagerArgsParse managerArgsParse, String idOrquestrador){
        Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
        return Horario() + "Comentário inserido no ticket " + jira_rest.GetJiraIssueName(idOrquestrador) + " (" + idOrquestrador + ")";
    }

    public static String PostCommentFailed(ManagerArgsParse managerArgsParse, String idOrquestrador, String statusCode){
        Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
        return Horario() + "Erro ao postar comentário junto à api Jira. Ticket " + jira_rest.GetJiraIssueName(idOrquestrador) + " (" + idOrquestrador + "). Status code: " + statusCode;
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

    public static String ListaDeComponentesUpdated(ManagerArgsParse managerArgsParse, String idOrquestrador){
        Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
        return Horario() + "Lista de componentes atualizada no Ticket: " + jira_rest.GetJiraIssueName(idOrquestrador) + " (" + idOrquestrador + ")";
    }

    public static String ListaDeComponentesUpdateFailed(String updateStatusCode){
        return Horario() + "Erro ao atualizar lista de componentes junto à api Jira. Status Code: " + updateStatusCode;
    }

}
