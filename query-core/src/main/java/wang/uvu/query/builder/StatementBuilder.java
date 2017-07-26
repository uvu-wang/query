package wang.uvu.query.builder;

import static wang.uvu.query.utils.Operators.*;

import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import wang.uvu.query.QueryException;
import wang.uvu.query.statement.*;
import wang.uvu.query.utils.BeanHelper;
import wang.uvu.query.utils.StringUtils;
import wang.uvu.query.utils.QueryUtils;
import wang.uvu.query.Query;

public class StatementBuilder {
	
	public static Statement build(Root<?> root, CriteriaBuilder criteriaBuilder, Query query) {
		Map<String, String> map = BeanHelper.describe(query);
		Statements statements = new Statements(criteriaBuilder,query.isOr_());
		Set<String> keySet = map.keySet();
		for (String fieldName : keySet) {
			String expression = map.get(fieldName);
			if (QueryUtils.ignore(fieldName) || StringUtils.isBlank(expression)) {
				continue;
			}
			//属性之间使用or
			Statements subStmt = new Statements(criteriaBuilder,true);
			for (String var : expression.split("\\|")) {
				Statements statement = buildStatements(root, criteriaBuilder,fieldName, var);
				subStmt.add(statement);
			}
			statements.add(subStmt);
		}
		return statements;
	}
 
	private static Statements buildStatements(Root<?> root, CriteriaBuilder criteriaBuilder, String fieldName, String expression) {
		StringBuffer buffer = new StringBuffer();
		Statements stmt = new Statements(criteriaBuilder);
		for (String s : expression.split(",")) {
			for (String operator : ALL) {
				if (s.startsWith(operator) && buffer.length() > 0) {//操作符开头，且存在buffer,意味着当前操作符结束了
					String childExp = buffer.toString().substring(1); 
					Statement statement = buildStatement(root, criteriaBuilder, fieldName, childExp);
					stmt.add(statement);
					buffer.setLength(0);
					continue ;
				}
			}
			buffer.append(",").append(s);
		}
		if (buffer.length() > 0) {//这个是最后一个了（唯一一个也是最后一个）
			String childExp = buffer.toString().substring(1); 
			Statement statement = buildStatement(root, criteriaBuilder, fieldName, childExp);
			stmt.add(statement);
		}
		return stmt;
	}
	
	private static Statement buildStatement(Root<?> root, CriteriaBuilder criteriaBuilder,String fieldName, String expression) {
		String operator = QueryUtils.getOperator(expression);
		String[] values = QueryUtils.getValues(expression, operator);
		switch (operator) {
		case EQUAL:
			if(values.length > 1){
				return new In(root, criteriaBuilder, fieldName, values);
			}
			return new Equal(root, criteriaBuilder, fieldName, values);
		case GREATER_THAN:
			return new GreaterThan(root, criteriaBuilder, fieldName, values);
		case GREATER_THAN_OR_EQUALTO:
			return new GreaterThanOrEqualTo(root, criteriaBuilder, fieldName, values);
		case LESS_THAN:
			return new LessThan(root, criteriaBuilder, fieldName, values);
		case LESS_THAN_OR_EQUALTO:
			return new LessThanOrEqualTo(root, criteriaBuilder, fieldName, values);
		case LIKE:
			return new Like(root, criteriaBuilder, fieldName, values);
		case NOT_LIKE:
			return new NotLike(root, criteriaBuilder, fieldName, values);
		case NOT_EQUAL:
			return new NotEqual(root, criteriaBuilder, fieldName, values);
		case IS_NOT_NULL:
			return new IsNotNull(root, criteriaBuilder, fieldName);
		case IS_NULL:
			return new IsNull(root, criteriaBuilder, fieldName);
		case IN:
			return new In(root, criteriaBuilder, fieldName, values);
		case NOT_IN:
			return new NotIn(root, criteriaBuilder, fieldName, values);
		case IS_EMPTY:
			return new IsEmpty(root, criteriaBuilder, fieldName);
		case IS_NOT_EMPTY:
			return new IsNotEmpty(root, criteriaBuilder, fieldName);
		}
		throw new QueryException("不支持的操作符：" + operator);
	}
}
