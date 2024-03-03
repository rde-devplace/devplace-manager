package com.mibottle.manager.util;

import com.mibottle.manager.model.IdeDomainInfo;
import com.mibottle.manager.service.IdeDomainService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Enumeration;
import java.util.Optional;

/**
 * 외부 서비스에 HTTP 요청을 전송하는 클래스.
 * 원본 요청의 헤더와 URI를 사용하여 요청을 구성하고, 지정된 도메인에 대한 요청을 전송한다.
 */
@Component
public class SendWithRequestHeader {
    private final IdeDomainService ideDomainService;

    private final RestTemplate restTemplate;
    public SendWithRequestHeader(IdeDomainService ideDomainService, RestTemplate restTemplate) {
        this.ideDomainService = ideDomainService;
        this.restTemplate = restTemplate;
    }

    public enum ACTION {
        DELETE_PATH,
        DELETE_STRING,
        INSERT_PATH
    }

    /**
     * 지정된 도메인에 HTTP 요청을 전송하고, 응답을 Class<T> 또는 ParameterizedTypeReference<T> 타입으로 반환한다.
     * 이 메서드는 응답 타입이 클래스 또는 파라미터화된 타입 참조일 때 사용된다.
     *
     * @param domainInfo   요청할 도메인의 정보.
     * @param method       HTTP 메소드.
     * @param request      현재 HTTP 요청 정보.
     * @param body         요청 바디.
     * @param responseType 응답 타입 (Class<T> 또는 ParameterizedTypeReference<T>).
     * @param <T>          응답 타입.
     * @return ResponseEntity<T> 응답 엔티티.
     */
    public <T> ResponseEntity<T> sendForGenericResponse(
            IdeDomainInfo domainInfo,
            String requestParameters,
            HttpMethod method,
            HttpServletRequest request,
            String deletePath,
            Object body,
            Object responseType) {

        HttpEntity<Object> entity = createHttpEntity(request, body);

        //String url = domainInfo.getDomainURL() + request.getRequestURI();
        String url = domainInfo.getDomainURL() + modifyRequestURI(request.getRequestURI(), deletePath, ACTION.DELETE_PATH);
        if (requestParameters != null && !requestParameters.isEmpty()) {
            url += "?" + requestParameters;
        }

        if (responseType instanceof Class) {
            Class<T> responseClass = (Class<T>) responseType;
            return restTemplate.exchange(url, method, entity, responseClass);
        } else if (responseType instanceof ParameterizedTypeReference) {
            ParameterizedTypeReference<T> responseTypeRef = (ParameterizedTypeReference<T>) responseType;
            return restTemplate.exchange(url, method, entity, responseTypeRef);
        } else {
            throw new IllegalArgumentException("Invalid response type");
        }
    }

    /**
     * 지정된 도메인에 HTTP 요청을 전송하고, 응답을 문자열로 반환한다.
     * 이 메서드는 주로 간단한 텍스트 기반 응답을 처리할 때 사용된다.
     *
     * @param domainInfo 요청할 도메인의 이름.
     * @param method     HTTP 메소드.
     * @param request    현재 HTTP 요청 정보.
     * @param body       요청 바디.
     * @return ResponseEntity<String> 문자열 응답 엔티티.
     */
    public <T> ResponseEntity<String> sendForStringResponse(
            IdeDomainInfo domainInfo,
            String requestParameters,
            HttpMethod method,
            HttpServletRequest request,
            String deletePath,
            Object body,
            Object responseType) {

        HttpEntity<Object> entity = createHttpEntity(request, body);

        //String url = domainInfo.getDomainURL() + request.getRequestURI();
        String url = domainInfo.getDomainURL() + modifyRequestURI(request.getRequestURI(), deletePath, ACTION.DELETE_PATH);
        if (requestParameters != null && !requestParameters.isEmpty()) {
            url += "?" + requestParameters;
        }

        if (responseType instanceof Class) {
            return restTemplate.exchange(url, method, entity, String.class);
        } else if (responseType instanceof ParameterizedTypeReference) {
            return restTemplate.exchange(url, method, entity, String.class);
        } else {
            throw new IllegalArgumentException("Invalid response type");
        }
    }





    /**
     * HttpServletRequest로부터 HttpHeaders를 생성하고 HttpEntity를 반환한다.
     *
     * @param request 현재 HTTP 요청 정보.
     * @param body    요청 바디.
     * @return HttpEntity<Object> 요청 엔티티.
     */
    private HttpEntity<Object> createHttpEntity(HttpServletRequest request, Object body) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(headerName, request.getHeader(headerName));
        }
        return new HttpEntity<>(body, headers);
    }


    // 주어진 요구사항에 따라 requestURI를 수정하는 함수
    public String modifyRequestURI(String requestURI, String modifyString, ACTION action) {

        switch (action) {
            case DELETE_PATH:
                return deletePath(requestURI, modifyString);
            case DELETE_STRING:
                return deleteString(requestURI, modifyString);
            case INSERT_PATH:
                return insertPath(requestURI, modifyString);
            default:
                throw new IllegalArgumentException("Unsupported action type");
        }
    }

    private static String deletePath(String requestURI, String pathToDelete) {
        // Normalize pathToDelete to remove leading and trailing slashes
        String normalizedPathToDelete = pathToDelete.replaceAll("^/|/$", "");
        String[] segments = requestURI.split("/");
        StringBuilder modifiedURI = new StringBuilder();

        boolean found = false;
        for (String segment : segments) {
            if (!segment.equals(normalizedPathToDelete) && !segment.isEmpty()) {
                if (found || !requestURI.contains("/" + normalizedPathToDelete + "/")) {
                    modifiedURI.append("/").append(segment);
                }
            } else {
                found = true; // Mark as found to delete only the first occurrence
            }
        }

        return modifiedURI.toString();
    }

    private static String deleteString(String requestURI, String stringToDelete) {
        return requestURI.replace(stringToDelete, "");
    }

    private static String insertPath(String requestURI, String pathToInsert) {
        // Normalize pathToInsert to ensure it doesn't end with a slash
        String normalizedPathToInsert = pathToInsert.replaceAll("^/|/$", "");
        return "/" + normalizedPathToInsert + requestURI;
    }


}
