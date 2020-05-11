package br.com.pabloraimundo.jira_connection;

import br.com.pabloraimundo.jira_api.Put;
import br.com.pabloraimundo.util.ExceptionsMessages;
import br.com.pabloraimundo.util.GetJson;
import br.com.pabloraimundo.util.MessageLog;
import br.com.pabloraimundo.workflow_manager.Jira_Manager;
import br.com.pabloraimundo.workflow_manager.ManagerArgsParse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Lista_De_Componentes {

    ManagerArgsParse managerArgsParse = null;

    public Lista_De_Componentes(ManagerArgsParse managerArgsParse){
        this.managerArgsParse = managerArgsParse;
    }

    public void Update(String message){
        List<String> messages = new ArrayList<>();
        messages = StringToList(message);

        String componenteName = "";
        String tipo = "";
        String status = "";
        String linguagem = "";
        String proc = "";
        String chave = "";
        String filler = "";

        String customFieldName = GetJson.GetCustomFieldJson("lista_de_componentes");

        String json = "{\n" +
                "\"fields\" : { \"" + customFieldName + "\" : \" {panel:title=Lista de Componentes} || NOME || TIPO || STATUS || LINGUAGEM || PROC || CHAVE || FILLER || \\n";

        String jsonComponentes = "";

        for(String componente : messages){

            componenteName = componente.substring(0, 8);
            tipo = componente.substring(8, 11);
            status = componente.substring(11, 19);
            linguagem = componente.substring(19, 27);
            proc = componente.substring(27, 35);
            chave = componente.substring(35, 43);
            filler = componente.substring(43);

            jsonComponentes = jsonComponentes + ListaDeComponentesConcatenada(componenteName, tipo, status, linguagem, proc, chave, filler) + "\\n ";
        }

        json = json + jsonComponentes + "\" {panel} } }";

        String idOrquestrador = Jira_Manager.SetIdOrquestrador(message.substring(43,48));

        Integer updateStatusCode = 0;

        updateStatusCode = Put.UpdateListaDeComponentes(managerArgsParse.getUrl(), managerArgsParse.getUser(), managerArgsParse.getPassword(), idOrquestrador, json);

        Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
        br.com.pabloraimundo.jira_api.Fields statusTicket = jira_rest.GetStatusTicket(idOrquestrador);

        if(updateStatusCode == 204) {
            MessageLog.SysOut(MessageLog.ListaDeComponentesUpdated(managerArgsParse, idOrquestrador));
        } else {
            MessageLog.SysOut(MessageLog.ListaDeComponentesUpdateFailed(updateStatusCode.toString()));
        }

    }

    private List<String> StringToList(String message){
        List<String> messages = new ArrayList<>();

        int numeroDeComponentes = Integer.parseInt(message.substring(48,52));
        String componentes = message.substring(52);

        int indiceInicioComponente = 0;
        for (int i = 1; i <= numeroDeComponentes; i++){
            int tamanhoStringComponente = 80 * i;

            messages.add(componentes.substring(indiceInicioComponente, tamanhoStringComponente));

            indiceInicioComponente = tamanhoStringComponente;
        }

        return  messages;
    }

    private String ListaDeComponentesConcatenada(String componente, String tipo, String status, String linguagem, String proc, String chave, String filler){
        String colunas = " | " + componente + " | " + tipo + " | " + status + " | " + linguagem + " | " + proc + " | " + chave + " | " + filler + " | ";

        return colunas;
    }
}
