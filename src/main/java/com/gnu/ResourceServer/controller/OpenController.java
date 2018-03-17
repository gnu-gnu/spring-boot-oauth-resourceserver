package com.gnu.ResourceServer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * 공개된 자원이 담긴 테스트 컨트롤러
 * 이 Controller는 permitAll() 상태이다
 * @author Geunwoo Shim(geunwoo.j.shim@gmail.com)
 *
 */
@RequestMapping("/open")
@RestController
public class OpenController {
	@RequestMapping("/hello")
	public String open(){
		return "hello open";
	}
}
