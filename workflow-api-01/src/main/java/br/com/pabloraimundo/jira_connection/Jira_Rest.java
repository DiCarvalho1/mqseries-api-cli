package br.com.pabloraimundo.jira_connection;

import br.com.pabloraimundo.jira_api.*;
import br.com.pabloraimundo.util.GetProperties;
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

            String jiraComment = ReplaceJiraComment("validacaosucesso");

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

            String jiraComment = ReplaceJiraComment("execucaosucesso");

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

        String jiraComment = ReplaceJiraComment("validacaoerro");

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

        String jiraComment =  ReplaceJiraComment("execucaoerro");

        try {
            System.out.println(MessageLog.PostComment(idOrquestrador));
            Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
        } catch (Exception e) {
            System.out.println(ExceptionsMessages.ErroAoPostarUmcomentario(e));
        }

        System.out.println(MessageLog.UpdateFailedLog(SubStatus.FALHAEXECUCAO.getDescription()));
    }

    private String ReplaceJiraComment(String wichMessage){
        String jiraComment = GetProperties.GetJiraComment(wichMessage);

        jiraComment = jiraComment.replace("<***returntype***>", managerArgsParse.getCodigoRetorno());
        jiraComment = jiraComment.replace("<***returncode***>", managerArgsParse.getCodigoRetorno());
        jiraComment = jiraComment.replace("<***mensagemMQ***>", managerArgsParse.getReturnMessage());
        jiraComment = jiraComment.replace("<***siteMQ***>", managerArgsParse.getSiteMaquina().replaceAll("^\\s+", ""));
        jiraComment = jiraComment.replace("<***instanciaMQ***>", managerArgsParse.getInstancia().replaceAll("^\\s+", ""));

        return jiraComment;
    }
}
