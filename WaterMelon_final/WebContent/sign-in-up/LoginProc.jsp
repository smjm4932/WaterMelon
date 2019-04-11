<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
   <%@ page import="notice.*"%>
 
 <jsp:useBean id="memMgr" class="notice.MemberDAO" />

<%
     String mem_id=request.getParameter("mem_id");
     String mem_passwd=request.getParameter("mem_passwd");
     MemberDTO mem=memMgr.getMember(mem_id);
     System.out.println("mem_id=>"+mem_id);
     System.out.println("mem_passwd=>"+mem_passwd);
     
     //MemberDAO ->loginCheck
     //MemberDAO memMgr=new MemberDAO();  //(1)
     boolean check=memMgr.loginCheck(mem_id, mem_passwd);
%>
<%
   //check->LoginSuccess.jsp(인증화면), LogError.jsp(에러메세지)
  if(check){  //if(check==true)
	  session.setAttribute("idKey",mem_id);//키명->idKey
	  //response.sendRedirect("LoginSuccess.jsp");
	  session.setAttribute("nickName", mem.getMem_name());
	  response.sendRedirect("../demo-03-html/home.jsp");
  }else{ //id가 없다는 경우
	  response.sendRedirect("LogError.jsp");
}
%>






