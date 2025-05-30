/*******************************************************************************
 * GalaxE.Healthcare Solutions Inc. Â©2019, Confidential and Proprietary - All Rights Reserved.
 * No unauthorized use permitted. The content contained herein may not be reproduced,
 * adapted/modified, published, performed or displayed without the express written
 * authorization of GalaxE.Healthcare Solutions, Inc..
 ******************************************************************************/
package com.galaxe.gxworkflow.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxe.gxworkflow.mapper.ServiceIntegration;

@Component
public class RESTClient {

//	private final static Logger log = LoggerFactory.getLogger(RESTClient.class);

	public String proccesRequest(ServiceIntegration reqData) throws Exception {

		String serviceLocation = reqData.getUrl();
		String method = reqData.getMethod();
		MediaType requestType = MediaType.parseMediaType(reqData.getContentType());
		String response = null;

		Map<String, Object> headerParams = new HashMap<>();
		Map<String, Object> requestBody = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();

		if (reqData.getAuthInfo() != null) {
			// FIXME: REMOVE HARD CODE
			headerParams.put("Authorization", reqData.getAuthInfo());
		}
		if (!StringUtils.isEmpty(reqData.getHeaderParams())) {

//			Map<String, Object> headers = mapper.readValue(reqData.getHeaderParams(), Map.class);
			headerParams.putAll(reqData.getHeaderParams());
		}

		if (!StringUtils.isEmpty(reqData.getQueryParams())) {

//			MultiValueMap<String, String> params = mapper.readValue(reqData.getQueryParams(), MultiValueMap.class);
			UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(serviceLocation)
					.queryParams(reqData.getQueryParams()).build();
			serviceLocation = uriComponents.toUriString();
		}

		if (!StringUtils.isEmpty(reqData.getRequestBody())) {

			Map<String, Object> request = mapper.readValue(reqData.getRequestBody(), Map.class);
			requestBody.putAll(request);
		}

		if (method != null) {
			// FIXME: REMOVE HARD CODE
			if (method.equalsIgnoreCase(Constants.POST) || method.equalsIgnoreCase(Constants.PUT)) {
				response = connectWithPost(serviceLocation, requestBody, requestType, headerParams, reqData, method);
			} else if (method.equalsIgnoreCase(Constants.GET)) {
				response = connectWithGet(serviceLocation, headerParams, requestType, reqData);
			} else if (method.equalsIgnoreCase(Constants.DELETE)) {
				response = connectWithDelete(serviceLocation, requestBody, requestType, headerParams, HttpMethod.DELETE,
						reqData);
			}
		}

		return response;
	}

	private String connectWithDelete(String serviceURL, Map<String, Object> requestBody, MediaType requestType,
			Map<String, Object> headerParam, HttpMethod httpMethod, ServiceIntegration reqData) throws Exception {

		String resultJSON = null;
		// Create HTTP headers

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = null;

		if (headerParam != null) {
			for (Map.Entry<String, Object> entry : headerParam.entrySet()) {
				headers.set(entry.getKey(), (String) entry.getValue());
			}
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String request = objectMapper.writeValueAsString(requestBody);

		entity = new HttpEntity<>(request, headers);

		ResponseEntity<String> responseEntity = null;

		// send the POST
		try {

			RestTemplate restTemplate = new RestTemplate();

			// SSL Authenticate

//			ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory(
//					adopterConfig.getSslCertificationsPath(), dto.getSslcertname(), dto.getSslcertpassword());
//			if (requestFactory != null) {
//				restTemplate.setRequestFactory(requestFactory);
//			}
//			restTemplate.setErrorHandler(new RestAdapterErrorHandler());

			responseEntity = restTemplate.exchange(serviceURL, httpMethod, entity, String.class);

		} catch (ResourceAccessException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		}

		if (responseEntity != null && !StringUtils.isEmpty(responseEntity.getBody())) {

			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				resultJSON = responseEntity.getBody();

			} else {
				throw new Exception(responseEntity.getBody().toString());
			}
		}
		return resultJSON;
	}

