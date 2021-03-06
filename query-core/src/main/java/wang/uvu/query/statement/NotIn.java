package wang.uvu.query.statement;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotIn implements Statement{

	private Root<?> root;
	private CriteriaBuilder criteriaBuilder;
	private String fieldName;
	private String[] values;
	
	public NotIn(Root<?> root, CriteriaBuilder criteriaBuilder,String fieldName,String[] values){
		this.root = root;
		this.criteriaBuilder = criteriaBuilder;
		this.fieldName = fieldName;
		this.values = values;
	}

	@Override
	public Predicate toPredicate() {
		return new In(root, criteriaBuilder, fieldName, values).toPredicate().not();
	}
}
