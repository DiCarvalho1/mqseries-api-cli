package br.com.pabloraimundo.jira_api;

public enum SubStatus {

	REQUISITADO("REQUISITADO", 10200),
	EMEXECUCAO("EM EXECUÇÃO", 10201),
	EXECUTADO("EXECUTADO", 10202),
	FALHAEXECUCAO("FALHA EXECUÇÃO", 10203),
	FALHAREQUISICAO("FALHA REQUISIÇÃO", 10204),
	FALHATIMEOUT("FALHA TIMEOUT", 10205);

	public String subStatusDescription;
	public int subStatusValue;
	SubStatus(String description, int value) {
		subStatusDescription = description;
		subStatusValue = value;
	}
	
	public String getDescription() {
		return subStatusDescription;
	}
	
	public int getValue() {
		return subStatusValue;
	}
}