	public String connectWithGet(String serviceURL, Map<String, Object> headerParams, MediaType contentType,
			ServiceIntegration reqData) throws Exception {
		String resultJSON = null;

		// Create HTTP headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(contentType);

		if (headerParams != null) {
			headerParams.entrySet().forEach(entry -> {
				headers.set(entry.getKey(), (String) entry.getValue());
			});
		}

		ResponseEntity<String> responseEntity = invokeGetService(serviceURL, headers, reqData);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			resultJSON = responseEntity.getBody();

		} else {

			throw new Exception(responseEntity.getBody().toString());
		}

		return resultJSON;
	}

	public String connectWithPost(String serviceURL, Map<String, Object> requestObj, MediaType requestType,
			Map<String, Object> headerParam, ServiceIntegration reqData, String method) throws Exception {

		String resultJSON = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(requestType);

		HttpEntity<String> entity = null;

		if (headerParam != null) {
			for (Entry<String, Object> entry : headerParam.entrySet()) {
				headers.set(entry.getKey(), (String) entry.getValue());
			}
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = objectMapper.writeValueAsString(requestObj);

		entity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> responseEntity = invokePostService(serviceURL, entity, method, reqData);

		MediaType contentType = responseEntity.getHeaders().getContentType();
		if (!StringUtils.isEmpty(responseEntity.getBody())) {
			resultJSON = responseEntity.getBody();
		} else {
			throw new Exception(responseEntity.toString());
		}

		return resultJSON;
	}

	private ResponseEntity<String> invokeGetService(String serviceURL, HttpHeaders headers,
			ServiceIntegration reqData) {

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = null;

		RestTemplate restTemplate = new RestTemplate();

		// send the GET
		// SSL Authenticate
//		ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory(adopterConfig.getSslCertificationsPath(),
//				dto.getSslcertname(), dto.getSslcertpassword());
//		if (requestFactory != null) {
//			restTemplate.setRequestFactory(requestFactory);
//		}

//		restTemplate.setErrorHandler(new RestAdapterErrorHandler());

		try {
			responseEntity = restTemplate.exchange(serviceURL, HttpMethod.GET, entity, String.class);
		} catch (ResourceAccessException exe) {
			throw exe;
		} catch (InternalServerError exe) {
			throw exe;
		} catch (Exception exe) {
			throw exe;
		}
		return responseEntity;
	}

	private ResponseEntity<String> invokePostService(String serviceURL, HttpEntity<String> entity, String method,
			ServiceIntegration reqData) {
		ResponseEntity<String> responseEntity = null;

		RestTemplate restTemplate = new RestTemplate();
		// send the POST
		// SSL Certificate Authentication

//		ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory(adopterConfig.getSslCertificationsPath(),
//				dto.getSslcertname(), dto.getSslcertpassword());
//		if (requestFactory != null) {
//			restTemplate.setRequestFactory(requestFactory);
//		}

//		restTemplate.setErrorHandler(null);
		HttpMethod httpMmethod = HttpMethod.POST;
		if (method.equalsIgnoreCase("PUT")) {
			httpMmethod = HttpMethod.PUT;
		}
		try {

			responseEntity = restTemplate.exchange(serviceURL, httpMmethod, entity, String.class);
		} catch (ResourceAccessException exe) {
			throw exe;
		} catch (Exception e) {
			throw e;
		}
		return responseEntity;
	}

	/*
	 * private static Map<String, Object> parseRestDataMap(Object jsonInput) throws
	 * AdapterException { ObjectMapper mapper = new ObjectMapper(); Map<String,
	 * Object> finalValueMap = new HashMap<>(); if
	 * (org.apache.commons.lang3.StringUtils.isNotBlank((String) jsonInput)) { try {
	 * Map<String, Object> inputValueMap = mapper.readValue((String) jsonInput, new
	 * TypeReference<Map<String, Object>>() { }); getRestDataMap(inputValueMap,
	 * finalValueMap); } catch (Exception e) { throw new
	 * AdapterException(e.getMessage(), 500); } } return finalValueMap; }
	 * 
	 * public static void getRestDataMap(Map<String, Object> inputValueMap,
	 * Map<String, Object> finalValueMap) { for (Entry<String, Object> entry :
	 * inputValueMap.entrySet()) { String key = entry.getKey(); Object value =
	 * entry.getValue(); if (value instanceof Map) { finalValueMap.put(key, value);
	 * if (value instanceof Map) {
	 * 
	 * @SuppressWarnings("unchecked") Map<String, Object> valueMap = (Map<String,
	 * Object>) value; getRestDataMap(valueMap, finalValueMap); } } else { value =
	 * fieldTypeCasting(value); finalValueMap.put(key, value); } } }
	 * 
	 * private ClientHttpRequestFactory getClientHttpRequestFactory(String
	 * keyStorePath, String keyFileName, String certPassword) { try { if
	 * (org.apache.commons.lang3.StringUtils.isNotBlank(keyStorePath) &&
	 * org.apache.commons.lang3.StringUtils.isNotBlank(keyFileName)) {
	 * 
	 * String keyStoreFilepath = keyStorePath + File.separator + keyFileName; File
	 * certFile = new File(keyStoreFilepath); if (certFile.exists()) {
	 * 
	 * char[] password =
	 * org.apache.commons.lang3.StringUtils.isNotBlank(certPassword) ?
	 * certPassword.toCharArray() : null;
	 * 
	 * KeyStore keystoreType = KeyStore.getInstance(KeyStore.getDefaultType());
	 * keystoreType.load(new FileInputStream(keyStoreFilepath), password);
	 * 
	 * KeyStore trustStoreType = KeyStore.getInstance(KeyStore.getDefaultType());
	 * trustStoreType.load(new FileInputStream(keyStoreFilepath), password);
	 * 
	 * SSLContext sslContext =
	 * SSLContexts.custom().setProtocol(SSLConnectionSocketFactory.SSL)
	 * .loadTrustMaterial(trustStoreType, new TrustSelfSignedStrategy())
	 * .loadKeyMaterial(keystoreType, password).build(); SSLConnectionSocketFactory
	 * sslConFactory = new SSLConnectionSocketFactory(sslContext);
	 * 
	 * CloseableHttpClient httpClient =
	 * HttpClients.custom().setSSLSocketFactory(sslConFactory).build();
	 * 
	 * log.info("SSLCERTIFICATION " + keyFileName);
	 * 
	 * return new HttpComponentsClientHttpRequestFactory(httpClient); } else { throw
	 * new AdapterException("SSL Certificate not found for " + keyFileName, null,
	 * 404); } } } catch (Exception exception) { throw new
	 * AdapterException(exception.getMessage(), exception, 424); } return null; }
	 * 
	 * private static Object fieldTypeCasting(Object val) {
	 * 
	 * Object typeValue = val;
	 * 
	 * if (!ObjectUtils.isEmpty(val) && (val.toString().equalsIgnoreCase("true") ||
	 * val.toString().equalsIgnoreCase("false"))) { typeValue =
	 * Boolean.parseBoolean(val.toString()); }
	 * 
	 * try { typeValue = Integer.parseInt(val.toString()); } catch (Exception eint)
	 * { try { typeValue = Long.parseLong(val.toString()); } catch (Exception elong)
	 * { try { typeValue = Float.parseFloat(val.toString()); } catch (Exception
	 * efloat) { typeValue = val; } } }
	 * 
	 * return typeValue; }
	 * 
	 * private static JSONObject mapToJson(Map<String, Object> map) { JSONObject
	 * jsonData = new JSONObject(); map.entrySet().forEach(data -> {
	 * jsonData.put(data.getKey(), data.getValue()); }); return jsonData; }
	 */
}
