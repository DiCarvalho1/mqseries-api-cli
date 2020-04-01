package br.com.pabloraimundo.jira_connection;

import br.com.pabloraimundo.jira_api.*;
import br.com.pabloraimundo.util.MessageLog;
import br.com.pabloraimundo.util.ExceptionsMessages;
import br.com.pabloraimundo.workflow_manager.ManagerArgsParse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Jira_Rest {

    ManagerArgsParse managerArgsParse = null;

    public Jira_Rest(ManagerArgsParse managerArgsParse) {
        this.managerArgsParse = managerArgsParse;
    }

    public void ValidacaoSucesso(String idOrquestrador, String customFieldId, String apiChangeman) {

        List<Fields> customFieldValue = null;

        try {
            customFieldValue = Get.GetJiraValues(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, managerArgsParse.getCustomFieldId());
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoReceberValorDoCustomField(e));
        }

        br.com.pabloraimundo.jira_api.Fields subStatusValue = customFieldValue.stream()
                .filter(x -> "SubStatus".equalsIgnoreCase(x.getName()))
                .findAny()
                .orElse(null);

        br.com.pabloraimundo.jira_api.Fields status = customFieldValue.stream()
                .filter(x -> "Status".equalsIgnoreCase(x.getName()))
                .findAny()
                .orElse(null);

        if (!subStatusValue.getValue().equalsIgnoreCase(SubStatus.EXECUTADO.getDescription())) {
            try {
                System.out.println(MessageLog.UpdateSubStatus(idOrquestrador));
                Put.UpdateSubStatus(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, SubStatus.EMEXECUCAO);
            } catch (Exception e) {
                System.out.println(ExceptionsMessages.ErroAoAtualizarSubStatus(e));
            }

            String jiraComment = JiraCommentWorkSucces(status.getValue(),"Validação", managerArgsParse.getChangemanPackage(), SubStatus.EMEXECUCAO);

            try {
                System.out.println(MessageLog.PostComment(idOrquestrador));
                Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
            } catch (Exception e) {
                System.out.println(ExceptionsMessages.ErroAoPostarUmcomentario(e));
            }

            System.out.println(MessageLog.UpdateSucessLog(subStatusValue.getValue(), SubStatus.EMEXECUCAO.getDescription()));

        }
        else {
            System.out.println(MessageLog.UpdateFailed(idOrquestrador));
            System.out.println("Não atualizado. Impossível atualizar para EM EXECUÇÃO, SubStatus se encontra EXECUTADO");
        }
    }

    public void ExecucaoSucesso(String idOrquestrador, String customFieldId, String apiChangeman) {

        List<br.com.pabloraimundo.jira_api.Fields> customFieldValue = null;

        try {
            customFieldValue = Get.GetJiraValues(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, managerArgsParse.getCustomFieldId());
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoReceberValorDoCustomField(e));
        }

        br.com.pabloraimundo.jira_api.Fields subStatusValue = customFieldValue.stream()
                .filter(x -> "SubStatus".equalsIgnoreCase(x.getName()))
                .findAny()
                .orElse(null);

        br.com.pabloraimundo.jira_api.Fields status = customFieldValue.stream()
                .filter(x -> "Status".equalsIgnoreCase(x.getName()))
                .findAny()
                .orElse(null);

        if (!subStatusValue.getValue().equalsIgnoreCase(SubStatus.EXECUTADO.getDescription())) {
            try {
                System.out.println(MessageLog.UpdateSubStatus(idOrquestrador));
                Put.UpdateSubStatus(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, SubStatus.EXECUTADO);
            } catch (Exception e) {
                System.out.println(ExceptionsMessages.ErroAoAtualizarSubStatus(e));
            }

            String jiraComment = JiraCommentWorkSucces(status.getValue(),"Execução", managerArgsParse.getChangemanPackage(), SubStatus.EXECUTADO);

            try {
                System.out.println(MessageLog.PostComment(idOrquestrador));
                Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
            } catch (Exception e) {
                System.out.println(ExceptionsMessages.ErroAoPostarUmcomentario(e));
            }

            System.out.println(MessageLog.UpdateSucessLog(subStatusValue.getValue(), SubStatus.EXECUTADO.getDescription()));

        }
        else {
            System.out.println(MessageLog.UpdateFailed(idOrquestrador));
            System.out.println("Status atual " + subStatusValue.getValue() + " Não atualizado, SubStatus já está EXECUTADO");
        }
    }

    public void ValidacaoFalha(String idOrquestrador){
        try {
            Put.UpdateSubStatus(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, SubStatus.FALHAREQUISICAO);
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoAtualizarSubStatus(e));
        }

        List<br.com.pabloraimundo.jira_api.Fields> customFieldValue = null;

        try {
            customFieldValue = Get.GetJiraValues(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, managerArgsParse.getCustomFieldId());
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoReceberValorDoCustomField(e));
        }

        br.com.pabloraimundo.jira_api.Fields status = customFieldValue.stream()
                .filter(x -> "Status".equalsIgnoreCase(x.getName()))
                .findAny()
                .orElse(null);

        String jiraComment = JiraCommentWorkFailed(status.getValue(), "Validação", managerArgsParse.getCodigoRetorno(), managerArgsParse.getChangemanPackage(), SubStatus.FALHAREQUISICAO);

        try {
            System.out.println(MessageLog.PostComment(idOrquestrador));
            Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoPostarUmcomentario(e));
        }

        System.out.println(MessageLog.UpdateFailedLog(SubStatus.FALHAREQUISICAO.getDescription()));
    }

    public void ExecucaoFalha(String idOrquestrador){
        try {
            Put.UpdateSubStatus(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, SubStatus.FALHAEXECUCAO);
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoAtualizarSubStatus(e));
        }

        List<br.com.pabloraimundo.jira_api.Fields> customFieldValue = null;

        try {
            customFieldValue = Get.GetJiraValues(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, managerArgsParse.getCustomFieldId());
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoReceberValorDoCustomField(e));
        }

        br.com.pabloraimundo.jira_api.Fields status = customFieldValue.stream()
                .filter(x -> "Status".equalsIgnoreCase(x.getName()))
                .findAny()
                .orElse(null);

        String jiraComment =  JiraCommentWorkFailed(status.getValue(),"Execução", managerArgsParse.getCodigoRetorno(), managerArgsParse.getChangemanPackage(), SubStatus.FALHAEXECUCAO);

        try {
            System.out.println(MessageLog.PostComment(idOrquestrador));
            Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoPostarUmcomentario(e));
        }

        System.out.println(MessageLog.UpdateFailedLog(SubStatus.FALHAEXECUCAO.getDescription()));
    }

    private String JiraCommentWorkSucces(String status, String acao, String pacote, SubStatus subStatus) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();

        String inputJson = "{\n" +
                "\"body\": \" *+" + status +"+* \\n" +
                "Status: *{color:#00875a}SUCESSO {color}* \\n" +
                "Ação: *"+ acao + "* \\n" +
                "Return Code: *00* \\n" +
                "Mensagem: *" + managerArgsParse.getReturnMessage() + "* \\n" +
                "Sub-Status: *" + subStatus.getDescription() + "* \" \n" +
                "}";

        return inputJson;
    }

    private String JiraCommentWorkFailed(String status, String acao, String returnCode, String pacote, SubStatus subStatus) {
        String inputJson = "{\n" +

                "\"body\": \" *+" + status + "+* \\n" +
                "Status: *{color:#de350b}FALHA{color}* \\n" +
                "Ação: *" + acao + "* \\n" +
                "Return Code: *" + returnCode + "* \\n" +
                "Mensagem: *"+ managerArgsParse.getReturnMessage() +"* \\n" +
                "Sub-Status: *" + subStatus.getDescription() + "* \" \n" +
                "}";

        return inputJson;
    }
}
