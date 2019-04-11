<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
     //세션 연결 상태=>바로 종료=>메모리 상의 데이터 삭제
     session.invalidate();
%>
<script>
   alert("정상적으로 로그아웃 되었습니다.")
</script>
<%
response.sendRedirect("../demo-03-html/home.jsp");
%>