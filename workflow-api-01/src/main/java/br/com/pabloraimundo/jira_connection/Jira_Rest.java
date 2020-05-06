package br.com.pabloraimundo.jira_connection;

import br.com.pabloraimundo.jira_api.*;
import br.com.pabloraimundo.util.GetJson;
import br.com.pabloraimundo.util.MessageLog;
import br.com.pabloraimundo.util.ExceptionsMessages;
import br.com.pabloraimundo.workflow_manager.ManagerArgsParse;
import org.codehaus.jettison.json.JSONException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Jira_Rest {

    ManagerArgsParse managerArgsParse = null;

    public Jira_Rest(ManagerArgsParse managerArgsParse) {
        this.managerArgsParse = managerArgsParse;
    }

    public void ValidacaoSucesso(String idOrquestrador) {
        br.com.pabloraimundo.jira_api.Fields status = GetStatusTicket(idOrquestrador);

        String jiraComment = ReplaceJiraComment("validacaosucesso", status.getValue());
        if (jiraComment == null ){
            MessageLog.SysOut(ExceptionsMessages.ErroAoSubstituirCamposNoArquivoComentariosJson());
            return;
        }

        Integer commentStatusCode = 0;

        try {
            commentStatusCode = Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
        } catch (Exception e) {
            MessageLog.SysOut(ExceptionsMessages.ErroAoPostarUmcomentario(e));
        }

        if (commentStatusCode == 201){
            MessageLog.SysOut(MessageLog.PostCommentSucceded(managerArgsParse, idOrquestrador));
        } else {
            MessageLog.SysOut(MessageLog.PostCommentFailed(managerArgsParse, idOrquestrador, commentStatusCode.toString()));
        }
    }


    public void ExecucaoSucesso(String idOrquestrador) {
        br.com.pabloraimundo.jira_api.Fields status = GetStatusTicket(idOrquestrador);

        Integer transitionStatusCode = 0;
        Integer commentStatusCode = 0;

        try {
            String transitionId = GetJson.GetStatusTransition(status.getValue(), true);
            transitionStatusCode = Post.StatusTransition(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, transitionId);
        } catch (Exception e) {
            MessageLog.SysOut(ExceptionsMessages.ErroAoAtualizarStatus(e));
        }

        if (transitionStatusCode == 204){
            MessageLog.SysOut(MessageLog.UpdateStatus(managerArgsParse, idOrquestrador));

            String jiraComment = ReplaceJiraComment("execucaosucesso", status.getValue());
            if (jiraComment == null ){
                MessageLog.SysOut(ExceptionsMessages.ErroAoSubstituirCamposNoArquivoComentariosJson());
                return;
            }

            try {
                commentStatusCode = Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
            } catch (Exception e) {
                MessageLog.SysOut(ExceptionsMessages.ErroAoPostarUmcomentario(e));
            }

            if (commentStatusCode == 201){
                MessageLog.SysOut(MessageLog.PostCommentSucceded(managerArgsParse, idOrquestrador));
            } else {
                MessageLog.SysOut(MessageLog.PostCommentFailed(managerArgsParse, idOrquestrador, commentStatusCode.toString()));
            }

        } else {
            MessageLog.SysOut(MessageLog.UpdateStatusFailed(managerArgsParse, idOrquestrador, transitionStatusCode.toString()));
        }
    }

    public void ValidacaoFalha(String idOrquestrador){
        br.com.pabloraimundo.jira_api.Fields status = GetStatusTicket(idOrquestrador);

        Integer transitionStatusCode = 0;
        Integer commentStatusCode = 0;

        try {
            String transitionId = GetJson.GetStatusTransition(status.getValue(), false);
            transitionStatusCode = Post.StatusTransition(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, transitionId);
        } catch (Exception e) {
            MessageLog.SysOut(ExceptionsMessages.ErroAoAtualizarStatus(e));
        }

        if (transitionStatusCode == 204) {
            MessageLog.SysOut(MessageLog.UpdateStatusWithFailure(managerArgsParse, idOrquestrador));

            String jiraComment = ReplaceJiraComment("validacaoerro", status.getValue());
            if (jiraComment == null) {
                MessageLog.SysOut(ExceptionsMessages.ErroAoSubstituirCamposNoArquivoComentariosJson());
                return;
            }

            try {
                commentStatusCode = Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
            } catch (Exception e) {
                MessageLog.SysOut(ExceptionsMessages.ErroAoPostarUmcomentario(e));
            }

            if (commentStatusCode == 201) {
                MessageLog.SysOut(MessageLog.PostCommentSucceded(managerArgsParse, idOrquestrador));
            } else {
                MessageLog.SysOut(MessageLog.PostCommentFailed(managerArgsParse, idOrquestrador, commentStatusCode.toString()));
            }

            MessageLog.SysOut(MessageLog.UpdateFailedLog(SubStatus.FALHAREQUISICAO.getDescription()));
        } else {
            MessageLog.SysOut(MessageLog.UpdateStatusFailed(managerArgsParse, idOrquestrador, transitionStatusCode.toString()));
        }
    }

    public void ExecucaoFalha(String idOrquestrador){
        br.com.pabloraimundo.jira_api.Fields status = GetStatusTicket(idOrquestrador);

        Integer commentStatusCode = 0;
        Integer transitionStatusCode = 0;

        try {
            String transitionId = GetJson.GetStatusTransition(status.getValue(), false);
            transitionStatusCode = Post.StatusTransition(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, transitionId);
        } catch (Exception e) {
            MessageLog.SysOut(ExceptionsMessages.ErroAoAtualizarStatus(e));
        }

        if (transitionStatusCode == 204) {
            MessageLog.SysOut(MessageLog.UpdateStatusWithFailure(managerArgsParse, idOrquestrador));

            String jiraComment = ReplaceJiraComment("execucaoerro", status.getValue());
            if (jiraComment.length() < 0) {
                MessageLog.SysOut(ExceptionsMessages.ErroAoSubstituirCamposNoArquivoComentariosJson());
                return;
            }

            try {
                commentStatusCode = Post.PostComment(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, jiraComment);
            } catch (Exception e) {
                MessageLog.SysOut(ExceptionsMessages.ErroAoPostarUmcomentario(e));
            }

            if (commentStatusCode == 201) {
                MessageLog.SysOut(MessageLog.PostCommentSucceded(managerArgsParse, idOrquestrador));
            } else {
                MessageLog.SysOut(MessageLog.PostCommentFailed(managerArgsParse, idOrquestrador, commentStatusCode.toString()));
            }

            MessageLog.SysOut(MessageLog.UpdateFailedLog(SubStatus.FALHAEXECUCAO.getDescription()));
        } else {
            MessageLog.SysOut(MessageLog.UpdateStatusFailed(managerArgsParse, idOrquestrador, transitionStatusCode.toString()));
        }
    }

    private String ReplaceJiraComment(String array, String status){
        String jiraComment = GetJson.GetJiraComment(array, status);

        if (jiraComment != null) {
            jiraComment = jiraComment.replace("<***status***>", status);
            jiraComment = jiraComment.replace("<***returntype***>", managerArgsParse.getTipoResposta().trim());
            jiraComment = jiraComment.replace("<***returncode***>", managerArgsParse.getCodigoRetorno().trim());
            jiraComment = jiraComment.replace("<***mensagemMQ***>", managerArgsParse.getReturnMessage().trim());
            jiraComment = jiraComment.replace("<***siteMQ***>", managerArgsParse.getSiteMaquina().trim());
            jiraComment = jiraComment.replace("<***instanciaMQ***>", managerArgsParse.getInstancia().trim());
        } else {
            MessageLog.SysOut(MessageLog.NoCommentsFound());
        }

        return jiraComment;
    }

    public String GetJiraIssueName(String idOrquestrador){
        String issueName = "";

        try {
            issueName = Get.GetIssueName(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador);
        } catch (JSONException e) {
            MessageLog.SysOut(ExceptionsMessages.ErroAoReceberValorDoCustomField(e));
        }

        return issueName;
    }

    public Fields GetStatusTicket(String idOrquestrador){
        List<br.com.pabloraimundo.jira_api.Fields> customFieldValue = null;

        try {
            customFieldValue = Get.GetJiraValues(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, managerArgsParse.getSubStatusId());
        } catch (Exception e) {
            MessageLog.SysOut(ExceptionsMessages.ErroAoReceberValorDoCustomField(e));
        }

        br.com.pabloraimundo.jira_api.Fields status = customFieldValue.stream()
                .filter(x -> "Status".equalsIgnoreCase(x.getName()))
                .findAny()
                .orElse(null);

        return  status;
    }
}
