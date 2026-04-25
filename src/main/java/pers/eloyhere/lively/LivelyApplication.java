package pers.eloyhere.lively;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableAspectJAutoProxy
@EntityScan(basePackages = "pers.eloyhere.lively.entity")
@EnableJpaRepositories(basePackages = "pers.eloyhere.lively.repository")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class LivelyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivelyApplication.class, args);
	}

}
