package wang.uvu.query.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class BeanHelper {
	public static Map<String, String> describe(final Object bean) {
		try {
			Map<String, String> describe = BeanUtils.describe(bean);
			describe.remove("class");
			return describe;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
}
