package wang.uvu.query.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import wang.uvu.query.QueryException;
import wang.uvu.query.Query;
import wang.uvu.query.builder.QueryBuilder;
import wang.uvu.query.builder.StatementBuilder;
import wang.uvu.query.statement.QueryStatement;
import wang.uvu.query.statement.Statement;
import wang.uvu.query.utils.StringUtils;

public class QueryRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, Serializable> implements QueryRepository<T> {
	private EntityManager entityManager;

	public QueryRepositoryImpl(Class<T> domainClass, EntityManager em) {
		this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
	}

	public QueryRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public <Q extends Query<?>> Page<T> findAll(Q query) {
		if(StringUtils.isBlank(query.getFields_())){
			return select(query);
		}
		return multiselect(query);
	}

	private <Q extends Query<?>> Page<T> select(Q query){
		Class<T> domainClass = getDomainClass();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);
		Root<T> root = criteriaQuery.from(domainClass);
		QueryStatement statement = QueryBuilder.build(root, criteriaBuilder, query);
		long total = count(query);
		if(total == 0){
			return page(statement.getPage(), total);
		}
		//where条件
		Predicate predicate = statement.toPredicate();
		criteriaQuery.where(predicate);
		
		//order by
		order(root, criteriaBuilder, criteriaQuery, statement);
		
		//limit
		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setFirstResult(statement.getPage().getOffset());
		typedQuery.setMaxResults(statement.getPage().getPageSize());
		
		List<T> result = typedQuery.getResultList();
		return page(statement.getPage(), result, total);
	}
	
	public Page<T>  page(PageRequest page,List<T> result,long total){
		return new PageImpl<T>(result, page, total);
	}
	
	public Page<T>  page(PageRequest page,long total){
		return new PageImpl<T>(new ArrayList<T>(), page, total);
	}
	
	private <Q extends Query<?>> long count(Q query){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(getDomainClass());
		Statement statement = StatementBuilder.build(root, criteriaBuilder, query);
		
		//where
		Predicate predicate = statement.toPredicate();
		criteriaQuery.where(predicate);
		
		//distinct，实际是不会用到的
		if (criteriaQuery.isDistinct()) {
			criteriaQuery.select(criteriaBuilder.countDistinct(root));
		} else {
			criteriaQuery.select(criteriaBuilder.count(root));
		}
		List<Long> totals = entityManager.createQuery(criteriaQuery).getResultList();
		Long total = 0L;
		for (Long element : totals) {
			total += element == null ? 0 : element;
		}
		return total;
	}
	
	private void order(Root<?> root, CriteriaBuilder builder,CriteriaQuery<?> criteriaQuery, QueryStatement statement){
		Sort sort = statement.getPage().getSort();
		List<Order> orders = QueryUtils.toOrders(sort, root, builder);
		if(!orders.isEmpty()){
			criteriaQuery.orderBy(orders);
		}
	}
	
	private <Q extends Query<?>> Page<T> multiselect(Q query){
		Class<T> domainClass = getDomainClass();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
		Root<T> root = criteriaQuery.from(domainClass);
		QueryStatement statement = QueryBuilder.build(root, criteriaBuilder, query);
		
		long total = count(query);
		if(total == 0){
			return page(statement.getPage(), total);
		}
		//字段
		List<Selection<?>> fields = statement.getFields();
		criteriaQuery.multiselect(fields);
		
		//order by
		order(root, criteriaBuilder, criteriaQuery, statement);
		
		//where
		Predicate predicate = statement.toPredicate();
		criteriaQuery.where(predicate);
		
		//limit
		TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setFirstResult(statement.getPage().getOffset());
		typedQuery.setMaxResults(statement.getPage().getPageSize());
		
		//Tuple->T
		List<Tuple> result = typedQuery.getResultList();
		List<T> list = new ArrayList<T>(result.size());
		try{
			for (Tuple tuple : result) {
				T domain = domainClass.newInstance(); 
				for (Selection<?> selection : fields) {
					String name = selection.getAlias();
					Object value = tuple.get(name);
					Field field = wang.uvu.query.utils.QueryUtils.getField(domainClass, name);
					field.setAccessible(true);
					field.set(domain, value);
				}
				list.add(domain);
			}
		}catch(Exception e){
			throw new QueryException(e);
		}
		return page(statement.getPage(), list, total);
	}
}