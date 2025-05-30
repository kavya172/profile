package com.galaxe.gxworkflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.galaxe.mail.dto.MailConfigDTO;



@EnableAutoConfiguration
@SpringBootConfiguration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EntityScan(basePackages = { "com.galaxe.mail","com.galaxe.gxworkflow"})
@EnableJpaRepositories(basePackages = { "com.galaxe.mail","com.galaxe.gxworkflow"})
@EnableTransactionManagement
@EnableCaching
@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.galaxe.mail","com.galaxe.gxworkflow"})
public class GxCaptureWorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(GxCaptureWorkflowApplication.class, args);
	}
	
	@Bean
	public MailConfigDTO createMailConfigDTO(@Value("${mail.error.notification.from}") String errorFrom
			, @Value("${mail.error.notification.to}") String errorTo) {
		MailConfigDTO mailConfigDTO = new MailConfigDTO();
		mailConfigDTO.setMailFrom(errorFrom);
		mailConfigDTO.setMailTo(errorTo);
	   return mailConfigDTO;
	}
	
	@Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }

	@Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setConfigLocation(new ClassPathResource("ehCache.xml"));
        factory.setShared(true);
        return factory;
    }
}
