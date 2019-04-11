package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//추가
import ksm.board.*;
import java.sql.Timestamp; // 추가할 부분(시간)

public class UpdateProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		//한글처리
	     request.setCharacterEncoding("utf-8");
	     String pageNum=request.getParameter("pageNum");
	     
	     BoardDTO article=new BoardDTO();
	     
	     article.setNum(Integer.parseInt(request.getParameter("num")));
	     article.setWriter(request.getParameter("writer"));
	     article.setEmail(request.getParameter("email"));
	     article.setSubject(request.getParameter("subject"));
	     article.setContent(request.getParameter("content"));
	     article.setPasswd(request.getParameter("passwd"));
	     
	     article.setIp(request.getRemoteAddr()); // 원격 ip 주소 저장
	     //readcount->default로 설정한 관계로 자동으로 0이 들어간다.
	
	
	     BoardDAO dbPro=new BoardDAO();
	     int check=dbPro.updateArticle(article);
	     
	     request.setAttribute("pageNum", pageNum);
	     request.setAttribute("check", check);
	     
	     //response.sendRedirect("http://localhost:8090/JspBoard2/list.do"); // response.sendRedirect("/index.jsp");
	     return "/updatePro.jsp"; //   "/index.jsp";
	}

}




