package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//�߰�
import lys.board.*;
import java.sql.Timestamp;//�߰��� �κ�(�ð�)

public class WriteProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
	     //�ѱ�ó��
	     request.setCharacterEncoding("utf-8");
	     //BoardDTO->Setter Method(5)+hidden (4)
	     //BoardDAO ��ü �ʿ�
	     BoardDTO article=new BoardDTO();
	     article.setNum(Integer.parseInt(request.getParameter("num")));
	     article.setWriter(request.getParameter("writer"));
	     article.setEmail(request.getParameter("email"));
	     article.setSubject(request.getParameter("subject"));
	     article.setPasswd(request.getParameter("passwd"));
	     article.setReg_date(new Timestamp(System.currentTimeMillis()));
	     article.setRef(Integer.parseInt(request.getParameter("ref")));
	     article.setRe_step(Integer.parseInt(request.getParameter("re_step")));
	     article.setRe_level(Integer.parseInt(request.getParameter("re_level")));
	     article.setContent(request.getParameter("content"));
	     article.setIp(request.getRemoteAddr());//����ip�ּ� ����
	     //readcount->default�� ������ ����� �ڵ����� 0�� ����.
	  
	  AccountsDAO dbPro=new AccountsDAO();
	  dbPro.insertArticle(article);
	  //response.sendRedirect("http://localhost:8090/JspBoard2/list.do");
		return "/writePro.jsp";// "/index.jsp"
	}
}
