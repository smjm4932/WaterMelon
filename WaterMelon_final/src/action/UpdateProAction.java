package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//�߰�
import ksm.board.*;
import java.sql.Timestamp; // �߰��� �κ�(�ð�)

public class UpdateProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		//�ѱ�ó��
	     request.setCharacterEncoding("utf-8");
	     String pageNum=request.getParameter("pageNum");
	     
	     BoardDTO article=new BoardDTO();
	     
	     article.setNum(Integer.parseInt(request.getParameter("num")));
	     article.setWriter(request.getParameter("writer"));
	     article.setEmail(request.getParameter("email"));
	     article.setSubject(request.getParameter("subject"));
	     article.setContent(request.getParameter("content"));
	     article.setPasswd(request.getParameter("passwd"));
	     
	     article.setIp(request.getRemoteAddr()); // ���� ip �ּ� ����
	     //readcount->default�� ������ ����� �ڵ����� 0�� ����.
	
	
	     BoardDAO dbPro=new BoardDAO();
	     int check=dbPro.updateArticle(article);
	     
	     request.setAttribute("pageNum", pageNum);
	     request.setAttribute("check", check);
	     
	     //response.sendRedirect("http://localhost:8090/JspBoard2/list.do"); // response.sendRedirect("/index.jsp");
	     return "/updatePro.jsp"; //   "/index.jsp";
	}

}




