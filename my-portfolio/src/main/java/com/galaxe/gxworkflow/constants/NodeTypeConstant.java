package com.galaxe.gxworkflow.constants;

public enum NodeTypeConstant {
	
	START("bpmn:startEvent", "Start Node"),
	END("bpmn:endEvent", "End Node"),
	USER("bpmn:userTask", "User Task Node"),
	SCRIPT("bpmn:scriptTask", "Script Node"),
	SERVICE("bpmn:serviceTask", "Service Node"),
	SEND("bpmn:sendTask", "Send Node"),
	SEQUENCE("bpmn:sequenceFlow", "Sequence Node"),
	PROCESS("bpmn:process", "Process Node"),
	DIAGRAMN("bpmndi:BPMNDiagram", "Diagram Node"),
	EXCLUSIVE("bpmn:exclusiveGateway","Exclusive Gateway Node"),
	TASK("bpmn:task","Exclusive Gateway Node");
	
	private String code;
	
	private String description;
	
	private NodeTypeConstant(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
