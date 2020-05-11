package br.com.pabloraimundo.workflow_manager;

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
                jira_rest.ValidacaoSucesso(idOrquestrador);
            }
            else {
            	Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
            	jira_rest.ValidacaoFalha(idOrquestrador);
            }
        }
        
        if (managerArgsParse.getTipoResposta().equalsIgnoreCase("E")) {
        	
			if (managerArgsParse.getCodigoRetorno().equals("00")) {
				Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
				jira_rest.ExecucaoSucesso(idOrquestrador);
			}
			else {
				Jira_Rest jira_rest = new Jira_Rest(managerArgsParse);
				jira_rest.ExecucaoFalha(idOrquestrador);
			}
        }
        
    }
    
    public static String SetIdOrquestrador(String idOrquestrador) {
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
