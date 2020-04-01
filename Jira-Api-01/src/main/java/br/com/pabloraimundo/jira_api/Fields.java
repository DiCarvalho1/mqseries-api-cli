package br.com.pabloraimundo.jira_api;

public class Fields {

	String Name;
	String Value;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	
	public Fields(String name, String value) {
		super();
		Name = name;
		Value = value;
	}
	
	
}
