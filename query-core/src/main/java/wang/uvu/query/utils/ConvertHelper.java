package wang.uvu.query.utils;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;

public class ConvertHelper {
	private static final DateConverter dateConverter = new DateConverter();
	
	static{
		dateConverter.setPatterns(new String[] { "yyyy-MM-dd",	"yyyy-MM-dd HH:mm:ss", "yyyyMMdd", "yyyyMMddHHmmss" });
	}
	
	private static void register() {
		ConvertUtils.register(dateConverter, Date.class);
	}

	public static Object[] convert(String[] sources, Class<?> target) {
		register();
		return (Object[]) ConvertUtils.convert(sources, target);
	}
}
