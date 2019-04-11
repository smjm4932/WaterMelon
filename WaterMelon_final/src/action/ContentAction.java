package action;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import ksm.board.*;


//content.jsp���� ó���� �ڹ��ڵ�κ��� ��� ó�����ִ� �׼�Ŭ����
public class ContentAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		   //list.do���� ��ũ content.do?num=3&pageNum=1
		   int num=Integer.parseInt(request.getParameter("num"));
		   String pageNum=request.getParameter("pageNum");
		   
		   BoardDAO dbPro=new BoardDAO();
		   BoardDTO article=dbPro.getArticle(num);
		   //��ũ���ڿ��� ���̸� ���̱� ���ؼ�
		   
		   int ref=article.getRef();
		   int re_step=article.getRe_step();
		   int re_level=article.getRe_level();
		   System.out.println("content.do�� �Ű�����");
		   System.out.println("ref=>"+ref+",re_step=>"+re_step+",re_level=>"+re_level);
	
		   request.setAttribute("num", num);
		   request.setAttribute("pageNum", pageNum);
		   request.setAttribute("article", article);
		   // ref, re_step, re_level �� ���� X
		   
		return "/content.jsp";
	}

}




