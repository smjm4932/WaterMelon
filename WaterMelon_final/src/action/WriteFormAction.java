package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WriteFormAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		 //1.jsp���� ó���ؾ��� �ڹ��ڵ带 ���ó�����ִ� ����->��� ����->jsp����
	      //list.jsp(�۾���)->�űԱ�, content.jsp(�ۻ󼼺���)->�۾���->�亯��
	      int num=0,ref=1,re_step=0,re_level=0; //writePro.jsp
	      
	      //content.jsp���� �Ű������� ����()
	      if(request.getParameter("num")!=null){//���(����,0�� �ƴϴ�)
	    	 num=Integer.parseInt(request.getParameter("num"));//"3"->3
	    	 ref=Integer.parseInt(request.getParameter("ref"));
	    	 re_step=Integer.parseInt(request.getParameter("re_step"));
	    	 re_level=Integer.parseInt(request.getParameter("re_level"));
	    	 System.out.println("content.jsp���� �Ѿ�� �Ű����� Ȯ��");
	    	 System.out.println("num=>"+num+",ref="+ref+
	    			                                 ",re_step="+re_step+",re_level=>"+re_level);
	      }
	      //2.������(��������,�Ű�����,�޼����� ��������)->request������ ����
	      request.setAttribute("num", num);//request.getAttribute("num")->${num}
	      request.setAttribute("ref", ref);
	      request.setAttribute("re_step", re_step);
	      request.setAttribute("re_level", re_level);
	      
		return "/writeForm.jsp";
	}

}


