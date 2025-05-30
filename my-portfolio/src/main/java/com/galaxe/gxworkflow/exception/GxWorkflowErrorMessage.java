/*******************************************************************************
 * GalaxE.Healthcare Solutions Inc. ©2019, Confidential and Proprietary - All Rights Reserved.
 * No unauthorized use permitted. The content contained herein may not be reproduced,
 * adapted/modified, published, performed or displayed without the express written 
 * authorization of GalaxE.Healthcare Solutions, Inc..
 ******************************************************************************/
package com.galaxe.gxworkflow.exception;

public class GxWorkflowErrorMessage {

	private int errorCode;
	private String errorMessage;
	private String details;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
