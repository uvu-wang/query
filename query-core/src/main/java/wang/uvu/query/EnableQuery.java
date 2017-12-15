package wang.uvu.query;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import wang.uvu.query.repository.QueryRepositoryFactoryBean;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableJpaRepositories(repositoryFactoryBeanClass = QueryRepositoryFactoryBean.class)
public @interface EnableQuery {

}
