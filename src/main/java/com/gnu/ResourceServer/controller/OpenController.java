package com.gnu.ResourceServer.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * 공개된 자원이 담긴 테스트 컨트롤러
 * 이 Controller는 permitAll() 상태이다
 * @author Geunwoo Shim(geunwoo.j.shim@gmail.com)
 *
 */
@RequestMapping("/open")
@Controller
public class OpenController {
	Logger logger = LoggerFactory.getLogger(OpenController.class);
	/**
	 * 
	 * Authorize 서버의 /oauth/authorize 에서 requestToken을 받아온다.
	 * 해당 토큰을 access token으로 전환한다.
	 * 
	 * @param map
	 * @return
	 */
	private String getRequestToken(Map<String, String> map){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.set("Authorization", "Basic "+Base64.getEncoder().encodeToString("u:p".getBytes())); // u:p는 Authorize 서버와 약속된 HTTP Basic Auth
		httpHeaders.set("Cache-control", "no-cache");
		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.setAll(map);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, httpHeaders);
		ResponseEntity<String> result = rest.postForEntity("http://localhost:9099/apps/oauth/authorize", requestEntity, String.class);
		String queryStrings = result.getHeaders().toSingleValueMap().get("Location").split("\\?")[1];
		String requestToken = "";
		for(String s : queryStrings.split("&")){
			// TODO 정규식 기반으로 추출하게 하면 더 짧겠지만 지금은 귀찮다....
			if(s.startsWith("code")){
				requestToken = s.split("=")[1];
				break;
			}
		}
		return requestToken;
	}
	/**
	 * 
	 * request token을 access token으로 교환한다.
	 * 
	 * @param requestToken
	 * @return
	 */
	private String getAccessToken(String requestToken){
		// TODO client_id, secret 은 사용자의 고유값으로 대치하도록 한다. 하드코딩된 값들은 모두 수정할 것
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("client_id", "code");
		paramMap.put("redirect_uri", "http://localhost:7077/resources/open/callback");
		paramMap.put("grant_type", "authorization_code");
		paramMap.put("state", "19850827");
		paramMap.put("code", requestToken);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.set("Authorization", "Basic "+Base64.getEncoder().encodeToString("code:".getBytes())); // code:secret은 Authorization_code 교환을 위한 basic auth, 추후에 이 부분은 사용자별 인증으로 대체한다.
		httpHeaders.set("Cache-control", "no-cache");
		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.setAll(paramMap);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, httpHeaders);
		ResponseEntity<String> result = rest.postForEntity("http://localhost:9099/apps/oauth/token", requestEntity, String.class);
		return result.getBody();
	}

	@RequestMapping("/hello")
	@ResponseBody
	public String open(){
		return "hello open";
	}
	/**
	 * 
	 * request_token을 받아온다.
	 * 여기서 받아온 request_token으로 access_token을 교환한다.
	 * 
	 * @param map
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping("/authorize")
	public @ResponseBody Map<String, String> authorize(@RequestParam Map<String, String> map) throws JsonParseException, JsonMappingException, IOException{
		String requestToken = getRequestToken(map);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(getAccessToken(requestToken), new TypeReference<Map<String, String>>() {});
	}
	
	@RequestMapping("/callback")
	public @ResponseBody Map<String, String[]> callback(HttpServletRequest req){
		System.out.println("callback");
		logger.info("--------- call for {}", req.getRequestURI());
		Enumeration<String> names = req.getHeaderNames();
		String key = "";
		while(names.hasMoreElements()){
			key = names.nextElement();
			logger.info("{} : {}", key, req.getHeader(key));
		}
		String paramKey = "";
		Enumeration<String> params = req.getParameterNames();
		while(params.hasMoreElements()){
			paramKey = params.nextElement();
			logger.info("{} : {}", paramKey, req.getParameter(paramKey));
		}
		return req.getParameterMap();
	}
}
