<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 위 3개의 메타 태그는 *반드시* head 태그의 처음에 와야합니다; 어떤 다른 콘텐츠들은 반드시 이 태그들 *다음에* 와야 합니다 -->
    <title>공지사항 게시판</title>

    <!-- 부트스트랩 -->
    <!-- <link rel="stylesheet" type="text/css" href="css/util.css"> -->
	<link rel="stylesheet" type="text/css" href="css/main.css">
    <link href="../notice/css/bootstrap.min.css" rel="stylesheet">

  </head>
  <body>
    <div class="container text-center">
       <h1 class="text">공지사항</h1>
   
    
  
       <h4 class="text" style="text-align:right; margin-right:30px;"><a href="/WaterMelon/notice_form.water">글쓰기</a></h4>
       <!-- 데이터의 유무 -->
		
			
		
		
        <table class="table">
         <thead>
             <tr class="header">
               <td>번호</td>
               <td>제목</td>
               <td>작성자</td>
               <td>작성일</td>
               <td>조회수</td>
            </tr>
         </thead>
         
            <tr class="center">
               <td class="row">번호</td>
               <td class="row">게시글제목</td>
               <td class="row">관리자</td>
               <td class="row">작성일자</td>
               <td class="row">조회수</td>
            </tr>
     
      </table>
   
      
     
	  
	
    </div>

    <!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요합니다) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- 모든 컴파일된 플러그인을 포함합니다 (아래), 원하지 않는다면 필요한 각각의 파일을 포함하세요 -->
    <script src="../js/bootstrap.min.js"></script>
  </body>
</html>