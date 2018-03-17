package com.gnu.ResourceServer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
/**
 * 보호된 자원이 존재하는 ResourceServer에 관한 설정 
 * 
 * @author Geunwoo Shim(geunwoo.j.shim@gmail.com)
 *
 */
@Configuration
@EnableResourceServer // 보호된 자원이 담겨 있는 ResourceServer를 설정한다.
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	@Autowired
	TokenStore tokenStore;
	/**
	 * Resource Server의 http 접근제어를 설정한다.<br>
	 * API에 대한 token의 scope기반 접근 제어는 GlobalMethodSecurityConfiguration을 상속한 Bean을 만든 후<br>
	 * PreAuthorize(("#oauth2.hasScope('~~')") 어노테이션으로 설정할 수 있다. <br>
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/", "/open/**").permitAll()
		.antMatchers("/api/**").authenticated()
		.anyRequest().authenticated();
	}
	
/*	
  	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// redis tokenStore 기반이 아닌, AuthServer에서 token을 validation할 경우, Auth Server의 /check_token을 RemoteTokenService로 활용한다
		// 이 때 AuthServer에서는 security.checkTokenAccess의 기본값이 denyAll() 이므로 이 부분이 적절하게 조절되어야 한다. 
		RemoteTokenServices tokenServices = new RemoteTokenServices();
		tokenServices.setCheckTokenEndpointUrl("http://localhost:9099/apps/oauth/check_token");
		tokenServices.setClientId("resourceServer");
		tokenServices.setClientSecret("resourceSecret");
		resources.tokenServices(tokenServices);
	}
*/	
	/**
	 * resource server 자체의 security에 관한 설정
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}
	
	/**
	 * 토큰의 생성, 파기, 확인 등에 관여하는 tokenServices 빈을 만든다.
	 * 
	 * @return 토큰 서비스 객체
	 */
	@Bean(name="tokenServices")
	@Primary
	public DefaultTokenServices tokenServices() {
	    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
	    defaultTokenServices.setTokenStore(tokenStore);
	    defaultTokenServices.setSupportRefreshToken(true);
	    return defaultTokenServices;
	}
}
