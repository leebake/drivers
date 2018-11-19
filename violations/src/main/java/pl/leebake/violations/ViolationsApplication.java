package pl.leebake.violations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ViolationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViolationsApplication.class, args);
    }

    @Configuration
    @EnableJpaRepositories(basePackages = {"pl.leebake.event.store"})
    @EntityScan(basePackages = {"pl.leebake.event.store"})
    public static class ViolationsApplicationConfiguration {

    }
}
