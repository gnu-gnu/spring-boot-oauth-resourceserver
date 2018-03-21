package com.gnu.ResourceServer.config;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * 
 * Controller에서 Method 수준 보안에 #oauth2.hasScope('[someScope]')를  사용하기 위해서 필요한 Bean
 * MethodExpression 기반 보안을 적용하더라도 MethodExpression은 WebSecurity 보다 우선 순위가 낮으므로
 * 메소드 기반 PermitAll 은 앞에서 이미 차단될 수 있음
 * @author Geunwoo Shim (gflhsin@gmail.com)
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	
	
	
	@Override
	public MethodInterceptor methodSecurityInterceptor() throws Exception {
		return super.methodSecurityInterceptor();
	}

	@Override
	public void setMethodSecurityExpressionHandler(List<MethodSecurityExpressionHandler> handlers) {
		// TODO Auto-generated method stub
		handlers.add(new OAuth2MethodSecurityExpressionHandler());
	}
}
