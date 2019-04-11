package action;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import ksm.board.*;

public class DeleteProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		
		  String pageNum=request.getParameter("pageNum");//BoardDTO�� ��������� X
		  String passwd=request.getParameter("passwd");
		  int num=Integer.parseInt(request.getParameter("num"));
		  System.out.println("num=>"+num+",passwd=>"+passwd+",pageNum=>"+pageNum);
		  
		  BoardDAO dbPro=new BoardDAO();
		  int check=dbPro.deleteArticle(num,passwd);//��ȣã��->������ ��ȣüũ
		 
		  request.setAttribute("check", check);
		  request.setAttribute("pageNum", pageNum);
		 
		  return "/deletePro.jsp";
	}

}

