<%@page import="java.text.SimpleDateFormat"%>
<%@page language="java" import="java.util.* ,java.awt.*" errorPage="jsps/error.jsp" pageEncoding="UTF-8"%>
<!-- JSP中的导包方式，见上面(两种方式) jsps/error.jsp自己设置一个jsps文件夹写一个error.jsp页面 -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<h2>Hello World!  yujinyu hel</h2>

<br/> 


<%

String res = (String) pageContext.findAttribute("name");//依次从：pageContext,request,session,applicat这4个从小到大的容器中去读取属性
out.println("name:"+res);


String res2 = (String) pageContext.findAttribute("name2");//若4个容器中都没有该属性，则输出:null
out.println("name2:"+res2);
%>

</body>
</html>
