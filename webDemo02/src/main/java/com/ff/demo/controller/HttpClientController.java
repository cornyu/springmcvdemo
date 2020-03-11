package com.ff.demo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

/**
 * @author cornyu
 * @version 创建时间：2019年2月22日 下午3:37:57 类说明 httpClient demo
 */

@Controller
@RequestMapping("/http")
public class HttpClientController {
	private static Logger logger = LoggerFactory.getLogger(TestController.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/demo")
	public void loginAction(HttpServletRequest httpServletRequest, HttpServletResponse response)
			throws IOException {
		HttpSession session = httpServletRequest.getSession();
		logger.info("httpServletRequest session :"+session);
		logger.info("ip:"+httpServletRequest.getLocalAddr());


		Cookie[] cookies = httpServletRequest.getCookies();
//		for (int i = 0; i < cookies.length; i++) {
//			Cookie cookie = cookies[i];
//			//
//			logger.info("httpServletRequest cookie :"+cookie.getName()+";"+cookie.getValue());
//		}
		logger.info("httpClient:==========================");
		// httpClient demo
		
		
		response.addCookie(new Cookie("cornyu", "wuliu"));

		Map result = new HashMap<String, Object>();
		result.put("result", "success");
		printResult(result, response);
		

	}

	@SuppressWarnings("rawtypes")
	private void printResult(Map result, HttpServletResponse response) throws IOException {
		String jsonString = JSON.toJSONString(result);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsonString);
	}

}
