package com.ff.demo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ff.demo.httpclient.MicroHttpClientManager;
import com.ff.ffutil.FFUtil;

/** 
* @author cornyu 
* @version 创建时间：2018年11月3日 下午2:21:53 
* 类说明 
*/

@Controller
@RequestMapping("/demo")
public class TestController {
	private static Logger logger = LoggerFactory.getLogger(TestController.class);  


	@RequestMapping("/set")
	public String loginAction(HttpSession session,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		logger.info("current thread classloader:"+Thread.currentThread().getContextClassLoader());
		logger.info("httpServletRequest classloader:"+request.getClass().getClassLoader());
		
		//获取name的值
		logger.info("ffutil_02:"+FFUtil.sayHello());
		logger.info("FFUtil class loader in webdmeo02:"+FFUtil.class.getClassLoader());
		
		
		logger.info("httpClient:==========================");
		//httpClient		

		String url = "http://localhost:8080/webDemo03/demo/get.do";
		MicroHttpClientManager httpcleintManager = new MicroHttpClientManager("em_app", "test", 10000, 20, 200, "AAAAAA", "code");
		
		httpcleintManager.login(url, request, response);
		
		return "demo";
	}
}
