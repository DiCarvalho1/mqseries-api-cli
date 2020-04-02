package br.com.pabloraimundo.workflow_manager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


import br.com.josemarsilva.mqseries_api_cli.App;
import br.com.josemarsilva.mqseries_api_cli.CliArgsParser;
import br.com.pabloraimundo.util.GetProperties;
import br.com.pabloraimundo.util.MessageLog;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class Manager {
	
	public static void main(String[] args) {

		CliArgsParser cliArgsParser = new CliArgsParser();
		
		ManagerArgsParse managerArgsParse = new ManagerArgsParse(args, cliArgsParser);

		List<String> message = null;

		try {
			System.out.println(MessageLog.Horario() + "Buscando mensagens na fila");
			message = App.GetAllMessagesFromMq(managerArgsParse.cliArgsParser);
			System.out.println(MessageLog.Horario() + "Busca concluída ");
		} catch (Exception e) {
			System.out.println(MessageLog.Horario() + "Erro ao receber mensagens da fila");
		}

		if (message.size() <= 0){
			System.out.println(MessageLog.Horario() + "Nenhuma mensagem encontrada");
			main(args);
		} else {
			int voltasNoLoop = 1;
			for (String string : message) {
				if (!TreatMessage(string)) {
					return;
				}

				System.out.println(MessageLog.Horario() +  "Mensagem recebida: " + string);

				String apiChangeman = string.substring(0, 15);
				String tipoResposta = string.substring(15, 16);
				String codigoRetorno = string.substring(16, 18);
				String changemanPackage = string.substring(18, 28);
				String ticketUsername = string.substring(28, 36);
				String ticketId = string.substring(36, 48);
				String siteMaquina = string.substring(48,58);
				String instancia = string.substring(58,68);
				String returMessage = string.substring(68);
				String customFieldId = managerArgsParse.getSubStatusId();

				managerArgsParse.setApiChangeman(apiChangeman);
				managerArgsParse.setTipoResposta(tipoResposta);
				managerArgsParse.setCodigoRetorno(codigoRetorno);
				managerArgsParse.setChangemanPackage(changemanPackage);
				managerArgsParse.setTicketUsername(ticketUsername);
				managerArgsParse.setTicketId(ticketId);
				managerArgsParse.setSiteMaquina(siteMaquina);
				managerArgsParse.setInstancia(instancia);
				managerArgsParse.setReturnMessage(returMessage);
				managerArgsParse.setCustomFieldId(customFieldId);

				Jira_Manager jira_Manager = new Jira_Manager(managerArgsParse);
				try {
					jira_Manager.Manager();
				}
				catch (Exception e){
					System.out.println(e);
				} finally {
					if(voltasNoLoop == message.size()){
						main(args);
					}
					voltasNoLoop++;
				}
			}
		}
	}
	
	private static Boolean TreatMessage(String message) {
		Boolean messageLength = true;

		if(message.length() < 68) {
			System.out.println("Mensagem encontrada não está de acordo com a esperada.");
			messageLength = false;
		}
		
		return messageLength;
	}
		
}
