package wang.uvu.query.utils;

import java.util.Collection;

public final class StringUtils {
	
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}
	
	public static String join(Object[] array) {
		StringBuffer buf = new StringBuffer(array.length * 2);
		for (int i = 0; i < array.length; i++) {
           buf.append(array[i]).append(",");
        }
		buf.setLength(buf.length() - 1);
        return buf.toString();
	}
	
	public static String join(Collection<Object> collection) {
		return join(collection.toArray());
	}
}
