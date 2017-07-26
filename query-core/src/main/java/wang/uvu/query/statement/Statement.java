package wang.uvu.query.statement;

import javax.persistence.criteria.Predicate;

public interface Statement {
	
	Predicate toPredicate();
}
