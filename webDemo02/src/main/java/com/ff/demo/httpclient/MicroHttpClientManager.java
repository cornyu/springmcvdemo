package com.ff.demo.httpclient;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送http请求
 * 
 * @author cornyu
 * 
 */
public class MicroHttpClientManager extends AbstractHttpClient {

	private static final Logger logger = LoggerFactory.getLogger(MicroHttpClientManager.class);
	String returnSuccessCode = "AAAAAA";
	String returnCodePath= "code";
	
	public MicroHttpClientManager(String loginChannel, String loginTrans, Integer conntimeout, Integer maxPerRoute, Integer maxTotal,
			String returnCode, String returnCodePath) {
		super(conntimeout,maxPerRoute,maxTotal);
		this.loginChannel = loginChannel;
		this.loginTrans = loginTrans;
		this.returnSuccessCode = returnCode;
		this.returnCodePath = returnCodePath;			
	}

	/**
	 * 发送get请求
	 * 将HttpServletRequest cookie 放入创建的 httpclient请求的cookie中 将httpclient 返
	 * 回的cookie写入到 HttpServletResponse response 的cookie中
	 */
	@SuppressWarnings("rawtypes")
	public String login(String url, HttpServletRequest request, HttpServletResponse httpServletResponse)
			throws Exception {
		String result = null;
		HashMap resultMap = null;

		HttpClientContext httpContext = initHttpClient(request, httpServletResponse);
		
		CloseableHttpClient httpClient = getDefaultHttpClient(httpContext.getCookieStore());		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.803.0 Safari/535.1");
		httpGet.setHeader("accept", "application/json");
		httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json");

		CloseableHttpResponse response = null;

		try {
			response = httpClient.execute(httpGet, httpContext);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {

				// 将联合登录返回的org.apache.http.cookie.Cookie 写会到 httpServletResponse
				List<Cookie> cookies = httpContext.getCookieStore().getCookies();
				writeCookieToResponse(cookies, request, httpServletResponse);

				result = getResultFromResponse(response);
				//resultMap = JSON.parseObject(result, HashMap.class);
				logger.info("返回结果：{}",result);
			} else {
				if (response != null) {
					logger.error("执行联合登录返回状态:{}", response.getStatusLine().getStatusCode());
				}
				logger.error("执行联合登录返回状态不正确，请求地址{},response:{}", url, response);
			}
		} catch (Exception e) {
			logger.error("执行联合登录失败，请求地址{},info：{}", url, e.getMessage());
			//throw new Exception("执行联合登录失败");
		} finally {
			try {			
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
				logger.error("关闭http资源报错，请求地址{},info：{}", url, e.getMessage());
			}
		}

		return result;
	}

	
	

}
