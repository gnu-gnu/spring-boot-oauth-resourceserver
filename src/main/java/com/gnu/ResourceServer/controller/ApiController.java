package com.gnu.ResourceServer.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * 보호된 자원이 담긴 테스트 컨트롤러<br>
 * 이 컨트롤러에 대한 기본 설정은 {@link com.gnu.ResourceServer.config.ResourceServerConfig#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)}를 참조한다<br>
 * scope 기반의 세부적인 설정은 메소드의 PreAuthorize 어노테이션을 참조한다 <br>
 * @author Geunwoo Shim(gflhsin@gmail.com)
 *
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	@RequestMapping("/hello")
	public String helloWorld(){
		return "hello";
	}
	@PreAuthorize(("#oauth2.hasScope('read')"))
	@RequestMapping("/read")
	public String read(){
		return "read";
	}
	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping("/write")
	public String write(){
		return "read";
	}
	
}
