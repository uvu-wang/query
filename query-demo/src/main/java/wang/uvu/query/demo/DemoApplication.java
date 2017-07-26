package wang.uvu.query.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import wang.uvu.query.repository.QueryRepositoryFactoryBean;


@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = QueryRepositoryFactoryBean.class)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
