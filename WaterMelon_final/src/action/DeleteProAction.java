package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//�߼�
import lys.board.*;

public class DeleteProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		  String pageNum=request.getParameter("pageNum");//BoardDTO�� ��������� X
		  String passwd=request.getParameter("passwd");
		  int num=Integer.parseInt(request.getParameter("num"));
		  System.out.println("num=>"+num+",passwd=>"+passwd+",pageNum=>"+pageNum);
		  
		  AccountsDAO dbPro=new AccountsDAO();
		  int check=dbPro.deleteArticle(num,passwd);//��ȣã��->������ ��ȣüũ
		  
		  //����
		  request.setAttribute("pageNum", pageNum);
		  request.setAttribute("check", check);
		  
		return "/deletePro.jsp";//��������ؼ� �̵��� ������
	}
}

