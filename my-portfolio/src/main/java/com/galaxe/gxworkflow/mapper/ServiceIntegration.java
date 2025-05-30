package com.galaxe.gxworkflow.mapper;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.MultiValueMap;

@XmlRootElement(name = "ServiceIntegration")
public class ServiceIntegration {
	
	private String authType;
	private String authInfo;
	private String method;
	private String url;
	private String contentType;
	private Map<String, Object> headerParams;
	MultiValueMap<String, String> queryParams;
	private String requestBody;
	private String connectionTimeout;
	private String retriesCount;
	private String retriesBackOff;
	
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getAuthInfo() {
		return authInfo;
	}
	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Map<String, Object> getHeaderParams() {
		return headerParams;
	}
	public void setHeaderParams(Map<String, Object> headerParams) {
		this.headerParams = headerParams;
	}
	public MultiValueMap<String, String> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(MultiValueMap<String, String> queryParams) {
		this.queryParams = queryParams;
	}
	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	public String getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(String connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public String getRetriesCount() {
		return retriesCount;
	}
	public void setRetriesCount(String retriesCount) {
		this.retriesCount = retriesCount;
	}
	public String getRetriesBackOff() {
		return retriesBackOff;
	}
	public void setRetriesBackOff(String retriesBackOff) {
		this.retriesBackOff = retriesBackOff;
	}

}
