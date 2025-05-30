package com.galaxe.gxworkflow.constants;

public enum ProcessStatusConstant {
	
	STARTED("S", "Started"),
	RUNNING("R", "Running"),
	PENDING("P", "Pending"),
	FAILED("F", "Failed"),
	COMPLETED("C", "Completed");
	
	private String code;
	
	private String description;
	
	private ProcessStatusConstant(String code, String description) {
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
