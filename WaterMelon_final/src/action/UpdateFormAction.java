package action;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import ksm.board.*;

public class UpdateFormAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
	
	     //content.jsp->�ۼ��� ->updateForm.jsp?num=?&pageNum=1
	   int num=Integer.parseInt(request.getParameter("num"));
	   String pageNum=request.getParameter("pageNum");
	   
	   BoardDAO dbPro=new BoardDAO();
	   BoardDTO article=dbPro.updateGetArticle(num);//��ȸ�� ����X
		
	   request.setAttribute("num", num);
	   request.setAttribute("pageNum", pageNum);
	   request.setAttribute("article", article);
	   
		return "/updateForm.jsp";
	}

}
