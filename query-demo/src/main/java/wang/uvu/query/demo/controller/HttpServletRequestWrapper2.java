package wang.uvu.query.demo.controller;


import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpServletRequestWrapper2 extends HttpServletRequestWrapper {

	public HttpServletRequestWrapper2(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {

		return super.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> parameterMap = super.getParameterMap();
		Set<String> keySet = parameterMap.keySet();
		for (String key : keySet) {
			String[] values = parameterMap.get(key);
			
			
		}
		return null;
	}

	public String[] getParameterValues(String name) {
		return null;
	}
}
