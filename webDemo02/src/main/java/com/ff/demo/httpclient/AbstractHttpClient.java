package com.ff.demo.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;



public class AbstractHttpClient implements IHttpClient {
	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClient.class);
	private static final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

	protected int maxTotal = 200; //最大连接数
	protected int maxPerRoute = 20;// 每个连接的路由数
	protected int conntimeout = 5000;//超时时间
	
	protected String loginChannel; // 新的需要登录的 接入方式
	protected String loginTrans; // 新的登录交易的 交易名
	
	public AbstractHttpClient(Integer conntimeout, Integer maxPerRoute, Integer maxTotal) {
		this.conntimeout = conntimeout;
		this.maxPerRoute = maxPerRoute;
		this.maxTotal = maxTotal;
		logger.info("init http connManaer,maxTotal:{},maxPerRoute:{},timeout:{}",maxTotal,maxPerRoute,conntimeout);
		connManager.setMaxTotal(maxTotal);
		connManager.setDefaultMaxPerRoute(maxPerRoute);
	}

	@Override
	public String get(String url) throws  Exception{

		return null;
	}

	@Override
	public String login(String url, HttpServletRequest request, HttpServletResponse httpServletResponse)
			throws Exception {
		return null;
	}

	@Override
	public String post(String url, Map<String, Object> input) throws Exception {
		return null;
	}

	
	

	public CloseableHttpClient getDefaultHttpClient(CookieStore cookieStore) {
		logger.info("cookieStore:{}",cookieStore.getCookies());
		for(Cookie cookie: cookieStore.getCookies()) {
			logger.info("cookiestore:{},{}",cookie.getName(),cookie.getValue());
		}
		
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(conntimeout)
				.setConnectionRequestTimeout(conntimeout).setSocketTimeout(conntimeout).setCookieSpec(CookieSpecs.DEFAULT)
				.build();

		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext paramHttpContext) {
				if (executionCount >= 3) {//
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					return false;
				}
				if (exception instanceof InterruptedIOException) {
					return true;
				}
				if (exception instanceof UnknownHostException) {
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {
					return false;
				}
				if (exception instanceof SSLException) {
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(paramHttpContext);
				HttpRequest request = clientContext.getRequest();
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}

				return false;
			}

		};

		return HttpClients.custom().setDefaultRequestConfig(config).setRetryHandler(retryHandler)
				.setConnectionManager(connManager).setDefaultCookieStore(cookieStore).build();

	}
	
	public HttpClientContext initHttpClient(HttpServletRequest request, HttpServletResponse httpServletResponse) {
		HttpClientContext httpContext = HttpClientContext.create();		
		org.apache.http.client.CookieStore cookieStore = new BasicCookieStore();
		httpContext.setCookieStore(cookieStore); //设置cookieStore

		initCookieStore(httpContext, request.getCookies()); //设置cookie 
		return httpContext;
	}

	public String getResultFromResponse(CloseableHttpResponse response)
			throws UnsupportedOperationException, IOException {
		InputStream in = null;
		BufferedReader bufferedReader = null;
		HttpEntity entity = response.getEntity();
		entity = new BufferedHttpEntity(entity);
		in = entity.getContent();

		StringBuffer sb = new StringBuffer();
		bufferedReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String tmpStr = null;
		while ((tmpStr = bufferedReader.readLine()) != null) {
			sb.append(tmpStr);
		}
		String responseStr = sb.toString();
		logger.info("小微3.0联合登录返回结果,response:{}", responseStr);
		return responseStr;
	}

	/**
	 * 将org.apache.http.cookie.Cookie 写回到 httpServletResponse
	 * 
	 * @param request
	 * 
	 * @param cookies
	 * @param httpServletResponse
	 */
	public void writeCookieToResponse(List<Cookie> apacheCookies, HttpServletRequest request,
			HttpServletResponse httpServletResponse) {
		logger.info("returnApacheCookie:{}", getApacheCookieInfo(apacheCookies));

		if (apacheCookies != null) {
			for (int i = 0; i < apacheCookies.size(); i++) {
				javax.servlet.http.Cookie cookie = ApacheCookieUtil
						.convertApacheCookieToServletCookie(apacheCookies.get(i));
				logger.info("cookie_name:{}",cookie.getName());
				
//				if (sessionCookieName.equals(cookie.getName())) {
//					WebUtil.addCookie(request, httpServletResponse, cookie.getName(), cookie.getValue(), cookie.getDomain(),
//							cookie.getPath(), cookie.getMaxAge(), true);
//					break;
//				}
				
			}

		}

	}

	public String getApacheCookieInfo(List<Cookie> apacheCookies) {
		if (apacheCookies == null) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < apacheCookies.size(); i++) {
			String domain = apacheCookies.get(i).getDomain();
			String name = apacheCookies.get(i).getName();
			String value = apacheCookies.get(i).getValue();
			String path = apacheCookies.get(i).getPath();
			String comment = apacheCookies.get(i).getComment();
			int version = apacheCookies.get(i).getVersion();
			stringBuilder = stringBuilder.append("	name:").append(name);
			stringBuilder = stringBuilder.append("	value:").append(value);
			stringBuilder = stringBuilder.append("	domain:").append(domain);
			stringBuilder = stringBuilder.append("	path:").append(path);
			stringBuilder = stringBuilder.append("	comment:").append(comment);
			stringBuilder = stringBuilder.append("	version:").append(version).append("\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * 构建cookieStore， 将javax.servlet.http.Cookie 转为
	 * org.apache.http.cookie.Cookie，并添加到cookieStore
	 * 
	 * @param httpContext
	 * @param servletCookies
	 */
	public void initCookieStore(HttpClientContext httpContext, javax.servlet.http.Cookie[] servletCookies) {
		if (servletCookies == null) {
			logger.warn("servletCookies is null");
			return;
		}
		for (int i = 0; i < servletCookies.length; i++) {
			org.apache.http.cookie.Cookie apahceCookie = ApacheCookieUtil
					.convertServetCookieToApacheCookie(servletCookies[i]);
			logger.info("initCookieStore :" + i + ":" + apahceCookie.toString());
			httpContext.getCookieStore().addCookie(apahceCookie);
		}
		
		
		BasicClientCookie apacheCookie = null;
		
		apacheCookie = new BasicClientCookie("cornyu", "cornyu_good");
		apacheCookie.setDomain("/");
		apacheCookie.setPath("/");
		apacheCookie.setComment("test");
		httpContext.getCookieStore().addCookie(apacheCookie);
		
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	public int getConntimeout() {
		return conntimeout;
	}

	public void setConntimeout(int conntimeout) {
		this.conntimeout = conntimeout;
	}

	public String getLoginChannel() {
		return loginChannel;
	}

	public void setLoginChannel(String loginChannel) {
		this.loginChannel = loginChannel;
	}

	public String getLoginTrans() {
		return loginTrans;
	}

	public void setLoginTrans(String loginTrans) {
		this.loginTrans = loginTrans;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static PoolingHttpClientConnectionManager getConnmanager() {
		return connManager;
	}

	/*
	 * public String getServletCookieInfo(javax.servlet.http.Cookie[]
	 * servletCookies) { StringBuilder stringBuilder = new StringBuilder(); for
	 * (int i = 0; i < servletCookies.length; i++) { String domain =
	 * servletCookies[i].getDomain(); String name = servletCookies[i].getName();
	 * String value = servletCookies[i].getValue(); String path =
	 * servletCookies[i].getPath(); String comment =
	 * servletCookies[i].getComment(); int version =
	 * servletCookies[i].getVersion(); stringBuilder = stringBuilder.append(
	 * "	name:").append(name); stringBuilder = stringBuilder.append(
	 * "	value:").append(value); stringBuilder = stringBuilder.append(
	 * "	domain:").append(domain); stringBuilder = stringBuilder.append(
	 * "	path:").append(path); stringBuilder = stringBuilder.append(
	 * "	comment:").append(comment); stringBuilder = stringBuilder.append(
	 * "	version:").append(version).append("\n"); } return
	 * stringBuilder.toString(); }
	 */

	
	
}
