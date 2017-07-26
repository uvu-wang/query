package wang.uvu.query.utils;

import static wang.uvu.query.utils.Operators.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QueryUtils {

	public static final List<String> IGNORES = Arrays.asList("orders_",
			"page_", "size_", "or_", "querys_", "fields_");

	public static final List<String> OPERATOR_ALL = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(EQUAL);
			add(GREATER_THAN);
			add(GREATER_THAN_OR_EQUALTO);
			add(LESS_THAN);
			add(LESS_THAN_OR_EQUALTO);
			add(LIKE);
			add(NOT_LIKE);
			add(NOT_EQUAL);
			add(IS_NOT_NULL);
			add(IS_NULL);
			add(IN);
			add(NOT_IN);
			add(IS_EMPTY);
			add(IS_NOT_EMPTY);
			Collections.sort(this, new Comparator<String>() {
				public int compare(String left, String right) {
					return left.length() - right.length();
				}
			});
		}
	};

	private static Class<?> getSupportType(Class<?> type) {
		switch (type.getTypeName()) {
		case "int":
			return Integer.class;
		case "short":
			return Integer.class;
		case "boolean":
			return Boolean.class;
		case "long":
			return Long.class;
		default:
			return type;
		}
	}
	
	public static Class<?> getFieldType(Class<?> clz, String fieldName){
		return getSupportType(getField(clz, fieldName).getType());
	}
	
	public static Field getField(Class<?> clz, String fieldName) {
		try {
			return clz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			if (clz != Object.class)
				return getField(clz.getSuperclass(), fieldName);
			throw new RuntimeException(e);
		}
	}
	
	public static String getOperator(String s) {
		for (String operator : OPERATOR_ALL) {
			if (s.startsWith(operator)) {
				return operator;
			}
		}
		return EQUAL;
	}

	public static String[] getValues(String expression, String operator) {
		for (String s : OPERATOR_ALL) {
			if (expression.startsWith(s)) {
				expression = expression.replace(s, "");
				break;
			}
		}
		return expression.split(",");
	}

	public static boolean ignore(String name) {
		return IGNORES.contains(name);
	}
}
