package wang.uvu.query.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import wang.uvu.query.Query;

@NoRepositoryBean
public interface QueryRepository<T> {
	
	<Q extends Query> Page<T> findAll(Q query);
}
