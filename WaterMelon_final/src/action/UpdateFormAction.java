package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//�߰�
import lys.board.*;

public class UpdateFormAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
	     //content.jsp->�ۼ��� ->/JspBoard2/updateForm.do?num=?&pageNum=1
	   int num=Integer.parseInt(request.getParameter("num"));
	   String pageNum=request.getParameter("pageNum");
	   
	   AccountsDAO dbPro=new AccountsDAO();
	   BoardDTO article=dbPro.updateGetArticle(num);//��ȸ�� ����X
	   
	   request.setAttribute("pageNum", pageNum);//${pageNum}
	   request.setAttribute("article", article);
	
		return "/updateForm.jsp";
	}

}
