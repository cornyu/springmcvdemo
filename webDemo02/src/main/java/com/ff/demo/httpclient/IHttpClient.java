package com.ff.demo.httpclient;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface IHttpClient {
	
	public String get(String url) throws Exception;
	
	public String login(String url,HttpServletRequest request,HttpServletResponse httpServletResponse) throws Exception;
	
	public String post(String url,Map<String, Object> input)throws Exception;
	
}
