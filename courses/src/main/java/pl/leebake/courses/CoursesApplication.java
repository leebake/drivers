package pl.leebake.courses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CoursesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursesApplication.class, args);
    }

    @Configuration
    @EnableJpaRepositories(basePackages = {"pl.leebake.event.store"})
    @EntityScan(basePackages = {"pl.leebake.event.store"})
    public static class CoursesApplicationConfiguration {

    }
}
