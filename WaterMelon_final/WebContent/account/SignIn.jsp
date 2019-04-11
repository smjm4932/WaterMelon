<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="accounts.*"%>

<jsp:useBean id="memMgr" class="accounts.AccountsDAO" />

<%
	String mem_id = request.getParameter("mem_id");
	String mem_passwd = request.getParameter("mem_passwd");
	AccountsDTO mem = memMgr.getMember(mem_id);
	System.out.println("mem_id=>" + mem_id);
	System.out.println("mem_passwd=>" + mem_passwd);

	//MemberDAO ->loginCheck
	//MemberDAO memMgr=new MemberDAO();  //(1)
	boolean check = memMgr.loginCheck(mem_id, mem_passwd);
%>
<%
	//check->LoginSuccess.jsp(인증화면), LogError.jsp(에러메세지)
	if (check) { //if(check==true)
		session.setAttribute("idKey", mem_id);//키명->idKey
		//response.sendRedirect("LoginSuccess.jsp");
		session.setAttribute("nickName", mem.getNickname());
		response.sendRedirect("../demo-03-html/home.jsp");
	} else { //id가 없다는 경우
%>
<script>
	alert("아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해 주세요!");
</script>
<meta http-equiv="Refresh" content="0;url=/account/sign.water">
<%
	}
%>






