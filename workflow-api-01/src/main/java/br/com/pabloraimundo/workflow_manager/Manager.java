package br.com.pabloraimundo.workflow_manager;

import java.util.List;
import java.util.concurrent.TimeUnit;


import br.com.josemarsilva.mqseries_api_cli.App;
import br.com.josemarsilva.mqseries_api_cli.CliArgsParser;
import br.com.pabloraimundo.jira_connection.Lista_De_Componentes;
import br.com.pabloraimundo.util.GetJson;
import br.com.pabloraimundo.util.MessageLog;

public class Manager {
	
	public static void main(String[] args) {

		CliArgsParser cliArgsParser = new CliArgsParser();
		
		ManagerArgsParse managerArgsParse = new ManagerArgsParse(args, cliArgsParser);

		List<String> message = null;

		try {
			System.out.println(MessageLog.Horario() + "Buscando mensagens na fila MQ");
			message = App.GetAllMessagesFromMq(managerArgsParse.cliArgsParser);
		} catch (Exception e) {
			System.out.println(MessageLog.Horario() + "Erro ao receber mensagens da fila MQ");
		}

		if (message.size() <= 0){
			System.out.println(MessageLog.Horario() + "Nenhuma mensagem encontrada, a aplicação procurará por novas mensagens dentro de 1 minuto");

			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			main(args);
		} else {
			int voltasNoLoop = 1;
			for (String string : message) {
				if (!TreatMessage(string)) {
					return;
				}

				System.out.println(MessageLog.Horario() +  "Mensagem recebida: " + string);

				if (string.substring(0, 7).equalsIgnoreCase("QPCCHGM")){
					Lista_De_Componentes lista_de_componentes = new Lista_De_Componentes(managerArgsParse);
					lista_de_componentes.Update(string);
					if (voltasNoLoop == message.size()) {
						main(args);
					}
					voltasNoLoop++;
				} else {

					String apiChangeman = string.substring(0, 15);
					String tipoResposta = string.substring(15, 16);
					String codigoRetorno = string.substring(16, 18);
					String changemanPackage = string.substring(18, 28);
					String ticketUsername = string.substring(28, 36);
					String ticketId = string.substring(36, 48);
					String reservedPositions = string.substring(48,52);
					String siteMaquina = string.substring(52, 60);
					String instancia = string.substring(60, 68);
					String returMessage = string.substring(68);
					String subStatusId = GetJson.GetCustomFieldJson("status_mainframe");

					managerArgsParse.setApiChangeman(apiChangeman);
					managerArgsParse.setTipoResposta(tipoResposta);
					managerArgsParse.setCodigoRetorno(codigoRetorno);
					managerArgsParse.setChangemanPackage(changemanPackage);
					managerArgsParse.setTicketUsername(ticketUsername);
					managerArgsParse.setTicketId(ticketId);
					managerArgsParse.setSiteMaquina(siteMaquina);
					managerArgsParse.setInstancia(instancia);
					managerArgsParse.setReturnMessage(returMessage);
					managerArgsParse.setSubStatusId(subStatusId);

					Jira_Manager jira_Manager = new Jira_Manager(managerArgsParse);
					try {
						jira_Manager.Manager();
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						if (voltasNoLoop == message.size()) {
							main(args);
						}
						voltasNoLoop++;
					}
				}
			}
		}
	}
	
	private static Boolean TreatMessage(String message) {
		Boolean messageLength = true;

		if(message.length() < 68) {
			System.out.println(MessageLog.Horario() + "Mensagem encontrada não está de acordo com a esperada.");
			System.out.println(MessageLog.Horario() + "Mensagem: " + message);
			messageLength = false;
		}
		
		return messageLength;
	}

}
