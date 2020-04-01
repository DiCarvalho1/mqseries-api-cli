package br.com.pabloraimundo.workflow_manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.pabloraimundo.jira_api.*;
import br.com.pabloraimundo.jira_connection.Jira_Rest;

public class Jira_Manager {
	
	ManagerArgsParse managerArgsParse = null;
	    
    public Jira_Manager(ManagerArgsParse managerArgsParse) {
		super();
		this.managerArgsParse = managerArgsParse;
	}

	public void Manager() {
		
		String idOrquestrador = SetIdOrquestrador(managerArgsParse.getTicketId());
        
        if (managerArgsParse.getTipoResposta().equalsIgnoreCase("V")){
        	
            if(managerArgsParse.getCodigoRetorno().equals("00")) {
				Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
                jira_rest.ValidacaoSucesso(idOrquestrador, managerArgsParse.getCustomFieldId(), managerArgsParse.getApiChangeman());
            }
            else {
            	Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
            	jira_rest.ValidacaoFalha(idOrquestrador);
            }
        }
        
        if (managerArgsParse.getTipoResposta().equalsIgnoreCase("E")) {
        	
			if (managerArgsParse.getCodigoRetorno().equals("00")) {
				Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
				jira_rest.ExecucaoSucesso(idOrquestrador, managerArgsParse.getCustomFieldId(), managerArgsParse.getApiChangeman());
			}
			else {
				Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
				jira_rest.ExecucaoFalha(idOrquestrador);
			}
        }
        
    }
    
    private static String SetIdOrquestrador(String idOrquestrador) {
    	int OrquestradorNumero = 0;

    	try {
			OrquestradorNumero = Integer.parseInt(idOrquestrador);
		}
		catch (Exception ex){
			System.out.println("Erro ao tentar converter idOrquestrador: " + idOrquestrador + ". Erro: " + ex);
		}

        return idOrquestrador = Integer.toString(OrquestradorNumero);
    }


    



	

	
}
