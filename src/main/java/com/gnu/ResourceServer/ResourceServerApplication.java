package com.gnu.ResourceServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * 
 * 본 프로젝트의 구동을 위해서는 127.0.0.1의 6379에 redis 설치를 권장한다<br>
 * <a href="https://redis.io/download">Redis 공식 홈페이지</a>를 참고할 것<br>
 * 
 * @author Geunwoo Shim(geunwoo.j.shim@gmail.com)
 *
 */
@SpringBootApplication
public class ResourceServerApplication extends WebMvcConfigurerAdapter{

	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}
	/**
	 * 
	 * RedisTokenStore를 Bean으로 등록함
	 * Auth Server와 Resource Server는 동일한 Redis를 Access token store로 공유하고 있음
	 * 
	 * @param factory
	 * @return
	 */
	@Bean
	public TokenStore tokenStore(RedisConnectionFactory factory){
		return new RedisTokenStore(factory);
	}
}
