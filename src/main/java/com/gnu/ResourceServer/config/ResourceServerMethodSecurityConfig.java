package com.gnu.ResourceServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * 
 * Controller에서 Method 수준 보안에 #oauth2.hasScope('[someScope]')를  사용하기 위해서 필요한 Bean 
 * @author Geunwoo Shim (gflhsin@gmail.com)
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
}
