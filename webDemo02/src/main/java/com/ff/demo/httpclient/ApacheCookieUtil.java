package com.ff.demo.httpclient;

import java.util.Date;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

public class ApacheCookieUtil {


	/**
	 * 将 org.apache.http.cookie.Cookie 转换为 javax.servlet.http.Cookie
	 * 
	 * @param cookie
	 * @return javax.servlet.http.Cookie
	 */
	public static javax.servlet.http.Cookie convertApacheCookieToServletCookie(Cookie apacheCookie) {
		if(apacheCookie == null){
			return null;
		}
		String name = apacheCookie.getName();
		String value = apacheCookie.getValue();
		
		javax.servlet.http.Cookie servletCookie = new javax.servlet.http.Cookie(name,value);
		
		//set  domain
		/*value = apacheCookie.getDomain();
		if (value != null) {
			servletCookie.setDomain(value);
		}*/
		
		//set  path
		value = apacheCookie.getPath();
		if(value != null){
			servletCookie.setPath(value);
		}
		
		//set comment
		value = apacheCookie.getComment();
		if(value != null){
			servletCookie.setComment(value);
		}
		
		//set version 
		servletCookie.setVersion(apacheCookie.getVersion());
	
		java.util.Date expiryDate = apacheCookie.getExpiryDate();
		if (expiryDate!=null) {
			long maxAge =(expiryDate.getTime() - System.currentTimeMillis())/1000;
			servletCookie.setMaxAge((int)maxAge);
		}
			
		return servletCookie;
	}
	
	/**
	 * 将javax.servlet.http.Cookie 转换为 org.apache.http.cookie.Cookie
	 * 
	 * @param cookie
	 * @return org.apache.http.cookie.Cookie
	 */
	public static Cookie convertServetCookieToApacheCookie(javax.servlet.http.Cookie servletCookie) {
		if (servletCookie == null) {
			return null;
		}
		
		BasicClientCookie apacheCookie = null;
		//get all the relevant parameters
		String domain = servletCookie.getDomain();
		String name = servletCookie.getName();
		String value = servletCookie.getValue();
		String path = servletCookie.getPath();
		int maxAge = servletCookie.getMaxAge();
		apacheCookie = new BasicClientCookie(name, value);
		apacheCookie.setDomain(domain==null?"/":domain);
		apacheCookie.setPath(path==null?"/":path);
		apacheCookie.setComment(servletCookie.getComment());
		apacheCookie.setVersion(servletCookie.getVersion());
		 
		if (maxAge>0) {
			apacheCookie.setExpiryDate(new Date(maxAge * 1000));
		}
		
		return apacheCookie;
	}
	

}
