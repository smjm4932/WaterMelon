package action;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import ksm.board.*;


//content.jsp에서 처리한 자바코드부분을 대신 처리해주는 액션클래스
public class ContentAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		   //list.do에서 링크 content.do?num=3&pageNum=1
		   int num=Integer.parseInt(request.getParameter("num"));
		   String pageNum=request.getParameter("pageNum");
		   
		   BoardDAO dbPro=new BoardDAO();
		   BoardDTO article=dbPro.getArticle(num);
		   //링크문자열의 길이를 줄이기 위해서
		   
		   int ref=article.getRef();
		   int re_step=article.getRe_step();
		   int re_level=article.getRe_level();
		   System.out.println("content.do의 매개변수");
		   System.out.println("ref=>"+ref+",re_step=>"+re_step+",re_level=>"+re_level);
	
		   request.setAttribute("num", num);
		   request.setAttribute("pageNum", pageNum);
		   request.setAttribute("article", article);
		   // ref, re_step, re_level 도 전달 X
		   
		return "/content.jsp";
	}

}




