package com.ff.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

/** 
* @author cornyu 
* @version 创建时间：2019年2月28日 下午9:25:42 
* 类说明 
*/
public class Test {

	public static void main(String[] args) throws UnknownHostException {
		String info=InetAddress.getLocalHost().toString();
		int n=info.lastIndexOf("/");
		String ip=info.substring(n+1);
		System.out.print(ip);
		

	      
		CloseableHttpClient httpClient = HttpClients.createDefault();
	    HttpGet get=new HttpGet("http://www.baidu.com");
	    HttpClientContext context = HttpClientContext.create();
	    try {
	        CloseableHttpResponse response = null;
	        
	        //response = httpClient.execute(get, context);
	        try{
	            System.out.println(">>>>>>cookies:");
	            //context.getCookieStore().getCookies().forEach(System.out::println);
	        }
	        finally {
	            response.close();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally {
	        try {
	            httpClient.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
		
		
		
		
		
		
	}

}
