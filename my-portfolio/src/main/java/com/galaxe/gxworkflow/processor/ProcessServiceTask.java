package com.galaxe.gxworkflow.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.galaxe.gxworkflow.constants.ProcessStatusConstant;
import com.galaxe.gxworkflow.dto.ProcessInstanceRequest;
import com.galaxe.gxworkflow.mapper.ServiceIntegration;
import com.galaxe.gxworkflow.repository.ProcessVariableDetailsRepository;
import com.galaxe.gxworkflow.services.impl.ProcessTransactionService;
import com.galaxe.gxworkflow.util.Constants;
import com.galaxe.gxworkflow.util.RESTClient;

@Service
public class ProcessServiceTask {

	@Autowired
	private ProcessVariableDetailsRepository processVariableDetailsRepository;

	@Autowired
	RESTClient restClientObj;
	
	@Autowired
	private ProcessTransactionService processTransactionService;

	@Autowired
	private ProcessComplete processComplete;

	public void ExecuteServiceTask(int processNodeInfoId, int processInstanceId, String createdBy, int processInstanceNodeInfoId,ProcessInstanceRequest processInstanceRequest) throws Exception {

		try {
			List<Object[]> details = processVariableDetailsRepository
					.getProcessVariableDetailsByProcessInstanceId(processInstanceNodeInfoId, Constants.SERVICE_INTEGRATION);

			ServiceIntegration externalReqData = new ServiceIntegration();
			if (!CollectionUtils.isEmpty(details)) {
				for (Object[] obj : details) {
					if (obj[0].equals("authType")) {
						externalReqData.setAuthType((String) obj[2]);

					} else if (obj[0].equals("authInfo")) {
						externalReqData.setAuthInfo((String) obj[2]);

					} else if (obj[0].equals("method")) {
						externalReqData.setMethod((String) obj[2]);

					} else if (obj[0].equals("url")) {
						externalReqData.setUrl((String) obj[2]);

					} else if (obj[0].equals("contentType")) {
						externalReqData.setContentType((String) obj[2]);

					} else if (obj[0].equals("connectionTimeout")) {

						externalReqData.setConnectionTimeout((String) obj[2]);
					} else if (obj[0].equals("retriesCount")) {
						externalReqData.setRetriesCount((String) obj[2]);

					} else if (obj[0].equals("retriesBackOff")) {
						externalReqData.setRetriesBackOff((String) obj[2]);

					}else if (obj[0].equals("requestBody")) {
						externalReqData.setRequestBody((String) obj[2]);

					}
				}
			}
			externalReqData.setHeaderParams(collectHeaders(processInstanceNodeInfoId));
			externalReqData.setQueryParams(collectQueryParams(processInstanceNodeInfoId));
			try {
				String response = this.externalRequestCall(externalReqData);

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		} catch (Exception e) {
			logFailedServiceTask(processNodeInfoId, processInstanceRequest);
			throw e;
		}
	}

	public String externalRequestCall(ServiceIntegration reqData) throws Exception {

		if (!StringUtils.isEmpty(reqData.getAuthInfo())) {

			reqAuthInfo(reqData);
		}

		String response = restClientObj.proccesRequest(reqData);

		return response;
	}

	public void reqAuthInfo(ServiceIntegration data) {

		switch (data.getAuthType()) {

		case Constants.BASICAUTH:
			if (!data.getAuthInfo().trim().startsWith("Basic")) {
				String token = "Basic " + data.getAuthInfo().trim();
				data.setAuthInfo(token);
			}

		default:
			if (!data.getAuthInfo().trim().startsWith("Bearer")) {
				String token = "Bearer " + data.getAuthInfo().trim();
				data.setAuthInfo(token);
			}
		}
	}

	private Map<String, Object> collectHeaders(int processInstanceNodeInfoId) {
		List<Object[]> details = processVariableDetailsRepository
				.getProcessVariableDetailsByProcessInstanceId(processInstanceNodeInfoId, Constants.HEADER_PARAM);
		Map<String, Object> headers = new HashMap<>();
		if (!CollectionUtils.isEmpty(details)) {
			for (Object[] obj : details) {
				headers.put((String) obj[0], obj[2]);
			}
		}
		return headers;

	}

	private MultiValueMap<String, String> collectQueryParams(int processInstanceNodeInfoId) {
		List<Object[]> details = processVariableDetailsRepository
				.getProcessVariableDetailsByProcessInstanceId(processInstanceNodeInfoId, Constants.QUERY_PARAM);
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		if (!CollectionUtils.isEmpty(details)) {
			for (Object[] obj : details) {
				queryParams.add((String) obj[0], (String) obj[2]);
			}
		}
		return queryParams;

	}
	private void logFailedServiceTask(int processNodeInfoId, ProcessInstanceRequest processInstanceRequest) throws Exception {
		processTransactionService.postTransactionFailed(processInstanceRequest.getProcessInstanceId(),
				processNodeInfoId, processInstanceRequest.getUserName(),ProcessStatusConstant.FAILED.getCode());
		processComplete.updateToFailed(processNodeInfoId, processInstanceRequest.getProcessInstanceId(),
				processInstanceRequest.getUserName());
	}

}
